package org.eclipse.dawnsci.macro.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacroUtils {

    /**
     * Return a legal name for most macro languages, but
     * especially in python.
     * 
     * @param setName
     * @return
     */
	public static String getLegalName(String setName) {
		if (setName.endsWith("/"))   setName = setName.substring(0,setName.length()-1);
		if (setName.indexOf('/')>-1) setName = setName.substring(setName.lastIndexOf('/'));
		
		setName = setName.replaceAll(" ", "_");
		setName = setName.replaceAll("[^a-zA-Z0-9_]", "");
		final Matcher matcher = Pattern.compile("(\\d+)(.+)").matcher(setName);
		if (matcher.matches()) {
			setName = matcher.group(2);
		}
		
		if (Pattern.compile("(\\d+)").matcher(setName).matches()) {
			setName= "x"+setName;
		}
		return setName;
	}

}
