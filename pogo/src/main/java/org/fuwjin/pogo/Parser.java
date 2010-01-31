package org.fuwjin.pogo;

import java.util.Map;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * The standard interface for the Pogo parser/matcher.
 */
public interface Parser {
   /**
    * Parses the input, persisting any state to the container.
    * @param context the input
    */
   void parse(PogoContext context);

   /**
    * Resolves this parser according to the grammar and type.
    * @param grammar the grammar for resolving any rule references
    * @param type the class for resolving any method references
    */
   void resolve(Map<String, Parser> grammar, ReflectionType type);
}
