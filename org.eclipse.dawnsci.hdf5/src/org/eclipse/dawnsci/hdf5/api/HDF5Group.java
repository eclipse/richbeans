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
import java.util.Iterator;

import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.analysis.tree.impl.GroupNodeImpl;

/**
 * A group acts like a file system directory. It holds a map of node links and can contain a
 * reference to a global pool of nodes which is used for checking linked nodes
 */
public class HDF5Group extends GroupNodeImpl implements HDF5Node, Serializable, Iterable<HDF5NodeLink> {
	private static final long serialVersionUID = 8830337783420707862L;

	/**
	 * Construct a HDF5 group with given object ID
	 * @param oid object ID
	 */
	public HDF5Group(final long oid) {
		super(oid);
	}

	public HDF5Group getGroup(String name) {
		return (HDF5Group) super.getGroupNode(name);
	}

	@Override
	public HDF5NodeLink getNodeLink(String name) {
		return (HDF5NodeLink) super.getNodeLink(name);
	}

	public boolean containsDataset(String name) {
		return containsDataNode(name);
	}

	public HDF5Dataset getDataset(String name) {
		return (HDF5Dataset) getDataNode(name);
	}

	@Override
	public HDF5Attribute getAttribute(String name) {
		return (HDF5Attribute) super.getAttribute(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<HDF5NodeLink> iterator() {
		return (Iterator<HDF5NodeLink>) getIterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<HDF5Attribute> getAttributeIterator() {
		return (Iterator<HDF5Attribute>) super.getAttributeIterator();
	}

	@Override
	public HDF5NodeLink findNodeLink(String pathname) {
		return (HDF5NodeLink) super.findNodeLink(pathname);
	}

	protected NodeLink createNodeLink(final Tree file, final String path, final String name, final Node n) {
		return new HDF5NodeLink((HDF5File) file, path, name, this, (HDF5Node) n);
	}

	protected NodeLink createNodeLink(final NodeLink link) {
		return new HDF5NodeLink(link);
	}

//	public List<String> getNodeNames() {
//		List<String> list = new ArrayList<String>();
//		list.addAll(nodes.keySet()); 
//		return list;
//	}

}
