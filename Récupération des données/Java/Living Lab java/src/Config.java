import java.io.FileOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class Config {
	
	private SortedProperties prop_loaded;
	
	public Config ()
	{
		File f = new File("config.properties");
		
		if(f.exists())
		{
			System.out.println("Fichier de configuration existant, chargement ...");
			this.load();
		}else {
			System.out.println("Fichier de configuration inexistant, création ...");
			this.create();
			this.load();
		}
		
		
	}
	
	public void create() {
		SortedProperties prop = new SortedProperties();
		OutputStream output = null;
	
		try {
	
			output = new FileOutputStream("config.properties");
	
			// set the properties value
			prop.setProperty("db_url", "//localhost:3306/living_lab?useSSL=false");
			prop.setProperty("db_user", "root");
			prop.setProperty("db_password", "");
			
			prop.setProperty("port_arduino", "COM3");
			prop.setProperty("freq_sensors", "1000");
			
			// save properties to project root folder
			prop.store(output, "Fichier de configuration de l'application living lab. Supprimer ce fichier pour réinitialiser les paramètres.");
			
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
		}
	}
  
	public void load() {
		InputStream input = null;
		
		try {
		
			input = new FileInputStream("config.properties");
			
			prop_loaded = new SortedProperties();
			
			// load a properties file
			prop_loaded.load(input);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
  }
	
	public String get_property(String prop)
	{
		return prop_loaded.getProperty(prop);
	}
}