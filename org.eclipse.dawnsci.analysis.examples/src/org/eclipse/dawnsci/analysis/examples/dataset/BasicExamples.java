package org.eclipse.dawnsci.analysis.examples.dataset;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.dataset.impl.DatasetUtils;
import org.eclipse.dawnsci.analysis.dataset.impl.DoubleDataset;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * How to do the basics, @see http://wiki.scipy.org/NumPy_for_Matlab_Users
 * 
 * 
 *
 * @author Matthew Gerring
 *
 */
public class BasicExamples {
	
	private IDataset a, b;

	@Before
	public void create() {
		a = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);
		b = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);
	}
	
	/**
	 * What follows in comments:
	 * 
	 * MATLAB CODE         NUMPY CODE
	 */
	
	/**
	 * ndims(a)            ndim(a) or a.adim
	 */
	@Test
	public void ndims() {
		System.out.println("a has rank "+a.getRank());
	}
	
	/**
	 * numel(a)            size(a) or a.size
	 */
	@Test
	public void size() {
		System.out.println("a has size "+a.getSize());
	}
	
	/**
	 * size(a,n)           a.shape[n-1]
	 */
	@Test
	public void sizeN() {
		System.out.println("a has size "+a.getShape()[0]);
	}
	
	/**
	 * 2x3 matrix literal
	 * [ 1 2 3; 4 5 6 ]     array([[1.,2.,3.],[4.,5.,6.]])
	 */
	@Test
	public void createMatrix() {
		IDataset set = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2,3);
		System.out.println("Created a dataset : "+set);
	}
	
	/**
	 * construct a matrix from blocks a,b,c, and d
	 * 
	 * [ a b; c d ]              vstack([hstack([a,b]), hstack([c,d])])
	 */
	@Test
	public void blocks() {
		
		IDataset c = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);
		IDataset d = new DoubleDataset(new double[]{1,2,3,4,5,6}, 2, 3);

		IDataset h1 = DatasetUtils.concatenate(new IDataset[]{a,b},   1);
		IDataset h2 = DatasetUtils.concatenate(new IDataset[]{c,d},   1);
		IDataset m  = DatasetUtils.concatenate(new IDataset[]{h1,h2}, 0);
		
		System.out.println("Created matrix from blocks "+m);
	}
	
	/**
	 * access last element in the 1xn matrix a
	 * a(end)                     a[-1]   or    a[:,-1][0,0]
	 */
	@Test
	public void lastElement() {
		double val = a.getDouble(-1,-1);
		System.out.println("The last value is "+val);
	}
	
	/**
	 * access element in second row, fifth column
	 * a(2,5)                     a[1,4]
	 */
	@Test
	public void element1x4() {
		double val = a.getDouble(1,4);
		System.out.println("The last value at (1,4) "+val);
	}
	
	
}
