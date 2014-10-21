package org.eclipse.dawnsci.analysis.examples.dataset;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.slice.SliceVisitor;
import org.eclipse.dawnsci.analysis.api.slice.Slicer;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;
import org.junit.Test;

/**
 * @see the full slicing unit tests in plugin    uk.ac.diamond.scisoft.analysis.test
 *                                     package   uk/ac/diamond/scisoft/analysis/dataset
 * @author Matthew Gerring
 *
 */
public class SlicingExamples {

	/**
	 * Slice using basic int[]
	 */
	@Test
	public void iterateImages1() {
		
		final ILazyDataset lz = Random.lazyRand(64, 100, 100);
		int count = 0;
		for (int i = 0; i < 64; i++) {
			IDataset image = lz.getSlice(new int[]{i, 0, 0}, new int[]{i+1,100,100}, new int[]{1,1,1});
			image.squeeze(); // This puts shape from 1,100,100 to 100,100
			++count;
            System.out.println("Array sliced "+count+" "+image);
		}
	}
	
	/**
	 * Slice using basic Slice object
	 */
	@Test
	public void iterateImages2() {
		
		final ILazyDataset lz = Random.lazyRand(64, 100, 100);
		int count = 0;
		for (int i = 0; i < 64; i++) {
			IDataset image = lz.getSlice(new Slice(i, i+1), null, null);
			image.squeeze(); // This puts shape from 1,100,100 to 100,100
			++count;
            System.out.println("Slice object sliced "+count+" "+image);
		}
	}


	/**
	 * Slice visitor
	 */
	@Test
	public void slicerImages() throws Exception {
		
		final ILazyDataset         lz   = Random.lazyRand(64, 100, 100);
		final Map<Integer, String> dims = new HashMap<Integer, String>();
		dims.put(0, "all");
		Slicer.visitAll(lz, dims, new SliceVisitor() {
			
			private int count = 0;
			@Override
			public void visit(IDataset data, Slice[] selectedSlice, int[] selectedShape) throws Exception {
				
                IDataset image = data.squeeze(); // We squeeze the slice to get the image.
                ++count;
                System.out.println("Image "+count+" "+image);
			}
			@Override
			public boolean isCancelled() {
				return false;
			}
		});
	}
	
}
