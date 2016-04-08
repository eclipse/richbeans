/*-
 * Copyright © 2016 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package org.eclipse.richbeans.generator.example;

public class NestedExample extends GuiGeneratorRunnerBase {

	public static void main(String[] args) {
		new NestedExample().run();
	}

	@Override
	protected Object createTestObject() {

		SimpleBean simpleBean = (SimpleBean) new SimpleExample().createTestObject();
		UpdatingBean updatingBean = (UpdatingBean) new UpdatingExample().createTestObject();

		WrapperBean wrapperBean = new WrapperBean();
		wrapperBean.setName("Nested object example");
		wrapperBean.setDescription("This example shows an auto-generated \nGUI for nested objects\n");
		wrapperBean.setSimpleBean(simpleBean);
		wrapperBean.setUpdatingBean(updatingBean);

		return wrapperBean;
	}

}
