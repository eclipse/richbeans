package org.eclipse.richbeans.widgets.decorator;

/**
 * 
 * Interface to 
 * @author Matthew Gerring
 *
 */
public interface IDecoratorValidator {
	
	/**
	 * The expression used to validate. May be a regex or 
	 * variable expression.
	 * @return
	 */
	public String getExpression();

	/**
	 * All decorators must check a value and return if it is valid or invalid.
	 * @param value
	 * @param delta
	 * @return
	 */
	default boolean check(String value, String delta) {
		return true;
	}


	/**
	 * Parse a string to the required value.
	 * @param totalString
	 * @return
	 */
	default Number parseValue(String totalString) {
		if ("".equals(totalString)) {
			return Double.NaN;
		}
		Number val = null;
		if ("∞".equals(totalString)) {
			val = Double.POSITIVE_INFINITY;
		} else if ("-∞".equals(totalString)) {
			val = Double.NEGATIVE_INFINITY;
		} else {
			try {
		        val = Double.parseDouble(totalString);
			} catch (Exception empty) {
				val = Double.NaN;
			}
		}
		return val;
	}

}
