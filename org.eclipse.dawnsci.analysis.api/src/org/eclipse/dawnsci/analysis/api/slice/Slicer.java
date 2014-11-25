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

package org.eclipse.dawnsci.analysis.api.slice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;

/**
 * Methods for slicing data using visit patterns.
 * This is Jakes algorithm moved out of the conversion API to make more use of it.
 */
public class Slicer {

	public static IDataset getFirstSlice(ILazyDataset lz, Map<Integer, String> sliceDimensions) throws Exception {
		
        return visit(lz, sliceDimensions, "Slice", null);
	}

	/**
	 * This method provides a way to slice over a lazy dataset providing the values
	 * in each dimension for the slice using a visit pattern.
	 * 
	 * @param lz
	 * @param sliceDimensions
	 * @param visitor
	 * @throws Exception 
	 */
	public static void visitAll(ILazyDataset lz, Map<Integer, String> sliceDimensions, SliceVisitor visitor) throws Exception {
		visitAll(lz, sliceDimensions, null, visitor);
	}

	/**
	 * This method provides a way to slice over a lazy dataset providing the values
	 * in each dimension for the slice using a visit pattern.
	 * 
	 * Block until complete.
	 * 
	 * @param lz
	 * @param sliceDimensions
	 * @param nameFragment may be null
	 * @param visitor
	 * @throws Exception 
	 */
	public static void visitAll(ILazyDataset lz, Map<Integer, String> sliceDimensions, String nameFragment, SliceVisitor visitor) throws Exception {
		visit(lz, sliceDimensions, nameFragment, visitor);
	}
	
	/**
	 * 
	 * @param lz
	 * @param sliceDimensions
	 * @return size of expected iteration, slightly faster than calling <code>getSlices(...).size()</code>
	 */
	public static int getSize(ILazyDataset lz, Map<Integer, String> sliceDimensions) {

		final int[] fullDims = lz.getShape();
		
		//Construct Slice String

		if (sliceDimensions == null) sliceDimensions = new HashMap<Integer, String>();
		
		Slice[] slices = getSliceArrayFromSliceDimensions(sliceDimensions, lz.getShape());
		
		//create array of ignored axes values
		int[] axes = getDataDimensions(fullDims, sliceDimensions);

		//Take view of original lazy dataset removing start/stop/step
		//Makes the iteration simpler
		ILazyDataset lzView = lz.getSliceView(slices);

		PositionIterator pi = new PositionIterator(lzView.getShape(), axes);
		int size = 0;
		while (pi.hasNext()) size++; // TODO Is this loop required or is slices.length the same?
		
		return size;

	}
	
	/**
	 * 
	 * @param lz
	 * @param sliceDimensions
	 * @param nameFragment
	 * @param visitor - if null just returns the first slice
	 * @return null if visitor != null otherwise returns first slice.
	 * @throws Exception
	 */
	private static IDataset visit(ILazyDataset lz, Map<Integer, String> sliceDimensions, String nameFragment, SliceVisitor visitor) throws Exception {
		
		Queue<SliceInfo> slices = getSlices(lz, sliceDimensions, nameFragment);
		
		for (SliceInfo sliceInfo : slices) {
			
			IDataset data = sliceInfo.slice();
			if (visitor!=null) {
			    visitor.visit(data, sliceInfo.getSlice(), sliceInfo.getData().getShape());
			} else {
				return data;
			}
			
			if (visitor.isCancelled()) break;
		}

		return null;
	}
	
	public static Queue<SliceInfo> getSlices(ILazyDataset lz, Map<Integer, String> sliceDimensions) {
		return getSlices(lz, sliceDimensions, null);
	}
	
	public static Queue<SliceInfo> getSlices(ILazyDataset lz, Map<Integer, String> sliceDimensions, String nameFragment) {
		
		
		final int[] fullDims = lz.getShape();
		
		//Construct Slice String

		if (sliceDimensions == null) sliceDimensions = new HashMap<Integer, String>();
		
		Slice[] slices = getSliceArrayFromSliceDimensions(sliceDimensions, lz.getShape());
		
		//create array of ignored axes values
		int[] axes = getDataDimensions(fullDims, sliceDimensions);

		//Take view of original lazy dataset removing start/stop/step
		//Makes the iteration simpler
		ILazyDataset lzView = lz.getSliceView(slices);

		PositionIterator pi = new PositionIterator(lzView.getShape(), axes);
		int[] pos = pi.getPos();
		final int[] viewDims = lzView.getShape();
		
		final Queue<SliceInfo> ret = new LinkedList<SliceInfo>();
		while (pi.hasNext()) {

			int[] end = pos.clone();
			for (int i = 0; i<pos.length;i++) {
				end[i]++;
			}

			for (int i = 0; i < axes.length; i++){
				end[axes[i]] = viewDims[axes[i]];
			}

			int[] st = pos.clone();
			for (int i = 0; i < st.length;i++) st[i] = 1;

			Slice[] sa = Slice.convertToSlice(pos, end, st);
			
			//TODO dont convert to slices just to create string - use create string on start stop step
			String sliceName = Slice.createString(sa);			
			sliceName = (nameFragment!=null ? nameFragment : "") + " ("+ sliceName+")";
			
			ret.add(new SliceInfo(sliceName, sa, lzView));
		}
		return ret;
	}

