/*-
 * Copyright (c) 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.dataset.impl;

import java.util.Arrays;

import org.eclipse.dawnsci.analysis.api.dataset.Slice;

/**
 * Class to represent a slice through all dimensions of a multi-dimensional dataset. A slice
 * comprises a starting position array, a stopping position array (not included) and a stepping size array.
 */
public class SliceND {
	private int[] lstart;
	private int[] lstop;
	private int[] lstep;
	private int[] lshape;

	private boolean allData;

	/**
	 * Construct ND slice for whole of shape
	 * @param shape
	 */
	public SliceND(final int[] shape) {
		int rank = shape.length;
		lstart = new int[rank];
		lstop  = shape.clone();
		lstep  = new int[rank];
		Arrays.fill(lstep, 1);
		lshape = shape.clone();
		allData = true;
	}

	/**
	 * Construct ND slice from an array of 1D slices
	 * @param shape
	 * @param slice
	 */
	public SliceND(final int[] shape, Slice... slice) {
		final int rank = shape.length;
		lstart = new int[rank];
		lstop  = new int[rank];
		lstep  = new int[rank];
		lshape  = new int[rank];
		final int length = slice == null ? 0 : slice.length;
		int i = 0;
		if (slice != null && length <= rank) {
			for (; i < length; i++) {

				Slice s = slice[i];
				if (s == null) {
					lstart[i] = 0;
					lstep[i] = 1;
					lshape[i] = lstop[i] = shape[i];
					continue;
				}
				int n;
				if (s.getStart() == null) {
					lstart[i] = s.getStep() > 0 ? 0 : shape[i] - 1;
				} else {
					n = s.getStart();
					if (n < 0)
						n += shape[i];
					if (n < 0 || n >= shape[i]) {
						throw new IllegalArgumentException(String.format(
								"Start is out of bounds: %d is not in [%d,%d)", n, s.getStart(), shape[i]));
					}
					lstart[i] = n;
				}

				if (s.getStop() == null) {
					lstop[i] = s.getStep() > 0 ? shape[i] : -1;
				} else {
					n = s.getStop();
					if (n < 0)
						n += shape[i];
					if (n < 0 || n > shape[i]) {
						throw new IllegalArgumentException(String.format("Stop is out of bounds: %d is not in [%d,%d)",
								n, s.getStop(), shape[i]));
					}
					lstop[i] = n;
				}

				n = s.getStep();
				if (n == 0) {
					throw new IllegalArgumentException("Step cannot be zero");
				}
				if (n > 0) {
					if (lstart[i] > lstop[i])
						throw new IllegalArgumentException("Start must be less than stop for positive steps");
					lshape[i] = (lstop[i] - lstart[i] - 1) / n + 1;
				} else {
					if (lstart[i] < lstop[i])
						throw new IllegalArgumentException("Start must be greater than stop for negative steps");
					lshape[i] = (lstop[i] - lstart[i] + 1) / n + 1;
				}
				lstep[i] = n;
			}
		}
		for (; i < rank; i++) {
			lstart[i] = 0;
			lstep[i] = 1;
			lshape[i] = lstop[i] = shape[i];
		}
		allData = Arrays.equals(shape, lshape);
		if (allData) {
			for (i = 0; i < rank; i++) {
				if (lstep[i] < 0) {
					allData = false;
					break;
				}
			}
		}
	}

	/**
	 * Construct ND slice parameters
	 * 
	 * @param shape
	 * @param start
	 *            can be null
	 * @param stop
	 *            can be null
	 * @param step
	 *            can be null
	 */
	public SliceND(final int[] shape, final int[] start, final int[] stop, final int[] step) {
		// number of steps, or new shape, taken in each dimension is
		// shape = (stop - start + step - 1) / step if step > 0
		// (stop - start + step + 1) / step if step < 0
		//
		// thus the final index in each dimension is
		// start + (shape-1)*step

		int rank = shape.length;

		if (start == null) {
			lstart = new int[rank];
		} else {
			lstart = start.clone();
		}
		if (stop == null) {
			lstop = shape.clone();
		} else {
			lstop = stop.clone();
		}
		if (step == null) {
			lstep = new int[rank];
			Arrays.fill(lstep, 1);
		} else {
			lstep = step.clone();
		}
		
		if (lstart.length != rank || lstop.length != rank || lstep.length != rank) {
			throw new IllegalArgumentException("No of indexes does not match data dimensions: you passed it start="
					+ lstart.length + ", stop=" + lstop.length + ", step=" + lstep.length + ", and it needs " + rank);
		}

		lshape = new int[rank];

		// sanitise input
		for (int i = 0; i < rank; i++) {
			final int d = lstep[i];
			final int s = shape[i];
			int b;
			int e;
			if (d == 0) {
				throw new IllegalArgumentException("The step array is not allowed any zero entries: " + i
						+ "-th entry is zero");
			} else if (d > 0) {
				b = lstart[i];
				if (b < 0) {
					lstart[i] = b += s;
				}
				if (b < 0 || b >= s) {
					throw new IllegalArgumentException("Start entry is outside bounds");
				}

				e = lstop[i];
				if (e < 0) {
					lstop[i] = e += s;
				}
				if (e < -1 || e > s) {
					throw new IllegalArgumentException("Stop entry is outside bounds");
				}
				if (b == e) {
					throw new IllegalArgumentException("Same indices in start and stop");
				} else if (b > e) {
					throw new IllegalArgumentException("Start=" + b + " and stop=" + e
							+ " indices are incompatible with step=" + d);
				}
				lshape[i] = (e - b - 1) / d + 1;
			} else {
				if (start != null) {
					b = lstart[i];
					if (b < 0) {
						lstart[i] = b += s;
					}
					if (b < 0 || b >= s) {
						throw new IllegalArgumentException("Start entry is outside bounds");
					}
				} else {
					lstart[i] = b = s - 1;
				}

				if (stop != null) {
					e = lstop[i];
					if (e < d) {
						lstop[i] = e += s;
					}
					if (e < d || e > s) {
						throw new IllegalArgumentException("Stop entry is outside bounds");
					}
				} else {
					lstop[i] = e = -1;
				}
				if (b == e) {
					throw new IllegalArgumentException("Same indices in start and stop");
				} else if (b < e) {
					throw new IllegalArgumentException("Start=" + lstart[i] + " and stop=" + lstop[i]
							+ " indices are incompatible with step=" + d);
				}
				lshape[i] = (e - b + 1) / d + 1;
			}
		}
		allData = Arrays.equals(shape, lshape);
		if (allData) {
			for (int i = 0; i < rank; i++) {
				if (lstep[i] < 0) {
					allData = false;
					break;
				}
			}
		}
	}

	/**
	 * @return resulting shape
	 */
	public int[] getShape() {
		return lshape;
	}

	public int[] getStart() {
		return lstart;
	}

	public int[] getStop() {
		return lstop;
	}

	public int[] getStep() {
		return lstep;
	}

	public boolean isAll() {
		return allData;
	}

	/**
	 * Convert to a slice array
	 * @return a slice array
	 */
	public Slice[] convertToSlice() {
		int orank = lshape.length;

		Slice[] slice = new Slice[orank];

		for (int j = 0; j < orank; j++) {
			slice[j] = new Slice(lstart[j], lstop[j], lstep[j]);
		}

		return slice;
	}

	@Override
	public String toString() {
		final int rank = lshape.length;
		if (rank == 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < rank; i++) {
			Slice.appendSliceToString(s, lshape[i], lstart[i], lstop[i], lstep[i]);
			s.append(',');
		}
	
		return s.substring(0, s.length()-1);
	}
}