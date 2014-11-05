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

import org.eclipse.dawnsci.analysis.api.tree.NodeLink;
import org.eclipse.dawnsci.analysis.tree.impl.NodeLinkImpl;

/**
 * Link two HDF5 nodes together. The name of the link provides a reference for users to the destination node
 */
public class HDF5NodeLink implements Serializable, NodeLink {
	private static final long serialVersionUID = -8586668618966201973L;

	private final NodeLinkImpl nlink;

	/**
	 * A node link
	 * @param path to source
	 * @param link name
	 * @param source node which link starts from (can be null)
	 * @param destination node which link points to
	 */
	public HDF5NodeLink(final HDF5File file, final String path, final String link, final HDF5Node source, final HDF5Node destination) {
		nlink = new NodeLinkImpl(file, path, link, source, destination);
	}

	public HDF5NodeLink(NodeLink link) {
		nlink = new NodeLinkImpl(link.getTree(), link.getPath(), link.getName(), link.getSource(), link.getDestination());
	}

	public HDF5File getFile() {
		return (HDF5File) nlink.getTree();
	}

	/**
	 * @return filename
	 */
	public String getFilename() {
		return getTree().getFilename();
	}


	public boolean isDestinationADataset() {
		return nlink.isDestinationData();
	}

	public boolean isDestinationAGroup() {
		return nlink.isDestinationGroup();
	}

	public boolean isDestinationASymLink() {
		return nlink.isDestinationSymbolic();
	}

	@Override
	public HDF5Node getSource() {
		return (HDF5Node) nlink.getSource();
	}

	@Override
	public HDF5Node getDestination() {
		return (HDF5Node) nlink.getDestination();
	}

	@Override
	public boolean isDestinationData() {
		return nlink.isDestinationData();
	}

	@Override
	public boolean isDestinationGroup() {
		return nlink.isDestinationGroup();
	}

	@Override
	public boolean isDestinationSymbolic() {
		return nlink.isDestinationSymbolic();
	}

	@Override
	public String getName() {
		return nlink.getName();
	}

	@Override
	public String getPath() {
		return nlink.getPath();
	}

	@Override
	public String getFullName() {
		return nlink.getFullName();
	}

	@Override
	public HDF5File getTree() {
		return (HDF5File) nlink.getTree();
	}
}
