package org.fuwjin.chrona;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class SystemProxy{
   public interface CLibrary extends Library {
      CLibrary INSTANCE = (CLibrary) Native.loadLibrary(
          ("jvm"), CLibrary.class);
      long Java_System_nanoTime();
  }

  public static void main(String[] args) {
      System.out.println(CLibrary.INSTANCE.Java_System_nanoTime());
      System.out.println(System.currentTimeMillis());
      System.out.println(System.nanoTime());
  }

}
