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
package org.eclipse.richbeans.examples.example6.ui;

import java.text.DecimalFormat;

import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.decorator.BoundsDecorator;
import org.eclipse.richbeans.widgets.decorator.FieldDecorator;
import org.eclipse.richbeans.widgets.decorator.FloatDecorator;
import org.eclipse.richbeans.widgets.decorator.IntegerDecorator;
import org.eclipse.richbeans.widgets.decorator.TextFieldDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This composite uses decorators over a Text widget to 
 * achieve the same result as using ScaleBox.
 * 
 * @author fcp94556
 *
 */
public class DecoratorComposite extends Composite {

	private FieldDecorator<Text> xDeco,yDeco;

	public DecoratorComposite(Composite parent, int style) {
		super(parent, style);
		createContent();
	}

	private void createContent() {
		
		setLayout(new GridLayout(2, false));
		
		Label label = new Label(this, SWT.NONE);
		label.setText("x");
		
		Text x = new Text(this, SWT.NONE);
		x.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final BoundsDecorator xbounds = new FloatDecorator(x);
		xbounds.setMinimum(0);
		xbounds.setMaximum(100000);
		xbounds.setDecimalFormat("00.###E0");
    	this.xDeco = new TextFieldDecorator(x, xbounds);
		
		label = new Label(this, SWT.NONE);
		label.setText("y");
		
		Text y = new Text(this, SWT.NONE);
		y.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		final IntegerDecorator ybounds = new IntegerDecorator(y);
		ybounds.setRequireIntegers(true);
		ybounds.setMinimum(0);
		ybounds.setMaximum(xbounds);
		this.yDeco = new TextFieldDecorator(y, ybounds);

	}

	
	public IFieldWidget getX() {
		return xDeco;
	}
	
	public IFieldWidget getY() {
		return yDeco;
	}

}
