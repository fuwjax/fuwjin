/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjax.sample;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.fuwjin.io.AbstractCodePointStream.stream;

import java.text.ParseException;
import java.util.List;

import org.fuwjin.io.CodePointStream;
import org.fuwjin.io.PogoException;
import org.fuwjin.jon.JonReader;
import org.fuwjin.jon.JonWriter;

/**
 * A program illustrating sample usage of JON. First a set of business control
 * objects are constructed in an IoC pattern. Then a series of data objects are
 * read in, processed, and the output is stored again to the file system. Please
 * note that it is not the goal of this sample to illustrate proper exception
 * handling or file usage. It is only attempting to demonstrate the basic flow
 * when using JON.
 */
public final class SampleUsage {
   private static final String EXPECTED_RESULT = "&0=(&1=java.util.ArrayList)[&2=(&3=org.fuwjax.sample.Model){name:\"Mike D\",description:\"me\",contactNumbers:&4=(&5=java.util.HashMap){(&6=org.fuwjax.sample.Phone$PhoneType)WORK:&7=(&8=org.fuwjax.sample.Phone){areaCode:123,block:456,index:7890}}},&9=(&3){name:\"Mike the Lesser\",description:\"some other Mike\",contactNumbers:&10=(&5){(&6)HOME:&11=(&8){areaCode:123,block:789,index:4560}}}]"; //$NON-NLS-1$

   private static TransformationService createService(final String context) throws PogoException {
      final JonReader reader = new JonReader(readFile(context));
      return reader.read(TransformationService.class);
   }

   /*
    * Currently there is no way to know when a read will fail because the parser
    * is at the end of the input.
    */
   private static List<Model> fetchData(final String data) throws PogoException {
      final JonReader reader = new JonReader(readFile(data));
      return reader.readAll(Model.class);
   }

   /**
    * Main Method.
    * @param args program arguments
    * @throws ParseException if there is a problem parsing
    */
   public static final void main(final String[] args) throws PogoException {
      final TransformationService service = createService("context2.jon"); //$NON-NLS-1$
      final List<Model> models = fetchData("data.jon"); //$NON-NLS-1$
      final Object output = service.transform(models);
      final String result = saveOutput(output);
      assert result.equals(EXPECTED_RESULT): result;
   }

   /*
    * Currently the only way to read JON formatted input is through a
    * CharSequence. This method simply reads all the lines in a file and smashes
    * them together for the JON parser.
    */
   private static CodePointStream readFile(final String filename) {
      return stream(getSystemResourceAsStream(filename));
   }

   /*
    * A java.io.Writer is an Appendable, so writing to a file is more
    * straightforward than reading.
    */
   private static String saveOutput(final Object output) throws PogoException {
      final JonWriter writer = new JonWriter();
      return writer.write(output);
   }
}
