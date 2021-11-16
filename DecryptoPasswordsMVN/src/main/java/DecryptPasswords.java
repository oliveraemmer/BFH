import akka.actor.typed.ActorSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.HashMap;


public class DecryptPasswords{

	static String hash(String algorithm,String textToHash) throws NoSuchAlgorithmException  {
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
		byte[] hashedByetArray = digest.digest(byteOfTextToHash);
		String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
		return encoded;
	}

	// Needed args: "C:\Users\bfh\git\BFH\DecryptoPasswordsMVN\resources\out.txt" "C:\Users\bfh\git\BFH\DecryptoPasswordsMVN\resources\xato-net-10-million-passwords.txt"
    public static void main(String[] args) {

		// first run: hack with Akka
		long start = System.currentTimeMillis();
		final ActorSystem<SystemRoot.Init> system = ActorSystem.create(SystemRoot.create(), "system");

		if(args.length!= 2){
			System.err.println("Program requires two file names as argument (hashed passwords and possible passwords)");
			return;
		}
		system.tell(new SystemRoot.CreateHackers(args[0], args[1], start));
	}

}
