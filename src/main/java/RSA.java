import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class RSA {

    private static BigInteger convertToASCII(String m_1) {
        char[] ascii = m_1.toCharArray();
        String s = "";
        for(char c:ascii){
            s += (int) c;
            System.out.println(s+"  ");
        }
        BigInteger sInt = new BigInteger(String.valueOf(s));
        return sInt;
    }

    private static BigDecimal calculatePrivateKey(BigDecimal a, BigDecimal m) {
        // A naive method to find modulor
        // multiplicative inverse of 'a'
        // under modulo 'm'

        for (BigDecimal x = BigDecimal.valueOf(1);
             x.compareTo(m) < 0;
             x = x.add(BigDecimal.ONE)) {
            System.out.println(x);
            BigDecimal temp = new BigDecimal(String.valueOf((a.remainder(m)).multiply(x.remainder(m))));
            if ((temp).remainder(m).equals(BigDecimal.valueOf(1)))
                return x;
        }
        return BigDecimal.ONE;
    }

    public static void main(String[] args) {
        // init data
        int p_1 = 26263;
        int q_1 = 937;
        BigDecimal p1q1 = new BigDecimal((p_1 - 1) * (q_1 - 1));
        BigDecimal e_1 = new BigDecimal(5);
        int n_1 = p_1 * q_1;
        int p_2 = 30109;
        int q_2 = 787;
        int e_2 = 5;
        int n = 36394973;
        int e_3 = 11;
        String m_1 = "since only he";
        String m_2 = "21309554    15663343     2879838     1883118     9858649     8952838    10592345   12868632     9858649";
        String m_3 = "10812546    36104025     1205107     1205107    28348277    23546779     8249900   36104025    18521934    27777398    24575075    18521934    19139329";

        BigInteger m_1_ascii = convertToASCII(m_1);
        System.out.println(m_1_ascii);

        BigDecimal pubKey = new BigDecimal(Math.pow(n_1, 5));
        System.out.println("Public Key = " + pubKey);

        BigDecimal privKey = calculatePrivateKey(p1q1, e_1);
        System.out.println("Private Key = " + privKey);

    }
}
