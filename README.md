#INTRODUCTION

This is a JAVA framework for the Resource Constrained Project Scheduling Problem (RCPSP).

This framework was created by Fabien Léhuédé, Damien Prot and Axel Grimault for pedagogic issues at [Institut Mines Télécom Atlantique](https://www.imt-atlantique.fr/).

#INSTALLATION

The project is available on github : [https://github.com/biblik/rcpsp-framework](https://github.com/biblik/rcpsp-framework).

The project is under licence [GNU General Public License, version 2](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html).

###Under ECLIPSE

Follow these steps :

1. Download an [archive zip](https://github.com/biblik/rcpsp-framework/archive/master.zip) of the project from github
2. Open Eclipse
3. Import a project (General->Existing Projects into Workspace) (*click on next*)
4. Select archive file (select the archive you downloaded at step 1). Under projects, select the mkp-framework (*click on Finish*)

#RUNNING

###Under ECLIPSE

Run a java application. You must specify arguments.
**Run**: `-help` for help.

###Command line

####Linux

Run this command (this command will show the help).

1. Create a bin directory (if not exist)  
`mkdir bin`
2. Compil the project  
`javac -d ./../bin/ ./../src/rcpsp/*.java -cp ./../lib/plot.jar` 
3. Run the program  
`java -cp "./../bin/:./../lib/plot.jar" -Djava.library.path=./../lib/ rcpsp.Main -help `

####Windows

Run this command (this command will show the help). To run the code under Windows, you must have a [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) available.

1. Create a bin directory (if not exist)  
`mkdir bin`
2. Compil the project
`javac -d "./../bin/" ./../src/rcpsp/*.java -cp "./../lib/plot.jar"` 
3. Run the program  
`java -cp "./../bin/:./../lib/plot.jar" -Djava.library.path="./../lib/" rcpsp.Main -help `

#INSTANCES

Some instances are provided for testing the framework. Optimal values for theses instances can be found in the file this [link](https://github.com/biblik/rcpsp-framework/blob/master/instances/optimal_solutions.txt).

## Benchmark

To run the framework over all instances, a script is available under the folder `/benchmark`. To run the script, you have to follow these commands.

The script produces two files:

1. standardOutput.txt : the standard output of the framework. `System.out.print` command in JAVA.
2. errorOutput.txt : the error output of the framework. `System.err.print` command in JAVA.

####Linux

The script executes the framework over all instances. In a terminal:

1. Go to the *benchmark* directory  
`cd benchmark`
2. Run the script  
`bash scriptUNIX.sh`

####Windows

The script executes the framework over all instances. In a terminal:

1. Go to the *benchmark* directory  
`cd benchmark`
2. Run the script  
`scriptWINDOWS.bat`


#DOCUMENTATION

A [doxygen](http://www.stack.nl/~dimitri/doxygen/) documentation of the code is provided under the folder `/doc`.