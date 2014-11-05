/*-
 * Copyright (c) 2014 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.tree.impl;

import java.io.File;
import java.net.URI;

import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.api.tree.Node;
import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.api.tree.Tree;

public class TreeImpl extends NodeImpl implements Tree {
	protected static final long serialVersionUID = -4612527676015545433L;

	protected final URI source;
	protected String host;
	private NodeLink link;
	private static final String UPDIR = "/..";

	/**
	 * Construct a HDF5 file with given object ID and URI 
	 * @param oid object ID
	 * @param uri
	 */
	public TreeImpl(final long oid, URI uri) {
		super(oid);

		source = uri;
		host = uri.getHost(); // this can return null for "file:/blah"
		
		link = createRootNodeLink(oid);
	}

	protected NodeLink createRootNodeLink(long oid) {
		return new NodeLinkImpl(this, null, ROOT, null, new GroupNodeImpl(oid));
	}
	protected NodeLink createRootNodeLink(Node src, Node dest) {
		return new NodeLinkImpl(this, null, ROOT, src, dest);
	}

	/**
	 * Construct a HDF5 file with given object ID and file name 
	 * @param oid object ID
	 * @param fileName
	 */
	public TreeImpl(final long oid, final String fileName) {
		this(oid, new File(fileName).toURI());
	}

	public TreeImpl(final Tree tree) {
		super(tree.getID());
		source = tree.getSourceURI();
		host = tree.getHostname();
		NodeLink l = tree.getNodeLink();
		link = l instanceof NodeLinkImpl ? (NodeLinkImpl) l : new NodeLinkImpl(l);
	}

	@Override
	public URI getSourceURI() {
		return source;
	}

	@Override
	public void setHostname(String hostname) {
		host = hostname;
	}

	@Override
	public String getHostname() {
		return host;
	}

	@Override
	public GroupNode getGroupNode() {
		return (GroupNode) link.getDestination();
	}

	@Override
	public void setGroupNode(GroupNode g) {
		link = createRootNodeLink(this,  g);
	}

	@Override
	public NodeLink getNodeLink() {
		return link;
	}

	@Override
	public String toString() {
		return source.toString();
	}

	@Override
	public NodeLink findNodeLink(final String pathname) {
			final String path = canonicalizePath(pathname);
			if (path.indexOf(SEPARATOR) != 0)
				return null;
	
			if (path.length() == 1) {
				return link;
			}
	
			// check if group is empty - this indicates an external link created this
			final GroupNodeImpl g = (GroupNodeImpl) link.getDestination();
//			if ((g.getNumberOfGroupNodes() + g.getNumberOfDataNodes() + g.getNumberOfAttributes()) == 0) {
//				
//			}
			// check if root attribute is needed
			final String spath = path.substring(1);
			if (!spath.startsWith(ATTRIBUTE)) {
				return g.findNodeLink(spath);
			}
	
			if (g.containsAttribute(spath.substring(1)))
				return link;
	
			return null;
		}

	private static final String CURDIR = "/.";

	/**
	 * Remove ".." and "." from pathname
	 * @param pathname
	 * @return canonical form of pathname
	 */
	public static String canonicalizePath(final String pathname) {
		if (!pathname.contains(UPDIR) && !pathname.contains(CURDIR))
			return pathname;
	
		StringBuilder path = new StringBuilder(pathname);
		int i = 0;
		while ((i = path.indexOf(UPDIR)) >= 0) {
			int j = path.lastIndexOf(SEPARATOR, i - 1);
			if (j <= 0) {
				// can not find SEPARATOR or preserve ROOT
				path.insert(0, ROOT);
				i++;
				j++;
			}
			path.delete(j, i + UPDIR.length());
		}
	
		while ((i = path.indexOf(CURDIR)) >= 0) {
			path.delete(i, i + CURDIR.length());
		}
	
		return path.toString();
	}
}
