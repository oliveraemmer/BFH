import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class RSA {

    private static ArrayList<String> convertToASCII(String m_1) {
        char[] ascii = m_1.toCharArray();
        StringBuffer s = new StringBuffer("");
        for(char c:ascii){
            s.append((int) c);
        }
        ArrayList<String> sArray = new ArrayList<>();
        while(s.length() > 0){
            if(s.length() >= 7){
                sArray.add(s.substring(0, 6));
                s.replace(0, 6, "");
            } else {
                sArray.add(s.toString());
                s.delete(0, s.length());
            }

        }
        return sArray;
    }

    private static int calculatePrivateKeyExp(int p1q1, int e_1) {
        System.out.println("p1q1 = " + p1q1 + " e_1 = " + e_1);
/*        for (BigDecimal x = p1q1.divide(e_1).setScale(0, RoundingMode.HALF_DOWN);
             x.compareTo(p1q1) < 0;
             x = x.add(BigDecimal.ONE)) {
            if ((e_1).multiply(x).remainder(p1q1).equals(BigDecimal.valueOf(1))) {
                return x;
            }
        }*/
        for (int x = (p1q1 / e_1); x < p1q1; x++) {
            if (((e_1 * x) % p1q1) == 1) {
                return x;
            }
        }
        return 1;
    }

    private static String encryptMessage(String message, int e_1, int n_1) {

        BigInteger pow = BigInteger.valueOf(Integer.parseInt(message)).pow(e_1);
        BigInteger mod = pow.remainder(BigInteger.valueOf(n_1));

        return String.valueOf(mod.intValue());
    }

    private static String decryptMessage(String encryptedMessage, int privateKeyExp, int n_1) {

        long a = 1;
        int encryptedInt = Integer.parseInt(encryptedMessage);
        for(int i = 0; i < privateKeyExp; i++){
            a = (a * encryptedInt) % n_1;
        }
        return String.valueOf(a);
    }

    public static void main(String[] args) {
        // init data
        int p_1 = 26263;
        int q_1 = 937;
        int p1q1 = (p_1 - 1) * (q_1 - 1);
        int e_1 = 5;
        int n_1 = p_1 * q_1;
        int p_2 = 30109;
        int q_2 = 787;
        int e_2 = 5;
        int n = 36394973;
        int e_3 = 11;
        String m_1 = "since only he";
        String m_2 = "21309554    15663343     2879838     1883118     9858649     8952838    10592345   12868632     9858649";
        String m_3 = "10812546    36104025     1205107     1205107    28348277    23546779     8249900   36104025    18521934    27777398    24575075    18521934    19139329";

        System.out.println("n_1 = " + n_1);

        ArrayList<String> m_1_ascii = convertToASCII(m_1);
        System.out.println("ASCII message = ");
        for(String s : m_1_ascii){
            System.out.println(s);
        }

        int privKeyExp = calculatePrivateKeyExp(p1q1, e_1);
        System.out.println("Private Key Exponent = " + privKeyExp);

        System.out.println("Encrypted Message = ");
        ArrayList<String> encryptedMessages = new ArrayList<>();
        for(String s : m_1_ascii){
            encryptedMessages.add(encryptMessage(s, e_1, n_1));
            System.out.println(encryptedMessages.get(encryptedMessages.size() - 1));
        }

        System.out.println("Decrypted Message = ");
        ArrayList<String> decryptedMessages = new ArrayList<>();
        for(String s : encryptedMessages){
            decryptedMessages.add(decryptMessage(s, privKeyExp, n_1));
            System.out.println(decryptedMessages.get(decryptedMessages.size() - 1));
        }

    }
}
