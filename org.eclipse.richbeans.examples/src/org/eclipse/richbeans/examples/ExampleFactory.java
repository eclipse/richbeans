/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
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
