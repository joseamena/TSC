package jose.antonio.mena.tonestackcalculator;

import org.jscience.mathematics.number.Complex;

public class Capacitor extends CircuitComponent {
	
	public Capacitor(double value, String name) {
		super (value, name);
		setType(CircuitComponent.Type.CAPACITOR);
	}

	@Override
	public Complex getImpedance(double frecuency) {
		return Complex.valueOf(0, 1/(2*Circuit.pi*frecuency*getValue()));
	}

}
