import gnu.io.CommPort;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class Serial
{
	private DataBase db;
	private Config conf;
	private String port_name;
	
    public Serial(DataBase db, Config conf, String port_name) throws Exception
    {
    	this.db = db;
    	this.conf = conf;
    	this.port_name = port_name;
    	
    	System.out.println("Connexion à l'arduino, port : " + port_name);
    	this.connect();
    }
    
    void init_time(OutputStream out) throws IOException
    {
    	System.out.println("Initialisation de la date Arduino");
		long millis = System.currentTimeMillis();
		int offset = TimeZone.getDefault().getOffset(millis);
		
		String time_message = "T" + ((millis + offset)/1000l);
		
		Date date=new Date(millis);
	
        out.write(time_message.getBytes(Charset.forName("UTF-8")));
        
        System.out.println("Date envoyee : " + date.toString());
    }
    
    void init_freq_sensors (OutputStream out, String freq) throws IOException
    {
        out.write(freq.getBytes(Charset.forName("UTF-8")));
        
        System.out.println("Freq envoyee : " + freq);
    }
    
    void connect () throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(this.port_name);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Erreur : port actuellement occupé");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                Thread.sleep(2000);
                (new Thread(new SerialReader(in, db))).start();
                (new Thread(new SerialWriter(out))).start();
                
                init_time(out);
                
                Thread.sleep(2000);
                
                init_freq_sensors(out, this.conf.get_property("freq_sensors"));
                
                Thread.sleep(1000);
            }
        }     
    }
    
    
    public static class SerialReader implements Runnable 
    {
    	private String valueReaded;
    	private String valueReadedTemp;
    	private String lastValueReaded;
    	
    	private DataBase db;
    	
        InputStream in;
        
        public SerialReader (InputStream in, DataBase db)
        {
        	this.db = db;
            this.in = in;
        }
        
        public void run ()
        {
        	System.out.println("Ecoute des messages envoyés sur le port ...");
        	while(1 == 1){
        		String message = "";
                byte[] buffer = new byte[200];
                int len = -1;
                
                try {
                	while ( ( len = this.in.read(buffer)) > -1 )
                    	{
                			message += new String(buffer,0,len);
                			
                			if(message.contains("#")){
                				int pos = message.indexOf('#');
                				
                				String complete_msg = message.substring(2, pos);
                				
                				System.out.println("R : " + complete_msg);
                				
                				if(complete_msg.startsWith("DATA;"))
                				{
                					decompose_message(complete_msg);
                				}
                				
                				message = message.substring(pos + 1, message.length());
                			}
                        }
    				
    			} catch (IOException e) {

    				e.printStackTrace();
    			}
               
        	}
        }
        
        private void decompose_message(String message)
        {
        	try{
            	//Do not treat first split as it is data header
            	String[] parts = message.split(";");
            	
            	org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            	DateTime date_time = dtf.parseDateTime(parts[1]);

            	for(int i=2; i<parts.length; i++)
            	{
            		String[] sensor_parts = parts[i].split(":");
            		String sensor_name = sensor_parts[0];
            		int sensor_value = Integer.parseInt(sensor_parts[1]);

            		Sensor mySensor = new Sensor(date_time, sensor_value, db, sensor_name);
            		mySensor.insert_to_db();
            	}
        	}catch (Exception e){
        		System.out.println("Une erreur est survenue lors de l'extraction du message " + e.getMessage());
        	}

        }
    }

    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
        	while(1 == 1){
	            try
	            {   
	            	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	            	String temp = br.readLine();
	            	
	            	System.out.println("T : " + temp  );
	                this.out.write(temp.getBytes(Charset.forName("UTF-8")));
	            }
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }
        	}
        }
    }

}