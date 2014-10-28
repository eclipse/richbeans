/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.analysis.api.processing;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyDataset;
import org.eclipse.dawnsci.analysis.api.metadata.AxesMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.IDiffractionMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.IMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.MaskMetadata;
import org.eclipse.dawnsci.analysis.api.metadata.MetadataType;
import org.eclipse.dawnsci.analysis.api.metadata.OriginMetadata;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;
import org.eclipse.dawnsci.analysis.api.processing.model.IOperationModel;

/**
 * Abstract implementation of IOperation.
 * 
 * Simplest method to add a new operation is to extend this class and override the process method, which receives either 1D or 2D
 * datasets and should return either 2D or 1D datasets with correctly configured axesmetadata, and single value datasets of shape [1]
 * as auxiliary data.
 * 
 * Overriding execute gives access to the unsqueezed data (rank the same as the initial dataset being processed) and all axes items,
 * but care must be taken to maintain the size 1 dimensions and axes, which the process method does for you.
 * 
 * Should return OperationData unless there is a very good reason to extend it.
 *
 * @param <T>
 * @param <D>
 */
public abstract class AbstractOperation<T extends IOperationModel, D extends OperationData> implements IOperation<T, D> {

	protected T model;

	private String            name;
	private String            description;
	private OperationCategory category;
	
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
	public D execute(IDataset slice, IMonitor monitor) throws OperationException {
		
		IDataset view = slice.getSliceView().squeeze();
		
		D output = process(view,monitor);
		
		try {
			output.getData().setMetadata(slice.getMetadata(OriginMetadata.class).get(0));
		} catch (Exception e) {
			throw new OperationException(this,e);
		}
		
		return updateOutputToFullRank(output, slice);
		
	}
	
	/**
	 * Pads the output dataset (and axes) to the match the rank of the input dataset, accouting for any loss of dimensionality
	 * in the process. Suitable only for 2D and 1D datasets
	 * 
	 * @param output
	 * @param original
	 * @return <D>
	 * @throws OperationException
	 */
	private D updateOutputToFullRank(D output, IDataset original) throws OperationException {
		
		int outr = output.getData().getRank();
		int inr = original.getRank();
		
		//Check ranks acceptable for this step
		if (getOutputRank().equals(OperationRank.ZERO) || getOutputRank().equals(OperationRank.NONE) || getOutputRank().getRank() > 2) throw new OperationException(null, "Invalid Operation Rank!");
		
		int rankDif = 0;
		
		if (!getOutputRank().equals(OperationRank.SAME)) {
			
			rankDif = getInputRank().getRank() - getOutputRank().getRank();
		}
		
		//Single image/line case, nothing to alter
		if (inr == outr) return output;
		
		List<AxesMetadata> metadata = null;
		List<AxesMetadata> metaout = null;
		
		try {
			metadata = original.getMetadata(AxesMetadata.class);
		} catch (Exception e) {
			throw new OperationException(this, e);
		}
		
		//Clone and sort dimensions for searching
		int[] datadims = getOriginalDataDimensions(original).clone();
		Arrays.sort(datadims);
		
		//Update rank of dataset (will automatically update rank of axes)
		updateOutputDataShape(output.getData(), inr-rankDif, datadims, rankDif);
		
		if (metadata != null && !metadata.isEmpty() && metadata.get(0) != null) {
			
			//update it all for new data;
			try {
				metaout = output.getData().getMetadata(AxesMetadata.class);
			} catch (Exception e) {
				throw new OperationException(this, e);
			}
			
			AxesMetadata inMeta = metadata.get(0);
			
			AxesMetadata axOut = null;
			if (metaout != null && !metaout.isEmpty()) axOut = metaout.get(0);
			if (axOut == null) axOut = inMeta.createAxesMetadata(output.getData().getRank());
			
			//Clone to get copies of lazy datasets
			AxesMetadata cloneMeta = (AxesMetadata) inMeta.clone();
			
			if (rankDif == 0) {
				
				for (int i = 0; i< original.getRank(); i++) {
					if (Arrays.binarySearch(datadims, i) < 0) {
						ILazyDataset[] axis = cloneMeta.getAxis(i);
						if (axis != null) axOut.setAxis(i, cloneMeta.getAxis(i));
					}
				}
				
			} else {
				int j = 0;
				int[] shape = new int[output.getData().getRank()];
				Arrays.fill(shape, 1);
				
				for (int i = 0; i< original.getRank(); i++) {
					if (Arrays.binarySearch(datadims, i) < 0) {
						ILazyDataset[] axis = cloneMeta.getAxis(i);
						if (axis != null) {
							for (ILazyDataset ax : axis) if (ax != null) ax.setShape(shape); 
							axOut.setAxis(i+j, cloneMeta.getAxis(i));
						}
						
					} else {
						j--;
					}
				}
			} 
		}
		
		updateAuxData(output.getAuxData(), original);
		
		return output;
	}
	
