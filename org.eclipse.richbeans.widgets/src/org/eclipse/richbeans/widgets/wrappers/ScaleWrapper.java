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
 * Used to wrap the SWT scale object to allow then to work with BeanUI.
 * 
 * @author Joel Ogden
 *
 *	
 */

public class ScaleWrapper extends ScaleComposite implements IFieldWidget
{
	private Listener listener;
	
	// default min and max values
	private double minimumValue = 0, maximumValue = 100;
	
	public ScaleWrapper(Composite parent, int style)
	{
		
		super(parent, SWT.NONE);
		
		// set the layout to a simple grid
		this.setLayout(new GridLayout(1, false));
		
		// create the scale
		this.scale = new Scale(this, style);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		mainControl = scale;
		
		// set the listener
		// used to detect change in the scale
		this.listener = new Listener()
		{
			
			@Override
			public void handleEvent(Event e)
			{
				// create and run the event with the new value
				final ValueEvent evt = new ValueEvent(scale, getFieldName());
				evt.setValue(getValue());
				eventDelegate.notifyValueListeners(evt);
			}
			
		};
		
		// add the new listener
		scale.addListener(SWT.Selection, listener);
	}

	
	@Override
	protected void checkSubclass () {
	}
	/**
	 * @param active - The active to set
	 */
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		scale.setVisible(active);
	}
	
	/**
	 * 
	 * Gets the true value of the scale. Uses the min and max to calculate this value.
	 * @return f(x) = (percentage across scale * (max - min)) + min
	 * 
	 * @see org.eclipse.richbeans.api.widget.IFieldWidget#getValue()
	 * 
	 */
	@Override
	public Object getValue()
	{
		double current = scale.getSelection();
		double min = scale.getMinimum();
		double max = scale.getMaximum();
		 		
		double percentageSelection = (current - min) / (max-min);
		
		// % across the scale
//		double percentageSelection = (current - this.minimumValue) / (this.minimumValue - this.minimumValue);
		
		double value = (percentageSelection * (this.maximumValue - this.minimumValue) ) + this.minimumValue;
		
		return value;
	}

	/**
	 * Set the selection value.
	 * @param value - The new value of the selection
	 */
	@Override
	public void setValue(Object value)
	{
		// if null set selection to the minimum
		if (value==null) {
			scale.setSelection(scale.getMinimum());
			return;
		}
		
		// if the value given is a string convert to a double
		if (value instanceof String) 
		{
			value = Double.valueOf((String)value);
		}
		
		// set the new value
		setCurrentValue(((Number)value).doubleValue());
		
		if (this.notifyType != null && 
			(
					notifyType == NOTIFY_TYPE.ALWAYS || 
					notifyType == NOTIFY_TYPE.VALUE_CHANGED
			))
		{
			final ValueEvent evt = new ValueEvent(scale, getFieldName());
			evt.setValue(getValue());
			eventDelegate.notifyValueListeners(evt);
		}
		
	}
	
	/**
	 * Set the selection to the new value, considering the min and max.
	 * @param value - The new value to set
	 */
	private void setCurrentValue(double value)
	{
		double scalePercentValue = (value - this.scale.getMinimum()) / (this.scale.getMaximum() - this.scale.getMinimum());
		int valueOnScale = (int) (scalePercentValue * scale.getMaximum());
		scale.setSelection(valueOnScale);
	}
	
	/**
	 * Set the max value the scale is able to accept. This does not mean a larger value can be set
	 * @param maxValue - The new maximum value
	 */
	public void setMaximumValue(double maxValue)
	{
		this.maximumValue = maxValue;
	}
	/**
	 * Set the min value the scale is able to accept. This does not mean a smaller value can be set
	 * @param minValue - The new minimum value
	 */
	public void setMinimumValue(double minValue)
	{
		this.minimumValue = minValue;
	}
	
	/**
	 * set the maximum the scale is able to accept. This is not the value but rather the range of precision.
	 * For example: Max scale of 10 with an increment of 1 will have 10 choices, a max scale of 100  with an increment of 10 will have also have 10 choices.
	 * The minimum is always zero.
	 * @param i - The new maximum
	 * 
	 */
	public void setMaximumScale(int i)
	{
		scale.setMinimum(0);
		scale.setMaximum(i);
	}
	/**
	 * The increment per movement of scale.
	 * @param d - The new increment amount
	 */
	public void setIncrement(int d)
	{
		scale.setIncrement(d);
		
	}
	
}
