/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.hdf5.api;

import java.io.File;
import java.net.URI;
import java.util.Iterator;

import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.Tree;
import org.eclipse.dawnsci.analysis.tree.impl.TreeFileImpl;
import org.eclipse.dawnsci.analysis.tree.impl.TreeImpl;

/**
 * Top level node for tree
 */
public class HDF5File extends TreeFileImpl implements HDF5Node {
	private static final long serialVersionUID = TreeFileImpl.serialVersionUID;

	public static final String ROOT = Tree.ROOT;

	/**
	 * Attribute name for a NeXus class
	 */
	public static final String NXCLASS = "NX_class";

	public static String canonicalizePath(final String pathname) {
		return TreeImpl.canonicalizePath(pathname);
	}

	/**
	 * Construct a HDF5 file with given object ID and URI 
	 * @param oid object ID
	 * @param uri
	 */
	public HDF5File(final long oid, URI uri) {
		super(oid, uri);
		setGroupNode(super.getGroupNode());
	}

	protected NodeLink createRootNodeLink(long oid) {
		return new HDF5NodeLink(this, null, ROOT, null, new HDF5Group(oid));
	}
	protected NodeLink createRootNodeLink(Node src, Node dest) {
		return new HDF5NodeLink(this, null, ROOT, (HDF5Node) src, (HDF5Node) dest);
	}

	/**
	 * Construct a HDF5 file with given object ID and file name 
	 * @param oid object ID
	 * @param fileName
	 */
	public HDF5File(final long oid, final String fileName) {
		this(oid, new File(fileName).toURI());
	}

	@Override
	public HDF5NodeLink findNodeLink(String pathname) {
		return (HDF5NodeLink) super.findNodeLink(pathname);
	}

	@Override
	public HDF5Group getGroupNode() {
		return (HDF5Group) super.getGroupNode();
	}

	public HDF5Group getGroup() {
		return getGroupNode();
	}

	public void setGroup(HDF5Group g) {
		super.setGroupNode(g);
	}

	@Override
	public HDF5NodeLink getNodeLink() {
		return (HDF5NodeLink) super.getNodeLink();
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
