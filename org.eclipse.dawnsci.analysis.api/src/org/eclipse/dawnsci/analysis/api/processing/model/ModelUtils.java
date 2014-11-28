/*-
 * Copyright 2014 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.dawnsci.analysis.api.processing.model;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModelUtils {

	
	public static boolean isFileType(Class<? extends Object> clazz) {
		if (File.class.isAssignableFrom(clazz))       return true;
		if (Path.class.isAssignableFrom(clazz))       return true;
		if (clazz.getName().equals("org.eclipse.core.resources.IResource"))  return true;
		return false;
	}

	public static OperationModelField getAnnotation(IOperationModel model, String fieldName) {
		
		try {
			Field field = getField(model, fieldName);
	        if (field!=null) {
	        	OperationModelField anot = field.getAnnotation(OperationModelField.class);
	        	if (anot!=null) {
	        		return anot;
	        	}
	        }
	        return null;
	        
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Field getField(IOperationModel model, String fieldName) throws NoSuchFieldException, SecurityException {
		
    	Field field;
		try {
			field = model.getClass().getDeclaredField(fieldName);
		} catch (Exception ne) {
			field = model.getClass().getSuperclass().getDeclaredField(fieldName);
		}
		return field;
	}

	
	/**
	 * Get a collection of the fields of the model that should be edited in the User interface
	 * for editing the model.
	 * 
	 * @return collection of fields.
	 * @throws Exception
	 */
	public static Collection<ModelField> getModelFields(IOperationModel model) throws Exception {
		
		// Decided not to use the obvious BeanMap here because class problems with
		// GDA and we have to read annotations anyway.
		final List<Field> allFields = new ArrayList<Field>(31);
		allFields.addAll(Arrays.asList(model.getClass().getDeclaredFields()));
		allFields.addAll(Arrays.asList(model.getClass().getSuperclass().getDeclaredFields()));
		
		// The returned descriptor
		final List<ModelField> ret = new ArrayList<ModelField>();
		
		// fields
		for (Field field : allFields) {
			
			// If there is a getter/isser for the field we assume it is a model field.
			try {
				if (model.isModelField(field.getName())) {			
					ret.add(new ModelField(model, field.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		Collections.sort(ret, new Comparator<ModelField>() {
			@Override
			public int compare(ModelField o1, ModelField o2) {
				return o1.getDisplayName().toLowerCase().compareTo(o2.getDisplayName().toLowerCase());
			}
		});
		
		return ret;
	}

}
