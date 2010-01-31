package org.fuwjin.pogo;

import static org.fuwjin.io.BufferedInput.buffer;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.io.Reader;
import java.text.ParseException;

import org.fuwjin.io.ParseContext;
import org.fuwjin.io.PogoContext;
import org.fuwjin.io.RootContext;
import org.fuwjin.io.SerialContext;

/**
 * The main parsing interface.
 */
public class Pogo {
   private Parser rule;

   /**
    * Creates a new instance.
    * @param rule the rule to use as a start.
    */
   public Pogo(final Parser rule) {
      this.rule = rule;
   }

   /**
    * Creates a new instance.
    */
   protected Pogo() {
      // for subclasses
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Pogo o = (Pogo)obj;
         return eq(getClass(), o.getClass()) && eq(rule, o.rule);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), rule);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @param object the object to fill
    * @return the filled object
    * @throws ParseException if the parse fails
    */
   public Object parse(final CharSequence input, final Object object) throws ParseException {
      final RootContext context = new ParseContext(input, object);
      rule.parse(context);
      context.assertSuccess();
      return context.get();
   }

   /**
    * Parses the context.
    * @param context the context
    * @throws ParseException if the parse fails
    */
   public void parse(final PogoContext context) throws ParseException {
      rule.parse(context);
      context.assertSuccess();
   }

   /**
    * Parses the input into an object.
    * @param reader the input reader
    * @return the parsed object
    * @throws ParseException if the parse fails
    */
   public Object parse(final Reader reader) throws ParseException {
      return parse(reader, null);
   }

   /**
    * Parses the input into an object.
    * @param reader the input reader
    * @param object the object to fill
    * @return the filled object
    * @throws ParseException if the parse fails
    */
   public Object parse(final Reader reader, final Object object) throws ParseException {
      return parse(buffer(reader), object);
   }

   /**
    * Parses the input into the object.
    * @param input the input stream
    * @return the parsed object
    * @throws ParseException if the parse fails
    */
   public Object parse(final String input) throws ParseException {
      return parse(input, null);
   }

   /**
    * Serializes the input into the string.
    * @param input the input to serialize
    * @return the serialized string
    */
   public String serial(final Object input) {
      final PogoContext context = new SerialContext(input);
      rule.parse(context);
      return context.match();
   }

   /**
    * Sets the rule underlying this parser.
    * @param rule the parser
    */
   protected void setRule(final Parser rule) {
      this.rule = rule;
   }
}
