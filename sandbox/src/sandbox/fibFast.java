package sandbox;

import java.math.BigInteger;

public class fibFast {

    public static void main(String[] args) {

        // Declare Variables
        // Start Matrix
        long result[][] = {{0,1},{1,1}};
        long fixBase[][] = {{0,1},{1,1}};

        // Max. 92 (long = 19 Digits)
        int fib;
        int fibPrint;
        fib = fibPrint = 91;

        // Start Timer
        long startTime = System.nanoTime();

        // Calculate Fibonacci
        while(fib > 1) {
            if((fib & 1) == 1){
                result = matrix_calc(result, fixBase);
            }
            fib >>= 1;
            result = matrix_calc(result, result);
        }

        // End Timer and Print
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time: " + total + " ns");

        // Print result
        System.out.println("Fibonaccinummer " + fibPrint + " = " + result[0][1]);

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
