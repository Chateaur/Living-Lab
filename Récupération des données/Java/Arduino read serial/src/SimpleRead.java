import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.filechooser.FileSystemView;

import gnu.io.*;

public class SimpleRead implements Runnable, SerialPortEventListener {
	private String folderPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\ArduinoValues\\";
	private static ArrayList<String> arrayValues; 
	
	private String myDateFormated;
	private Date maintenant;
	private String valueReaded;
	
    static CommPortIdentifier portId;
    static Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    public static void main(String[] args) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	Boolean portFound = false;
    	
    	System.out.println("Bienvenue dans le programme de recuperation de donnees Arduino. @ESIGELEC -- PING 93");
    	System.out.print("Entrez le port de communication (ex. COM3) : ");
    	String input = br.readLine();
    	
    	arrayValues = new ArrayList <String> ();
    	
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals(input)) {
                	System.out.println("Lecture des données sur le port : " + input);
                	portFound = true;
                    SimpleRead reader = new SimpleRead();
                }
            }
        }
        
        if(!portFound){
        	System.out.println("Impossible de lire sur le port : " + input);
        	System.out.println("Appuyer sur une touche pour terminer le programme");
        	String quit = br.readLine();
        }
    }

    public SimpleRead() {
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {System.out.println(e);}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
            byte[] readBuffer = new byte[20];

            try {
                while (inputStream.available() > 0) {
                    int numBytes = inputStream.read(readBuffer);
                }
                
                maintenant = new Date();
                DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
                myDateFormated = shortDateFormat.format(maintenant);
                
                //Ajout des données dans un arrayList
                valueReaded = new String(readBuffer).trim();
                if(valueReaded != null && !valueReaded.isEmpty()){
                	System.out.println("Donnees recues : " + valueReaded);
                	arrayValues.add(myDateFormated + ";" + valueReaded + "\n");
                }
                
                //Ecriture du tableau dans un fichier texte
                if(arrayValues.size() > 10) {
                	writeToFile(arrayValues, maintenant);
                	arrayValues.clear();
                }
            } catch (IOException e) {System.out.println(e);}
            break;
        }
    }
    
    public void writeToFile(ArrayList<String> arrayTemp, Date myDate) throws IOException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd") ;
    	String filePath = folderPath +  dateFormat.format(myDate) + ".csv";
    	String finalToWrite = "";
    	
    	for (String valueToWrite : arrayTemp) {
    		finalToWrite += valueToWrite;
		}
    	
    	if(!Files.exists(Paths.get(filePath))){
    		System.out.println("Creation du fichier : " + filePath);
    		File dir = new File(folderPath);
    		
    		File f = new File(filePath);
    		
    		if(dir.mkdir()){
    			System.out.println("Dossier cree " + folderPath);
    		}
    		
    		f.createNewFile();
    		System.out.println("Fichier cree");
    	}
    			
    	Files.write(Paths.get(filePath), finalToWrite.getBytes(), StandardOpenOption.APPEND);
    	System.out.println("Valeurs enregistrées dans le fichier : " + filePath);
    }
}