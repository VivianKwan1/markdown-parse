test: MarkdownParse.class MarkdownParseTest.class 
   java -cp .:libs/junit-4.12.jar:libs/hamcrest-core-1.3.jar org.junit.runner.JUnitCore MarkdownParseTest

MarkdownParse.class: MarkdownParse.java
   javac MarkdownParse.java

MarkdownParseTest.class: MarkdownParseTest.java
   javac MarkdownParseTest.java