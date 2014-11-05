/*-
 * Copyright (c) 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.tree.impl;

import java.io.Serializable;

import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.SymbolicNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;

/**
 * Symbolic link to another node
 */
public class SymbolicNodeImpl extends NodeImpl implements SymbolicNode, Serializable {
	protected static final long serialVersionUID = -2348087598312513187L;

	private TreeImpl tree;
	private String path;

	/**
	 * Construct a HDF5 symbolic link with given object ID, file name and node path
	 * @param oid object ID
	 * @param treeWithNode
	 * @param pathToNode (ends in separator if group, otherwise a dataset)
	 */
	public SymbolicNodeImpl(final long oid, final Tree treeWithNode, final String pathToNode) {
		super(oid);
		tree = treeWithNode instanceof TreeImpl ? (TreeImpl) treeWithNode : new TreeImpl(treeWithNode);
		path = pathToNode;
	}

	public SymbolicNodeImpl(final SymbolicNode sym) {
		super(sym);
		Tree t = sym.getTree();
		tree = t instanceof TreeImpl ? (TreeImpl) t : new TreeImpl(t);
		path = sym.getPath();
	}

	@Override
	public NodeLink getNodeLink() {
		return tree.findNodeLink(path);
	}

	@Override
	public NodeImpl getNode() {
		NodeLink l = getNodeLink();
		return l == null ? null : (NodeImpl) l.getDestination();
	}

	@Override
	public boolean isData() {
		return !path.endsWith(SEPARATOR);
	}

	@Override
	public TreeImpl getTree() {
		return tree;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		String attrs = super.toString();
		out.append(attrs);
		return out.toString();
	}
}
