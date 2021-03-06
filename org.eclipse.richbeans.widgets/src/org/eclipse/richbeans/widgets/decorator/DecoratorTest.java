/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.widgets.decorator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DecoratorTest {
	
	public static void main(String[] args) {
		
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		
		Composite composite = new Composite(shell, SWT.NORMAL);
		composite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Free Integer");
		Text integerText = new Text(composite, SWT.BORDER);
		integerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new IntegerDecorator(integerText);
		integerText.setText("10");
		
		label = new Label(composite, SWT.NONE);
		label.setText("Bound Integer Low (0-100)");
		integerText = new Text(composite, SWT.BORDER);
		integerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		IntegerDecorator low = new IntegerDecorator(integerText);
		integerText.setText("10");
//		low.setMaximum(100);
//		low.setMinimum(0);
		
		label = new Label(composite, SWT.NONE);
		label.setText("Bound Integer High (0-100)");
		integerText = new Text(composite, SWT.BORDER);
		integerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		IntegerDecorator high = new IntegerDecorator(integerText);
		integerText.setText("1000");
//		high.setMaximum(100);
//		high.setMinimum(0);
		
		label = new Label(composite, SWT.NONE);
		label.setText("Bound Integer High (low-high)");
		integerText = new Text(composite, SWT.BORDER);
		integerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		IntegerDecorator id = new IntegerDecorator(integerText);
		integerText.setText("1000");
		id.setMaximum(high);
		id.setMinimum(low);

		label = new Label(composite, SWT.NONE);
		label.setText("+ve Infinity");
		integerText = new Text(composite, SWT.BORDER);
		integerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		id = new IntegerDecorator(integerText);
		id.setValue(Double.POSITIVE_INFINITY);

		
		label = new Label(composite, SWT.NONE);
		label.setText("Free Real");
		Text realText = new Text(composite, SWT.BORDER);
		realText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new FloatDecorator(realText);
		realText.setText("10.7");
		
		label = new Label(composite, SWT.NONE);
		label.setText("Bad Real");
		realText = new Text(composite, SWT.BORDER);
		realText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		new FloatDecorator(realText);
		realText.setText("BAD");

		label = new Label(composite, SWT.NONE);
		label.setText("Bound Real (0-100)");
		realText = new Text(composite, SWT.BORDER);
		realText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		FloatDecorator fd = new FloatDecorator(realText);
		realText.setText("10.00");
		fd.setMaximum(100);
		fd.setMinimum(0);
		
		label = new Label(composite, SWT.NONE);
		label.setText("Bound Real (0-100)");
		realText = new Text(composite, SWT.BORDER);
		realText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		fd = new FloatDecorator(realText);
		realText.setText("10000.00");
		fd.setMaximum(100);
		fd.setMinimum(0);

		@SuppressWarnings("unused")
		Text none = new Text(composite, SWT.NORMAL);
		
		composite.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()){
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


}
