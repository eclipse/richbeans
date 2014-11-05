/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.hdf5.api;

import java.util.Iterator;

import org.eclipse.dawnsci.analysis.tree.impl.DataNodeImpl;


/**
 * Leaf node to hold a (lazy) dataset or string
 */
public class HDF5Dataset extends DataNodeImpl implements HDF5Node {
	private static final long serialVersionUID = DataNodeImpl.serialVersionUID;

	/**
	 * Construct a HDF5 dataset with given object ID
	 * @param oid object ID
	 */
	public HDF5Dataset(final long oid) {
		super(oid);
	}

	@Override
	public HDF5Attribute getAttribute(String name) {
		return (HDF5Attribute) super.getAttribute(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<HDF5Attribute> getAttributeIterator() {
		return (Iterator<HDF5Attribute>) super.getAttributeIterator();
	}
}
