/*-
 *******************************************************************************
 * Copyright (c) 2011, 2016 Diamond Light Source Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Matthew Gerring - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.richbeans.test.doe;

import java.io.Serializable;
import java.util.List;

public class TestContainerList implements Serializable {

	private List<TestContainer> testContainers;

	/**
	 * @return Returns the testContainers.
	 */
	public List<TestContainer> getTestContainers() {
		return testContainers;
	}

	/**
	 * @param testContainers The testContainers to set.
	 */
	public void setTestContainers(List<TestContainer> testContainers) {
		this.testContainers = testContainers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testContainers == null) ? 0 : testContainers.hashCode());
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
		TestContainerList other = (TestContainerList) obj;
		if (testContainers == null) {
			if (other.testContainers != null)
				return false;
		} else if (!testContainers.equals(other.testContainers))
			return false;
		return true;
	}


}
