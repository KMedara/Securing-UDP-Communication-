/*Kevin Medara
 * 3/19/17
 * myRC4.java
 * This class contains all functions needed for RC4 Encryption/Decryption
 * The constructor creates a new S box every time the class is called
 */
import javax.xml.bind.DatatypeConverter;
public class myRC4 {
	static int[] S = new int[256];
	int[] T = new int[256];

	public myRC4(String key) throws Exception {
		int j = 0, temp = 0;

		int[] K = new int[key.length()];
		for (int a = 0; a < key.length(); a++) {
			K[a] = key.charAt(a);
		}
		int keyLength = key.length();
		for (int a = 0; a < 256; a++) {
			S[a] = a;
			T[a] = Integer.parseInt(Integer.toHexString((char) K[a % (keyLength)]), 16);
		}
		for (int a = 0; a < 256; a++) {
			j = (j + S[a] + T[a]) % 256;
			temp = S[a];
			S[a] = S[j];
			S[j] = temp;
		}
	}// constructor

	public static byte[] encrypt(byte[] pt) {
		byte[] cipher = new byte[pt.length];// cipher text array

		int i = 0, k = 0, j = 0, t = 0;
		byte tmp; // temp placeholder
		for (int count = 0; i < pt.length; count++) {
			i = (i + 1) % 256;
			j = (j + S[i]) % 256;
			// perform swap
			tmp = (byte) S[j];
			S[j] = S[i];
			S[i] = tmp;
			t = (S[i] + S[j] + 256) % 256;
			k = S[t];
			cipher[count] = (byte) (pt[count] ^ k);// XOR

		}
		return cipher;
	}

	/***********
	 * HEX TO BYTE ARRAY
	 ***********/
	public static byte[] hexToByteArray(String hex) {

		return DatatypeConverter.parseHexBinary(hex);

	}

	/********
	 * BYTE ARRAY TO HEX STRING
	 *********/
	public static String bytesToHexString(byte[] in) {
		final StringBuilder builder = new StringBuilder();
		for (byte b : in) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

}
