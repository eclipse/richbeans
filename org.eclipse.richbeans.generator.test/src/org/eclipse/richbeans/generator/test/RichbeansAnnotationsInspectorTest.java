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

package org.eclipse.richbeans.generator.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.eclipse.richbeans.api.generator.RichbeansAnnotations.MaximumValue;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.MinimumValue;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.UiHidden;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.UiTooltip;
import org.eclipse.richbeans.api.generator.RichbeansAnnotations.Units;
import org.eclipse.richbeans.generator.RichbeansAnnotationsInspector;
import org.junit.Test;
import org.metawidget.inspector.impl.propertystyle.Property;

public class RichbeansAnnotationsInspectorTest {
	@Test
	public void testSetsUpMinimumValue() throws Exception{
		assertThat(inspectMockProperty().get(RichbeansAnnotationsInspector.MINIMUM_VALUE), is("5"));
	}

	@Test
	public void testSetsUpMaximumValue() throws Exception{
		assertThat(inspectMockProperty().get(RichbeansAnnotationsInspector.MAXIMUM_VALUE), is("500"));
	}

	@Test
	public void testSetsUpUnits() throws Exception{
		assertThat(inspectMockProperty().get(RichbeansAnnotationsInspector.UNITS), is("BTUH"));
	}

	@Test
	public void testSetsTooltip() throws Exception{
		assertThat(inspectMockProperty().get(RichbeansAnnotationsInspector.TOOLTIP), is("a tooltip"));
	}

	@Test
	public void testSetsHiddenBoolean() throws Exception{
		assertThat(inspectMockProperty().get(RichbeansAnnotationsInspector.HIDDEN), is("true"));
	}

	private Map<String, String> inspectMockProperty() throws Exception {
		Property property = new MockProperty();
		Map<String, String> map = new RichbeansAnnotationsInspector().inspectProperty(property);
		return map;
	}

	public class MockProperty implements Property{
		@MinimumValue("5")
		@MaximumValue("500")
		@Units("BTUH")
		@UiHidden()
		@UiTooltip("a tooltip")
		public void annotatedMethod(){}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotation) {
			try {
				return this.getClass().getMethod("annotatedMethod").getAnnotation(annotation);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
			return false;
		}
		@Override
		public String getName() {
			return null;
		}
		@Override
		public void write(Object obj, Object value) {}
		@Override
		public Object read(Object obj) {
			return null;
		}
		@Override
		public boolean isWritable() {
			return false;
		}
		@Override
		public boolean isReadable() {
			return false;
		}
		@Override
		public String getType() {
			return null;
		}
		@Override
		public String getGenericType() {
			return null;
		}
	}
}
