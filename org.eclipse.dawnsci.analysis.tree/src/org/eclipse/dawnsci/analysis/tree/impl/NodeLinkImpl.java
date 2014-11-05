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

import org.eclipse.dawnsci.analysis.api.tree.DataNode;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.SymbolicNode;
import org.eclipse.dawnsci.analysis.api.tree.Tree;

public class NodeLinkImpl implements NodeLink, Serializable {
	protected static final long serialVersionUID = -8586668618966201973L;

	private NodeImpl from;
	private NodeImpl to;
	private String name;
	private String path;
	private TreeImpl tree;

	/**
	 * A node link
	 * @param path to source
	 * @param link name
	 * @param source node which link starts from (can be null)
	 * @param destination node which link points to
	 */
	public NodeLinkImpl(final Tree tree, final String path, final String link, final Node source, final Node destination) {
		if (link == null || destination == null) {
			throw new IllegalArgumentException("Path name, link name and destination must be defined");
		}

		this.tree = tree instanceof TreeImpl ? (TreeImpl) tree : new TreeImpl(tree);
		this.path = path == null ? "" : path;
		name = link;
		from = source == null ? null : (source instanceof NodeImpl ? (NodeImpl) source : new NodeImpl(source));
		to   = destination instanceof NodeImpl ? (NodeImpl) destination: new NodeImpl(destination);
	}

	public NodeLinkImpl(NodeLink link) {
		this(link.getTree(), link.getPath(), link.getName(), link.getSource(), link.getDestination());
	}

	@Override
	public NodeImpl getSource() {
		return from;
	}

	@Override
	public NodeImpl getDestination() {
		return to;
	}

	@Override
	public boolean isDestinationData() {
		return to instanceof DataNode;
	}

	@Override
	public boolean isDestinationGroup() {
		return to instanceof GroupNode;
	}

	@Override
	public boolean isDestinationSymbolic() {
		return to instanceof SymbolicNode;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return path + name + '\n' + to.toString();
	}

	@Override
	public String getFullName() {
		return path + name;
	}

	@Override
	public TreeImpl getTree() {
		return tree;
	}
}
