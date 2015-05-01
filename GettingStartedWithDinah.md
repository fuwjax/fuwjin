# Dinah #

## Getting Dinah ##
If you're using a dependency management tool, you can find information on the latest public release at [MvnRepository](http://mvnrepository.com/artifact/org.fuwjin/dinah). Otherwise, you can download public releases from the [Sonatype Open Source Releases repository](https://oss.sonatype.org/content/repositories/releases/org/fuwjin/dinah) or if you're feeling particularly brave, the latest snapshot release from the [Sonatype Open Source Snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/org/fuwjin/dinah).

## Understanding Virtual Signatures ##

Dinah is a reflection library which effectively strips the Object-Orientedness from Java. The idea is to transform all of Java into a Function which looks and acts much like a static method would. For instance, consider the following Java snippet:

```
package org.sample;

public class SomeClass{
  public String doSomething(int count, Object[] args) {
    // do something
  }
}
```

Dinah would take the above "doSomething" method and turn it into a Function with the following virtual signature:

```
  java.lang.String org.sample.SomeClass.doSomething(org.sample.SomeClass, int , java.lang.Object[])
```

Fields are given a getter and setter regardless of whether one already exists. If the SomeClass class above were given a "private final int count;" field, then the corresponding virtual signatures for the Functions created by Dinah would be:

```
  int org.sample.SomeClass.count(org.sample.SomeClass)
  void org.sample.SomeClass.count(org.sample.SomeClass, int)
```

Constructors are also created, so for the default constructor in SomeClass, the virtual signature would be

```
  org.sample.SomeClass org.sample.SomeClass.new()
```

Static members work much the same way, but do not have a reference to "this". For instance:

```
package org.sample;

public class StaticsClass{
  private static String id;
  public static int length(Object array) {
    // do something
  }
}
```

The Dinah generated virtual signatures would be
```
  java.lang.String org.sample.StaticsClass.id()
  void org.sample.StaticsClass.id(java.lang.String)
  int org.sample.StaticsClass.length(java.lang.Object)
```