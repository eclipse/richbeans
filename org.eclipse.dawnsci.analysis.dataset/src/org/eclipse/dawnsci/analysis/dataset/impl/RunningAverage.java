/*-
 * Copyright 2015 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.dataset.impl;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;

public class RunningAverage {
	
	private Dataset average;
	private Dataset error;
	private int count = 2;
	
	public RunningAverage(IDataset dataset, boolean canDirty) {
		
		average =  canDirty ? DatasetUtils.convertToDataset(dataset) : DatasetUtils.convertToDataset(dataset).clone();
		error = average.getErrorBuffer() == null ? null : average.getErrorBuffer().getSlice();
	}
	
	public RunningAverage(IDataset dataset) {
		average = DatasetUtils.convertToDataset(dataset).clone();
		error = average.getErrorBuffer() == null ? null : average.getErrorBuffer().getSlice();
	}

	public void update(IDataset dataset) {
		updateDirty(DatasetUtils.convertToDataset(dataset).clone());
	}
	
	public void updateDirty(Dataset dataset) {
		
		if (error != null) {
			Dataset eb = dataset.getErrorBuffer();
			if (eb != null) error.iadd(eb);
			else error = null;
		}
		dataset.isubtract(average);
		dataset.idivide(count++);
		average.iadd(dataset);
	}
	
	public Dataset getCurrentAverage() {
		if (error != null) {
			Dataset e  = Maths.power(error,0.5);
			e.idivide(count-1);
			DatasetUtils.makeFinite(e);
			average.setError(e);
		}

		return average;
	}
}
