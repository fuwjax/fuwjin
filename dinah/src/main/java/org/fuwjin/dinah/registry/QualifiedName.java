package org.fuwjin.dinah.registry;

public class QualifiedName {
   private final String[] parts;

   public static QualifiedName split(String qname){
      return new QualifiedName(qname.split(":"));
   }
   
   public QualifiedName(String... parts){
      this.parts = parts;
   }
}
