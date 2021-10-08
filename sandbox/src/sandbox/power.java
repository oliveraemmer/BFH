package sandbox;

public class power {

	public static void main(String[] args) {
		System.out.println(power_calc(4, 2));
	}
	
	public static int power_calc(int base, int power) {
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