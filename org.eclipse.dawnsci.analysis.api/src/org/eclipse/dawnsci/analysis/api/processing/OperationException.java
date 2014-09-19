/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.processing;

public class OperationException extends RuntimeException {

	private final IOperation operation;

	public OperationException(IOperation operation) {
		super();
		this.operation = operation;
	}
	public OperationException(IOperation operation, Throwable cause) {
		super(cause);
		this.operation = operation;
	}
	
	public OperationException(IOperation operation, String reason) {
		super(reason);
		this.operation = operation;
	}

	public IOperation getOperation() {
		return operation;
	}

}
