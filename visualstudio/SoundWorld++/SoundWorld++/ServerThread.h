#pragma once
#include <thread>         // std::thread
#undef UNICODE

#define WIN32_LEAN_AND_MEAN
#define DEFAULT_PERIODE 1

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>

#include "ServerAgent.h"

class ServerThread {

private:
	//===== Variables=====//

	ServerAgent serverAgent;
	bool somethingToRead = false;
	bool somethingToSend = false;
	int periode = DEFAULT_PERIODE;
	string recvbuf;
	string sendbuf;
	int rcvCount = 0;
	int sndCount = 0;
	std::thread the_thread;

	//===== Functions=====//

	//***** Allow to put and header before each print. 
	//		it makes easier to determine from which objet and/or thread come the print
	void	threadPrint(string str);

	//***** The function runned by the_thread
	void	serverRoutine();

	//***** Reboot the socket server
	void	reboot();

public:
	//***** Constructor
	ServerThread();

	//***** Destructor
	virtual ~ServerThread();

	//***** Function to call after creating ServerThread object
	void	start();
	
	//*****
	void	disconnect();


	//***** copy the given str to the thread send buffer and notice the routine that 
	//		something needs to be sent
	void	threadSend(string str);

	//***** Function called from the outside
	//		It pull down the flag variable and copy 
	//		return the thread receive buffer
	string	threadRead();

	//***** Called from the outside
	//		it print a cmd if it is valid
	void	printCmd(string cmd, string params[]);

	//***** give the current statut of the connection
	bool	getConnected();

	//***** Tell if date is waiting for being red
	bool	getSomethingToRead();

	//***** Set the thread period (in ms)
	void	setPeriod(int newPeriod);

	//***** Give the thread period (in ms)
	int 	getPeriod();

		
};


