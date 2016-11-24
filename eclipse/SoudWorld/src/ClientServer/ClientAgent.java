package ClientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientAgent {
	
	private final int DEFAULT_BUFLEN = 512;
	private final String DEFAULT_PORT = "20015";
	private final String IP_ADDR = "127.0.0.1";
	private final int ERR = -1;
	private boolean connected = false;
	private Socket skt;
	BufferedReader in;
	BufferedWriter out;
	String IPAddr;

	private final boolean debug = true;
	//======================================[ Constructor ]===========================================//
	public ClientAgent(){
		try {
	        skt = new Socket();
	     }
	     catch(Exception e) {
	    	if(debug)System.out.println("Whoops! Socket creation didn't work!\n");
	     }
	}
	
	//=======================================[ Functions ]============================================//
		public void connect(String IPAddr, int port){

			try {
				this.IPAddr = IPAddr;
				skt.connect(new InetSocketAddress(IPAddr, port));
			} catch (IOException e) {
				//e.printStackTrace();
				//System.out.println("Can't connect to server at " + IPAddr);
				connected = false;
				return;
			}
			while(!skt.isConnected()){
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					if(debug)e.printStackTrace();
				}
			}
	        try {
				in = new BufferedReader	(new InputStreamReader	(skt.getInputStream() ));
				out = new BufferedWriter(new OutputStreamWriter	(skt.getOutputStream()));
			} catch (IOException e) {
				if(debug)System.out.println("Whoops! Buffer creation didn't work: "+ e.getMessage()+" \n");
				if(debug)e.printStackTrace();
				connected = false;
				return;
			} 
	        if(debug)System.out.println("Connected at "+skt.getInetAddress().toString()+"\n");
	        connected = true;
		}
		
		public int sendString(String cmd){
			// "#abc);12;abcd;decembre2015$"
			if(!skt.isConnected()){
				if(debug)System.out.println("Socket not connected!");
				connected = false;
				return ERR;
			}
			
			try {
				out.write(cmd);
				out.flush();
			} catch (IOException e) {
				if(debug)System.out.println("Whoops! Sending didn't work: "+ e.getMessage().toString() );
				connected = false;
				return ERR;
			}
			return cmd.length();
		}
		
		public String receive(){
			// "#abc);12;abcd;decembre2015$"
			int i = 0;
			char[] cbuf = new char [DEFAULT_BUFLEN];
						
			if(!skt.isConnected()){
				if(debug)System.out.println("Socket not connected!");
				connected = false;
			}
			try {
				if( in.ready()){
					in.read( cbuf, 0, DEFAULT_BUFLEN);
					//Recherche de la fin de chaine de char
					for(i=0; i<DEFAULT_BUFLEN;i++){
						if(cbuf[i]== 0 )
							break;
					}
					//On renvoi la chaine découpée pile poil
					return String.valueOf(cbuf,0,i);
				}
				return "";
			} catch (IOException e) {
				if(debug)System.out.println("Whoops! Reading didn't work: "+ e.getMessage().toString());
				connected = false;
				return "";
			}
		}
		
		public void reboot(){
			shutdown();
			try {
		        skt = new Socket();
		     }
		     catch(Exception e) {
		    	 if(debug)System.out.println("Whoops! Socket creation didn't work!\n");
		     }
		}
		public boolean getConnected() {
			return connected;
		}
		
		public void setConnected(boolean connected) {
			this.connected = connected;
		}
		
		public void shutdown(){
		    try {
		    	//out.flush();
				out.close();
			    in.close();
			    skt.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(debug)System.out.println("Whoops! Shutdown didn't work!\n");
			}
	 
		}

}
