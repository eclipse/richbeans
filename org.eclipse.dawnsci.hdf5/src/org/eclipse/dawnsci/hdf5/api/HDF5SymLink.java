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

import org.eclipse.dawnsci.analysis.tree.impl.SymbolicNodeImpl;

/**
 * Symbolic link to another node
 */
public class HDF5SymLink extends SymbolicNodeImpl implements HDF5Node {
	private static final long serialVersionUID = SymbolicNodeImpl.serialVersionUID;

	/**
	 * Construct a HDF5 symbolic link with given object ID, file name and node path
	 * @param oid object ID
	 * @param fileWithNode
	 * @param pathToNode (ends in separator if group, otherwise a dataset)
	 */
	public HDF5SymLink(final long oid, final HDF5File fileWithNode, final String pathToNode) {
		super(oid, fileWithNode, pathToNode);
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

	@Override
	public HDF5NodeLink getNodeLink() {
		return (HDF5NodeLink) super.getNodeLink();
	}
}
