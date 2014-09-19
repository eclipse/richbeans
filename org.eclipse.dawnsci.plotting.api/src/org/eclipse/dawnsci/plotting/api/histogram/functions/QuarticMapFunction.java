/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.plotting.api.histogram.functions;

/**
 * A quartic (raised to the fourth power) mapping function 
 */
public class QuarticMapFunction extends AbstractMapFunction {
	@Override
	public String getMapFunctionName() {
		return "x^4";
	}

	@Override
	public double mapFunction(double input) {
		return input*input*input*input;
	}
}
