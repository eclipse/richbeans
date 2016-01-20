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
package org.eclipse.richbeans.widgets.decorator;

import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Text;

public class IntegerArrayDecorator extends ArrayDecorator {

	public IntegerArrayDecorator(Text text) {
		this(text, ",");
	}

	public IntegerArrayDecorator(Text text, String delimiter) {
		super(text, "[-0-9∞]+", new DecimalFormat("##########0"), delimiter, -1);
	}

}
