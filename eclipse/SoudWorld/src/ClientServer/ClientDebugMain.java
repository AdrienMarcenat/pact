package ClientServer;

public class ClientDebugMain {


	
	public static void main(String[] args) throws InterruptedException {

		debugPrintln("Client Started");
		long startTime = System.nanoTime();
		long testTime = System.nanoTime();
		ClientThread client = new ClientThread();
		KinectCommand cmd = new KinectCommand();
		String recv="";
		long estimatedTime, estimatedTestTime;
		String microSec;
		int occ = 10000;
		final boolean prints = false;
		int i=0,j=0, k=0;
		long moy = 1, min = 100000000, max = 1, cpt = 0, win=0, fail = 0;

		// ===== CONNEXION
		client.connect("127.0.0.1", 20015);
		while(client.getConnected() == false)
			Thread.sleep(5);
		client.start();
		
		// ===== WE ARE CONNECTED

		// ===== TEST 1
		debugPrintln("==========> Test 1 <==========");
		testTime = System.nanoTime(); 
		debugPrintln("Sending no parameter 'test01' commmand " + occ + " times...");
		for(i = 0; i<occ; i++){
			cmd.setCmdName("test01");
			startTime = System.nanoTime(); 
			client.threadSend(cmd.buildFormatedCommand());
			//wait for answer
			while(client.getSomethingToRead() == false)
				Thread.sleep(0,500);
				//debugPrintln("Waiting answer...");
			
			recv = client.threadRead();
			if(prints)debugPrintln("Received : " + recv);
			cmd = new KinectCommand(recv);
			estimatedTime = (System.nanoTime() - startTime);

			if(cmd.getCmdName().equals("resp01")){
				//CALCUL DE STATS
				if(estimatedTime < min) min =  estimatedTime;
				if(estimatedTime >max) 	max =  estimatedTime;
				cpt += estimatedTime;
				win++;
			}
			else{
				fail++;
			}
		}
		estimatedTestTime = (System.nanoTime() - testTime);
		moy = cpt / occ;
		debugPrintln("==> test01 finished. It took "+convertTime(estimatedTestTime)+" ms");
		debugPrintln("average time passed for test1 : " + convertTime(moy) + " ms");
		debugPrintln("MIN = "+ convertTime(min)+" ms and MAX = " + convertTime(max) + " ms");
		debugPrintln("There were "+ win +" success and "+fail+" fails.");
		moy = 1; min = 100000000; max = 1; cpt = 0; win=0; fail = 0;
		CRLF();
		
		// ===== TEST 2
		debugPrintln("==========> Test 2 <==========");
		testTime = System.nanoTime(); 
		debugPrintln("Sending 508 characters long 'test02' commmand " + occ + " times...");
		// a tcp message is set to 512 max. 
		// wet get 1 + 6 + 1+ 125 + 1 + 125 + 1 + 125 + 1 + 125 + 1 
		for(i=0;i< occ; i++){
			cmd.setCmdName("test02");
			String str = "";
			for(k = 0;k<4;k++){
				for(j=0;j<124;j++){
					str+="a";
				}
				cmd.add(str);
				str = "";
			}
			if(prints)debugPrintln("length of cmd: " + cmd.buildFormatedCommand().length());
			startTime = System.nanoTime(); 
			client.threadSend(cmd.buildFormatedCommand());
			//wait for answer
			while(client.getSomethingToRead() == false)
				Thread.sleep(0,500);
			
			recv = client.threadRead();
			if(prints)debugPrintln("Received : " + recv);
			cmd = new KinectCommand(recv);
			estimatedTime = (System.nanoTime() - startTime);

			if(cmd.getCmdName().equals("resp02")){
				//CALCUL DE STATS
				if(estimatedTime < min) min =  estimatedTime;
				if(estimatedTime >max) 	max =  estimatedTime;
				cpt += estimatedTime;
				win++;
			}
			else{
				fail++;
			}			
		}
		estimatedTestTime = (System.nanoTime() - testTime);
		moy = cpt / occ;
		debugPrintln("==> test02 finished. It took "+convertTime(estimatedTestTime)+" ms");
		debugPrintln("average time passed for test2 : " + convertTime(moy) + " ms");
		debugPrintln("MIN = "+ convertTime(min)+" ms and MAX = " + convertTime(max) + " ms");
		debugPrintln("There were "+ win +" success and "+fail+" fails.");
		moy = 1; min = 100000000; max = 1; cpt = 0; win=0; fail = 0;
		CRLF();
		//Thread.sleep(2000);
		
		debugPrintln("==========> Test 3 <==========");
		testTime = System.nanoTime(); 
		occ = 5;
		debugPrintln("Sending random unknown commmand to the server" + occ + " times...");
		for(i=0; i<occ; i++){
			cmd.setCmdName("hola13");
			cmd.add("HEEEYYYY");
			startTime = System.nanoTime(); 
			client.threadSend(cmd.buildFormatedCommand());
			//wait for answer
			while(client.getSomethingToRead() == false)
				Thread.sleep(0,500);
			recv = client.threadRead();
			cmd = new KinectCommand(recv);
			estimatedTime = (System.nanoTime() - startTime);
			if(cmd.getCmdName().equals("noKnow")){
				//CALCUL DE STATS
				if(estimatedTime < min) min =  estimatedTime;
				if(estimatedTime >max) 	max =  estimatedTime;
				cpt += estimatedTime;
				win++;
			}
			else{
				fail++;
			}	
		}
		estimatedTestTime = (System.nanoTime() - testTime);
		moy = cpt / occ;
		debugPrintln("==> test03 finished. It took "+convertTime(estimatedTestTime)+" ms");
		debugPrintln("average time passed for test3 : " + convertTime(moy) + " ms");
		debugPrintln("MIN = "+ convertTime(min)+" ms and MAX = " + convertTime(max) + " ms");
		debugPrintln("There were "+ win +" success and "+fail+" fails.");
		moy = 1; min = 100000000; max = 1; cpt = 0; win=0; fail = 0;
		CRLF();
		
		debugPrintln("==========> Test 4 <==========");
		testTime = System.nanoTime(); 
		occ = 5;
		debugPrintln("Sending incorrectly built command to the server " + occ + " times...");
		for(i=0; i<occ; i++){
			startTime = System.nanoTime(); 
			client.threadSend("#HOLAAAAAAA$");
			//wait for answer
			while(client.getSomethingToRead() == false)
				Thread.sleep(0,500);
			recv = client.threadRead();
			cmd = new KinectCommand(recv);
			estimatedTime = (System.nanoTime() - startTime);
			if(cmd.getCmdName().equals("noRead")){
				//CALCUL DE STATS
				if(estimatedTime < min) min =  estimatedTime;
				if(estimatedTime >max) 	max =  estimatedTime;
				cpt += estimatedTime;
				win++;
			}
			else{
				fail++;
			}	
		}
		estimatedTestTime = (System.nanoTime() - testTime);
		moy = cpt / occ;
		debugPrintln("==> test04 finished. It took "+convertTime(estimatedTestTime)+" ms");
		debugPrintln("average tie passed for test3 : " + convertTime(moy) + " ms");
		debugPrintln("MIN = "+ convertTime(min)+" ms and MAX = " + convertTime(max) + " ms");
		debugPrintln("There were "+ win +" success and "+fail+" fails.");
		moy = 1; min = 100000000; max = 1; cpt = 0; win=0; fail = 0;
		CRLF();
		
		debugPrintln("======================================");
		debugPrintln("Ending program...");

		System.exit(0);
	}

	final private static void debugPrintln(String str){
		System.out.println("TEST_ROUTINE : "+str);
	}
	
	final private static void debugPrintERRln(String str){
		System.out.println("ERR : "+str);
	}
	
	final private static void CRLF(){
		System.out.println("");
	}
	final private static String convertTime(long time){
		String microSec = String.valueOf(time/10000);
		microSec  = microSec.substring(microSec.length()-2);
		return time/1000000+ "," +microSec;
	}
}
