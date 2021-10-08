package sandbox;

public class fib_slow {

	public static void main(String[] args) {
		
		long startTime = System.nanoTime();
		
		long fib = 92;
		long currentNum = 0;
		long nextNum = 1;
		while (fib > -1) {
			System.out.println(currentNum);
			long sum = currentNum + nextNum;
			currentNum = nextNum;
			nextNum = sum;
			fib--;
		}
		
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		
		System.out.println("Time: " + total + " ns");
		
	}

}
