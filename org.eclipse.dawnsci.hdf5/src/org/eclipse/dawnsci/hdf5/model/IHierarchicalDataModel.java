package org.eclipse.dawnsci.hdf5.model;

import org.eclipse.core.resources.IFile;

/**
 * A Model of a collection of Hierarchical Data File (e.g. Nexus/HDF5) where
 * each file is referred to by its IFile
 *
 * @author Tracy Miranda
 */
public interface IHierarchicalDataModel {

	/**
	 * Get the File model for the given IFile.
	 *
	 * @param file
	 *            IFile to load
	 * @return a File model of the Hierarchical Data File (e.g. Nexus/HDF5)
	 */
	IHierarchicalDataFileModel getFileModel(IFile file);
}
