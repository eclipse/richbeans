package org.eclipse.richbeans.examples;

public class ExampleFactory {

	
	public static IShellCreator createExample1() {
		return new org.eclipse.richbeans.examples.example1.ExampleRunner();
	}

	public static IShellCreator createExample2() {
		return new org.eclipse.richbeans.examples.example2.ExampleRunner();
	}
}
