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
		
		String time_message = "T" + ((millis+offset)/1000l);
		
		Date date=new Date(millis);
		System.out.println("Date envoyee : " + date.toString());
		
        out.write(time_message.getBytes(Charset.forName("UTF-8")));
        
        System.out.println("Initialisation terminee");
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
                
                Thread.sleep(5000);
                (new Thread(new SerialReader(in))).start();
                initTime(out);
                
               
               (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /** */
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
                byte[] buffer = new byte[200];
                int len = -1;
                
                try {
                	valueReadedTemp = " ";
                	valueReaded = " ";
                	lastValueReaded = " ";
                	
                    while(valueReadedTemp.charAt(valueReadedTemp.length() - 1) != '#'){

                        while (this.in.available() > 0) {
                            int numBytes = this.in.read(buffer);
                        }
                        valueReadedTemp = new String(buffer).trim();
                        
                        if(valueReadedTemp.length() == 0){
                        	valueReadedTemp=" ";
                        }
                        if(!lastValueReaded.equals(valueReadedTemp) && !valueReadedTemp.equals("#")){
                        	valueReaded += valueReadedTemp;
                        	lastValueReaded = valueReadedTemp;
                        }

                    }
                    
                    System.out.println("R : " + valueReaded);
                    /*
                	int numBytes = 0;
    				while (this.in.available() > 0) {
    					numBytes = this.in.read(buffer);
    				}
    				
    				if(numBytes > 0){
    					System.out.println("R : " + new String(buffer).trim());
    				}
    				*/
    				
    			} catch (IOException e) {

    				e.printStackTrace();
    			}
               
        	}

        }
    }

    /** */
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
	            	String input = br.readLine();
	            	
	            	System.out.println("T : " + input  );
	                this.out.write(input.getBytes(Charset.forName("UTF-8")));            
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
        	System.out.println("LAUNCHING");
        	readAndWrite temp = new readAndWrite();
            temp.connect("COM3");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}