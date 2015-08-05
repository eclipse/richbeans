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
package org.eclipse.richbeans.examples.example2;

import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.examples.ExampleJSONWritingValueListener;
import org.eclipse.richbeans.examples.example2.data.ExampleItem;
import org.eclipse.richbeans.examples.example2.data.ExampleParameters;
import org.eclipse.richbeans.examples.example2.ui.ExampleComposite;
import org.eclipse.richbeans.reflection.BeanService;
import org.eclipse.richbeans.widgets.util.SWTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * An example of using the composite and bean together.
 * 
 * @author Matthew Gerring
 */
public class ExampleRunner {

	public static void main(String[] args) throws Exception {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Change a value to see bean as JSON");

		// Composite
		final ExampleComposite ui = new ExampleComposite(shell, SWT.NONE);
		ui.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Something to show value.
		final Label value = new Label(shell, SWT.WRAP);
		value.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		shell.pack();
		shell.setSize(420, 600);

		// Set some initial values
		final ExampleParameters bean = new ExampleParameters();
		bean.setElement("Fe");
		bean.setEdge("K");
		bean.setStart(100d);
		bean.setStop(200d);
		bean.addItem(new ExampleItem(1, 2));

		// Connect the UI and bean
		final IBeanController controller = BeanService.getInstance().createController(ui, bean);
		controller.addValueListener(new ExampleJSONWritingValueListener(controller, value));
		controller.beanToUI();
		controller.switchState(true);

		SWTUtils.showCenteredShell(shell);
	}
}
