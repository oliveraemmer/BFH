package sandbox;

public class power {

	public static void main(String[] args) {
		// Start Timer
		long startTime = System.nanoTime();

		// Calculate
		long res = power_calc(4, 10);

		// End Timer and Print
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Time: " + total + " ns");

		System.out.println(res);
	}
	
	public static long power_calc(long base, long power) {
		if(power == 0) {
			return 1;
		}
		if (power < 2) {
			return base;
		}
		if(power % 2 == 0) {
			return power_calc(base, power/2) * power_calc(base, power/2);
		} else {
			return base * power_calc(base, power-1);
		}
	}
}