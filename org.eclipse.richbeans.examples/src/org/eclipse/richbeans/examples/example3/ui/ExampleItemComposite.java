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
package org.eclipse.richbeans.examples.example3.ui;

import org.eclipse.richbeans.api.event.ValueAdapter;
import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.event.ValueListener;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.examples.example3.data.ExampleItem;
import org.eclipse.richbeans.examples.example3.data.ExampleItem.ItemChoice;
import org.eclipse.richbeans.widgets.BoundsProvider;
import org.eclipse.richbeans.widgets.scalebox.ScaleBox;
import org.eclipse.richbeans.widgets.wrappers.ComboWrapper;
import org.eclipse.richbeans.widgets.wrappers.TextWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ExampleItemComposite extends Composite {

	private TextWrapper  itemName;
	private ScaleBox     x,y;
	private ComboWrapper choice;
	private ScaleBox     r,theta;

	public ExampleItemComposite(Composite parent, int style) {
		super(parent, style);
		createContent();
	}

	private void createContent() {
		
		setLayout(new GridLayout(2, false));
		
		Label label = new Label(this, SWT.NONE);
		label.setText("Name");
	
		this.itemName = new TextWrapper(this, SWT.BORDER);
		itemName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		label = new Label(this, SWT.NONE);
		label.setText("Coordinate System");
		
		choice = new ComboWrapper(this, SWT.READ_ONLY);
		choice.setItems(ExampleItem.ItemChoice.names());
		choice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		choice.addValueListener(new ValueAdapter("Conditional Visibility Example") {	
			@Override
			public void valueChangePerformed(ValueEvent e) {
				updateVisibility();
			}
		});
		choice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		x = new ScaleBox(this, SWT.NONE);
		x.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		x.setLabel("x     ");
		x.setUnit("m");
		x.setMinimum(0);
		x.setMaximum(1000);
				
		y = new ScaleBox(this, SWT.NONE);
		y.setLabel("y     ");
		y.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		y.setUnit("m");
		y.setMinimum(0);
		y.setMaximum(new BoundsProvider() {
			
			@Override
			public double getBoundValue() {
				return x.getNumericValue()*10;
			}
			
			@Override
			public void addValueListener(ValueListener l) {
				x.addValueListener(l);
			}
		});
		
		r = new ScaleBox(this, SWT.NONE);
		r.setLabel("Radius      ");
		r.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		r.setUnit("m");
		r.setMinimum(0);
		r.setMaximum(1000);
		
		theta = new ScaleBox(this, SWT.NONE);
		theta.setLabel("Angle (θ)   ");
		theta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		theta.setUnit("rad");
		theta.setMinimum(0);
		theta.setMaximum(2*Math.PI);

		setVisible(r,     false);
		setVisible(theta, false);

	}

	public IFieldWidget getItemName() {
		return itemName;
	}
	
	public IFieldWidget getX() {
		return x;
	}
	
	public IFieldWidget getY() {
		return y;
	}
	
	public IFieldWidget getR() {
		return r;
	}
	
	public IFieldWidget getTheta() {
		return theta;
	}
	
	public IFieldWidget getChoice() {
		return choice;
	}

	void updateVisibility() {
		setVisible(x,     choice.getValue() == ItemChoice.XY);
		setVisible(y,     choice.getValue() == ItemChoice.XY);
		setVisible(r,     choice.getValue() == ItemChoice.POLAR);
		setVisible(theta, choice.getValue() == ItemChoice.POLAR);
		layout();
	}
	public static void setVisible(final Control widget, final boolean isVisible) {
		
		if (widget == null) return;
		if (widget.getLayoutData() instanceof GridData) {
			final GridData data = (GridData) widget.getLayoutData();
			data.exclude = !isVisible;
		}
		widget.setVisible(isVisible);
	}

}
