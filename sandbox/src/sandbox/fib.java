package sandbox;

public class fib {
	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
		long base[][] = {{0,1},{1,1}};
		
		// Max. 92 (long = 19 Digits)
		int fib = 92; 
		
		System.out.println("Fibonaccinummer " + fib + " = " + fib_calc(base, fib)[0][1]);

		long endTime = System.nanoTime();
		long total = endTime - startTime;
		
		System.out.println("Time: " + total + " ns");
		
	}
	
	public static long[][] fib_calc(long[][] base, int power) {
		if (power < 2) {
			return base;
		}
		if(power % 2 == 0) {
			return matrix_calc(fib_calc(base, power/2), fib_calc(base, power/2));
		} else {
			return matrix_calc(base, fib_calc(base, power-1));
		}
	}
	
	public static long[][] matrix_calc(long a[][], long b[][]) {
		long res[][] = new long[2][2];
		for(int i=0;i<2;i++) {
			for(int j=0;j<2;j++) {
				for(int k=0;k<2;k++) {
					res[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return res;
	}
}
