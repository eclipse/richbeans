/*-
 *******************************************************************************
 * Copyright (c) 2011, 2014 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.richbeans.examples.example4.data;


public class OptionItem {

    private String optionName;
	private boolean showAxes, showTitle, showLegend, showData;
	private static int count = 0;
	
	/**
	 * Everything false
	 */
	public OptionItem() {
		this(false, false,false, false);
	}
	public OptionItem(boolean showAxes, boolean showTitle, boolean showLegend,
			boolean showData) {
		
		super();
		this.showAxes = showAxes;
		this.showTitle = showTitle;
		this.showLegend = showLegend;
		this.showData = showData;
		
		optionName = "Option"+(++count);
	}
	
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((optionName == null) ? 0 : optionName.hashCode());
		result = prime * result + (showAxes ? 1231 : 1237);
		result = prime * result + (showData ? 1231 : 1237);
		result = prime * result + (showLegend ? 1231 : 1237);
		result = prime * result + (showTitle ? 1231 : 1237);
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
		OptionItem other = (OptionItem) obj;
		if (optionName == null) {
			if (other.optionName != null)
				return false;
		} else if (!optionName.equals(other.optionName))
			return false;
		if (showAxes != other.showAxes)
			return false;
		if (showData != other.showData)
			return false;
		if (showLegend != other.showLegend)
			return false;
		if (showTitle != other.showTitle)
			return false;
		return true;
	}

	public boolean isShowAxes() {
		return showAxes;
	}

	public void setShowAxes(boolean showAxes) {
		this.showAxes = showAxes;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}
	

	@Override
	public String toString() {
		return "OptionItem [optionName=" + optionName + ", showAxes="
				+ showAxes + ", showTitle=" + showTitle + ", showLegend="
				+ showLegend + ", showData=" + showData + "]";
	}

}
