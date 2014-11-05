/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.hdf5.api;

import java.io.Serializable;

import org.eclipse.dawnsci.analysis.api.tree.Attribute;
import org.eclipse.dawnsci.analysis.tree.impl.AttributeImpl;

/**
 * Represent an attribute using a dataset
 */
public class HDF5Attribute extends AttributeImpl implements Serializable, Attribute {
	private static final long serialVersionUID = AttributeImpl.serialVersionUID;

	/**
	 * Create an attribute with file, node, name, value and sign
	 * @param hdf5File
	 * @param nodeName
	 * @param attrName
	 * @param attrValue (usually, this is a Java array)
	 * @param isUnsigned true if items are unsigned but held in signed primitives
	 */
	public HDF5Attribute(final HDF5File hdf5File, final String nodeName, final String attrName, final Object attrValue, final boolean isUnsigned) {
		super(hdf5File, nodeName, attrName, attrValue, isUnsigned);
	}

	public HDF5Attribute(final Attribute attr) {
		super(attr);
	}

	/**
	 * @return hdf5 file of attribute
	 */
	public HDF5File getHDF5File() {
		return (HDF5File) getTree();
	}
}
