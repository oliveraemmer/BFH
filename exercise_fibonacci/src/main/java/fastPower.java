
public class fastPower {
    static int res = 1;
    public static void main(String[] args) {

        // Start Timer
        long startTime = System.nanoTime();

        // Calculate
        power_calc(4, 13);

        // End Timer and Print
        long endTime = System.nanoTime();
        long total = endTime - startTime;
        System.out.println("Time: " + total + " ns");

        System.out.println(res);
    }

    public static void power_calc(long base, long power) {
        while(true) {
            if(power % 2 != 0){
                res *= base;
            }
            power >>= 1;
            if(power <= 0) {
                break;
            }
            base *= base;
        }
    }

}
