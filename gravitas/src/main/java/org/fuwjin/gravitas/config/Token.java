package org.fuwjin.gravitas.config;

import java.util.Map;


public interface Token{
   String toIdent();

   String apply(Target target, String gesture);

   String value(Map<String, String> values);
}
