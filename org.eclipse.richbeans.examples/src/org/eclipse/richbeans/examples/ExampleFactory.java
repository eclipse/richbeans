package org.eclipse.richbeans.examples;

public class ExampleFactory {

	
	public static IShellCreator createExample1() {
		return new org.eclipse.richbeans.examples.example1.ExampleRunner();
	}

	public static IShellCreator createExample2() {
		return new org.eclipse.richbeans.examples.example2.ExampleRunner();
	}
	
	public static IShellCreator createExample3() {
		return new org.eclipse.richbeans.examples.example3.ExampleRunner();
	}
	
	public static IShellCreator createExample4() {
		return new org.eclipse.richbeans.examples.example4.ExampleRunner();
	}
	
	public static IShellCreator createExample5() {
		return new org.eclipse.richbeans.examples.example5.ExampleRunner();
	}
}
