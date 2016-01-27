// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package org.eclipse.richbeans.generator;

import static org.metawidget.inspector.InspectionResultConstants.ACTION;
import static org.metawidget.inspector.InspectionResultConstants.NAME;
import static org.metawidget.inspector.InspectionResultConstants.NO_SETTER;
import static org.metawidget.inspector.InspectionResultConstants.PROPERTY;
import static org.metawidget.inspector.InspectionResultConstants.TRUE;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.metawidget.swt.SwtMetawidget;
import org.metawidget.util.CollectionUtils;
import org.metawidget.util.simple.ObjectUtils;
import org.metawidget.util.simple.PathUtils;
import org.metawidget.util.simple.StringUtils;
import org.metawidget.widgetprocessor.iface.AdvancedWidgetProcessor;
import org.metawidget.widgetprocessor.iface.WidgetProcessorException;

/**
 * This is a improved implementation of the eclipse core databinding provided by metawidget. It implements two way binding with dynamic updating of UI to model
 * and on request binding of model to UI (as the model may not implement property change listeners).
 * <p>
 * Original metawidget java doc:
 * <p>
 * Property binding implementation based on <code>eclipse.core.databinding</code>.
 * <p>
 * This implementation does <em>not</em> require JFace. JFace is separate from <code>eclipse.core.databinding</code>, as discussed here
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=153630.
 * <p>
 * <p>
 * Note: <code>eclipse.core.databinding</code> does not bind <em>actions</em>, such as invoking a method when a <code>Button</code> is pressed. For that, see
 * <code>ReflectionBindingProcessor</code> and <code>MetawidgetActionStyle</code>.
 */
public class TwoWayDataBindingProcessor implements AdvancedWidgetProcessor<Control, SwtMetawidget> {

	private List<DisplayRealm> mRealms = CollectionUtils.newArrayList();
	private final Map<ConvertFromTo, IConverter> mConverters = CollectionUtils.newHashMap();

	public TwoWayDataBindingProcessor() {
		this(new TwoWayDataBindingProcessorConfig());
	}

	public TwoWayDataBindingProcessor(TwoWayDataBindingProcessorConfig config) {

		// Register converters
		IConverter[] converters = config.getConverters();

		if (converters != null) {
			for (IConverter converter : converters) {
				mConverters.put(new ConvertFromTo((Class<?>) converter.getFromType(), (Class<?>) converter.getToType()), converter);
			}
		}
	}

	@Override
	public void onStartBuild(SwtMetawidget metawidget) {
		metawidget.setData(TwoWayDataBindingProcessor.class.getName(), null);
	}

	@Override
	public Control processWidget(Control control, String elementName, Map<String, String> attributes, SwtMetawidget metawidget) {

		if (ACTION.equals(elementName)) {
			return control;
		}

		// Nested Metawidgets are not bound, only remembered
		if (control instanceof SwtMetawidget) {

			State state = getState(metawidget);

			if (state.nestedMetawidgets == null) {
				state.nestedMetawidgets = CollectionUtils.newHashSet();
			}

			state.nestedMetawidgets.add((SwtMetawidget) control);
			return control;
		}

		String controlProperty = metawidget.getValueProperty(control);

		if (controlProperty == null) {
			return control;
		}

		// Observe the control
		State state = getState(metawidget);
		Realm realm = state.bindingContext.getValidationRealm();

		IObservableValue observeTarget = null;
		if (controlProperty.equalsIgnoreCase("selection")) {
			observeTarget = WidgetProperties.selection().observe(realm, control);
		} else if (controlProperty.equalsIgnoreCase("text")) {
			try {
				// Use SWT.Modify if possible to catch all changes on Text or StyledText widgets
				observeTarget = WidgetProperties.text(SWT.Modify).observe(realm, control);
			} catch (Exception e) {
				// Fall back to trying normal text observation for other widgets (e.g. Label, Button etc)
				observeTarget = WidgetProperties.text().observe(realm, control);
			}
		} else {
			// Not sure how to do the binding, so act as if controlProperty is null
			return control;
		}

		UpdateValueStrategy targetToModel;

		// (NO_SETTER model values are one-way only)

		if (TRUE.equals(attributes.get(NO_SETTER))) {
			targetToModel = new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER);
		} else {
			targetToModel = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		}

		// Observe the model
		Object toInspect = metawidget.getToInspect();
		String propertyName = PathUtils.parsePath(metawidget.getInspectionPath()).getNames()
				.replace(StringUtils.SEPARATOR_FORWARD_SLASH_CHAR, StringUtils.SEPARATOR_DOT_CHAR);

		if (PROPERTY.equals(elementName)) {
			if (propertyName.length() > 0) {
				propertyName += StringUtils.SEPARATOR_DOT_CHAR;
			}

			propertyName += attributes.get(NAME);
		}

