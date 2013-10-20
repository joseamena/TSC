package jose.antonio.mena.tonestackcalculator;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import android.util.Log;

public class ComplexMatrixOperations {
	
	private final static String TAG = "Matrix OPS";
	
	public static FieldMatrix<Complex> multiplyRowByValue(FieldMatrix <Complex> matrix,
			int row, Complex value ) {

		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			matrix.multiplyEntry(row, i, value);
		}
		return matrix;
	}
	
	public static FieldMatrix<Complex> multiplyRowAndAddtoRow(FieldMatrix <Complex> matrix,
			int row1, int row2, Complex multiplier) {

		//System.out.println("multiplier: " + multiplier);
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			
			matrix.setEntry(row2, i,(matrix.getEntry(row1, i)
					.multiply(multiplier).add(matrix.getEntry(row2, i))));
		}

		
		return matrix;
	}

	public static Complex getDeterminant (FieldMatrix <Complex> matrix) {
		///Complex result = null;
		
		if (matrix.getRowDimension() == 2) {
			 return matrix.getEntry(0, 0).
					multiply(matrix.getEntry(1, 1)).
					subtract(matrix.getEntry(0, 1).multiply(matrix.getEntry(1, 0))) ;
		}
		
		return null;	
		
	}
}