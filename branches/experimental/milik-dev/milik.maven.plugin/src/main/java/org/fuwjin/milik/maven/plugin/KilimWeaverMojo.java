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

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal weave
 * 
 * @phase process-classes
 */
public class KilimWeaverMojo extends AbstractKilimMojo {
  /**
   * Location of the compiled classes to weave.
   * 
   * @parameter
   * @required
   */
  private List<String> classes;
  /**
   * Reg-ex filter for excluding classes from weaving.
   * 
   * @parameter alias="exclude"
   */
  private String exclude;

  public void execute() throws MojoExecutionException {
    ClassLoader loader = loader(classes);
    for (String dir : classes) {
      final String[] args;
      if (exclude != null) {
        args = new String[] { "-x", exclude, "-d", dir, dir };
      } else {
        args = new String[] { "-d", dir, dir };
      }
      exec(loader, "org.fuwjin.milik.tools.Weaver", args);
    }
  }
}
