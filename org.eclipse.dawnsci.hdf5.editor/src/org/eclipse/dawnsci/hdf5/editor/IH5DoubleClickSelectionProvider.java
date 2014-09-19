/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.dawnsci.hdf5.editor;

import org.eclipse.jface.viewers.ISelection;

public interface IH5DoubleClickSelectionProvider  {

	/**
	 * We implement an overriding selection to transform a double click into firing
	 * some other kind of selection event.
	 * 
	 * @param selection
	 * @param filePath
	 * @return
	 */
	public ISelection getSelection(ISelection selection, String filePath) throws Exception;
}
