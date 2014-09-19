/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.metadata;

import javax.measure.unit.Unit;

/**
 * This metadata describes the unit associated with the quantity stored in a dataset
 */
public interface UnitMetadata extends MetadataType {

	/**
	 * Get unit
	 * @return unit of dataset
	 */
	public Unit<?> getUnit();
}
