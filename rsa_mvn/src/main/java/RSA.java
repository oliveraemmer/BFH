import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class RSA {

    private static ArrayList<Integer> convertToASCII(String m_1) {
        char[] ascii = m_1.toCharArray();
        ArrayList<Integer> sArray = new ArrayList<>();
        for(char c:ascii){
            sArray.add((int) c);
        }
        return sArray;
    }

    private static int calculatePrivateKeyExp(int p1q1, int e_1) {
        // Start for loop at (p1q1 / e_1) because anything smaller won't be the result anyway since mod p1q1 is applied
        for (int x = (p1q1 / e_1); x < p1q1; x++) {
            if (((e_1 * x) % p1q1) == 1) {
                return x;
            }
        }
        return 1;
    }

    private static long encryptMessage(int message, int e_1, int n_1) {

        // Exponentiate the message e_1 times
        // With this approach a never grows bigger than n_1
        long a = 1;
        for(int i = 0; i < e_1; i++){
            a = (int) ((a * message) % n_1);
        }
        return a;
    }

    private static int decryptMessage(long encryptedMessage, int privateKeyExp, int n_1) {

        // Exponentiate the encryptedMessage privateKeyExp times
        // With this approach a never grows bigger than n_1
        int a = 1;
        for(int i = 0; i < privateKeyExp; i++){
            a = (int) ((a * encryptedMessage) % n_1);
        }
        return a;
    }

    private static int[] bruteForceRSA(int n_3) {

        int[] returnValues = {0,0};

        int a = (int) Math.sqrt((double) n_3);
        boolean found = false;
        while(!found) {
            for (int i = 1; i < a; i++) {
                if (n_3 == ((a + i) * (a - i))) {
                    returnValues[0] = (a + i);
                    returnValues[1] = (a - i);
                    return returnValues;
                }
            }
            a++;
        }
        return returnValues;
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
        int p2q2 = (p_2 - 1) * (q_2 - 1);
        int e_2 = 5;
        int n_2 = p_2 * q_2;

        int n_3 = 36394973;
        // e_3 = 11 not working. Works with e_3 = 5;
        int e_3 = 5;
        String m_1 = "since only he";
        String m_2 = "21309554 15663343 2879838 1883118 9858649 8952838 10592345 12868632 9858649";
        String m_3 = "10812546 36104025 1205107 1205107 28348277 23546779 8249900 36104025 18521934 27777398 24575075 18521934 19139329";

        ////////////// Exercise 5 a.) /////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("\n\nExercise 5 a.)");

        // Take every single char of String m_1 and convert it to an ascii int
        // Add each int to the m_1_ascii ArrayList<>
        ArrayList<Integer> m_1_ascii = convertToASCII(m_1);
        System.out.println("ASCII message = " + m_1_ascii.toString());


        // Calculate the Private Key Exponent
        // Private Key Exponent(x) = (x * e_1) mod p1q1 == 1
        int privKeyExp = calculatePrivateKeyExp(p1q1, e_1);
        System.out.println("Private Key Exponent = " + privKeyExp);

        // Encrypted message = (m_1_ascii ^ e_1) mod n_1
        ArrayList<Long> encryptedMessages = new ArrayList<>();
        for(int s : m_1_ascii){
            encryptedMessages.add(encryptMessage(s, e_1, n_1));
        }
        System.out.println("Encrypted Message = " + encryptedMessages.toString());

        // Decrypted message = (encryptedMessage ^ privateKeyExp) mod n_1
        // Take each entry in encrypted Messages and call decryptMessage()
        ArrayList<Character> decryptedMessages = new ArrayList<>();
        for(long s : encryptedMessages){
            char c = (char) decryptMessage(s, privKeyExp, n_1);
            decryptedMessages.add(c);
        }
        System.out.println("Decrypted Message = " + decryptedMessages.toString());


        ////////////// Exercise 5 c.) /////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("\n\nExercise 5 c.)");

        // Calculate the Private Key Exponent
        // Private Key Exponent(x) = (x * e_2) mod p2q2 == 1
        int privKeyExp2 = calculatePrivateKeyExp(p2q2, e_2);
        System.out.println("Private Key Exponent 2 = " + privKeyExp2);

        // Decrypted message = (m_2_splitted(x) ^ privateKeyExp) mod n_2
        // Take each entry of m_2_splitted, parse to Long and call decryptMessage
        String[] m_2_splitted = m_2.split(" ");
        ArrayList<Character> decryptedMessages2 = new ArrayList<>();
        for(String s : m_2_splitted){
            long i = Long.parseLong(s);
            char c = (char) decryptMessage(i, privKeyExp2, n_2);
            decryptedMessages2.add(c);
        }
        System.out.println("Decrypted Message 2 = " + decryptedMessages2.toString());


        ////////////// Exercise 5 d.) /////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("\n\nExercise 5 d.)");

        String[] m_3_splitted = m_3.split(" ");

        // Find p and q by bruteforce
        int[] pq = bruteForceRSA(n_3);
        int p_3 = pq[0];
        int q_3 = pq[1];
        int p3q3 = 0;
        if((p_3 * q_3) == n_3){
            System.out.println("Found primes: p = " + p_3 + ", q = " + q_3);
            p3q3 = (p_3 - 1) * (q_3 - 1);
        }

        int privKeyExp3 = calculatePrivateKeyExp(p3q3, e_3);
        System.out.println("Private Key Exponent 3 = " + privKeyExp3);

        ArrayList<Character> decryptedMessages3 = new ArrayList<>();
        for(String s : m_3_splitted){
            long i = Long.parseLong(s);
            char c = (char) decryptMessage(i, privKeyExp3, n_3);
            decryptedMessages3.add(c);
        }
        System.out.println("Decrypted Message 3 = " + decryptedMessages3.toString());

    }
}
