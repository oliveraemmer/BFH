package sandbox;
import java.math.BigInteger;

public class fib_slow {

	public static void main(String[] args) {

		// Declare Variables
		long fib = 1000000;
		BigInteger currentNum = new BigInteger("0");
		BigInteger nextNum = new BigInteger("1");
		BigInteger sum = new BigInteger("0");
		int whileCounter = 0;

		// Start Timer
		long startTime = System.nanoTime();

		// Calculate Fibonacci (BigInteger)
		while (fib > 1) {
			whileCounter++;
			sum = currentNum.add(nextNum);
			currentNum = nextNum;
			nextNum = sum;
			fib--;
		}

		// End Timer and Print
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Time: " + total + " ns");

		// Print Result
		System.out.println("Fibonaccinummer " + fib + nextNum.toString());
		System.out.println("whileCounter = " + whileCounter);

		/*
		// Declare Variables
		long fib = 10000;
		long currentNum = 0;
		long nextNum = 1;
		long sum;

		// Start Timer
		long startTime = System.nanoTime();

		// Calculate Fibonacci (long)
		while (fib > 1) {
			sum = currentNum + nextNum;
			currentNum = nextNum;
			nextNum = sum;
			fib--;
		}

		// End Timer and Print
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Time: " + total + " ns");

		// Print Result
		System.out.println(nextNum);

		 */

	}

}
