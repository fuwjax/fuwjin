package org.fuwjin.gravitas.util.pipe;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class OutputStreamToReader{
   private final ByteBuffer bytes;

   private CharBuffer chars;

   private volatile boolean closed;
   private final Charset conversion;
   private final ReentrantLock lock = new ReentrantLock();
   private final Condition hasBytes = lock.newCondition();
   private PrintStream log;
   private final InnerOutputStream outputStream = new InnerOutputStream();
   private final InnerReader reader = new InnerReader();
   public OutputStreamToReader(){
      this(1000, "UTF-8");
   }
   public OutputStreamToReader(final int bufferSize, final String charset){
      bytes = ByteBuffer.allocate(bufferSize);
      conversion = Charset.forName(charset);
   }

   public void logTo(final PrintStream log){
      this.log = log;
   }

   public OutputStream outputStream(){
      return outputStream;
   }

   public Reader reader(){
      return reader;
   }

   void assertOpen() throws EOFException{
      if(closed){
         throw new EOFException();
      }
   }

   void closeImpl(){
      closed = true;
   }

   boolean readyImpl(){
      return chars != null && chars.hasRemaining();
   }

   void waitForChars() throws InterruptedIOException{
      lock.lock();
      try{
         if(bytes.position() == 0){
            hasBytes.await();
         }
         bytes.flip();
         chars = conversion.decode(bytes);
         if(log != null){
            log.print(chars.toString());
         }
         bytes.compact();
      }catch(final InterruptedException e){
         throw new InterruptedIOException();
      }finally{
         lock.unlock();
      }
   }

   private class InnerOutputStream extends OutputStream{
      @Override
      public void close() throws IOException{
         closeImpl();
      }

      @Override
      public void write(final byte[] b) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put(b);
            hasBytes.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }

      @Override
      public void write(final byte[] b, final int off, final int len) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put(b, off, len);
            hasBytes.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }

      @Override
      public void write(final int b) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put((byte)b);
            hasBytes.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }
   }

   private class InnerReader extends Reader{
      @Override
      public void close() throws IOException{
         closeImpl();
      }

      @Override
      public int read() throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         return chars.get();
      }

      @Override
      public int read(final char[] cbuf, final int off, final int len) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         final int rem = chars.remaining();
         if(rem < len){
            chars.get(cbuf, off, rem);
            return rem;
         }
         chars.get(cbuf, off, len);
         return len;
      }

      @Override
      public int read(final CharBuffer target) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         final int start = target.position();
         target.put(chars);
         return target.position() - start;
      }

      @Override
      public boolean ready(){
         return readyImpl();
      }

      @Override
      public long skip(final long n) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         final int rem = chars.remaining();
         if(rem < n){
            chars.position(chars.limit());
            return rem;
         }
         chars.position(chars.position() + (int)n);
         return (int)n;
      }
   }
}
