
/*Kevin Medara
 * 3/19/17
 * SecureUDPServer.java
 * This program :
 *               1. receives an encrypted string from the client
 *               2. Decrypts said string
 *               3. capitalizes the string
 *               4. encrypts string once again
 *               5. sends new encryption to client
 */
import java.net.*;
import java.util.*;

class SecureUDPServer {
	public static void main(String args[]) throws Exception {

		String key = "iamakey!"; // Initial key

		InetAddress srvIP = InetAddress.getByName("192.168.0.104");

		DatagramSocket serverSocket = new DatagramSocket(5000, srvIP);

		byte[] receiveData = new byte[16];
		byte[] sendData = new byte[16];

		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);

			String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();

			// please do the RC4 decryption here
			myRC4 rc4 = new myRC4(key);//rc4 class instance

			System.out.println("Received: " + sentence);//string received from packet
			byte[] CipherBytes = rc4.hexToByteArray(sentence);//Ciphertext bytes

			byte[] PlainBytes = rc4.encrypt(CipherBytes);//DECRYPT CIPHER
			String s = new String(PlainBytes);// decrypted plaintext

			String capitalizedSentence = s.toUpperCase();// capitalize the
															// plaintext

			sendData = capitalizedSentence.getBytes();

			// please do the DES encryption here
			myRC4 rc42 = new myRC4(key);
			CipherBytes = rc42.encrypt(sendData);
			String HexSend = rc4.bytesToHexString(CipherBytes);
			sendData = HexSend.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
