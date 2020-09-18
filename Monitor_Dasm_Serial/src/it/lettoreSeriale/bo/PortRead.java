package it.lettoreSeriale.bo;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class PortRead {
	
	String msg;
	public PortRead(SerialPort comPort) {
		
		
		comPort.addDataListener(new SerialPortDataListener() {
		   String playload="";
		   @Override
		   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
		   @Override
		   
		   public void serialEvent(SerialPortEvent event)
		   {
			  
		      byte[] by = event.getReceivedData();

		      for (int i = 0; i < by.length; ++i)
		      { 
		    	
		    			try {

		    				if(by[i]!=10 && by[i]!=13)
		    				{                         

		    					playload+=(char)by[i];
		    				}
		    				else
		    				{                       
		    					if(by[i]==13)
		    					{
		    						msg=playload;
		    				//		System.out.println("Mess: "+msg);
		    						playload="";
		    					}     
		    				}

		    			}catch (Exception ex) {
		    				ex.printStackTrace();
		    			}                           
		    		}
		      }
		   
		});
	
	}

	public  String getMessages() {
		synchronized (this){
			return msg;
		}
	}
	}