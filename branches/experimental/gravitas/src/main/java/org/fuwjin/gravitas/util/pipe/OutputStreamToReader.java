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
   private CharBuffer chars;
   private final ByteBuffer bytes;
   private final Charset conversion;
   private final InnerReader reader = new InnerReader();
   private final InnerOutputStream outputStream = new InnerOutputStream();
   private final ReentrantLock lock = new ReentrantLock();
   private final Condition hasBytes = lock.newCondition();
   private volatile boolean closed;
   private PrintStream log;

   public OutputStreamToReader(){
      this(1000, "UTF-8");
   }

   public OutputStreamToReader(int bufferSize, String charset){
      bytes = ByteBuffer.allocate(bufferSize);
      conversion = Charset.forName(charset);
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
      public boolean ready(){
         return readyImpl();
      }

      @Override
      public long skip(long n) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         int rem = chars.remaining();
         if(rem < n){
            chars.position(chars.limit());
            return rem;
         }
         chars.position(chars.position() + (int)n);
         return (int)n;
      }

      @Override
      public int read(CharBuffer target) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         int start = target.position();
         target.put(chars);
         return target.position() - start;
      }

      @Override
      public int read(char[] cbuf, int off, int len) throws IOException{
         if(closed){
            return -1;
         }
         if(!ready()){
            waitForChars();
         }
         int rem = chars.remaining();
         if(rem < len){
            chars.get(cbuf, off, rem);
            return rem;
         }
         chars.get(cbuf, off, len);
         return len;
      }
   }

   public Reader reader(){
      return reader;
   }

   public OutputStream outputStream(){
      return outputStream;
   }

   void closeImpl(){
      closed = true;
   }

   void assertOpen() throws EOFException{
      if(closed){
         throw new EOFException();
      }
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
      }catch(InterruptedException e){
         throw new InterruptedIOException();
      }finally{
         lock.unlock();
      }
   }

   private class InnerOutputStream extends OutputStream{
      @Override
      public void write(int b) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put((byte)b);
            hasBytes.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }

      @Override
      public void write(byte[] b) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put(b);
            hasBytes.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException{
         assertOpen();
         lock.lock();
         try{
            bytes.put(b, off, len);
            hasBytes.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            lock.unlock();
         }
      }

      @Override
      public void close() throws IOException{
         closeImpl();
      }
   }

   public void clear(){
      bytes.clear();
      chars = null;
   }

   public void logTo(PrintStream log){
      this.log = log;
   }
}
