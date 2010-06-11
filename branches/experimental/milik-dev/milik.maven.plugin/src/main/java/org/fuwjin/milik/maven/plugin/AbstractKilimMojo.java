package org.fuwjin.milik.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Abstract base for kilim.
 */
public abstract class AbstractKilimMojo extends AbstractMojo {
  /**
   * @parameter expression="${plugin.artifacts}"
   * @readonly
   * @since 1.1-beta-1
   */
  private List pluginDependencies;
  /**
   * The enclosing project.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project;

  public void exec(ClassLoader loader, final String main, final String[] args) throws MojoExecutionException {
    getLog().info("Executing " + main + " with " + Arrays.toString(args));
    final AtomicReference<Throwable> ex = new AtomicReference<Throwable>();
    Thread thread = new Thread() {
      public void run() {
        try {
          kilim(main, args);
        } catch (Throwable e) {
          ex.set(e);
        }
      }
    };
    thread.setContextClassLoader(loader);
    thread.start();
    try {
      thread.join();
      if (ex.get() != null) {
        throw new MojoExecutionException(main + " could not complete", ex.get());
      } else {
        getLog().info(main + " completed");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void kilim(final String main, final String[] args) throws Throwable {
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class<?> cls = loader.loadClass(main);
      Method mainM = cls.getMethod("main", String[].class);
      mainM.invoke(null, (Object) args);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      throw cause;
    }
  }

  public ClassLoader loader(List<String> classes) throws MojoExecutionException {
    try {
      List<URL> classpathURLs = new ArrayList<URL>();
      for (String cls : classes) {
        File file = new File(cls);
        URL url = file.toURI().toURL();
        classpathURLs.add(url);
        getLog().info("Adding " + url + " to classpath");
      }
      for (Artifact classPathElement : (List<Artifact>) pluginDependencies) {
        this.getLog().info("Adding plugin dependency artifact: " + classPathElement.getArtifactId() + " to classpath");
        classpathURLs.add(classPathElement.getFile().toURI().toURL());
      }
      for (Artifact classPathElement : (List<Artifact>) project.getTestArtifacts()) {
        this.getLog().info("Adding plugin dependency artifact: " + classPathElement.getArtifactId() + " to classpath");
        classpathURLs.add(classPathElement.getFile().toURI().toURL());
      }
      return new URLClassLoader(classpathURLs.toArray(new URL[0]));
    } catch (Exception e) {
      throw new MojoExecutionException("Kilim Mojo classloader could not be initialized", e);
    }
  }
}
