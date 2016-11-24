// ServerThread routine
#include "stdafx.h"
#include "ServerThread.h"


//***** Constructor
ServerThread::ServerThread():the_thread()
{
	printf("\n=========================[ NEW CONNECTION ]=========================\n");
	threadPrint("Listening for client at " IP_ADDR " on port " DEFAULT_PORT "...\n");

}

//***** Destructor
ServerThread::~ServerThread()
{
	//Fermeture propre du thread
	//stop_thread = true;
	if (the_thread.joinable()) the_thread.join();
}


//***** Function to call after creating ServerThread object
void ServerThread::start() {
	// This will start the thread by running the serverRoutine function
	the_thread = std::thread(&ServerThread::serverRoutine, this);
}

//***** The function runned by the_thread
void ServerThread::serverRoutine(){
	//blocking method
	serverAgent.acceptClient();
	threadPrint("Client connected\n\n");
	serverAgent.setBlocking(false);
	while (1) {
		// Loop until the peer shuts down the connection
		while (serverAgent.getConnected()) {
			//Si le main a demandé à envoyer quelque chose
			if (somethingToSend) {
				//on demande au serverAgent d'envoyer
				serverAgent.sendString(sendbuf);
				somethingToSend = false;
			}
			//Si tout ce qui devait etre lu a été lu
			if (!somethingToRead) {
				rcvCount = serverAgent.receive(&recvbuf);
				if (rcvCount > 0) {
					somethingToRead = true;
					rcvCount = 0;
				}
			}
			// Tempo de tour de boucle SUPER IMPORTANTE. Elle evite d'overload le CPU. 
			// Ne pas prendre de periode inférieur à 5ms!
			Sleep(periode);
		}
		reboot();
	}
}
//==========================================================================================================//
//=============================================[ Functions ]================================================//
//==========================================================================================================//


//***** Reboot the socket server
void ServerThread::reboot() {
	serverAgent.shutdownConnection();
	threadPrint("Socket Closed\n");
	printf("\n=========================[ NEW CONNECTION ]=========================\n");
	threadPrint("Listening for client at " IP_ADDR " on port " DEFAULT_PORT "...\n");
	serverAgent.setBlocking(true);
	//blocking method
	serverAgent.acceptClient();
	threadPrint("Client connected\n\n");
	serverAgent.setBlocking(false);
}

void ServerThread::disconnect() {
	serverAgent.setConnected(false);
}
//***** copy the given str to the thread send buffer and notice the routine that 
//		something needs to be sent
void ServerThread::threadSend(string str) {
	sendbuf = str;
	somethingToSend = true;
}
//***** Function called from the outside
//		It pull down the flag variable and copy 
//		return the thread receive buffer
string ServerThread::threadRead() {
	somethingToRead = false;
	string tmp = recvbuf;
	recvbuf.clear();
	return tmp;
}

//****** Allow to put and header before each print. 
//		it makes easier to determine from which objet and/or thread come the print
void ServerThread::threadPrint(string str) {
	std::cout << "ServerThread : " << str;
}

//***** give the current statut of the connection
bool ServerThread::getConnected() {
	return serverAgent.getConnected();
}

//***** Tell if date is waiting for being red
bool ServerThread::getSomethingToRead() {
	return somethingToRead;
}

//***** Set the thread period (in ms)
void ServerThread::setPeriod(int newPeriod) {
	periode = newPeriod;
}

//***** Give the thread period (in ms)
int ServerThread::getPeriod() {
	return periode;
}