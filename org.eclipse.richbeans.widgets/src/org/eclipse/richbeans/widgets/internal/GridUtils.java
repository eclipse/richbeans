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
package org.eclipse.richbeans.widgets.internal;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Class to deal with setting items in a grid layout invisible. Allows batch updating of controls to reduce flicker
 */
public class GridUtils {
	/**
	 * Simplified version of setVisibleAndLayout(...) which cannot cause
	 * a memory leak. Does not work with startMultiLayout() and endMultiLayout()
	 * 
	 * You need to call layout once on the parent widget after using this method. For instance:
	 * 
	 * 	    
	        GridUtils.setVisible(wLabel, wVisible);
			GridUtils.setVisible(w,      wVisible);
			GridUtils.setVisible(kLabel, kVisible);
			GridUtils.setVisible(kStart, kVisible);
			getShell().layout();
	 * 
	 * @param widget
	 * @param isVisible
	 */
	public static void setVisible(final Control widget, final boolean isVisible) {
		
		if (widget == null) return;
		if (widget.getLayoutData() instanceof GridData) {
			final GridData data = (GridData) widget.getLayoutData();
			data.exclude = !isVisible;
		}
		widget.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param area
	 */
	public static void removeMargins(Composite area) {
		removeMargins(area, 0, 0);
	}
	
	/**
	 * 
	 * @param area
	 */
	public static void removeMargins(Composite area, int hspacing, int vspacing) {
		final GridLayout layout = (GridLayout)area.getLayout();
		if (layout==null) return;
		layout.horizontalSpacing=hspacing;
		layout.verticalSpacing  =vspacing;
		layout.marginBottom     =0;
		layout.marginTop        =0;
		layout.marginLeft       =0;
		layout.marginRight      =0;
		layout.marginHeight     =0;
		layout.marginWidth      =0;

	}
}
