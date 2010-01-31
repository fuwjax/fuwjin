package org.fuwjin.jon;

import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.text.ParseException;

import org.fuwjin.io.SerialContext;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.Grammar;

public class JonWriter {
   private static final Grammar JON;
   static {
      try {
         JON = readGrammar("jon.writer.pogo");
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final SerialContext context;
   private final ReferenceStorage storage;

   public JonWriter() {
      context = new SerialContext(null);
      storage = new ReferenceStorage();
   }

   public String write(final Object obj) throws ParseException {
      context.set(storage.get(obj, null), true, null);
      JON.parse(context);
      return context.match();
   }
}
