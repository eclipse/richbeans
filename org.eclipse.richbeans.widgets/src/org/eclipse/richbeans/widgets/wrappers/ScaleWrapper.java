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

package org.eclipse.richbeans.widgets.wrappers;

import org.eclipse.richbeans.api.event.ValueEvent;
import org.eclipse.richbeans.api.widget.IFieldWidget;
import org.eclipse.richbeans.widgets.ScaleComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

/**
 * 
 * @author uij85458 - Joel Ogden
 *
 *	This is rushed job to get the code working asap
 *	I will fix this when I can -> asap.
 */

public class ScaleWrapper extends ScaleComposite implements IFieldWidget
{
	protected Scale scale;
	private Listener listener;
	
	private double minimumValue = 0, maximumValue = 100;
	
	public ScaleWrapper(Composite parent, int style)
	{
		
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		
		this.scale = new Scale(this, style);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		mainControl = scale;
		
		this.listener = new Listener()
		{
			
			@Override
			public void handleEvent(Event e)
			{
				final ValueEvent evt = new ValueEvent(scale, getFieldName());
				evt.setValue(getValue());
				eventDelegate.notifyValueListeners(evt);
			}
			
		};
		scale.addListener(SWT.Selection, listener);
	}

	
	@Override
	protected void checkSubclass () {
	}
	/**
	 * @param active the active to set
	 */
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		scale.setVisible(active);
	}
	
	@Override
	public Object getValue()
	{
		// TODO Auto-generated method stub
		double min = scale.getMinimum();
		double max = scale.getMaximum();
		double current = scale.getSelection();
		
		double percentageSelection = (current - min) / (max-min);
		
		double value = (percentageSelection * (this.maximumValue - this.minimumValue) ) + this.minimumValue;
		
		return value;
	}

	@Override
	public void setValue(Object value)
	{
		if (value==null) {
			scale.setSelection(0);
			return;
		}
		
		if (value instanceof String) 
		{
			value = Double.valueOf((String)value);
		}
		
		setCurrentValue(((Number)value).doubleValue());
		
		if (this.notifyType!=null&&(notifyType==NOTIFY_TYPE.ALWAYS||notifyType==NOTIFY_TYPE.VALUE_CHANGED)) {
			final ValueEvent evt = new ValueEvent(scale, getFieldName());
			evt.setValue(getValue());
			eventDelegate.notifyValueListeners(evt);
		}
		
	}
	
	private void setCurrentValue(double value)
	{
		double scalePercentValue = (value - this.minimumValue) / (this.maximumValue - this.minimumValue);
		int valueOnScale = (int) (scalePercentValue * scale.getMaximum());
		scale.setSelection(valueOnScale);
	}
	
	public void setMaximumValue(double maxValue)
	{
		this.maximumValue = maxValue;
	}
	public void setMinimumValue(double minValue)
	{
		this.minimumValue = minValue;
	}
	
	
	public void setMaximumScale(int i)
	{
		scale.setMinimum(0);
		scale.setMaximum(i);
	}
	
	public void setIncrement(int d)
	{
		scale.setIncrement(d);
		
	}
	
}
