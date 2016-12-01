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

public class TestContainer  implements Serializable{

	private TestBean testBean;

	/**
	 * @return Returns the testBean.
	 */
	public TestBean getTestBean() {
		return testBean;
	}

	/**
	 * @param testBean The testBean to set.
	 */
	public void setTestBean(TestBean testBean) {
		this.testBean = testBean;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testBean == null) ? 0 : testBean.hashCode());
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
		TestContainer other = (TestContainer) obj;
		if (testBean == null) {
			if (other.testBean != null)
				return false;
		} else if (!testBean.equals(other.testBean))
			return false;
		return true;
	}
}
