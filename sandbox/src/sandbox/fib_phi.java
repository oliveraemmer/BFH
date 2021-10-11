package sandbox;

import java.lang.Math;
import java.math.BigDecimal;

public class fib_phi {

	public static void main(String[] args) {

		//BigDecimal Phi = new BigDecimal((1 + Math.sqrt(5))/2);
		//System.out.println(Phi);
		//BigDecimal phi = new BigDecimal((1-Math.sqrt(5))/2);

		// Start Timer
		long startTime = System.nanoTime();

		// Calculate Fibonacci with Phi
		double fib = 92;
		BigDecimal res = new BigDecimal(((Math.pow(((1 + Math.sqrt(5)) / 2), fib) - Math.pow(((1 - Math.sqrt(5)) / 2), fib))/Math.sqrt(5)));

		// Stop Timer
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Time: " + total + " ns");

		// Print Result
		System.out.println("Fibonacci: " + res);

		// Start Timer
		long startTime2 = System.nanoTime();

		// Calculate Fibonacci with Phi
		double fib2 = 92;
		double Phi = Math.pow(((1 + Math.sqrt(5)) / 2), fib2);
		double phi = Math.pow(((1 - Math.sqrt(5)) / 2), fib2);
		double res2 = (Phi - phi)/Math.sqrt(5);

		// Stop Timer
		long endTime2 = System.nanoTime();
		long total2 = endTime2 - startTime2;
		System.out.println("Time: " + total2 + " ns");

		// Print Result
		String resultat = String.format("%.1f", res2);
		System.out.println("Fibonacci: " + resultat);

	}
}
