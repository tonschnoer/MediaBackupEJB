package de.kmt.ndr;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class TCPClient.
 */
public class TCPClient {
	
	/** The logger. */
	protected final Logger logger = LogManager.getLogger();
	
	
	public void run() {
		byte[] buffer = new byte[1048576];
		long bytesread = 0;

		try {
			
			logger.info("Startup TCPClient");
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(Config.directory + Config.file));
			Socket clientSocket = new Socket(Config.server,Config.serverport);
			OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
			InputStream is = clientSocket.getInputStream();
			
			
			while (true) {
					
				// request file from server
				System.out.print("Request " + Config.file);
				out.write("GET |"+Config.file);
				out.flush();

				while (true) {
					
					// read up to 1048576 bytes from stream
					int readbytes = is.read(buffer,0,1048576);
					bytesread = bytesread + readbytes;
					
					System.out.println("Read " + String.valueOf(bytesread) + " bytes");
					

					// check if 1048576 bytes where read (i.e. there are some more)
					if (readbytes<1048576) {
						
						// less than 1048576 bytes are read. Check if last 3 bytes are "END". If so: stop receiving data
						if ((buffer[readbytes-3]==0x45) & (buffer[readbytes-2]==0x4e) & (buffer[readbytes-1]==0x44)) {
							System.out.println("Schluss");
							
							// if there is some interesting data, before END, write it to file
							if (readbytes>3) {
								dos.write(buffer,0,readbytes-3);
							}
							// leave while(true) loop
							break;
						} else {
							// if bytesread < 1048576, but no ending Tag "END" -> write buffer to file
							dos.write(buffer,0,readbytes);
						}
					} else {
						// if bytesread = 1048576, assume there is more and write buffer to file
						dos.write(buffer,0,readbytes);
					}
				}
				
				// close output file
				dos.close();

				// Send a response back to the client
				out.write(".");

				// Close the socket and streams for this client
				clientSocket.close();
				
				// exit while(true)
				break;
			}

		} catch (Exception e) {
			System.out.println("An exception has occurred:");
			e.printStackTrace();
		}
	
	}
}
