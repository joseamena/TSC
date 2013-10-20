package jose.antonio.mena.tonestackcalculator;

//import org.apache.commons.math3.complex.Complex;
//import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
//import org.apache.commons.math3.linear.FieldMatrix;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.ComplexVector;
import org.jscience.mathematics.vector.Vector;


import android.util.Log;

public class BigMuffPiCircuit extends Circuit {

	private Resistor r1,r2;
	private Capacitor c1,c2;
	private Potentiometer tone;

	private ComplexVector row1, row2, B;
	
	private final static String TAG = "bmpSolver";

	private void initializeComponentsArray() {
		addComponent(r1);
		addComponent(r2);
		addComponent(c1);
		addComponent(c2);
		addComponent(tone);
	}
	public BigMuffPiCircuit () {
		super("Big Muff");
		this.setType(CircuitType.BIGMUFF);
		r1 = new Resistor(39000, "R1");
		r2 = new Resistor(22000, "R2");
		tone = new Potentiometer(100000, "Tone", 0.5);
		c1 = new Capacitor(10e-9, "C1");
		c2 = new Capacitor(4e-9, "C2");
		
		this.initializeComponentsArray();
		
	}

	public BigMuffPiCircuit(double r1Value, String r1Name, 
							double r2Value, String r2Name, 
							double c1Value, String c1Name,
							double c2Value, String c2Name,
							double toneValue, String toneName, double tonePosition) {
		super("Big Muff");
		this.setType(CircuitType.BIGMUFF);
		
		this.r1 = new Resistor(r1Value, r1Name);
		this.r2 = new Resistor(r2Value, r2Name);
		this.tone = new Potentiometer (toneValue, toneName, tonePosition);
		this.c1 = new Capacitor(c1Value, c1Name);
		this.c2 = new Capacitor(c2Value, c2Name);
		
		this.initializeComponentsArray();
		
	}
	


	@Override
	public double[] getCurve(double[] input, boolean isLog) {
		
		if(output == null || output.length != input.length) {
			output = new double[input.length];
		}

		for (int i = 0; i < input.length; i++) {
			//Log.v (TAG, "calculating for frecuency: " + input[i]);
			row1 = ComplexVector.valueOf(
					Complex.valueOf(1/r1.getValue() + 1/tone.getValue() , 2*pi*input[i]*c1.getValue()),
					Complex.valueOf(-1/tone.getValue(), 0));
			
			
			row2 = ComplexVector.valueOf(
					Complex.valueOf(-1/tone.getValue(),0), 
					Complex.valueOf(1/r2.getValue()+1/tone.getValue(), 2*pi*input[i]*c2.getValue()));
			
			
			matrix = ComplexMatrix.valueOf(row1, row2);
			
			
			//debug
			/*
			for (int k = 0; k < matrix.getNumberOfRows(); k++) {
				
			
				Log.v(TAG, "row" + k );
				
				for (int l = 0; l < matrix.getNumberOfColumns(); l++) {
					Log.v (TAG, "M" + k + l + "= " + matrix.get(k, l));
				}
				
			}*/
			
			B = ComplexVector.valueOf(
					Complex.valueOf(1/r1.getValue(), 0), 
					Complex.valueOf(0, 2*pi*input[i]*c2.getValue()));
			
			solution = matrix.solve(B);
			
			//Complex temp[] = new Complex[input.length];
			
			//temp[i] = (solution.get(0).times(tone.getPosition())).plus((solution.get(1).times(1-tone.getPosition())));
			
			//output[i] = temp[i].magnitude();
			//output[i] = solution.get(0).magnitude();
			output[i] = (solution.get(1).times(tone.getPosition()).plus( 
					solution.get(0).times(1-tone.getPosition()))).magnitude();
			
			
			
			//output[i] = 1;
			if (isLog) {
				//Log.v (TAG, "is log");
				output[i] = 20 * Math.log10(output[i]);
			} 
			
			//Log.v(TAG, "freq[" + i + "] = " + input[i] + ", " + "output[" + i + "] = " + output[i]);
		
			//Log.v(TAG, "matrix: " + matrix);
			/*
			Log.v (TAG,"SOL: " + solution);
			
			Complex a,b,c,d, B1, B2, V2, V1;
			
			a = matrix.get(0, 0);
			b = matrix.get(0, 1);
			c = matrix.get(1, 0);
			d = matrix.get(1, 1);
			B1 = B.get(0);
			B2 = B.get(1);
			
			V2 = B2.minus(c.times(B1).divide(a)).divide(d.minus(c.times(b).divide(a)));
			
			V1 = B1.divide(a).minus(b.divide(a).times(V2));
			Log.v(TAG, "V1:" + V1 + ", V2: " + V2);*/
			
			
		}
		
		
		return output;
	
	}
	
	
}
