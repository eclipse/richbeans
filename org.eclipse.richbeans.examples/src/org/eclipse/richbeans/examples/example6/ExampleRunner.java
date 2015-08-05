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
package org.eclipse.richbeans.examples.example6;

import org.eclipse.richbeans.api.reflection.IBeanController;
import org.eclipse.richbeans.examples.ExampleJSONWritingValueListener;
import org.eclipse.richbeans.examples.example6.data.DecoratorBean;
import org.eclipse.richbeans.examples.example6.ui.DecoratorComposite;
import org.eclipse.richbeans.reflection.BeanService;
import org.eclipse.richbeans.widgets.util.SWTUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
		final DecoratorComposite ui = new DecoratorComposite(shell, SWT.NONE);
		ui.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Button enable = new Button(shell, SWT.TOGGLE);
		enable.setSelection(true);
		enable.setText("Enable");

		// Something to show value.
		final Label value = new Label(shell, SWT.WRAP);

		shell.pack();
		shell.setSize(420, 400);

		// Set some initial values
		final DecoratorBean bean = new DecoratorBean();
		bean.setX(10.0);
		bean.setY(5);

		// Connect the UI and bean
		final IBeanController controller = BeanService.getInstance().createController(ui, bean);
		controller.addValueListener(new ExampleJSONWritingValueListener(controller, value));
		controller.beanToUI();
		controller.switchState(true);

		// Enable/disable by reflection
		enable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					controller.setUIEnabled(enable.getSelection());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		SWTUtils.showCenteredShell(shell);
	}
}
