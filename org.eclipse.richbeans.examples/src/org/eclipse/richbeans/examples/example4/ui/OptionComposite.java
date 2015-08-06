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
package org.eclipse.richbeans.examples.example4.ui;

import org.eclipse.richbeans.widgets.wrappers.BooleanWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class OptionComposite extends Composite {

	private BooleanWrapper showAxes, showTitle, showLegend, showData;
	
	public OptionComposite(Composite parent, int style) {
		super(parent, style);
		
        setLayout(new GridLayout(2,false));
        
        showAxes = new BooleanWrapper(this, SWT.NONE);
        showAxes.setLabel("Show axes");
        
        showTitle = new BooleanWrapper(this, SWT.NONE);
        showTitle.setLabel("Show title");

        showLegend = new BooleanWrapper(this, SWT.NONE);
        showLegend.setLabel("Show legend");
        
        showData = new BooleanWrapper(this, SWT.NONE);
        showData.setLabel("Show data");

	}
	public BooleanWrapper getShowAxes() {
		return showAxes;
	}
	public BooleanWrapper getShowTitle() {
		return showTitle;
	}
	public BooleanWrapper getShowLegend() {
		return showLegend;
	}
	public BooleanWrapper getShowData() {
		return showData;
	}

}
