import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSA {
	
	public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
	public static String public_key_file = "C:/keys/public.key";
	public static int keySize;

	
	public static void generateKey() {
	    try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(keySize);
			final KeyPair key = keyGen.generateKeyPair();
			
			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(public_key_file);
			
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();
			
			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();
			
			
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();
			
			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static boolean areKeysPresent() {

		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(public_key_file);
	
		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}
	
	public static byte[] encrypt(byte[] text, PublicKey key) throws IllegalBlockSizeException,BadPaddingException {
		byte[] cipherText = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
			
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text);
		} catch (IllegalBlockSizeException e2) {
			e2.printStackTrace();
		} catch (BadPaddingException e3) {
			e3.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}
	
	public static String decrypt(byte[] text, PrivateKey key) throws IllegalBlockSizeException,BadPaddingException {
		byte[] dectyptedText = null;
	    try {
	    	// get an RSA cipher object and print the provider
	    	final Cipher cipher = Cipher.getInstance("RSA");

	    	// decrypt the text using the private key
	    	cipher.init(Cipher.DECRYPT_MODE, key);
	    	dectyptedText = cipher.doFinal(text);

	    } catch (IllegalBlockSizeException e2) {
			e2.printStackTrace();
		} catch (BadPaddingException e3) {
			e3.printStackTrace();
		} catch (Exception ex) {
	    	ex.printStackTrace();
	    }

	    return new String(dectyptedText);
	}

}
