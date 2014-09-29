/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.processing;

import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.metadata.AxesMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.IDiffractionMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.IMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.MaskMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.MetadataType;
import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;
import org.eclipse.dawnsci.analysis.api.processing.model.IOperationModel;

public abstract class AbstractOperation<T extends IOperationModel, D extends OperationData> implements IOperation<T, D> {

	protected T model;

	private String name;
	private String description;
	
	private boolean storeOutput = false;
	private boolean passUnmodifiedData = false;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		if (description == null)
			return getId();
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractOperation<?, ?> other = (AbstractOperation<?, ?>) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractOperation [name=" + name + "]";
	}

	@Override
	public T getModel() {
		return model;
	}

	@Override
	public void setModel(T model) {
		this.model = model;
	}

	public ILazyDataset[] getFirstAxes(IDataset slice) {
		List<AxesMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(AxesMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		AxesMetadata am = metaList.get(0);

		return am.getAxes();
	}

	public ILazyDataset getFirstMask(IDataset slice) {

		List<MaskMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(MaskMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		MaskMetadata mm = metaList.get(0);

		return mm.getMask();
	}

	public IDiffractionMetadata getFirstDiffractionMetadata(IDataset slice) {

		List<IMetadata> metaList;

		try {
			metaList = slice.getMetadata(IMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		for (IMetadata meta : metaList)
			if (meta instanceof IDiffractionMetadata)
				return (IDiffractionMetadata) meta;

		return null;
	}

	public int[] getOriginalDataDimensions(IDataset slice) {

		List<OriginMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(OriginMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		OriginMetadata om = metaList.get(0);

		return om.getDataDimensions();

	}

	public void copyMetadata(IDataset original, IDataset out) {
		try {
			List<MetadataType> metadata = original.getMetadata(null);

			for (MetadataType m : metadata) {
				out.addMetadata(m);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setStoreOutput(boolean storeOutput) {
		this.storeOutput = storeOutput;
	}
	
	@Override
	public boolean isStoreOutput() {
		return storeOutput;
	}
	
	@Override
	public void setPassUnmodifiedData(boolean passUnmodifiedData) {
		this.passUnmodifiedData = passUnmodifiedData;
	}

	@Override
	public boolean isPassUnmodifiedData() {
		return passUnmodifiedData;
	}
}