	/**
	 * Update the rank of the output data, and its axes to be consistent with the input
	 * 
	 * @param out
	 * @param rank
	 * @param dataDims
	 * @param rankDif
	 */
	private void updateOutputDataShape(IDataset out, int rank, int[] dataDims, int rankDif) {
		int[] shape = out.getSliceView().squeeze().getShape();
		
		int[] updated = new int[rank];
		Arrays.fill(updated, 1);
		
		if (rankDif == 0) {
			//1D-1D or 2D - 2D
			for (int i = 0; i< shape.length; i++) updated[dataDims[i]] = shape[i]; 
		} else if ( rankDif > 0) {
			//1D
			updated[dataDims[0]] = shape[0];
		} else if (rankDif < 0) {
			//2D from 1D
			updated[dataDims[0]] = shape[0];
			updated[updated.length-1] = shape[1];
		}
		
		for (int i = 0 ; i < Math.min(dataDims.length,shape.length); i++) {
			updated[dataDims[i]] = shape[i]; 
		}
		
		out.setShape(updated);
		
		
	}
	
	/**
	 * Updates the auxiliary data (of shape [1]) to correct rank and adds all axes of rank zero (when squeezed)
	 * @param auxData
	 * @param original
	 */
	private void updateAuxData(Serializable[] auxData, IDataset original){
		
		if (auxData == null || auxData[0] == null) return;
		
		int[] datadims = getOriginalDataDimensions(original).clone();
		
		if (datadims.length > getOutputRank().getRank()) {
			datadims = new int[]{datadims[0]};
		}
		
		Arrays.sort(datadims);

		
		int[] shape = new int[original.getRank()-datadims.length];
		Arrays.fill(shape, 1);
		
		List<AxesMetadata> metadata = null;
		
		try {
			metadata = original.getMetadata(AxesMetadata.class);
		} catch (Exception e) {
			throw new OperationException(this, e);
		}
		
		
		for (int i = 0; i < auxData.length; i++) {
			if (!(auxData[i] instanceof IDataset) || ((IDataset)auxData[i]).getRank() != 0 ) {
				continue;
			}
			
			IDataset ds = (IDataset)auxData[i];
			ds.setShape(shape);
			
			if (metadata != null && !metadata.isEmpty() && metadata.get(0) != null) {
				AxesMetadata outMeta = metadata.get(0).createAxesMetadata(shape.length);
				AxesMetadata inMeta = metadata.get(0);
				int counter = 0;
				for (int j = 0; j < original.getRank();j++) {
					if (Arrays.binarySearch(datadims, j)<0) {
						ILazyDataset[] axes = inMeta.getAxis(j);
						if (axes != null && axes[0] != null) {
							ILazyDataset view = axes[0].getSliceView();
							view.setShape(shape);
							outMeta.setAxis(counter++, new ILazyDataset[]{view});
						}
						
					}
				}
				ds.setMetadata(outMeta);
			}
			
		}
	}
	
	/**
	 * Simplest method to override to produce a new Operation.
	 * 
	 * Is given data of the input rank, should produce data of the expected output rank (and aux data of shape [1] if required)
	 * 
	 * @param input
	 * @param monitor
	 * @return <D>
	 * @throws OperationException
	 */
	@SuppressWarnings("unused")
	protected D process(IDataset input, IMonitor monitor) throws OperationException {
		return null;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
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
		AbstractOperation other = (AbstractOperation) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
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

	/**
	 * Convenience method to get first set of axes from the Datasets metadata, can return null
	 * @param slice
	 * @return axes
	 */
	public static ILazyDataset[] getFirstAxes(IDataset slice) {
		List<AxesMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(AxesMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		AxesMetadata am = metaList.get(0);
		if (am == null)
			return null;

		return am.getAxes();
	}

	/**
	 * Convenience method to get first mask from the Datasets metadata, can return null
	 * @param slice
	 * @return mask
	 */
	public static ILazyDataset getFirstMask(IDataset slice) {

		List<MaskMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(MaskMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		MaskMetadata mm = metaList.get(0);
		if (mm == null)
			return null;

		return mm.getMask();
	}

	/**
	 * Convenience method to get first diffraction metadata from the Dataset, can return null
	 * @param slice
	 * @return dm
	 */
	public static IDiffractionMetadata getFirstDiffractionMetadata(IDataset slice) {

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

	/**
	 * Convenience method to get the data dimensions of the original Dataset, can return null, but really should never happpen
	 * @param slice
	 * @return datadims
	 */
	public static int[] getOriginalDataDimensions(IDataset slice) {

		OriginMetadata originMetadata = getOriginMetadata(slice);

		return originMetadata == null ? null : originMetadata.getDataDimensions();

	}
	
	/**
	 * Convenience method to origin metadata from slice, can return null, but really should never happpen
	 * @param slice
	 * @return origin
	 */
	public static OriginMetadata getOriginMetadata(IDataset slice){
		List<OriginMetadata> metaList = null;

		try {
			metaList = slice.getMetadata(OriginMetadata.class);
			if (metaList == null || metaList.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		return metaList.get(0);
	}

	/**
	 * Convenience method to copy the metadata from one dataset to another.
	 * Use if a process doesnt change the shape of the data to maintain axes, masks etc
	 * 
	 * @param original
	 * @param out
	 */
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

	public OperationCategory getCategory() {
		return category;
	}

	public void setCategory(OperationCategory category) {
		this.category = category;
	}
}
