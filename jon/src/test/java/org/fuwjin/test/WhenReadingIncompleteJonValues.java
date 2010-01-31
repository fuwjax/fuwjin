package org.fuwjin.test;

import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Map;

import org.fuwjin.jon.JonReader;
import org.junit.Test;

public class WhenReadingIncompleteJonValues {
   @Test
   public void shouldNotPutForwardReferenceInMap() throws ParseException {
      final Map<?, ?> result = new JonReader("{\"a\":&1}").read(Map.class);
      assertNull(result.get("a"));
   }
}
