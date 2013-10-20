package jose.antonio.mena.tonestackcalculator;

import org.jscience.mathematics.number.Complex;

public class Resistor extends CircuitComponent {
	
	
	public Resistor(double value, String name) {
		super (value, name);
		this.setType(CircuitComponent.Type.RESISTOR);	
	}

	@Override
	public Complex getImpedance(double frecuency) {
		return Complex.valueOf(getValue(), 0);
	}

}
