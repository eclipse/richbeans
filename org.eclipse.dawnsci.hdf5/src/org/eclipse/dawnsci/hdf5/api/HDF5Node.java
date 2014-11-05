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

import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.tree.impl.NodeImpl;

/**
 * Compatibility interface
 */
public interface HDF5Node extends Node {
	public static final String ATTRIBUTE = NodeImpl.ATTRIBUTE;

	public static final String SEPARATOR = Node.SEPARATOR;

	@Override
	public HDF5Attribute getAttribute(String name);

	@Override
	public Iterator<HDF5Attribute> getAttributeIterator();
}
