package it.lettoreSeriale.EXE;

import java.util.ArrayList;

import it.lettoreSeriale.bo.PortReader;
import jssc.SerialPort;

public class MainPort {

	
	public static void main(String[] args) {
		SerialPort serialPort = new SerialPort("COM5");
	      
	  try {    
        System.out.println("Open port");
        
        serialPort.openPort();

         PortReader portReader = new PortReader(serialPort);

		 serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
		   
		   
        serialPort.setParams(SerialPort.BAUDRATE_9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);
      //  String s="S\r\n";
        System.out.println("Send command");
        byte[] array = {0x53,0x0A};
        serialPort.writeBytes(array);
        
  
        
   //     serialPort.closePort();
        
  //      System.out.println("Close port");
	  }
	  catch (Exception e) {
		e.printStackTrace();
	}
	}
	  
      
      
        
        
}
