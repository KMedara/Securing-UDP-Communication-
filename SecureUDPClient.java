
/*Kevin Medara
 * 3/19/17
 * SecureUDPClient.java
 * This program :
 *               1. Accepts a users 8-byte input
 *               2. Encrypts the string using byte arrays
 *               3.sends string to server 
 *               4. receives another encrypted string from server
 *               5. decrypts new message and displays plaintext
 */
import java.io.*;
import java.net.*;

class SecureUDPClient {
	public static void main(String args[]) throws Exception {

		String key = "iamakey!";//initial key

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		DatagramSocket clientSocket = new DatagramSocket(); // create a client
															// socket

		InetAddress IPAddress = InetAddress.getByName("192.168.0.104");

		byte[] sendData = new byte[16];
		byte[] receiveData = new byte[16];

		System.out.println("****Welcome to Secure UDP Client Program****");
		System.out.println("Please enter a 8-byte message that will be sent to the Secure UDP Server");

		String sentence = inFromUser.readLine();
		if (sentence.length() != 8) {
			do {
				System.out.println("Please enter a message including 8 characters");
				sentence = inFromUser.readLine();
			} while (sentence.length() != 8);
		}
		// End of Entry Data Verification

		// Send Data to Server
		sendData = sentence.getBytes();

		// please do the RC4 encryption here
		myRC4 rc4 = new myRC4(key);//instance of rc4 class
		byte[] CipherBytes = rc4.encrypt(sendData);// encrypted bytes

		String HexSend = rc4.bytesToHexString(CipherBytes);// hex value of

		sendData = HexSend.getBytes();

		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
		clientSocket.send(sendPacket);

		// Obtain Data from Server

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);

		String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

		// please do the DES decryption here
		myRC4 rc42 = new myRC4(key);//new rc4 instance for fresh s box
		CipherBytes = rc4.hexToByteArray(modifiedSentence);
		byte[] PlainBytes = rc4.encrypt(CipherBytes);//decrypt CipherBytes 
		String s = new String(PlainBytes); //convert to ascii 
		System.out.println("Message returned from server:" + s);//display
		clientSocket.close();
	}
}
