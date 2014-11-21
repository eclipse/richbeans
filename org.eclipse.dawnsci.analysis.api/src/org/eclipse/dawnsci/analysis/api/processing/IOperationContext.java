/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

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
