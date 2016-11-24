#pragma once
#undef UNICODE

#define WIN32_LEAN_AND_MEAN

#include <windows.h>
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdlib.h>
#include <stdio.h>

#include <string>
using namespace std;

// Need to link with Ws2_32.lib
#pragma comment (lib, "Ws2_32.lib")
// #pragma comment (lib, "Mswsock.lib")

#define DEFAULT_BUFLEN 512
#define DEFAULT_PORT "20015"
#define IP_ADDR "127.0.0.1"
#define ERR -1


#pragma once
class ServerAgent
{
private:
	//===== Variables=====//

	WSADATA		wsaData;
	int			iResult;
	SOCKADDR_IN client_info = { 0 };
	int			addrsize = sizeof(client_info);
	SOCKET		ListenSocket;
	SOCKET		ClientSocket;
	struct		addrinfo *result;
	struct		addrinfo hints;
	int			iSendResult;
	bool		connected = false;
	bool		debugTCP;

	//===== Functions=====//

	//***** Init the connection
	//		Call the 5 folowing functions
	void init();
	//***** Start the windows service for TCP/IP Sockets
	int startWindowsSocket();
	//***** Set the socket IP and Port
	int initHints();
	//***** Create a SOCKET for connecting to this server
	int createListenerSocket();
	//***** Bind the listen socket
	int assignPortToSocket();
	//***** Open the port for incoming connections
	int listenAtSocket();
	//End Init functions

	//***** Allow to put and header before each print. 
	//		it makes easier to determine from which objet and/or thread come the print
	void serverAgentPrint(string str);

public:
	//***** Constructor
	ServerAgent();

	//***** Destructor
	virtual ~ServerAgent();

	//***** Accept any client on the listen socket and assign it to the client socket
	//		This function blocks the thread.
	void acceptClient();
	int receive(string * recvbuf);
	int sendString(string str);
	void shutdownConnection();
	int setBlocking(bool blocking);
	bool getConnected();
	void setConnected(bool connected);
};