		// Setup the bean to UI binding. If the bean had property change support use it else to on request binding
		IObservableValue observeModel = null;
		UpdateValueStrategy modelToTarget = null;
		// Check if the bean has an addPropertyChangeListener method
		try {
			// calling getMethod will throw the NoSuchMethodException if its not available i.e. bean has no
			// property change support
			toInspect.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class);
			// If the addPropertyChangeListener method exists add dynamic 2 way binding
			observeModel = BeanProperties.value(toInspect.getClass(), propertyName).observe(realm, toInspect);
			modelToTarget = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		} catch (NoSuchMethodException | SecurityException e) { // No PropertyChangeListener or can't access
			observeModel = PojoProperties.value(toInspect.getClass(), propertyName).observe(realm, toInspect);
			modelToTarget = new UpdateValueStrategy(UpdateValueStrategy.POLICY_ON_REQUEST);
		}

		// Check for enums and if they exist make a converter on the fly
		// (Note: this logic seems to work but is potentially incomplete - see comments inside Metawidget's PropertyTypeInspector)
		final Class<?> elementType = (Class<?>) observeModel.getValueType();
		if (elementType != null && elementType.isEnum()) {
			Converter converter = new Converter(String.class, elementType) {
				@Override
				public Object convert(Object fromObject) {
					// Try to compare results of toString() first
					for (Object value : elementType.getEnumConstants()) {
						if (value.toString().equals(fromObject.toString())) {
							return value;
						}
					}
					// Otherwise compare declared enum names
					for (Object value : elementType.getEnumConstants()) {
						if (((Enum<?>) value).name().equalsIgnoreCase(fromObject.toString())) {
							return value;
						}
					}
					return null;
				}
			};
			mConverters.put(new ConvertFromTo(String.class, elementType), converter);
		}

		// Add converters
		targetToModel.setConverter(getConverter((Class<?>) observeTarget.getValueType(), (Class<?>) observeModel.getValueType()));
		modelToTarget.setConverter(getConverter((Class<?>) observeModel.getValueType(), (Class<?>) observeTarget.getValueType()));

		// Bind it
		state.bindingContext.bindValue(observeTarget, observeModel, targetToModel, modelToTarget);

		return control;
	}

	public Object convertFromString(String value, Class<?> expectedType) {

		IConverter converterFromString = getConverter(String.class, expectedType);

		if (converterFromString != null) {
			return converterFromString.convert(value);
		}

		return value;
	}

	@Override
	public void onEndBuild(SwtMetawidget metawidget) {

		State state = getState(metawidget);
		state.bindingContext.updateTargets();
	}

	public void save(final SwtMetawidget metawidget) {

		// Our bindings
		State state = getState(metawidget);
		state.bindingContext.updateModels();

		for (Object validationStatusProvider : state.bindingContext.getValidationStatusProviders()) {
			Binding binding = (Binding) validationStatusProvider;
			Status bindingStatus = (Status) binding.getValidationStatus().getValue();

			if (bindingStatus.isOK()) {
				continue;
			}

			throw WidgetProcessorException.newException(bindingStatus.getException());
		}

		// Nested Metawidgets
		if (state.nestedMetawidgets != null) {
			for (SwtMetawidget nestedMetawidget : state.nestedMetawidgets) {
				save(nestedMetawidget);
			}
		}
	}

	private State getState(SwtMetawidget metawidget) {

		State state = (State) metawidget.getData(TwoWayDataBindingProcessor.class.getName());

		if (state == null) {
			state = new State();
			state.bindingContext = new DataBindingContext(getRealm(metawidget.getDisplay()));

			metawidget.setData(TwoWayDataBindingProcessor.class.getName(), state);

		}

		return state;
	}

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */
	private Realm getRealm(final Display display) {

		synchronized (mRealms) {
			for (DisplayRealm realm : mRealms) {
				if (realm.mDisplay == display) {
					return realm;
				}
			}
			DisplayRealm realm = new DisplayRealm(display);
			mRealms.add(realm);
			return realm;
		}
	}

	/**
	 * Gets the IConverter for the given Class (if any).
	 * <p>
	 * Includes traversing superclasses of the given <code>sourceClass</code> for a suitable IConverter, so for example registering a IConverter for
	 * <code>Number.class</code> will match <code>Integer.class</code>, <code>Double.class</code> etc., unless a more subclass-specific IConverter is also
	 * registered.
	 */
	private IConverter getConverter(Class<?> sourceClass, final Class<?> targetClass) {

		Class<?> sourceClassTraversal = sourceClass;

		while (sourceClassTraversal != null) {
			IConverter converter = mConverters.get(new ConvertFromTo(sourceClassTraversal, targetClass));

			if (converter != null) {
				return converter;
			}

			sourceClassTraversal = sourceClassTraversal.getSuperclass();
		}

		return null;
	}

	//
	// Inner class
	//

	/**
	 * Simple, lightweight structure for saving state.
	 */

	/* package private */static class State {

		/* package private */DataBindingContext bindingContext;

		/* package private */Set<SwtMetawidget> nestedMetawidgets;
	}

	/**
	 * From org.eclipse.jface.databinding.swt.SWTObservables (EPLv1)
	 */

	static class DisplayRealm extends Realm {

		//
		// Private members
		//

		Display mDisplay;

		//
		// Constructor
		//

		DisplayRealm(Display display) {

			mDisplay = display;
		}

		//
		// Public methods
		//

		@Override
		public boolean isCurrent() {

			return Display.getCurrent() == mDisplay;
		}

		// Do not override equals/hashCode, we are not going to be comparing
		// this or hashing it
	}

	/* package private */static final class ConvertFromTo {

		//
		// Private members
		//

		private Class<?> mSource;

		private Class<?> mTarget;

		//
		// Constructor
		//

		public ConvertFromTo(Class<?> source, Class<?> target) {

			mSource = source;
			mTarget = target;
		}

		//
		// Public methods
		//

		@Override
		public boolean equals(Object that) {

			if (this == that) {
				return true;
			}

			if (!ObjectUtils.nullSafeClassEquals(this, that)) {
				return false;
			}

			if (!ObjectUtils.nullSafeEquals(mSource, ((ConvertFromTo) that).mSource)) {
				return false;
			}

			if (!ObjectUtils.nullSafeEquals(mTarget, ((ConvertFromTo) that).mTarget)) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {

			int hashCode = 1;
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(mSource.hashCode());
			hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(mTarget.hashCode());

			return hashCode;
		}
	}
}
