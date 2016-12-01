/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.test.doe;

import java.io.Serializable;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.richbeans.annot.DOEField;

/**
 * Bean to hold slice data
 */
public class TestDimsData implements Serializable {


	@DOEField(value=1, type=java.lang.Integer.class)
	private String    sliceRange;

	private int       dimension;
	private int       axis;
	private int       slice;

	public TestDimsData() {

	}

	public TestDimsData(final int dim) {
		this.dimension = dim;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + axis;
		result = prime * result + dimension;
		result = prime * result + slice;
		result = prime * result + ((sliceRange == null) ? 0 : sliceRange.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestDimsData other = (TestDimsData) obj;
		if (axis != other.axis)
			return false;
		if (dimension != other.dimension)
			return false;
		if (slice != other.slice)
			return false;
		if (sliceRange == null) {
			if (other.sliceRange != null)
				return false;
		} else if (!sliceRange.equals(other.sliceRange))
			return false;
		return true;
	}

	public String getSliceRange() {
		return sliceRange;
	}

	public void setSliceRange(String sliceRange) {
		this.sliceRange = sliceRange;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public int getAxis() {
		return axis;
	}

	public void setAxis(int axis) {
		this.axis = axis;
	}

	public int getSlice() {
		return slice;
	}

	public void setSlice(int slice) {
		this.slice = slice;
	}

	@Override
	public String toString() {
		try {
			return BeanUtils.describe(this).toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
