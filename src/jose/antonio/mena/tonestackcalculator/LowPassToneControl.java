package jose.antonio.mena.tonestackcalculator;

import android.util.Log;

public class LowPassToneControl extends Circuit{

	private Resistor r1;
	private Capacitor c1;
	private Potentiometer tone;
	
	private final static String TAG = "Low Pass";
	
	public LowPassToneControl() {
		super("Low Pass");
		this.setType(CircuitType.LOWPASS);
		
		r1 = new Resistor(10000, "R1");
		tone = new Potentiometer(50000, "Tone", 0.5);
		c1 = new Capacitor(3.3e-9, "C1");
		
		addComponent(r1);
		addComponent(c1);
		addComponent(tone);
		
	}

	@Override
	public double[] getCurve(double[] input, boolean isLog) {
		
		if(output == null || output.length != input.length) {
			output = new double[input.length];
		}
		
		for (int i = 0; i < input.length; i++) {
			
			double temp = 2 * pi * input[i] * (r1.getValue() + (tone.getValue() * tone.getPosition())) * c1.getValue();
			temp = temp * temp;
			output[i] = 1 / (Math.sqrt(1 + temp));
			Log.v(TAG, "freq:" + input[i] + ", output[" + i + "]= " + output[i]);
			if (isLog) {
				output[i] = 20 * Math.log10(output[i]);
			} 
		}
		return output;
	}

}
