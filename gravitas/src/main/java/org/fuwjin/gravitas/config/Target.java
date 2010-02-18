package org.fuwjin.gravitas.config;

import org.fuwjin.gravitas.engine.Command;

public interface Target{
   boolean set(String name, String value);

   Command toCommand();
}
