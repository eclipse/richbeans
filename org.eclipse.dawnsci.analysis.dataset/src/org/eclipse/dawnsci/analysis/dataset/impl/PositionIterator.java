/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Peter Chang - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.dawnsci.analysis.dataset.impl;

import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.dataset.Slice;


/**
 * <p>Class to provide iteration through a dataset</p>
 * <p>Instantiate an iterator and use it in a while loop:
 * <pre>
 * Dataset ds = DatasetUtils.linSpace(0,10,0.25, Dataset.FLOAT64);
 * PositionIterator iter = ds.getPositionIterator();
 * int[] pos = iter.getPos()
 *
 * while (iter.hasNext()) {
 *     ds.set(1.2, pos);
 * }
 * </pre>
 *
 */
public class PositionIterator extends IndexIterator {
	final private int[] shape;
	private int[] start;
	private int[] stop;
	private int[] step;
	final private int endrank;

	final private boolean[] omit; // axes to miss out

	/**
	 * position in dataset
	 */
	private int[] pos;
	private boolean once;

	/**
	 * Constructor for an iterator over elements of a dataset that are within
	 * the shape
	 *
	 * @param shape
	 */
	public PositionIterator(int[] shape) {
		this(shape, null);
	}

	/**
	 * Constructor for an iterator that misses out an axis
	 * @param shape
	 * @param axis missing axis
	 */
	public PositionIterator(int[] shape, int axis) {
		this(shape, new int[] {axis});
	}

	/**
	 * Constructor for an iterator that misses out several axes
	 * @param shape
	 * @param axes missing axes, can be null for full dataset
	 */
	public PositionIterator(int[] shape, int[] axes) {
		this.shape = shape;
		int rank = shape.length;
		endrank = rank - 1;

		omit = new boolean[rank];
		if (axes != null) {
			for (int a : axes) {
				if (a < 0) {
					a += rank;
				}
				if (a >= 0 && a <= endrank) {
					omit[a] = true;
				} else if (a > endrank) {
					throw new IllegalArgumentException("Specified axis exceeds dataset rank");
				}
			}
		}
		start = new int[rank];
		stop = shape.clone();
		step = new int[rank];
		pos = new int[rank];
		Arrays.fill(step, 1);
		reset();
	}

	/**
	 * Constructor for an iterator that misses out several axes
	 * @param shape
	 * @param slice
	 * @param axes missing axes
	 */
	public PositionIterator(int[] shape, Slice[] slice, int[] axes) {
		this(shape, axes);
		Slice.convertFromSlice(slice, shape, start, stop, step);
		reset();
	}

	/**
	 * Constructor for an iterator that misses out several axes
	 * @param shape
	 * @param start
	 * @param stop
	 * @param step
	 * @param axes missing axes
	 */
	public PositionIterator(int[] shape, int[] start, int[] stop, int[] step, int[] axes) {
		this(shape, axes);
		int rank = shape.length;
		if (step == null) {
			this.step = new int[rank];
			Arrays.fill(this.step, 1);
		} else {
			this.step = step;
			for (int s : step) {
				if (s < 0) {
					throw new UnsupportedOperationException("Negative steps not implemented");
				}
			}
		}
		if (start == null) {
			this.start = new int[rank];
		} else {
			this.start = start;
		}
		if (stop == null) {
			this.stop = shape.clone();
		} else {
			this.stop = stop;
		}
		reset();
	}

	@Override
	public boolean hasNext() {
		// now move on one position
		if (once) {
			once = false;
			return true;
		}
		for (int j = endrank; j >= 0; j--) {
			if (omit[j]) {
				continue;
			}
			pos[j] += step[j];
			if (pos[j] >= stop[j]) {
				pos[j] = start[j];
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getPos() {
		return pos;
	}

	/**
	 * @return omit array - array where true means miss out
	 */
	public boolean[] getOmit() {
		return omit;
	}

	@Override
	public void reset() {
		for (int i = 0; i <= endrank; i++)
			pos[i] = start[i];

		int j = 0;
		for (; j <= endrank; j++) {
			if (!omit[j])
				break;
		}
		if (j > endrank) {
			once = true;
			return;
		}

		if (omit[endrank]) {
			pos[endrank] = start[endrank];
			
			for (int i = endrank - 1; i >= 0; i--) {
				if (!omit[i]) {
					pos[i] -= step[i];
					break;
				}
			}
		} else {
			pos[endrank] -= step[endrank];
		}
	}

	@Override
	public int[] getShape() {
		return shape;
	}
}
