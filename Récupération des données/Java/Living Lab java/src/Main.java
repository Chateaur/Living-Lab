public class Main
{

    public static void main ( String[] args )
    {
        try
        {
        	System.out.println("Bienvenue dans le programme de recuperation de donnees Arduino. @ESIGELEC -- PING 93");
        	
        	//Load configuration or create it
        	Config conf = new Config();
        	
        	//Connect to db
        	DataBase db = new DataBase(conf);
        	
        	//Get Arduino port
        	String port_arduino = conf.get_property("port_arduino");
        	
        	Serial arduino = new Serial(db, conf, port_arduino);
        	
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}