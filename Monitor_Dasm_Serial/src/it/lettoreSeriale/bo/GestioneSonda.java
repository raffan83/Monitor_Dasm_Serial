package it.lettoreSeriale.bo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import it.lettoreSeriale.DTO.SondaDTO;



public class GestioneSonda implements SerialPortDataListener{
	
	SerialPort serialPort;
	
	public static String msg =null;
	public static HashMap<String, String> containerSonda = new HashMap<String, String>();

	
	public GestioneSonda(SerialPort serialPort) 
	{
	    this.serialPort = serialPort;
	    
	}
	
	public static ArrayList<SondaDTO> getListaSonde(PortRead portReader) throws SerialException {

		ArrayList<SondaDTO> listaSonde= new ArrayList<>();

		long currentTime=System.currentTimeMillis();
	
		containerSonda = new HashMap<String, String>();
		
		while(true) 
		{

		long actualTime= System.currentTimeMillis()-currentTime;	
		msg=portReader.getMessages();

//		System.out.println(msg);
		
		 

			if(msg.length()>0){
				SondaDTO sonda =null;
				
				String idSonda="";  
				
				if(msg.indexOf("[")>0 && msg.indexOf("]")>0)
					{

						try
						{	  
							if(msg.length()>3)
							{
								idSonda=msg.substring(0,msg.indexOf("=")); 
								msg=msg.substring(msg.indexOf("[")+1,msg.indexOf("]"));  

								String[] param=msg.split(";");

								if(!param[0].substring(param[0].indexOf("=")+1,param[0].length()).startsWith("NC"))
								{


									sonda = new SondaDTO();
									sonda.setId_sonda(idSonda);
									sonda.setMinScale(new BigDecimal(param[0]));
									sonda.setMaxScale(new BigDecimal(param[1]));
									sonda.setUm(param[2]);
									sonda.setPrecision(new BigDecimal(param[3]));
									sonda.setLabel(param[4]);

									if(param.length> 5 && param[5].equalsIgnoreCase("REL"))
									{
										sonda.setZero(true);
									}
									else
									{
										sonda.setZero(false);
									}
									
									if(!containerSonda.containsKey(idSonda))
									{
										listaSonde.add(sonda);
										containerSonda.put(idSonda, idSonda);
									}
								}
							}
						}catch (Exception e) {
							System.err.println("Errore Lettura");
						}
					}  

				}
			if(actualTime>1000) 
			{
				break;
			}
			}
			
		return ordina(listaSonde);
	}
	
	private static ArrayList<SondaDTO> ordina(ArrayList<SondaDTO> listaSonde) {
		
		ArrayList<SondaDTO> listaSondeOrdinata= new ArrayList<SondaDTO>();
		
		try 
		{
		SondaDTO[] listaOrd= new SondaDTO[listaSonde.size()];
		
		for (int i = 0; i < listaSonde.size(); i++) 
		{
			int index=Integer.parseInt((listaSonde.get(i).getId_sonda().substring(2,listaSonde.get(i).getId_sonda().length())));
			listaOrd[index-1]=listaSonde.get(i);
		}
		
		
		
		for (SondaDTO sondaDTO : listaOrd) {
			
			listaSondeOrdinata.add(sondaDTO);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaSondeOrdinata;
	}

	public static Double getValue(SondaDTO sonda,PortRead portReader) throws SerialException {

		Double toReturn=null;
		
		long currentTime=System.currentTimeMillis();
		
		
		while(true) {
		msg =portReader.getMessages();
		long actualTime=System.currentTimeMillis()-currentTime;
		
		if(msg.length()>0){

					try
					{
						if(msg.startsWith(sonda.getId_sonda()+"="))
						{
							toReturn=Double.parseDouble(msg.substring(msg.indexOf("=")+1, msg.indexOf("[")));
						//	System.out.println(toReturn);
							return toReturn;
						}
					}catch (Exception e) 
					{
						System.err.println("RoAD: "+toReturn);
						e.printStackTrace();
						return null;
					}

				}
				if(actualTime>500) 
				{
					break;
				}
			}
			
		return null;
	}


	private static HashMap sortByValues(HashMap map) { 
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		} 
		return sortedHashMap;
	}

	
//	public void serialEvent(SerialPortEvent event) {
//		  if (event.. && event.getEventValue() > 0) {
//		        try {
//		        
//						
//						 String receivedData = serialPort.readString(event.getEventValue());
//						 String[] parts= receivedData.split("\\n");
//				         
//						 for (int i = 0; i < parts.length; i++) {
//							 System.out.println(parts[i].replaceAll("\\r", ""));
//						}
//							
//						
//						// System.out.println(receivedData);
//					
//		           
//		        } catch (SerialPortException ex) {
//		            System.out.println("Error in receiving string from COM-port: " + ex);
//		        }
//		    }
//		
//	}

	@Override
	public int getListeningEvents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		
	    
//	try {	
//		 String receivedData = serialPort.re
//		 String[] parts= receivedData.split("\\n");
//         
//		 for (int i = 0; i < parts.length; i++) {
//			 System.out.println(parts[i].replaceAll("\\r", ""));
//		}
//			
		
		// System.out.println(receivedData);
	
   
//} catch (SerialException ex) {
//    System.out.println("Error in receiving string from COM-port: " + ex);
//}
	}


}
