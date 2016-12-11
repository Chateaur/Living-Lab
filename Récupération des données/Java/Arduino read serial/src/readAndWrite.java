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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class readAndWrite
{
    public readAndWrite()
    {
        super();
    }
    
    void initTime(OutputStream out) throws IOException
    {
    	System.out.println("Initialisation de la date Arduino");
		long millis = System.currentTimeMillis();
		int offset = TimeZone.getDefault().getOffset(millis);
		
		String time_message = "T" + ((millis + offset)/1000l);
		
		Date date=new Date(millis);
		System.out.println("Date envoyee : " + date.toString());
		
        out.write(time_message.getBytes(Charset.forName("UTF-8")));
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
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
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
                
                initTime(out);
                
                Thread.sleep(1000);
               

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    
    public static class SerialReader implements Runnable 
    {
    	private String valueReaded;
    	private String valueReadedTemp;
    	private String lastValueReaded;
    	
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
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
                				
                				System.out.println("R : " + message.substring(0, pos));
                				message = message.substring(pos + 1, message.length());
                			}
                        }
    				
    			} catch (IOException e) {

    				e.printStackTrace();
    			}
               
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
    
    public static void main ( String[] args )
    {
        try
        {
        	System.out.println("Bienvenue dans le programme de recuperation de donnees Arduino. @ESIGELEC -- PING 93");
        	System.out.print("Entrez le port de communication (ex. COM3) : ");
        	
        	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        	String input = br.readLine();
        	
        	readAndWrite arduino = new readAndWrite();
        	arduino.connect(input);
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}