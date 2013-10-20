package jose.antonio.mena.tonestackcalculator;

import java.text.DecimalFormat;

import org.jscience.mathematics.number.Complex;

import android.util.Log;

public abstract class CircuitComponent {
	
	public enum Type {RESISTOR, CAPACITOR, INDUCTOR, POTENTIOMENTER};
	private String mName;
	private double mValue;
	private Type mType;
	private final static String TAG = "Circuit Component";
	
	public CircuitComponent (double value, String name) {
		setValue(value);
		setName(name);
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public double getValue() {
		return mValue;
	}

	public void setValue(double mValue) {
		this.mValue = mValue;
	}

	public Type getType() {
		return mType;
	}
	
	public void setType(Type type) {
		mType = type;
	}
	
	public abstract Complex getImpedance(double frecuency);
	
	public String getStringValue() {
		double value = getValue();
		
		String s = null;
		DecimalFormat df = new DecimalFormat("0.0#");
		if (value < 1e-9) {
			value = value * 1e12;
			s = df.format(value).concat("p");
		} else if (value >= 1e-9 && value < 1e-6) {
			value = value * 1e9;
			s = df.format(value).concat("n");
		} else if (value >= 1e-6 && value < 1e-3){
			value = value * 1e6;
			s = df.format(value).concat("u");
		} else if (value >= 1e-3 && value < 1){
			value = value * 1000;
			s = df.format(value).concat("m");
		} else if (value >= 1 && value < 1000) {
			s = df.format(value);
		} else if (value >= 1000 && value < 1000000) {
			value = value/1000;
			s = df.format(value).concat("K");
		} else if (value >= 1000000) {
			value = value/1000000;
			s = df.format(value).concat("M");
		}
		return s;
	}
	
	public static boolean parseValue (String s, double[] v) { 
		//s = s.toLowerCase();
		double value = 0; 
		Log.v(TAG, "parsing string " + s);
		try {
			value = Double.parseDouble(s);
			//Log.v (TAG, "parse value: " + value);
			v[0] = value;
			return true;
		} catch (NumberFormatException e) {
			if (s.charAt(s.length()-1) == 'p'){
				s = s.replace("p", "e-12");
			} else if (s.charAt(s.length()-1) == 'n') {
				s = s.replace("n", "e-9");
			} else if (s.charAt(s.length()-1) == 'u'){
				s = s.replace("u", "e-6");
			} else if (s.charAt(s.length()-1) == 'm') {
				s = s.replace("m", "e-3");
			} else if (s.charAt(s.length()-1) == 'k') {
				s = s.replace("k", "e3");
			} else if (s.charAt(s.length()-1) == 'K') {
				s = s.replace("K", "e3");
			} else if (s.charAt(s.length()-1) == 'M') {
				s = s.replace("M", "e6");
			} else {
				return false;
			}
			
			return parseValue(s, v);		
		}
	}
	public double parseAndSetValue(String s) {
		
		double value = 0;
		try {
			value = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			Log.v(TAG, "number exception");
			if (s.contains("u")) {
				s.replace("u", "");
				value = parseAndSetValue(s);
				value = value/1000000;
			}
		}
		
		Log.v (TAG, "value set to " + value);
		return value;
	}
	
}
