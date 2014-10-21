package org.eclipse.dawnsci.analysis.examples.dataset;

import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;
import org.junit.Test;

/**
 * Examples for carrying errors through your calculations.
 * 
 * @author Matthew Gerring
 *
 */
public class ErrorExamples {

	/**
	 * Set the error on some data.
	 */
	@Test
	public void setErrorSimple() {
		
		Dataset rand  = Random.rand(0, 100, new int[]{1024, 1024});
		Dataset error = Random.rand(0, 1,   new int[]{1024, 1024});
		
		rand.setError(error);
		// The error now stays with the data and can be retrieved, however:
		
		rand.idivide(10);
		error = rand.getError();
		
		// Error is not carried through calculations, the max is still ~1.0
		System.out.println(error.max());
	}
}
