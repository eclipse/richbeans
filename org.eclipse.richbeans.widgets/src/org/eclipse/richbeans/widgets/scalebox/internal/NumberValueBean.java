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

package org.eclipse.richbeans.widgets.scalebox.internal;

import java.util.Iterator;
import java.util.List;

public abstract class NumberValueBean {

	public abstract Number getValue();
	
	public static String getString(List<? extends NumberValueBean> vals) {
		
		final StringBuilder buf = new StringBuilder();
		for (Iterator<? extends NumberValueBean> iterator = vals.iterator(); iterator.hasNext();) {
			buf.append(iterator.next().getValue());
			if (iterator.hasNext()) buf.append(", ");
		}
		
		return buf.toString();
	}

}
