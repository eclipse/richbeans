/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.slicing.api.system;

/**
 * Mode for slicing, whether the slicing allows multiple ranges
 * to be defined or not.
 * 
 * @author fcp94556
 *
 */
public enum RangeMode {

	NO_RANGES, SINGLE_RANGE, MULTI_RANGE;
	
	public boolean isRange() {
		return this==SINGLE_RANGE || this==MULTI_RANGE;
	}
}
