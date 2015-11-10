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
package org.eclipse.richbeans.examples.example1;

import org.eclipse.richbeans.api.binding.IBeanController;
import org.eclipse.richbeans.binding.BeanService;
import org.eclipse.richbeans.examples.ExamplePrintBeanValueListener;
import org.eclipse.richbeans.examples.example1.data.SimpleBean;
import org.eclipse.richbeans.examples.example1.ui.SimpleComposite;
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
		final SimpleComposite ui = new SimpleComposite(shell, SWT.NONE);
		ui.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Something to show value.
		final Label value = new Label(shell, SWT.WRAP);

		shell.pack();
		shell.setSize(420, 400);

		// Set some initial values
		final SimpleBean bean = new SimpleBean();
		bean.setX(10.0);
		bean.setY(5);

		// Connect the UI and bean
		final IBeanController controller = BeanService.getInstance().createController(ui, bean);
		controller.addValueListener(new ExamplePrintBeanValueListener(controller, value));
		controller.beanToUI();
		controller.switchState(true);

		SWTUtils.showCenteredShell(shell);
	}
}
