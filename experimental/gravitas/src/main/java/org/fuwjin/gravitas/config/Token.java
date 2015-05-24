package org.fuwjin.gravitas.config;

import java.util.Map;

public interface Token{
   String apply(Target target, String gesture);

   String toIdent();

   String value(Map<String, String> values);
}
