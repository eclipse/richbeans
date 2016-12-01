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

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.richbeans.annot.DOEField;

public class TestBean implements Serializable {

	@DOEField(value=1, type=java.lang.Integer.class)
	private String i;

	@DOEField(5)
	private String j;

	@DOEField(9)
	private String k;

	private int d;

	public TestBean(int d) {
		this.d = d;
	}
	public TestBean() {

	}
	public TestBean(String i, String j, String k) {
		setI(i);
		setJ(j);
		setK(k);
	}

	/**
	 * @return Returns the i.
	 */
	public String getI() {
		return i;
	}

	/**
	 * @param i The i to set.
	 */
	public void setI(String i) {
		this.i = i;
	}

	/**
	 * @return Returns the j.
	 */
	public String getJ() {
		return j;
	}

	/**
	 * @param j The j to set.
	 */
	public void setJ(String j) {
		this.j = j;
	}

	/**
	 * @return Returns the k.
	 */
	public String getK() {
		return k;
	}

	/**
	 * @param k The k to set.
	 */
	public void setK(String k) {
		this.k = k;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + d;
		result = prime * result + ((i == null) ? 0 : i.hashCode());
		result = prime * result + ((j == null) ? 0 : j.hashCode());
		result = prime * result + ((k == null) ? 0 : k.hashCode());
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
		TestBean other = (TestBean) obj;
		if (d != other.d)
			return false;
		if (i == null) {
			if (other.i != null)
				return false;
		} else if (!i.equals(other.i))
			return false;
		if (j == null) {
			if (other.j != null)
				return false;
		} else if (!j.equals(other.j))
			return false;
		if (k == null) {
			if (other.k != null)
				return false;
		} else if (!k.equals(other.k))
			return false;
		return true;
	}

	@Override
	public String toString() {
		try {
			return BeanUtils.describe(this).toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
}
