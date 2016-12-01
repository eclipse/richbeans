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
import java.util.ArrayList;
import java.util.List;

public class TestList implements Serializable {

	private List<TestBean> testBeans;

	/**
	 * @return Returns the testBeans.
	 */
	public List<TestBean> getTestBeans() {
		return testBeans;
	}

	/**
	 * @param testBeans The testBeans to set.
	 */
	public void setTestBeans(List<TestBean> testBeans) {
		this.testBeans = testBeans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testBeans == null) ? 0 : testBeans.hashCode());
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
		TestList other = (TestList) obj;
		if (testBeans == null) {
			if (other.testBeans != null)
				return false;
		} else if (!testBeans.equals(other.testBeans))
			return false;
		return true;
	}

	public void add(TestBean t) {
		if (testBeans==null) testBeans = new ArrayList<TestBean>(3);
		testBeans.add(t);
	}

}
