The Fuwjin Suite is a collection of tools designed to enable rich communication between developers and users.

The central product behind the Fuwjin Suite is [Chessur](GettingStartedWithChessur.md), a Java implementation of the language [Grin](GettingStartedWithGrin.md).

Grin is a scripting language designed to be simple yet robust, surprisingly efficient in both time and space.

Chessur is built on the advanced reflection package [Dinah](GettingStartedWithDinah.md).

In addition the Fuwjin Suite contains a set of experimental projects in the [experimental branch](http://code.google.com/p/fuwjin/source/browse/#svn/branches/experimental).

The maven generated site for the current release, including JavaDocs and source XRef is hosted at [fuwjin.org](http://fuwjin.org). And occasionally I talk about the Suite on my [blog](http://fuwjax.org).

### What is Grin? ###

Grin was created to enable developers to easily build parsers for rich, complicated domain languages. While it excels at this task, Grin is being used in several other contexts including Log/Screen Ripping, Dependency Injection and lean Application Containers, Code Generation, Text Formatting and Transformation, Testing and Executable Requirements. If you need to work with character streams or if you need to perform complicated or custom processes, Grin might be just the language abstraction you need. And when you need something more domain specific, Grin can help you build the parser, serializer and formatter for whatever language you want.

  * GettingStartedWithGrin - a simple introduction to the Grin Language
  * EmbeddedGrin - how Grin interoperates with its host language, in particular, the Java binding in Chessur
  * AdvancedScriptManagement - towards more manageable Grin Catalogs and Scripts
  * DebuggingAndTestingScripts - understanding errors, logging and user aborts
  * GrinLanguageSpecification - the full current specification including the grammar
  * GrinQuickReference - a distilled down version of the specification

In addition, there's some help on the Chessur and Dinah APIs

  * GettingStartedWithChessur - an introduction to the Chessur API
  * GettingStartedWithDinah - an introduction to the Dinah API
  * ChessurMavenPlugin - the maven plugin for executing a .cat file during the build

### Latest News ###

The Fuwjin Suite v0.9.6 is live on Maven Central. The releases are available directly from central under the [org.fuwjin](http://repo2.maven.org/maven2/org/fuwjin/) groupId.  The artifacts should also show up on the central search sites, such as [mvnrepository.com](http://mvnrepository.com/search.html?query=org.fuwjin). Snapshots and pre-releases are available at [oss.sonatype.org](https://oss.sonatype.org/index.html#nexus-search;quick~org.fuwjin).