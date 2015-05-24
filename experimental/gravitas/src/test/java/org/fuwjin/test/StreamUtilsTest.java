package org.fuwjin.test;

import java.io.FileNotFoundException;

import org.fuwjin.util.StreamUtils;
import org.junit.Test;

public class StreamUtilsTest{
   @Test(expected = FileNotFoundException.class)
   public void testFailedLookup() throws Exception{
      StreamUtils.open("does.not.exist", StreamUtilsTest.class);
   }
}
