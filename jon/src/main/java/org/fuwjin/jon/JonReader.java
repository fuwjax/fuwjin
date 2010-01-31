package org.fuwjin.jon;

import static org.fuwjin.io.BufferedInput.buffer;
import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.io.Reader;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.jon.container.JonContainer;
import org.fuwjin.pogo.Grammar;

public class JonReader {
   private static final Grammar JON;
   static {
      try {
         JON = readGrammar("jon.reader.pogo");
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final JonReadContext context;

   public JonReader(final CharSequence seq) {
      context = new JonReadContext(seq);
   }

   public JonReader(final Reader reader) {
      this(buffer(reader));
   }

   public JonContainer container() {
      return context.container();
   }

   public <T> T fill(final T obj) throws ParseException {
      context.set(obj, true, null);
      JON.parse(context);
      return (T)context.get();
   }

   public Object read() throws ParseException {
      return read(null);
   }

   public <T> T read(final Class<T> type) throws ParseException {
      return (T)fill(getBuilder(type));
   }

   public List<Object> readAll() throws ParseException {
      return readAll(null);
   }

   public <T> List<T> readAll(final Class<T> type) throws ParseException {
      final List<T> list = new LinkedList<T>();
      while(context.hasRemaining()) {
         list.add(read(type));
      }
      return list;
   }
}
