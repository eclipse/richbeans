/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.eclipse.dawnsci.hdf5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HierarchicalInfo {
	
	public static final String NAPIMOUNT = "napimount";
	public static final String NAPISCHEME = "nxfile";

	private List<String>         dataSetNames;
	private Map<String, Integer> dataSetSizes;
	private Map<String, int[]>   dataSetShapes;
	private Map<String, String>  attributeValues;
	
	protected HierarchicalInfo() {
		dataSetNames  = new ArrayList<String>(31);
		dataSetSizes  = new HashMap<String,Integer>(31);
		dataSetShapes = new HashMap<String,int[]>(31);
		attributeValues = new LinkedHashMap<String, String>(89);
	}
	
	public List<String> getDataSetNames() {
		return dataSetNames;
	}
	public void setDataSetNames(List<String> dataSetNames) {
		this.dataSetNames = dataSetNames;
	}
	public Map<String, Integer> getDataSetSizes() {
		return dataSetSizes;
	}
	public void setDataSetSizes(Map<String, Integer> dataSetSizes) {
		this.dataSetSizes = dataSetSizes;
	}
	public Map<String, int[]> getDataSetShapes() {
		return dataSetShapes;
	}
	public void setDataSetShapes(Map<String, int[]> dataSetShapes) {
		this.dataSetShapes = dataSetShapes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataSetNames == null) ? 0 : dataSetNames.hashCode());
		result = prime * result
				+ ((dataSetShapes == null) ? 0 : dataSetShapes.hashCode());
		result = prime * result
				+ ((dataSetSizes == null) ? 0 : dataSetSizes.hashCode());
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
		HierarchicalInfo other = (HierarchicalInfo) obj;
		if (dataSetNames == null) {
			if (other.dataSetNames != null)
				return false;
		} else if (!dataSetNames.equals(other.dataSetNames))
			return false;
		if (dataSetShapes == null) {
			if (other.dataSetShapes != null)
				return false;
		} else if (!dataSetShapes.equals(other.dataSetShapes))
			return false;
		if (dataSetSizes == null) {
			if (other.dataSetSizes != null)
				return false;
		} else if (!dataSetSizes.equals(other.dataSetSizes))
			return false;
		return true;
	}

	public void addName(String fullName) {
		this.dataSetNames.add(fullName);
	}

	public void putShape(String fullName, int[] intShape) {
		this.dataSetShapes.put(fullName, intShape);
	}

	public void putSize(String fullName, int size) {
		this.dataSetSizes.put(fullName, size);
	}
	
	public void putAttribute(String fullAttributePath, String value) {
		this.attributeValues.put(fullAttributePath, value);
	}

	/**
	 * Returns a list of all the full paths to all the attributes.
	 * @return
	 */
	public Collection<String> getAttributes() {
		return attributeValues.keySet();
	}
	
	public String getAttributeValue(String key) {
		return attributeValues.get(key);
	}

}
