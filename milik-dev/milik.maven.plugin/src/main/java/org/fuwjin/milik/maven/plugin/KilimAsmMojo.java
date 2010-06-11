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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal asm
 * 
 * @phase process-sources
 */
public class KilimAsmMojo extends AbstractKilimMojo {
  private static final FilenameFilter jfilter = new FilenameFilter() {
    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(".j");
    }
  };
  private static final FileFilter dirfilter = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return pathname.isDirectory() && !pathname.getName().startsWith(".");
    }
  };

  public void execute() throws MojoExecutionException {
    ClassLoader loader = loader(Collections.<String> emptyList());
    List<String> args = new ArrayList<String>();
    args.add("-d");
    args.add(project.getBuild().getOutputDirectory());
    for (String dir : (List<String>) project.getCompileSourceRoots()) {
      find(new File(dir), args);
    }
    exec(loader, "org.fuwjin.milik.tools.Asm", args.toArray(new String[args.size()]));

    args = new ArrayList<String>();
    args.add("-d");
    args.add(project.getBuild().getTestOutputDirectory());
    for (String dir : (List<String>) project.getTestCompileSourceRoots()) {
      find(new File(dir), args);
    }
    exec(loader, "org.fuwjin.milik.tools.Asm", args.toArray(new String[args.size()]));
  }

  private void find(File dir, List<String> found) {
    for (String f : dir.list(jfilter)) {
      found.add(new File(dir, f).getAbsolutePath());
    }
    for (File sub : dir.listFiles(dirfilter)) {
      find(sub, found);
    }
  }
}
