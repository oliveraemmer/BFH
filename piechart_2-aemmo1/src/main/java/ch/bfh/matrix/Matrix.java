/*
	* Project and Training 2: Pie Chart - Computer Science, Berner Fachhochschule
	*/
package ch.bfh.matrix;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;

/**
	* Represents a two-dimensional matrix of double-values. Objects are immutable
	* and methods implementing matrix operations always return new matrix objects.
	*/
public class Matrix {

	// expected precision in floating point calculations
	public static final double EPSILON = 1e-10;

	// the matrix values in lines and columns
	protected double[][] values;

	/**
		* Creates a matrix with values given in a two-dimensional array. First
		* dimension represents lines, second the columns.
		*
		* @param values a non-empty and rectangular two-dimensional array
		*/
	public Matrix(final double[][] values) throws UnsupportedOperationException {
		if (values == null) {
			throw new UnsupportedOperationException();
		}

		this.values = new double[values.length][values[0].length];
		int len = values[0].length;
		for (int i = 0; i < values.length; i++) {
			if (values[i].length != len || values[i].length == 0) {
				throw new UnsupportedOperationException();
			}
			for (int j = 0; j < values[0].length; j++) {
				this.values[i][j] = values[i][j];
			}
		}
	}

	/**
		* @return the number of lines in this matrix
		*/
	public int getNbOfLines() {
		return values.length;
	}

	/**
		* @return the number of columns in this matrix
		*/
	public int getNbOfColumns() {
		return values[0].length;
	}

	/**
		* Returns the value at the given position in the matrix.
		*
		* @param line indicates the index for the line
		* @param col  indicates the index for the column
		* @return the value at the indicated position
		*/
	public double get(final int line, final int col) throws UnsupportedOperationException {
		return values[line][col];
	}

	/**
		* Calculates the transpose of this matrix.
		*
		* @return the transpose of this matrix
		*/
	public Matrix transpose() {

		double[][] transposedValues = new double[values[0].length][values.length];
		for (int i = 0; i < values[0].length; i++) {
			for (int j = 0; j < values.length; j++) {
				transposedValues[i][j] = values[j][i];
			}
		}
		return new Matrix(transposedValues);
	}

	/**
		* Calculates the product of this matrix with the given scalar value.
		*
		* @param scalar the scalar value to multiply with
		* @return the scalar product
		*/
	public Matrix multiply(final double scalar) {
		double[][] multipliedValues = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				multipliedValues[i][j] =
						BigDecimal.valueOf(values[i][j]).multiply(BigDecimal.valueOf(scalar)).doubleValue();
			}
		}
		return new Matrix(multipliedValues);
	}

	/**
		* Calculates the product of this matrix with another matrix.
		*
		* @param other the other matrix to multiply with
		* @return the matrix product
		*/
	public Matrix multiply(final Matrix other) {
		if (values[0].length != other.getNbOfLines()) {
			throw new UnsupportedOperationException();
		}
		double[][] multipliedValues = new double[values.length][other.getNbOfColumns()];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < other.getNbOfColumns(); j++) {
				double valuesOfLine = 0;
				for (int k = 0; k < values[0].length; k++) {
					valuesOfLine += values[i][k] * other.get(k, j);
				}
				multipliedValues[i][j] = valuesOfLine;
			}
		}
		return new Matrix(multipliedValues);
	}

	/**
		* Calculates the sum of this matrix with another matrix.
		*
		* @param other the other matrix to add with
		* @return the matrix sum
		*/
	public Matrix add(final Matrix other) {
		if ((values.length != other.getNbOfLines()) || values[0].length != other.getNbOfColumns()) {
			throw new UnsupportedOperationException();
		}
		double[][] addedValues = new double[values.length][values[0].length];
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < other.getNbOfColumns(); j++) {
				addedValues[i][j] = values[i][j] + other.get(i, j);
			}
		}
		return new Matrix(addedValues);
	}

	@Override
	public int hashCode() {
		BigInteger hashCode = BigInteger.ZERO;
		for (int i = 0; i < values.length; i++) {
			hashCode.add(BigInteger.valueOf(Arrays.hashCode(values[i])));
		}
		return hashCode.intValue();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Matrix m = (Matrix) obj;

		if ((this.getNbOfColumns() != m.getNbOfColumns()) || (this.getNbOfLines() != m.getNbOfLines())) {
			return false;
		}

		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[0].length; j++) {
				if (!BigDecimal.valueOf(this.get(i, j))
						.setScale(10, RoundingMode.HALF_DOWN)
						.equals(BigDecimal.valueOf(m.get(i, j))
								.setScale(10, RoundingMode.HALF_DOWN))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < values.length; i++) {
			string += Arrays.toString(values[i]);
		}
		// implementation optional
		return string;
	}
}
