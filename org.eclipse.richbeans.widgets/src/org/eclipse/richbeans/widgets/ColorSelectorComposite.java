/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Joel Ogden
 *******************************************************************************/
package org.eclipse.richbeans.widgets;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.swt.widgets.Composite;

/**
 * @author uij85458 - Joel Ogden
 */


/**
 * This is very much a rushed job to get it done in time, and may have a lot of bugs
 * I will improve this asap.
 * @author uij85458
 *
 */
public abstract class ColorSelectorComposite extends FieldComposite
{

	protected IPropertyChangeListener changeListener;
	protected ColorSelector colourSelector;
	
	public ColorSelectorComposite(Composite parent, int style)
	{
		super(parent, style);
	}
	
	private boolean scaleCreated = false;
	

	/**
	 * Called to add a listener which will be run when the ColourSelector is pressed.
	 * @param l 
	 */
	public void addListener(final IPropertyChangeListener l) {
		if (changeListener!=null) colourSelector.removeListener(changeListener);
		changeListener = l;
		if (!scaleCreated) {
			createButton();
			scaleCreated = true;
		}
		if (colourSelector != null) colourSelector.addListener(changeListener);
	}
	
	@Override
	public void dispose() {
		if (colourSelector!=null&&changeListener!=null) {
			colourSelector.removeListener(changeListener);
			((IFieldWidget) colourSelector).dispose();
		}
		super.dispose();
	}
	
	protected void createButton() {
		
		if(this.colourSelector!=null) return;
		this.colourSelector = new ColorSelector(this);
		layout();
	}

	
	
}
