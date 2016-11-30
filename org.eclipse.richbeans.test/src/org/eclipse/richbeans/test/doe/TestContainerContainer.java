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

public class TestContainerContainer implements Serializable {

	private TestContainer testContainer;

	/**
	 * @return Returns the testContainer.
	 */
	public TestContainer getTestContainer() {
		return testContainer;
	}

	/**
	 * @param testContainer The testContainer to set.
	 */
	public void setTestContainer(TestContainer testContainer) {
		this.testContainer = testContainer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testContainer == null) ? 0 : testContainer.hashCode());
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
		TestContainerContainer other = (TestContainerContainer) obj;
		if (testContainer == null) {
			if (other.testContainer != null)
				return false;
		} else if (!testContainer.equals(other.testContainer))
			return false;
		return true;
	}

}
