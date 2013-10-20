package jose.antonio.mena.tonestackcalculator;

import java.util.ArrayList;
import java.util.Iterator;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.vector.Vector;

public abstract class Circuit {
	
	protected String mTitle;
	protected ArrayList <CircuitComponent> mComponents;
	
	protected ComplexMatrix matrix;
	protected Vector<Complex> solution;
	
	protected double[] output;
	private CircuitType type;
	
	public final static double pi = 3.14159265;
	
	public enum CircuitType {BIGMUFF, BAXANDALL, LOWPASS};
	
	public Circuit(String title) {
		mTitle = title;
		mComponents = new ArrayList<CircuitComponent>();
	}
	
	public void addComponent(CircuitComponent component) {
		mComponents.add(component);
	}
	
	public void removeCompoment(CircuitComponent component) {
		mComponents.remove(component);
	}
	
	public int getNumberOfComponents() {
		return mComponents.size();
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public CircuitComponent getCircuitComponentByName(String name) {
	
		int length = mComponents.size();
		
		for (int i = 0; i < length; i++) {
			if (mComponents.get(i).getName().contentEquals(name)){
				return mComponents.get(i);
			}
		}
		return null;
	}
	
	public CircuitComponent getCircuitComponent(CircuitComponent c) {
		
		int index = mComponents.indexOf(c);
		if (index >= 0)
			return mComponents.get(index);
		return null;
		
	}
	
	public int getNumberOfPotentiometers(){
		int count = 0;
		for (int i = 0; i < mComponents.size(); i++) {
			if(mComponents.get(i).getType() == CircuitComponent.Type.POTENTIOMENTER)
				count++;
		}
		return count;
	}
	
	public ArrayList<CircuitComponent> getPotentiometers() {
		ArrayList<CircuitComponent> potentiometers = new ArrayList<CircuitComponent>();
		
		for (int i = 0; i < mComponents.size(); i++) {
			if(mComponents.get(i).getType() == CircuitComponent.Type.POTENTIOMENTER)
				potentiometers.add(mComponents.get(i));
		}
		return potentiometers;
	}
	
	/*
	 * This method must be implemented by subclasses it receives an array of input voltages
	 * and returns the output, if isLog is SET, it should return the values in dB
	 */
	public abstract double[] getCurve(double[] input, boolean isLog);

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mTitle;
	}

	public CircuitType getType() {
		return type;
	}

	public void setType(CircuitType type) {
		this.type = type;
	}
	
	

}
