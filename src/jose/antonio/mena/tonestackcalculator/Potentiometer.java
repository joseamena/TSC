package jose.antonio.mena.tonestackcalculator;

import org.jscience.mathematics.number.Complex;

public class Potentiometer extends CircuitComponent {

	private double mPosition;
	public Potentiometer(double value, String name) {
		super(value, name);
		setType(CircuitComponent.Type.POTENTIOMENTER);
	}
	
	public Potentiometer(double value, String name, double position) {
		super(value, name);
		setType(CircuitComponent.Type.POTENTIOMENTER);
		this.setPosition(position);
		
	}

	@Override
	public Complex getImpedance(double frecuency) {
		return Complex.valueOf(getValue(), 0);
	}

	public double getPosition() {
		return mPosition;
	}

	public void setPosition(double position) {
		this.mPosition = position;
	}

}
