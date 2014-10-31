/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.processing;


/**
 * This interface is designed to be called when a series of operations has been 
 * completed on a dataset which is being processed. It is a visitor pattern
 * which is notified of the processed data in the stack. The user then has the option
 * of keeping the full data in memory (if possible), writing to file or plotting and
 * then letting the data go out of scope.
 */
public interface IExecutionVisitor {
	
	/**
	 * Initialise the execution visitor with the series of operations that are going to run
	 *  
	 * @throws Exception
	 */
	public void init(ExecutionEvent evt) throws Exception;
	
    /**
     * Called when an execution in the pipeline has run, before the end	but after a given operation.
     * 
     * Provides the option of saving the steps information to a file if required.
     * 
     */
	public void notify(ExecutionEvent evt);
	
	/**
	 * Called when the series of operations has been done, with the current slice
	 */
	public void executed(ExecutionEvent evt) throws Exception;
	
	
	/**
	 * Tell the execution visitor to close, releasing its resources.
	 * 
	 * Called when the pipeline has run through completely.
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	
	public class Stub implements IExecutionVisitor {

		@Override
		public void executed(ExecutionEvent evt) throws Exception {
			
		}

		@Override
		public void notify(ExecutionEvent evt) {
			
		}

		@Override
		public void init(ExecutionEvent evt) {
			
		}

		@Override
		public void close() throws Exception {
			
		}
		
	}



}
