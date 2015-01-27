# RSA
A Java application that performs basic RSA encryption and decryption.

Installation
========

First download the files by doing:

<code>git clone git@github.com:g12mcgov/RSA.git</code>

In order to run this application, you will need the following JARs for Unit Testing:

    hamcrest-core-1.3.jar
    junit-4.11.jar

If you're using Intellij, simply download the jars, and go to:

    File > Project Structure > Dependencies

Now, click on the "<code>+</code>" in the bottom left corner, and add the JARs. 

Running
========

To run the main application, simply run the <code>Main</code> file. This serves an interface between the Modular Arithmetic class and the RSA class. 

To compile type:

<code>javac Main.java</code>

To execute type:

<code>java Main</code>

Testing
========

To run the Unit Tests, compile and run the <code>ModularArithmeticTest.java</code> file.

To compile type:

<code>javac ModularArithmeticTest.java</code>

To execute type:

<code>java ModularArithmeticTest</code>

Assuming all went well, 13 of the 13 test should have passed.

