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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;

/**
 * 
 * @author uij85458 - Joel Ogden
 *  
 */

public abstract class ScaleComposite extends FieldComposite
{
	protected SelectionListener scaleSelection;
	protected Scale scale;
	
	public ScaleComposite(Composite parent, int style)
	{
		super(parent, style);
		
	}
	
	/** NOTE Just used to make RCP Developer recognise bean **/
	private boolean scaleVisible = false;
	
	/**
	 * Called to show scale.
	 * 
	 * @param vis
	 */
	public void setScaleVisible(final boolean vis)
	{
		scaleVisible = vis;
		if (scale != null)
			scale.setVisible(vis);
		
		if (vis)
		{
			createScale();
			scale.addSelectionListener(scaleSelection);
		}
		else
		{
			if (scaleSelection != null)
				scale.removeSelectionListener(scaleSelection);
			scaleSelection = null;
		}
	}
	
	/**
	 * Returns true is scale showing.
	 * 
	 * @return true/false
	 */
	public boolean isScaleVisible()
	{
		return scaleVisible;
	}
	
	/**
	 * Called to add a scale listener which will be run when the
	 * scale is used.
	 * 
	 * @param l
	 */
	public void addScaleListener(final SelectionListener l)
	{
		if (scaleSelection != null)
			scale.removeSelectionListener(scaleSelection);
		scaleSelection = l;
		if (!scaleVisible)
		{
			createScale();
			scaleVisible = true;
			scale.setVisible(true);
		}
		if (scale != null)
			scale.addSelectionListener(scaleSelection);
	}
	
	@Override
	public void dispose()
	{
		if (scale != null && scaleSelection != null)
		{
			scale.removeSelectionListener(scaleSelection);
			scale.dispose();
		}
		super.dispose();
	}
	
	protected void createScale()
	{
		
		if (this.scale != null)
			return;
		
		this.scale = new Scale(this, SWT.PUSH)
		{
			@Override
			protected void checkSubclass()
			{
			}
			
			@Override
			public boolean forceFocus()
			{
				return false;
			}
			
			@Override
			public boolean isFocusControl()
			{
				return false;
			}
			
			@Override
			public boolean setFocus()
			{
				return false;
			}
		};
		
		// Scale not used normally.
		scale.setToolTipText("..");
		if (getLayout() instanceof GridLayout)
		{
			scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		}
		layout();
	}
	
}
