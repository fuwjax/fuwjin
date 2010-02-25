package org.fuwjin.util.pipe;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WriterToInputStream{
   private class InnerInputStream extends InputStream{
      @Override
      public void close() throws IOException{
         closeImpl();
      }

      @Override
      public int read() throws IOException{
         if(closed){
            return -1;
         }
         if(!readyImpl()){
            waitForBytes();
         }
         return bytes.get();
      }

      @Override
      public int read(final byte[] buf, final int off, final int len) throws IOException{
         if(closed){
            return -1;
         }
         if(!readyImpl()){
            waitForBytes();
         }
         final int rem = bytes.remaining();
         if(rem < len){
            bytes.get(buf, off, rem);
            return rem;
         }
         bytes.get(buf, off, len);
         return len;
      }

      @Override
      public long skip(final long n) throws IOException{
         if(closed){
            return -1;
         }
         if(!readyImpl()){
            waitForBytes();
         }
         final int rem = bytes.remaining();
         if(rem < n){
            bytes.position(bytes.limit());
            return rem;
         }
         bytes.position(bytes.position() + (int)n);
         return (int)n;
      }
   }

   private class InnerWriter extends Writer{
      @Override
      public void close() throws IOException{
         closeImpl();
      }

      @Override
      public void flush() throws IOException{
         // always flushed...
      }

      @Override
      public void write(final char[] b) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(b);
            }
            chars.put(b);
            hasChars.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(final char[] b, final int off, final int len) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               final char[] buf = new char[len];
               System.arraycopy(b, off, buf, 0, len);
               log.print(buf);
            }
            chars.put(b, off, len);
            hasChars.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(final int b) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print((char)b);
            }
            chars.put((char)b);
            hasChars.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(final String str) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(str);
            }
            chars.append(str);
            hasChars.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(final String str, final int off, final int len) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(str.substring(off, off + len));
            }
            chars.append(str, off, len);
            hasChars.signalAll();
         }catch(final BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }
   }

   private ByteBuffer bytes;
   private final CharBuffer chars;
   private volatile boolean closed;
   private final Charset conversion;
   private final InnerInputStream inputStream = new InnerInputStream();
   private PrintStream log;
   private final ReentrantLock pipeLock = new ReentrantLock();
   private final Condition hasChars = pipeLock.newCondition();
   private final InnerWriter writer = new InnerWriter();

   public WriterToInputStream(){
      this(1000, "UTF-8");
   }

   public WriterToInputStream(final int bufferSize, final String charset){
      chars = CharBuffer.allocate(bufferSize);
      conversion = Charset.forName(charset);
   }

   public InputStream inputStream(){
      return inputStream;
   }

   public void logTo(final PrintStream log){
      this.log = log;
   }

   public Writer writer(){
      return writer;
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
      return bytes != null && bytes.hasRemaining();
   }

   void waitForBytes() throws InterruptedIOException{
      pipeLock.lock();
      try{
         if(chars.position() == 0){
            hasChars.await();
         }
         chars.flip();
         bytes = conversion.encode(chars);
         chars.compact();
      }catch(final InterruptedException e){
         throw new InterruptedIOException();
      }finally{
         pipeLock.unlock();
      }
   }
}
