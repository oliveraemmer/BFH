package sandbox;

import java.lang.Math;
import java.math.BigDecimal;
import java.math.BigInteger;

public class fib_phi {

	public static void main(String[] args) {

		//BigDecimal Phi = new BigDecimal((1 + Math.sqrt(5))/2);
		//System.out.println(Phi);
		//BigDecimal phi = new BigDecimal((1-Math.sqrt(5))/2);

		// Start Timer
		long startTime = System.nanoTime();

		// Calculate Fibonacci with Phi
		double fib = 1474;
		//BigDecimal test = new BigDecimal("4992254605478014635319857953135215353321284010902946699409814219761730335952310426947145539056283541210440601965473058380090413298293580720257549004407513263120328485489050580887743083761849357751270345392837939087473082990665206754582223614777276044440062805924961078441215376667453401411372076087647194316849922546054780146353198579531352153533212840109029466994098142197617303359523104269471455390562835412104406019654730583800904132982935807202575490044075132631203284854890505808877430837618493577512703453928379390874730829906652067545822236147772760444400628059249610784412153766674534014113720760876471943168");
		BigDecimal PhiTest = new BigDecimal((Math.pow(((1 + Math.sqrt(5)) / 2), fib)));
		//BigDecimal res = new BigDecimal(((Math.pow(((1 + Math.sqrt(5)) / 2), fib) - Math.pow(((1 - Math.sqrt(5)) / 2), fib))/Math.sqrt(5)));


		// Stop Timer
		long endTime = System.nanoTime();
		long total = endTime - startTime;
		System.out.println("Time: " + total + " ns");

		// Print Result
		//System.out.println("Fibonacci: " + res);
		System.out.println("Phi: " + PhiTest);
		System.out.println("MAX_Value = " + Double.MAX_VALUE);

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
