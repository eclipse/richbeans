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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.richbeans.annot.DOEUtils;
import org.eclipse.richbeans.annot.RangeInfo;
import org.junit.Test;

public class DOEInfoTest {

	@Test
	public void testNested1() throws Throwable {

		final TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");

	    final TestContainer c = new TestContainer();
	    c.setTestBean(t);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

		System.out.println(RangeInfo.format(os));
	}

	@Test
	public void testNested2() throws Throwable {

		final TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");

	    final TestContainer c = new TestContainer();
	    c.setTestBean(t);

	    final TestContainerContainer r = new TestContainerContainer();
	    r.setTestContainer(c);

		final List<RangeInfo> os = DOEUtils.getInfo(r);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}

	@Test
	public void testNestedList1() throws Throwable {

		final List<TestBean> beans = new ArrayList<TestBean>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");
		beans.add(t);

		final TestList c = new TestList();
	    c.setTestBeans(beans);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}


	@Test
	public void testNestedList2() throws Throwable {

		final List<TestBean> beans = new ArrayList<TestBean>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");
		beans.add(t);

		t = new TestBean();
		t.setI("4.0");
		t.setJ("4.0");
		t.setK("4.0");
		beans.add(t);

	    final TestList c = new TestList();
	    c.setTestBeans(beans);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}

	@Test
	public void testNestedList3() throws Throwable {

		final List<TestBean> beans = new ArrayList<TestBean>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");
		beans.add(t);

		t = new TestBean();
		t.setI("4.0;4.5;1.0"); // Only one value.
		t.setJ("4.0;4.5;1.0"); // Only one value.
		t.setK("4.0;4.5;1.0"); // Only one value.
		beans.add(t);

	    final TestList c = new TestList();
	    c.setTestBeans(beans);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}

	@Test
	public void testNestedList4() throws Throwable {

		final List<TestBean> beans = new ArrayList<TestBean>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");
		beans.add(t);

		t = new TestBean();
		t.setI("4.0;5;1.0");
		t.setJ("4.0;5;1.0");
		t.setK("4.0;5;1.0");
		beans.add(t);

	    final TestList c = new TestList();
	    c.setTestBeans(beans);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=216) throw new Exception("Three parameters, each with three values should give 216 experiments!");

	}

	@Test
	public void testNestedList5() throws Throwable {

		final List<TestBean> beans = new ArrayList<TestBean>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");
		beans.add(t);

		t = new TestBean();
		t.setI("4.0");
		t.setJ("4.0");
		t.setK("4.0");
		beans.add(t);

		t = new TestBean();
		t.setI("5.0");
		t.setJ("5.0");
		t.setK("5.0");
		beans.add(t);

		final TestList c = new TestList();
	    c.setTestBeans(beans);

		final List<RangeInfo> os = DOEUtils.getInfo(c);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}

	@Test
	public void testNestedListContainer() throws Throwable {

		final List<TestContainer> tcl = new ArrayList<TestContainer>(2);
		TestBean t = new TestBean();
		t.setI("1;3;1");
		t.setJ("1;3;1");
		t.setK("1;3;1");

	    TestContainer c = new TestContainer();
	    c.setTestBean(t);
	    tcl.add(c);

	    TestContainerList cl = new TestContainerList();
	    cl.setTestContainers(tcl);

		final List<RangeInfo> os = DOEUtils.getInfo(cl);
		if (os.size()!=27) throw new Exception("Three parameters, each with three values should give 27 experiments!");

	}


}
