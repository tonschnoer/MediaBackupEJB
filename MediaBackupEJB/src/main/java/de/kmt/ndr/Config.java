package de.kmt.ndr;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Config {
	
	public static int listenerport=0;
	public static int serverport = 0;
	public static String server ="";
	public static String directory="";
	public static String file="";
	
	public static String hours="";
	public static String minutes="";
	public static String seconds="";
	

	
	public static void readini() {
		try {
			
			Configurations c = new Configurations();
			INIConfiguration ic = c.ini("MediaBackup.ini");
			SubnodeConfiguration general = ic.getSection("general");
			SubnodeConfiguration files = ic.getSection("files");
			SubnodeConfiguration schedule = ic.getSection("schedule");
			

			listenerport = general.getInt("listenerport");
			serverport = general.getInt("serverport");
			server = general.getString("server");
			
			directory = files.getString("directory");
			file = files.getString("file");
	
			hours = schedule.getString("hours");
			minutes = schedule.getString("minutes");
			seconds = schedule.getString("seconds");
			
			System.out.println("ini file read. schedule=" + hours + " " + minutes + " " + seconds);
			
			
		} catch (ConfigurationException e) {
			
			e.printStackTrace();
		}
		
	}
}
