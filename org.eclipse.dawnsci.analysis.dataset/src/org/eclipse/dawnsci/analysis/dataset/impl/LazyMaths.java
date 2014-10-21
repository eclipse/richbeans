/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.dataset.impl;

import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mathematics class for lazy datasets
 */
public final class LazyMaths {
	/**
	 * Setup the logging facilities
	 */
	protected static final Logger logger = LoggerFactory.getLogger(LazyMaths.class);

	/**
	 * @param data
	 * @param axis (can be negative)
	 * @return sum along axis in lazy dataset
	 */
	public static Dataset sum(final ILazyDataset data, int axis) {
		int[][] sliceInfo = new int[3][];
		int[] shape = data.getShape();
		final Dataset result = prepareDataset(axis, shape, sliceInfo);

		final int[] start = sliceInfo[0];
		final int[] stop = sliceInfo[1];
		final int[] step = sliceInfo[2];
		final int length = shape[axis];

		for (int i = 0; i < length; i++) {
			start[axis] = i;
			stop[axis] = i + 1;
			result.iadd(data.getSlice(start, stop, step));
		}

		result.setShape(AbstractDataset.squeezeShape(shape, axis));
		return result;
	}

	/**
	 * @param data
	 * @param axis (can be negative)
	 * @return product along axis in lazy dataset
	 */
	public static Dataset product(final ILazyDataset data, int axis) {
		int[][] sliceInfo = new int[3][];
		int[] shape = data.getShape();
		final Dataset result = prepareDataset(axis, shape, sliceInfo);
		result.fill(1);

		final int[] start = sliceInfo[0];
		final int[] stop = sliceInfo[1];
		final int[] step = sliceInfo[2];
		final int length = shape[axis];

		for (int i = 0; i < length; i++) {
			start[axis] = i;
			stop[axis] = i + 1;
			result.imultiply(data.getSlice(start, stop, step));
		}

		result.setShape(AbstractDataset.squeezeShape(shape, axis));
		return result;
	}
	
	public static Dataset mean(ILazyDataset data, int[] ignoreAxes) {
		
		int[] shape = data.getShape();
		boolean first = true;
		Dataset average = null;
		int count = 1;
		
		PositionIterator iter = new PositionIterator(shape, ignoreAxes);
		iter.toString();
		int[] pos = iter.getPos();
		
		while (iter.hasNext()) {
			
			int[] end = pos.clone();
			for (int i = 0; i<pos.length;i++) {
				end[i]++;
			}

			for (int i = 0; i < ignoreAxes.length; i++){
				end[ignoreAxes[i]] = shape[ignoreAxes[i]];
			}

			int[] st = pos.clone();
			for (int i = 0; i < st.length;i++) st[i] = 1;
			
			DoubleDataset ds = new DoubleDataset((Dataset)data.getSlice(pos,end,st));
			ds.squeeze();

			if (first) {
				average = ds;
				count++;
				first = false;
			} else {
				ds.isubtract(average);
				ds.idivide(count++);
				average.iadd(ds);// = Maths.add(average, ds);
			}
		}
		
		return average;
	}

	private static Dataset prepareDataset(int axis, int[] shape, int[][] sliceInfo) {
		int rank = shape.length;
		if (axis < 0)
			axis += rank;
		if (axis < 0 || axis >= rank) {
			logger.error("Axis argument is outside allowed range");
			throw new IllegalArgumentException("Axis argument is outside allowed range");
		}

		sliceInfo[0] = new int[rank];
		sliceInfo[1] = shape.clone();
		sliceInfo[2] = new int[rank];
		Arrays.fill(sliceInfo[2], 1);

		final int[] nshape = shape.clone();
		nshape[axis] = 1;

		return DatasetFactory.zeros(nshape, Dataset.FLOAT64);
	}
}
