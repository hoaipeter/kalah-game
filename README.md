[![Work in Repl.it](https://classroom.github.com/assets/work-in-replit-14baed9a392b3a25080506f3b7b6d57f295ec2978f6f33ec97e36a161684cbe9.svg)](https://classroom.github.com/online_ide?assignment_repo_id=2945771&assignment_repo_type=AssignmentRepo)
# KalahStandardStarter
The starter kit for the standard version of Kalah.

## Contents
1. `README.md`
 this file
2. `resources` directory
 * `IO.html` documentation for `IO` interface in test infrastructure
 * `kalah20200717.jar` jar file containing infrastructure, including
   test class and test specifications.
 * `test_specifications` directory containing the specifications for the tests.
   This is what is in the jar file, but has been unpacked for easy access.
 *  `junit-3.8.2.jar` jar file for 3.8 JUnit.
3. `src/kalah` directory
 * `Kalah.java` Stub class for Kalah set up to use test infrastructure.

## Makefile

The make utility provides a simple way to run non-trivial commands (it's
actually a lot more than that but that's all we're using it for for
COMPSCI701). The make utility reads the configuration file (called a
"makefile") to determine what to do. The default name for a makefile is
`Makefile`.

The supplied `Makefile` has 3 "targets", names that identify different
operations that the makefile supports. Those targets are:
<dl>
<dt><code>tests</code> (default)</dt>
<dd>
Compile the code (assumes everything is called from `Kalah.java`)
and then run the tests.
</dd>
<dt><code>play</code></dt>
<dd>
Compile the code (assumes everything is called from `Kalah.java`)
and then play the game.
</dd>
<dt><code>compile</code></dt>
<dd>
Compile the code (assumes everything is called from `Kalah.java`).
</dd>
</dl>

Targets are used by issuing the `make` command, followed by the target name.
If no target name is given then the default is used. So
<pre>
<code>&gt; make</code>
</pre>
will run the `tests` target.
<pre>
<code>&gt; make tests</code>
</pre>
Will do the same thing. Whereas
<pre>
<code>&gt; make compile</code>
</pre>
will just do the compilation.

Note that the <tt>repl.it</tt> `Run` command will just execute `make`, however
you can issue these commands in the bash shell in <tt>repl.it</tt>.


