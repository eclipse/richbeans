package org.eclipse.dawnsci.hdf5.model.internal;

import org.eclipse.dawnsci.hdf5.model.IHierarchicalDataFileModel;


public interface IHierarchicalDataModelGetFileModel {
	IHierarchicalDataFileModel createFileModel(String fullPath);
}
