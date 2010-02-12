package org.fuwjin.gravitas.util.pipe;

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
   private ByteBuffer bytes;
   private final CharBuffer chars;
   private final Charset conversion;
   private final InnerWriter writer = new InnerWriter();
   private final InnerInputStream inputStream = new InnerInputStream();
   private final ReentrantLock pipeLock = new ReentrantLock();
   private final Condition hasChars = pipeLock.newCondition();
   private volatile boolean closed;
   private PrintStream log;

   public WriterToInputStream(){
      this(1000, "UTF-8");
   }

   public WriterToInputStream(int bufferSize, String charset){
      chars = CharBuffer.allocate(bufferSize);
      conversion = Charset.forName(charset);
   }

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
      public long skip(long n) throws IOException{
         if(closed){
            return -1;
         }
         if(!readyImpl()){
            waitForBytes();
         }
         int rem = bytes.remaining();
         if(rem < n){
            bytes.position(bytes.limit());
            return rem;
         }
         bytes.position(bytes.position() + (int)n);
         return (int)n;
      }

      @Override
      public int read(byte[] buf, int off, int len) throws IOException{
         if(closed){
            return -1;
         }
         if(!readyImpl()){
            waitForBytes();
         }
         int rem = bytes.remaining();
         if(rem < len){
            bytes.get(buf, off, rem);
            return rem;
         }
         bytes.get(buf, off, len);
         return len;
      }
   }

   public Writer writer(){
      return writer;
   }

   public InputStream inputStream(){
      return inputStream;
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
      }catch(InterruptedException e){
         throw new InterruptedIOException();
      }finally{
         pipeLock.unlock();
      }
   }

   private class InnerWriter extends Writer{
      @Override
      public void write(int b) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print((char)b);
            }
            chars.put((char)b);
            hasChars.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void flush() throws IOException{
         // always flushed...
      }

      @Override
      public void write(String str) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(str);
            }
            chars.append(str);
            hasChars.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(String str, int off, int len) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(str.substring(off, off+len));
            }
            chars.append(str, off, len);
            hasChars.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(char[] b) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               log.print(b);
            }
            chars.put(b);
            hasChars.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void write(char[] b, int off, int len) throws IOException{
         assertOpen();
         pipeLock.lock();
         try{
            if(log != null){
               char[] buf = new char[len];
               System.arraycopy(b, off, buf, 0, len);
               log.print(buf);
            }
            chars.put(b, off, len);
            hasChars.signalAll();
         }catch(BufferOverflowException e){
            throw new IOException(e);
         }finally{
            pipeLock.unlock();
         }
      }

      @Override
      public void close() throws IOException{
         closeImpl();
      }
   }

   public void clear(){
      chars.clear();
      bytes = null;
   }

   public void logTo(PrintStream log){
      this.log = log;
   }
}
