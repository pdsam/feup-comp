# COMP - Project 1

## PROJECT TITLE: A compiler of Java-- programs to Java bytecodes.

## GROUP: 5B

NAME1: Carlos Albuquerque, NR1: up201706735, GRADE1: 19, CONTRIBUTION1: 23%

NAME2: Maria Ferreira, NR2: up201704508, GRADE2: 19, CONTRIBUTION2: 23%

NAME3: Moisés Rocha, NR3: up201707329, GRADE3: 20, CONTRIBUTION3: 31%

NAME4: Paulo Marques, NR4: up201705615, GRADE4: 19, CONTRIBUTION4: 23%

GLOBAL Grade of the project: 20

## Summary:
The compiler, named jmm, translates programs in Java-- ,into java bytecodes. The compiler, besides having the normal stages of a compiler(syntatic analysis, semantic analysis, code generation) has some optimization options such as Constant propagation and folding, while template, boolean expression optimization, optimized register alocation.

## Execute: 

### Compile

To compile the program, run ``gradle build``. This will compile and generate a JAR file in the root directory.

### Run

To run you have two options: Run the ``.class`` files or run the JAR.

#### Run ``.class``

To run the ``.class`` files, do the following:

```cmd
java -cp "./build/classes/java/main/" <class_name> <arguments>
```

Where ``<class_name>`` is the name of the class you want to run and ``<arguments>`` are the arguments to be passed to ``main()``.

#### Run ``.jar``
    
To run the JAR, do the following command:

```cmd
java -jar <jar filename> <arguments>
```

Where ``<jar filename>`` is the name of the JAR file that has been copied to the root folder, and ``<arguments>`` are the arguments to be passed to ``main()``.

#### Avaiable flags
+ -v - Turns on the verbose mode, which turns on the programms debug prints.
+ -werror - The Warnings will be treated like errors by the compiler.
+ -r=``<n>`` -  the compiler tries to assign the local variables used in each function of  each Java-- class to  the  first <n> local  variables  of  the JVM.
+ -o - Enables the following optimizations: While loop templates, Constant propagation and folding and Booleans Expression Optimization.
+ -ob - Enables the boolean expressions optimization.
+ -lp - Enables the while loop template separately.
+ -pfc - Enables Constant propagation and folding separately.

## Dealing With Syntatic Errors: 
Our compiler supports deep error recovery in the while condition, finding up to a predetermined number of errors (currently 10). In other code blocks our tool does not performe error recovery, exiting after the first error.

We also feature detailed error messages for the while condition.

## SEMANTIC ANALYSIS:
For the semantic analysis stage we use a Visitor design pattern to go through the Abstract Syntax Tree.  
The required semantic rules are all implemented. This include:
+ operands must be of same type (e.g. a sum must be applied to to integers)
+ arrays cannot be directly used on operations (e.g. arr1 + arr2 is not permitted)
+ array accesses must be done to an array
+ array access index must be an integer
+ type checking for assignments (e.g. a_int = b_boolean is not permitted)
+ operations type checking (e.g. boolean operators can only be applied to boolean expressions)
+ conditional expressions (i.e. ifs and whiles) must be of type boolean
+ checking var initialization (by default this only produces a warning, see -werror flag to produce errors instead)
+ methods must be invoked on a class that implements the method (either explicitly our by inheritance)
+ checking if methods not declared are imported
+ checking if the number and types of arguments of an invocation match the method's prototype

Note that we assure method overloading and inheritance. This among other ordinary features of a language are part of the
specification of the project.  
As extra rules, we implemented:
+ method return type checking (e.g. a method declared to return boolean must return boolean)
+ using non static variables, fields or methods in a static context is not allowed
+ non initialized variables produce errors (when the -werror flag is active)
+ expression statements without meaning are not permitted (e.g. true; or 1+20; are not valid statements)

## INTERMEDIATE REPRESENTATIONS (IRs): 
Our tool does not use an intermediate representation. However we do annotate the AST with extra information in diferent stages of the compiler. For example, the semantic analyser adds to the nodes their corresponding symbol table.
## CODE GENERATION: 
For the code generation stage, our tool revisits the AST generating the necessary code for each node.
The generated code is directly written to the output file (a file with the name of the class and '.j' extension).  
For better performance, we generate JVM with lower cost instructions when possible (e.g. i=i+1; is translated to an iinc instruction).
By default we also try to optimize if and while conditions by generating the least possible code for specific cases.  
We tested thoroughly our tool and the generated code does not appear to have any problem.

## OVERVIEW: 
Our tool does not use any third party tool or package.

After generating the AST, we use the Visitor pattern to "visit" each node of the tree and perform the necessary operations in that node.
Every stage of the compiler has a diferent visitor class, therefore, the tree is visited multiple times.
We also make use of a symbol table hierarchy by having three different types of symbol tables: we have a symbol table for the document, one for the class and one for each method.

The document table stores the imported methods and classes.
The class table stores the information of every method in the class and any possible methods from a superclass.
The method table stores the local variables and parameters used in the method definition. 

## TASK DISTRIBUTION: 
### Checkpoint 1
* Parser development - Moisés Rocha, Carlos Albuquerque
* Error treatment and Recovery mechanism - Paulo Marques
* Syntax tree generation - Moisés Rocha, Helena Ferreira
### Checkpoint 2
* Symbol Tables - Carlos Albuquerque, Maria Helena
* Semantic Analysis - Carlos Albuquerque, Paulo Marques
* Jasmin code generation for invocation of functions - Moisés Rocha
* Jasmin code generation for arithmetic expressions - Moisés Rocha
### Checkpoint 3
* Jasmin code generation for conditional expressions- Carlos Albuquerque, Maria Helena
* Jasmin code generation for loops- Moisés Rocha, Paulo Marques
* Jasmin code generation to deal with arrays - Moisés Rocha
* Test classes - Carlos Albuquerque, Maria Ferreira, Paulo Marques
### Optimizations
* Option –r=<n> - Carlos Albuquerque, Maria Ferreira
* Option -o - Moisés Rocha, Paulo Marques
* Constant folding - Moisés Rocha
* Boolean Expression Optimization - Moisés Rocha

## PROS: (Identify the most positive aspects of your tool)
* Helpful and detailed error messages.
* Use of flags for custom usage.
* Some optimizations.
* Detects a wide variety of sematic error checking.

## CONS: (Identify the most negative aspects of your tool)
* Further optimizations could be implemented in the compiler.
