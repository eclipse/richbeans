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
package org.eclipse.richbeans.widgets.cell;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class LongStringDialog extends Dialog {

	private StyledText text;
	private String value;

	protected LongStringDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE|SWT.RESIZE);
		setBlockOnOpen(true);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        
		final Composite comp = (Composite)super.createDialogArea(parent);
		comp.setLayout(new GridLayout(2, false));
		
		this.text = new StyledText(comp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.addModifyListener((e) -> this.value = text.getText());
        return comp;
	}

	public void setSize(int width, int height) {
		getShell().setSize(width, height);
	}

	public void setValue(String value) {
		text.setText(value);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