	/**
	 * This method provides a way to slice over a lazy dataset providing the values
	 * in each dimension for the slice using a visit pattern. The call on to the 
	 * SliceVisitor is done in a parallel way by delegating the calling of the visit method
	 * to a thread pool.
	 * 
	 * Blocks until complete or timeout of 5s is reached.
     *
	 * @param lz
	 * @param sliceDimensions
	 * @param nameFragment may be null
	 * @param visitor - if used with visitAllParallel, visit should not normally throw
	 * an exception or if it does it will stop the execution but not throw back to the
	 * calling code. Instead an internal RuntimeException is thrown back to the fork/join API.
	 * @throws Exception 
	 */
	public static void visitAllParallel(ILazyDataset lz, Map<Integer, String> sliceDimensions, String nameFragment, final SliceVisitor visitor) throws Exception {
        visitAllParallel(lz, sliceDimensions, nameFragment, visitor, 5000);
	}

	/**
	 * This method provides a way to slice over a lazy dataset providing the values
	 * in each dimension for the slice using a visit pattern. The call on to the 
	 * SliceVisitor is done in a parallel way by delegating the calling of the visit method
	 * to a thread pool.
	 * 
	 * Blocks until complete or timeout is reached.
	 * 
	 * @param lz
	 * @param sliceDimensions
	 * @param nameFragment may be null
	 * @param visitor - if used with visitAllParallel, visit should not normally throw
	 * an exception or if it does it will stop the execution but not throw back to the
	 * calling code. Instead an internal RuntimeException is thrown back to the fork/join API.
	 * @param timeout in ms.
	 * @throws Exception 
	 */
	public static void visitAllParallel(ILazyDataset lz, Map<Integer, String> sliceDimensions, String nameFragment, final SliceVisitor visitor, long timeout) throws Exception {

		// Just farm out each slice to a different runnable.
		final ForkJoinPool pool = new ForkJoinPool();
		
		final SliceVisitor parallel = new SliceVisitor() {

			@Override
			public void visit(final IDataset slice, final Slice[] slices, final int[] shape) throws Exception {
				
				pool.execute(new Runnable() {
					
					@Override
					public void run() {
						try {
						    visitor.visit(slice, slices, shape);
						} catch (Throwable ne) {
							ne.printStackTrace();
							// TODO Fix me - should runtime exception really be thrown back to Fork/Join?
							//throw new RuntimeException(ne.getMessage(), ne);
						}
					}
				});
			}

			@Override
			public boolean isCancelled() {
				return visitor.isCancelled();
			}
		};
		
		Slicer.visitAll(lz, sliceDimensions, nameFragment, parallel);
		
		pool.shutdown();
		
		boolean allDone = pool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		if (!allDone) throw new TimeoutException("The timeout of "+timeout+" was exceeded for parallel run, please increase it!");
	}
	
	public static int[] getDataDimensions(int[] shape, Map<Integer, String> sliceDimensions) {

		//assume single image/line
		if (sliceDimensions == null) {
			int[] dd = new int[shape.length];
			for (int i = 0; i < shape.length; i++) dd[i] = i;
			return dd;
		}
		
		//create array of ignored axes values
		Set<Integer> axesSet = new HashSet<Integer>();
		for (int i = 0; i < shape.length; i++) axesSet.add(i);
		axesSet.removeAll(sliceDimensions.keySet());
		int[] axes = new int[axesSet.size()];
		int count = 0;
		Iterator<Integer> iter = axesSet.iterator();
		while (iter.hasNext()) axes[count++] = iter.next();

		return axes;
	}
	
	public static Slice[] getSliceArrayFromSliceDimensions(Map<Integer, String> sliceDimensions, int[] shape) {
		
		//Construct Slice String
		StringBuilder sb = new StringBuilder();
		if (sliceDimensions == null) sliceDimensions = new HashMap<Integer, String>();
		
		for (int i = 0; i < shape.length; i++) {
			if (sliceDimensions.containsKey(i)) {
				String s = sliceDimensions.get(i);
				if (s.contains("all")) s = ":";
				sb.append(s);
				sb.append(",");
			} else {
				sb.append(":");
				sb.append(',');
			}
		}
		
		sb.deleteCharAt(sb.length()-1);
		return Slice.convertFromString(sb.toString());
		
	}
}
