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

import java.util.Map;

import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;
import org.eclipse.dawnsci.analysis.api.processing.model.IOperationModel;

public interface IOperationContext {

	/**
	 * 
	 * @return The data which we are slicing
	 */
	public ILazyDataset getData();
	
	/**
	 * 
	 * The data which we are slicing
	 */
	public void setData(ILazyDataset data);


	/**
	 * 
	 * @return A map of data dimension to String representing the slice done on this dimension. 
	 */
	public Map<Integer, String> getSlicing();

	/**
	 * 
	 * A map of data dimension to String representing the slice done on this dimension. 
	 */
	public void setSlicing(Map<Integer, String> slicing);
	
	/**
	 * Shortcut method that saves creating a map
	 * @param slices
	 */
	public void setSlicing(String... slices);
	
	/**
	 * 
	 * @return the pipeline
	 */ 
	public IOperation<? extends IOperationModel, ? extends OperationData>[] getSeries();

	/**
	 * Set the pipeline, must be non-null.
	 * @param series
	 */
	public void setSeries(IOperation<? extends IOperationModel, ? extends OperationData>... series);

	/**
	 * 
	 * @return monitor  or null
	 */
	public IMonitor getMonitor();

	/**
	 * 
	 * @param monitor
	 */
	public void setMonitor(IMonitor monitor);

	/**
	 * 
	 * @return vistor or null
	 */
	public IExecutionVisitor getVisitor();

	/**
	 * 
	 * @param visitor
	 */
	public void setVisitor(IExecutionVisitor visitor);

	/**
	 * 
	 * @return execution type.
	 */
	public ExecutionType getExecutionType();
	
	/**
	 * 
	 * @param executionType
	 */
	public void setExecutionType(ExecutionType executionType) ;

	/**
	 * Timeout in parallel mode.
	 * @return timeout
	 */
	public long getParallelTimeout();


	/**
	 * Optionally configure the parallel timeout of this service.
	 * 
	 * @param timeoutMs
	 */
	public void setParallelTimeout(long timeoutMs);

}
