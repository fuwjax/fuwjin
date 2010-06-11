/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TaskTestClassLoader extends ClassLoader {
  static String wclassDir;

  static {
    String name = "org/fuwjin/milik/test/TaskTestClassLoader.class";
    URL baseURL = Thread.currentThread().getContextClassLoader().getResource(name);
    String path = baseURL.getPath();
    wclassDir = path.substring(0, path.lastIndexOf("/", path.length() - name.length() - 2)) + "/wclasses/";
  }

  public TaskTestClassLoader(ClassLoader aParent) {
    super(aParent);
  }

  @Override
  public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
    Class<?> ret = findLoadedClass(className);
    if (ret == null && className.startsWith("org.fuwjin.milik.")) {
      File f = new File(wclassDir + className.replace('.', '/') + ".class");
      if (f.exists()) {
        try {
          byte[] bytes = getBytes(f);
          // if (resolve) {
          ret = defineClass(className, bytes, 0, bytes.length);
          // }
        } catch (IOException ioe) {
          throw new RuntimeException("Error loading class " + className + " from file " + f.getPath(), ioe);
        }
      }
    }
    if (ret == null) {
      return resolve ? findSystemClass(className) : getParent().loadClass(className);
    } else {
      return ret;
    }
  }

  private byte[] getBytes(File f) throws IOException {
    int size = (int) f.length();
    byte[] bytes = new byte[size];
    int remaining = size;
    int i = 0;
    FileInputStream fis = new FileInputStream(f);
    while (remaining > 0) {
      int n = fis.read(bytes, i, remaining);
      if (n == -1)
        break;
      remaining -= n;
      i += n;
    }
    return bytes;
  }

  public static void main(String[] args) throws Exception {
    Class<?> c = new TaskTestClassLoader(Thread.currentThread().getContextClassLoader()).loadClass(args[0], true);
    c.newInstance();
  }
}
