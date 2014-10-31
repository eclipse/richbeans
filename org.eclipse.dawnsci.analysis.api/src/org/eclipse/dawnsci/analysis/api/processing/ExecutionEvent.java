/*-
 * Copyright 2014 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.analysis.api.processing;

import java.util.Arrays;
import java.util.EventObject;

import org.eclipse.dawnsci.analysis.api.dataset.Slice;
import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;
import org.eclipse.dawnsci.analysis.api.processing.model.IOperationModel;

/**
 * Class to contain information required by an execution visitor implementation
 */
public class ExecutionEvent extends EventObject {
	
	/**
	 * 
	 */
	private IMonitor monitor;

	/**
	 * The operation series
	 */
	private IOperation<? extends IOperationModel, ? extends OperationData>[] series;
	
	/**
	 * The OriginMetadata, may be null.
	 */
	private OriginMetadata origin;
	
	/**
	 * 
	 */
	private IOperation<? extends IOperationModel, ? extends OperationData> intermediateData;
	
	/**
	 */
	private OperationData data;
	
	/**
	 * 
	 */
	private Slice[] slices;
	
	/**
	 * 
	 */
	private int[] shape;
	
	/**
	 * 
	 */
	private int[] dataDims;
	
	public ExecutionEvent(IOperationService source) {
		super(source);
	}

	/**
	 * 
	 * @param i
	 * @param data2
	 * @param slices2
	 * @param shape2
	 * @param dataDims2
	 */
	public ExecutionEvent(IOperationService source, IOperation<? extends IOperationModel, ? extends OperationData> i, OperationData data2, Slice[] slices2, int[] shape2, int[] dataDims2) {
		super(source);
		intermediateData = i;
		data     = data2;
		slices   = slices2;
		shape    = shape2;
		dataDims = dataDims2;
	}

	public ExecutionEvent(IOperationService source, IOperation<? extends IOperationModel, ? extends OperationData>[] series2, OriginMetadata originMetadata) {
		super(source);
		this.series = series2;
		this.origin = originMetadata;
	}

	public IOperation<? extends IOperationModel, ? extends OperationData>[] getSeries() {
		return series;
	}

	public void setSeries(IOperation<? extends IOperationModel, ? extends OperationData>[] series) {
		this.series = series;
	}

	public OriginMetadata getOrigin() {
		return origin;
	}

	public void setOrigin(OriginMetadata origin) {
		this.origin = origin;
	}

	public IOperation<? extends IOperationModel, ? extends OperationData> getIntermediateData() {
		return intermediateData;
	}

	public void setIntermediateData(IOperation<? extends IOperationModel, ? extends OperationData> intermediateData) {
		this.intermediateData = intermediateData;
	}

	public OperationData getData() {
		return data;
	}

	public void setData(OperationData data) {
		this.data = data;
	}

	public Slice[] getSlices() {
		return slices;
	}

	public void setSlices(Slice[] slices) {
		this.slices = slices;
	}

	public int[] getShape() {
		return shape;
	}

	public void setShape(int[] shape) {
		this.shape = shape;
	}

	public int[] getDataDims() {
		return dataDims;
	}

	public void setDataDims(int[] dataDims) {
		this.dataDims = dataDims;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + Arrays.hashCode(dataDims);
		result = prime * result + ((intermediateData == null) ? 0 : intermediateData.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + Arrays.hashCode(series);
		result = prime * result + Arrays.hashCode(shape);
		result = prime * result + Arrays.hashCode(slices);
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
		ExecutionEvent other = (ExecutionEvent) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (!Arrays.equals(dataDims, other.dataDims))
			return false;
		if (intermediateData == null) {
			if (other.intermediateData != null)
				return false;
		} else if (!intermediateData.equals(other.intermediateData))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (!Arrays.equals(series, other.series))
			return false;
		if (!Arrays.equals(shape, other.shape))
			return false;
		if (!Arrays.equals(slices, other.slices))
			return false;
		return true;
	}

	public IMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(IMonitor monitor) {
		this.monitor = monitor;
	}

}
