# Chessur #

Chessur is the Java reference implementation for Grin. Designed to be as simple as Grin itself, it defers much of the complexity of function binding to Dinah.

Chessur is a composite of three main offerings. It is an API for loading Grin Catalogs and executing Grin Scripts, an Executable Object Model (ExecOM), and a set of 4 Catalogs which provide the following reference transformations:
  * GrinParser.cat: transforms .cat files into ExecOM Catalogs
  * GrinSerializer.cat: transforms ExecOM Catalogs into canonical .cat files
  * GrinFormatter.cat: transforms .cat files into canonical .cat files
  * GrinCodeGenerator.cat: transforms ExecOM Catalogs into Chessur-free Java code.
Before we look at the API, let's get a Chessur build. If you're using a dependency management tool, you can find information on the latest public release at [MvnRepository](http://mvnrepository.com/artifact/org.fuwjin/chessur). Otherwise, you can download public releases from the [Sonatype Open Source Releases repository](https://oss.sonatype.org/content/repositories/releases/org/fuwjin/chessur) or if you're feeling particularly brave, the latest snapshot release from the [Sonatype Open Source Snapshots repository](https://oss.sonatype.org/content/repositories/snapshots/org/fuwjin/chessur). Chessur depends on [Dinah](GettingStartedWithDinah.md), so you'll need to pull down the corresponding version of that as well.

To test the build, copy the Chessur and Dinah jars into a directory with a HelloWorld.cat file with the following text:
```
<HelloWorld> {
  publish "hello, world!"
}
```
To run the HelloWorld script, use the command line runner like this.
```
$ java -jar chessur-0.9.5.jar HelloWorld.cat
hello, world!
HelloWorld returned null
```

## Chessur API ##

The API is relatively straightforward and follows these steps:
  1. Create a CatalogManager
  1. Load a Catalog through CatalogManager.loadCat
  1. Get a Script from the Catalog (or use the Catalog as a Script)
  1. Execute the Script

### Create a CatalogManager ###

There are three ways of creating a CatalogManager. The first is with a custom FunctionProvider. A FunctionProvider determines how Grin Invocations are mapped to Java code. To use a custom provider, simply pass the provider to the CatalogManager constructor.
```
  CatalogManager manager = new CatalogManager(new CustomFunctionProvider());
```

A CatalogManager could be created with a set of default function bindings. These bindings bind all the members of a Java class to the fully qualified name, and in addition, bind all the instance members of the Java Class object for the class to the same fully qualified name. However, even with the default functions, it may be useful to customize the Adapter, which controls how types can be convertered to other types.
```
  CatalogManager manager = new CatalogManager(new CustomAdapter());
```

To use the default function bindings and type conversion, use the default constructor.
```
  CatalogManager manager = new CatalogManager();
```

### Load a Catalog ###

There are two ways of loading a Catalog. If the Catalog is on the file system, then it can be loaded through the following:
```
  Catalog catalog = manager.loadCat(new File("path/to/file.cat"));
```
If the Catalog is instead available on the class path, then use the String version.
```
  Catalog catalog = manager.loadCat("class/path/to/file.cat");
```
Both these methods assume the .cat file is encoded with UTF-8. If the file is in a different encoding, it may be loaded by specifying the encoding after the path, for instance.
```
  Catalog catalog = manager.loadCat("class/path/to/file.cat","UTF-16");
```

### Get a Script ###

A Catalog is just a container for Scripts, so getting a Script is straightforward.
```
  Script script = catalog.get("NameOfScript");
```
In many cases, the Catalog has one main entry point Script. This Script can be named anything, but it must be first in the .cat file. The Catalog may then be executed as a Script.
```
  Script script = catalog;
```

### Execute the Script ###

There are many overloaded methods for executing a Script. A Script may optionally take any of the following arguments:
  * An input, either an InputStream or a Reader
  * An output, either a PrintStream or a Writer
  * An environment, a Map<String,Object>
If an argument is omited, it will be treated as nonexistent. So an omitted input is a zero length stream, an omitted output will throw away appends, and an omitted environment will have no variable mappings.

Every exec method returns the return value of the script. In addition, it may throw an ExecutionException if the script is aborted or fails.

The standard runner works like this:
```
  try{
    Object result = script.exec(System.in, System.out);
    System.out.println(script +" returned "+result);
  }catch(ExecutionException e){
    e.printStackTrace();
  }
```