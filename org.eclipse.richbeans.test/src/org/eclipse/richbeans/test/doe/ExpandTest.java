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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.dawnsci.doe.DOEUtils;
import org.junit.Test;

public class ExpandTest {

	@Test
    public void expandGoodPositive() throws Exception {		
		assertEquals(11, DOEUtils.expand("0; 10; 1").size());
		assertEquals(1, DOEUtils.expand("0; 10; 100").size());
		assertEquals(11, DOEUtils.expand("0; 100; 10").size());	
	}
	
	@Test
    public void expandGoodNegative() throws Exception {		
		assertEquals(11, DOEUtils.expand("0; -10; -1").size());
		assertEquals(1, DOEUtils.expand("0; -10; -100").size());
		assertEquals(11, DOEUtils.expand("0; -100; -10").size());
	}

	@Test(expected=java.lang.IllegalArgumentException.class)
    public void expandBadZero() throws Exception {		
		DOEUtils.expand("0; -10; 0");
	}
	
	@Test(expected=java.lang.IllegalArgumentException.class)
    public void expandBadPositive() throws Exception {		
		DOEUtils.expand("0; -10; 1");
	}

	@Test(expected=java.lang.IllegalArgumentException.class)
    public void expandBadNegative() throws Exception {		
		DOEUtils.expand("0; 10; -1");
	}

	@Test
	public void expandSingleValue() throws Exception {		
       	List<Number> list = DOEUtils.expand("1.1");
 		assertEquals(1, list.size());
		assertEquals(list.get(0), 1.1);
	}

	@Test
	public void expandSingleValueUnit() throws Exception {		
       	List<Number> list = DOEUtils.expand("1.1 m", "m");
 		assertEquals(1, list.size());
		assertEquals(list.get(0), 1.1);
	}

	@Test
	public void expandListIntegerThree() throws Exception {	
    	List<Number> list = DOEUtils.expand("1, 2, 3");
		assertEquals(3, list.size());
		assertEquals(list.get(0).intValue(), 1);
		assertEquals(list.get(1).intValue(), 2);
		assertEquals(list.get(2).intValue(), 3);
	}
    
	@Test
	public void expandListDoubleThree() throws Exception {		
       	List<Number> list = DOEUtils.expand("1.1, 2.2, 3.3");
 		assertEquals(3, list.size());
		assertEquals(list.get(0), 1.1);
		assertEquals(list.get(1), 2.2);
		assertEquals(list.get(2), 3.3);
	}

	@Test
	public void expandListIntegerThreeUnit() throws Exception {	
    	List<Number> list = DOEUtils.expand("1, 2, 3 fred", "fred");
		assertEquals(3, list.size());
		assertEquals(list.get(0).intValue(), 1);
		assertEquals(list.get(1).intValue(), 2);
		assertEquals(list.get(2).intValue(), 3);
	}
    
	@Test
	public void expandListDoubleThreeUnit() throws Exception {		
       	List<Number> list = DOEUtils.expand("1.1, 2.2, 3.3 kg/m^3", "kg/m^3");
 		assertEquals(3, list.size());
		assertEquals(list.get(0), 1.1);
		assertEquals(list.get(1), 2.2);
		assertEquals(list.get(2), 3.3);
	}

}
