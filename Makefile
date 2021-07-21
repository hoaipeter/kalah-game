tests: compile
	java -cp resources/junit-3.8.2.jar:resources/kalah20200717.jar:bin junit.textui.TestRunner kalah.test.TestKalah

play: compile
	java -cp resources/junit-3.8.2.jar:resources/kalah20200717.jar:bin kalah.Kalah
  
compile:
	mkdir -p bin
	javac -d bin -cp resources/junit-3.8.2.jar:resources/kalah20200717.jar:bin:src src/kalah/Kalah.java
