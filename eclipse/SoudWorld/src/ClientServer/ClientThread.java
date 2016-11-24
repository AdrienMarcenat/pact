package ClientServer;
//import java.lang.reflect.*;



public class ClientThread extends Thread {
	
	ClientAgent clientAgent;
	boolean somethingToRead = false;
	boolean somethingToSend = false;
	public final long DEFAULT_PERIODE 	= 1L;
	public final int ERR 				= -1;

	public final int PARAM_LEN 		= 100;
	private long period;
	String recvbuf;
	String sendbuf;
	int rcvCount = 0;
	int sndCount = 0;
	
	private       boolean  encore ;
	//long startTime = System.nanoTime();    
	private final boolean debug = false;

	
	public ClientThread()
	{
		this.encore     = true ;
		this.period     = DEFAULT_PERIODE ;
		this.clientAgent = new ClientAgent();
	}
	
	final void ClientRoutine(){
		// Loop until the peer shuts down the connection
		if(clientAgent.getConnected()){
			//Si le main a demandé à envoyer quelque chose
			if(somethingToSend){
				//on demande au serverAgent d'envoyer
				//threadPrint("Send button pressed! Sending the command...");
				clientAgent.sendString(sendbuf);
				somethingToSend = false;
			}
			//Si tout ce qui devait etre lu a été lu
			if (!somethingToRead) {
				recvbuf="";
				recvbuf = clientAgent.receive();
				if (recvbuf.length() > 0) {
					somethingToRead = true;				
					rcvCount = 0;
				}
			}
		} else{
			//Deconnection detectee, on ferme tout
			clientAgent.reboot();
		}

	}
	
	final public void disconnect(){
		clientAgent.setConnected(false);
	}

	//Destroy the socket then the thread.
	final public void destroyThreadAndConnection(){
		clientAgent.shutdown();
		encore = false;
		//threadPrint("Socket Closed, closing thread...\n");
	}
	final public void connect(String IPAdrr, int port){
		clientAgent.connect(IPAdrr, port);
	}
	
	final public void threadSend(String str){
		//startTime = System.nanoTime(); 
		sendbuf = str;
		somethingToSend = true;
	}
	
	final public String threadRead(){
		//long estimatedTime = System.nanoTime() - startTime;
		//System.out.println("time passed : " +estimatedTime);
		somethingToRead = false;
		String tmp = recvbuf;
		recvbuf = "";
		return tmp;
	}

	//****** Allow to put and header before each print. 
	//	it makes easier to determine from which objet and/or thread come the print
	private void threadPrint(String str) {
	System.out.println( "clientThread : " + str);
	}
	
	//***** give the current statut of the connection
	public boolean getConnected() {
	return clientAgent.getConnected();
	}
	
	//***** Tell if date is waiting for being red
	public boolean getSomethingToRead() {
	return somethingToRead;
	}
		
	
	public void run()
	{	
		//===== Thread init
		
		//===== Thread loop
		while (encore) {
			ClientRoutine();
			try {
				Thread.sleep(period) ;
			} catch (InterruptedException e) {
				if(debug)e.printStackTrace();
			}
		}
	}
	
	
	
	public final void endThread()
	{
		encore = false ;
	}
	
	public final void setPeriod(long period)
	{
		//period en ms
		if (period < 0) {
			period = 0L ;
		} else if (period > 1000000000L) {
			period = 1000000000L ;
		}
		this.period = period ;
	}
	
	public final long getPeriod()
	{
		return period ;
	}

}
