package sandbox;

import java.math.BigInteger;

public class fibBigInteger {
    public static int whileCounter = 0;
    public static void main(String[] args) {

        // Declare Variables
        // Start Matrix
        BigInteger base[][] = {{new BigInteger("0"),new BigInteger("1")},{new BigInteger("1"),new BigInteger("1")}};
        BigInteger result[][] = {{new BigInteger("0"),new BigInteger("1")},{new BigInteger("1"),new BigInteger("1")}};

        int fib;
        int fibPrint;
        fib = fibPrint = 1000000;

        // Start Timer
        long startTime = System.nanoTime();

        // Calculate Fibonacci
        while(fib > 0) {
            whileCounter++;
            // Check if even
            if((fib & 1) != 0){
                result = matrix_calc(result, base);
            }
            // Divide by 2
            fib >>= 1;
            if(fib >= 0) {
                base = matrix_calc(base, base);
            }
        }

        // End Timer and Print
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time: " + total + " ns");

        // Print result
        System.out.println("Fibonaccinummer " + fibPrint + " = " + result[0][0].toString());
        System.out.println("whileCounter = " + whileCounter);

    }

    public static BigInteger[][] matrix_calc(BigInteger[][] a, BigInteger[][] b) {
        BigInteger res[][] = {{new BigInteger("0"),new BigInteger("0")},{new BigInteger("0"),new BigInteger("0")}};
        for(int i=0;i<2;i++) {
            for(int j=0;j<2;j++) {
                for(int k=0;k<2;k++) {
                    res[i][j] = res[i][j].add(a[i][k].multiply(b[k][j]));
                    whileCounter++;
                }
            }
        }
        return res;
    }
}
