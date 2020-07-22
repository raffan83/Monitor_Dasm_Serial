package it.lettoreSeriale.EXE;

import it.lettoreSeriale.GUI.DASM_GUI;

import java.awt.EventQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainMonitorDASM {
	
	
	
	public static void main(String[] args) {
		
	
		
		EventQueue.invokeLater(new Runnable(){
	        public void run() 
	        {
	        	try
	        	{
	        	DASM_GUI g1 = new DASM_GUI();
	        	
	        	g1.setDefaultCloseOperation(3);
	  	        g1.setVisible(true);
	  	        
	  	        }
	        	catch(Exception ex)
	  	        {
				
	  	        	ex.printStackTrace();
	  	        }
	        }
	
	

    });

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
}
