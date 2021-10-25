package sandbox;

import java.math.BigDecimal;
import java.math.BigInteger;

public class fibonacciExercise {

    public static BigInteger powerSlow(BigInteger base, int power) {
        BigInteger res = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");

        // Start Timer
        long startTime = System.nanoTime();

        for (int i = power; i != 0; i--){
            res = res.multiply(base);
        }
        // End Timer and Print
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time for powerSlow(): " + total + " ns");

        return res;
    }

    public static BigInteger powerFast(BigInteger base, int power) {
        BigInteger res = new BigInteger("1");
        BigInteger zero = new BigInteger("0");
        BigInteger two = new BigInteger("2");
        // Start Timer
        long startTime = System.nanoTime();

        while(true) {
            if(power % 2 != 0){
                res = res.multiply(base);
            }
            power >>= 1;
            if(power <= 0) {
                break;
            }
            base = base.multiply(base);
        }

        // End Timer and Print
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time for powerFast(): " + total + " ns");

        return res;
    }

    public static BigInteger fibonacciSlow(int fib){
        // Declare Variables
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
        System.out.println("Time for fibonacciSlow(): " + total + " ns");
        System.out.println("whileCounter = " + whileCounter);
        return nextNum;
    }

    public static double fibonacciPhiDouble(int fib) {

        // Start Timer
        long startTime = System.nanoTime();

        // Calculate Fibonacci with Phi
        double Phi = Math.pow(((1 + Math.sqrt(5)) / 2), fib);
        double phi = Math.pow(((1 - Math.sqrt(5)) / 2), fib);
        double res = (Phi - phi)/Math.sqrt(5);

        // Stop Timer
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time for fibonacciPhiDouble(): " + total + " ns");
        System.out.println("Math.sqrt(5): " + Math.sqrt(5));
        System.out.println("Phi: " + Phi);
        System.out.println("phi: " + phi);

        // Print Result
        //String resultat = String.format("%.1f", res);
        //System.out.println("Fibonacci: " + resultat);

        return res;
    }

    public static BigDecimal fibonacciPhiBigDecimal(int fib) {

        // Start Timer
        long startTime = System.nanoTime();

        //BigDecimal PhiTest = new BigDecimal((Math.pow(((1 + Math.sqrt(5)) / 2), fib)));
        BigDecimal res = new BigDecimal(((Math.pow(((1 + Math.sqrt(5)) / 2), fib) - Math.pow(((1 - Math.sqrt(5)) / 2), fib))/Math.sqrt(5)));


        // Stop Timer
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time for fibonacciPhiBigDecimal(): " + total + " ns");

        // Print Result
        //System.out.println("Fibonacci: " + res);
        //System.out.println("Phi: " + PhiTest);
        //System.out.println("Double.MAX_VALUE = " + Double.MAX_VALUE);

        return res;
    }

    public static String fibonacciBigInteger (int fib) {

        // Start Matrix
        BigInteger base[][] = {{new BigInteger("0"),new BigInteger("1")},{new BigInteger("1"),new BigInteger("1")}};
        BigInteger result[][] = {{new BigInteger("0"),new BigInteger("1")},{new BigInteger("1"),new BigInteger("1")}};

        // Start Timer
        long startTime = System.nanoTime();

        // Calculate Fibonacci
        while(fib > 0) {
            //whileCounter++;
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
        System.out.println("Time for fibonacciBigInteger(): " + total + " ns");

        return result[0][0].toString();
    }

    public static BigInteger[][] matrix_calc(BigInteger[][] a, BigInteger[][] b) {
        BigInteger res[][] = {{new BigInteger("0"),new BigInteger("0")},{new BigInteger("0"),new BigInteger("0")}};
        for(int i=0;i<2;i++) {
            for(int j=0;j<2;j++) {
                for(int k=0;k<2;k++) {
                    res[i][j] = res[i][j].add(a[i][k].multiply(b[k][j]));
                    //whileCounter++;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BigInteger base = new BigInteger("3");
        int power = 1000;
        int fib = 1000000;

        //System.out.println(base.toString() + "^" + power + " = " + powerSlow(base, power) + "\n");
        //System.out.println(base.toString() + "^" + power + " = " + powerFast(base, power) + "\n");
        //System.out.println("Fibonacci Number " + fib + " = " + fibonacciSlow(fib) + "\n");
        //String resultat = String.format("%.1f", fibonacciPhiDouble(fib));
        //System.out.println("Fibonacci Number " + fib + " = " + resultat + "\n");
        //System.out.println("Fibonacci Number " + fib + " = " + fibonacciPhiBigDecimal(fib) + "\n");
        System.out.println("Fibonacci Number " + fib + " = " + fibonacciBigInteger(fib) + "\n");

    }

}
