# COMP - Project 1

## PROJECT TITLE: A compiler of Java-- programs to Java bytecodes.

## GROUP: <identifier of the group>
(Names, numbers, self assessment, and contribution of the members of the group to the project according to:)

NAME1: Carlos Albuquerque, NR1: up201706735, GRADE1: <0 to 20 value>, CONTRIBUTION1: <0 to 100 %>

NAME2: Maria Ferreira, NR2: up201704508, GRADE2: <0 to 20 value>, CONTRIBUTION2: <0 to 100 %>

NAME3: Moisés Rocha, NR3: up201707329, GRADE3: <0 to 20 value>, CONTRIBUTION3: <0 to 100 %>

NAME4: Paulo Marques, NR4: up201705615, GRADE3: <0 to 20 value>, CONTRIBUTION3: <0 to 100 %>

...

(Note that the sum of the CONTRIBUTION? values must be 100 %)
GLOBAL Grade of the project: 20

## Summary:
The compiler, named jmm, translates programs in Java-- ,into java bytecodes. The compiler, besides having the normal stages of a compiler(syntatic analysis, semantic analysis, code generation) has some optimization options such as Constant propagation and folding, while template, boolean expression optimization, optimized register alocation.


## Execute: 

### Compile

To compile the program, run ``gradle build``. This will compile and generate a JAR file in the root directory.

### Run

To run you have two options: Run the ``.class`` files or run the JAR.

### Run ``.class``

To run the ``.class`` files, do the following:

```cmd
java -cp "./build/classes/java/main/" <class_name> <arguments>
```

Where ``<class_name>`` is the name of the class you want to run and ``<arguments>`` are the arguments to be passed to ``main()``.

### Run ``.jar``

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
+ -lp - Enables the while loop template separately.
+ -pfc - Enables Constant propagation and folding separately.

## Dealing With Syntatic Errors: 
Our compiler supports deep error recovery in the while condition, finding up to a predetermined number of errors (currently 10). In other code blocks our tool does not performe error recovery, exiting after the first error.

We also feature detailed error messages for the while condition.

## SEMANTIC ANALYSIS: 
The required semantic rules are all implemented. This include:
+ operands must be of same type (e.g. a sum must be applied to to integers)
+ arrays cannot be directly used on operations (e.g. arr1 + arr2 is not permitted)
+ array accesses must be done to an array
+ array access index must be an integer
+ type checking for assignements (e.g. a_int = b_boolean is not permitted)
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
(describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)
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
* Parser development - 
* Error treatment and Recovery mecanism - Paulo Marques
* Syntax tree generation -
### Checkpoint 2
* Symbol Tables - Carlos Albuquerque, Maria Helena
* Semantic Analysis - Carlos Albuquerque, Paulo Marques
* Jasmin code generation for invocation of functions - Moisés Rocha
* Jasmin code generation for arithmetic expressions - Moisés Rocha
### Checkpoint 3
* Jasmin code generation for conditional expressions-
* Jasmin code generation for loops-
* Jasmin code generation to deal with arrays - 
* Test classes - Carlos Albuquerque, Maria Ferreira, Paulo Marques
### Optimizations
* Option –r=`<n>` - Carlos Albuquerque, Maria Ferreira
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


For this project, you need to [install Gradle](https://gradle.org/install/)

## Project setup

Copy your ``.jjt`` file to the ``javacc`` folder. If you change any of the classes generated by ``jjtree`` or ``javacc``, you also need to copy them to the ``javacc`` folder.

Copy your source files to the ``src`` folder, and your JUnit test files to the ``test`` folder.

## Compile

To compile the program, run ``gradle build``. This will compile your classes to ``classes/main/java`` and copy the JAR file to the root directory. The JAR file will have the same name as the repository folder.

### Run

To run you have two options: Run the ``.class`` files or run the JAR.

### Run ``.class``

To run the ``.class`` files, do the following:

```cmd
java -cp "./build/classes/java/main/" <class_name> <arguments>
```

Where ``<class_name>`` is the name of the class you want to run and ``<arguments>`` are the arguments to be passed to ``main()``.



#### Avaiable flags
+ -v - Turns on the verbose mode, which turns on the programms debug prints.
+ -werror - The Warnings will be treated like errors by the compiler.

### Run ``.jar``

To run the JAR, do the following command:

```cmd
java -jar <jar filename> <arguments>
```

Where ``<jar filename>`` is the name of the JAR file that has been copied to the root folder, and ``<arguments>`` are the arguments to be passed to ``main()``.

## Test

To test the program, run ``gradle test``. This will execute the build, and run the JUnit tests in the ``test`` folder. If you want to see output printed during the tests, use the flag ``-i`` (i.e., ``gradle test -i``).

## CheckPoint 2 - Checklist

### Symbol Table
    . global: inclui info de imports e a classe declarada
    . classe-specific: inclui info de extends, fields e methods
    . method-specific: inclui info dos arguments e local variables
    . sub topics:
       + tem de permitir method overload (i.e. métodos com mesmo nome mas assinatura de parâmetros diferente)
       + tem de permitir consulta da tabela por parte da análise semantica (e geração de código)
       + tem de permitir ligar e desligar a sua impressão para fins de debug (neste caso para fins de avaliação)
  
### Type Verification
    . verificar se operações são efetuadas com o mesmo tipo (e.g. int + boolean tem de dar erro)
    . não é possível utilizar arrays diretamente para operações aritmeticas (e.g. array1 + array2)
    . verificar se um array access é de facto feito sobre um array
    . verificar se o indice do array access é um inteiro
    . verificar se valor do assignee é igual ao do assigned (a_int = b_boolean não é permitido!)
    . verificar se operação booleana é efetuada só com booleanos
    . verificar se conditional expressions (if e while) resulta num booleano
    . verificar se variáveis são inicializadas, dando um WARNING em vez de ERRO
       + parametros são assumidos como inicializados
       + devem fazer uma análise através do control flow, i.e., se há um if e a variável só é inicializada dentro de ou o then ou o else, deve-se dar um warning a indicar que poderá não estar inicializada
       + será considerado bónus a quem resolver esta verificação usando erros em vez de warning.
            - cuidado que se a analise não estiver bem feita os erros vão fazer com que o vosso compilador não passe para a geração de código!
			- caso pretendam fazer esta abordagem com erros adicionem uma forma de ativar/desativar o erro para facilitar no caso de haver problemas.


### Function Verification
	* verificar se o "target" do método existe, e se este contém o método (e.g. a.foo, ver se 'a' existe e se tem um método 'foo')
	    - caso seja do tipo da classe declarada (e.g. a usar o this), verificar se é método do extends olhando para o que foi importado (isto se a classe fizer extends de outra classe importada)
	* caso o método não seja da classe declarada, isto é importada, verificar se método foi importado
	* verificar se o número de argumentos na invocação é igual ao número de parâmetros da declaração
	* verificar se o tipo dos parâmetros coincide com o tipo dos argumentos
	    - não esquecer que existe method overloading



  ### Code Generation
    * estrutura básica de classe (incluindo construtor <init>)
	* estrutura básica de fields
	* estrutura básica de métodos (podem desconsiderar os limites neste checkpoint: limit_stack 99, limit_locals 99)
	* assignments
	* operações aritméticas (com prioridade de operações correta)
		- neste checkpoint não é necessário a seleção das operações mais eficientes mas isto será considerado no CP3 e versão final
	* invocação de métodos

