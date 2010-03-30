package org.fuwjin.bespect;

import java.util.Collections;
import java.util.List;

public class BespectConfig{

   public String getNativeMethodPrefix(){
      return "$chrona_";
   }

   public List<String> getRetransformClasses(){
      return Collections.singletonList("java.lang.System");
   }
   
   public List<String> getAdvisedClasses(){
      return Collections.singletonList("java.lang.System");
   }
   
   public String getAdvisor(){
      return "org.fuwjin.chrona.Chrona";
   }
}
