package it.lettoreSeriale.bo;

import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;



public class Serial {

static SerialPort serialPort=null;

public static SerialPort getConnection (int indice) {
   
        try {
        	serialPort = SerialPort.getCommPorts()[indice]; 
            serialPort.openPort();

        } catch (Exception e) {
            System.out.println("There are an error on writing string to port: " + e);
        }
		return serialPort;
    }

 }



