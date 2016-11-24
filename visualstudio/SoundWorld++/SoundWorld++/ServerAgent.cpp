#include "stdafx.h"
#include "ServerAgent.h"


//***** Constructor
ServerAgent::ServerAgent()
{
	wsaData;
	iResult;
	inet_pton(AF_INET, IP_ADDR, &(client_info.sin_addr));
	ListenSocket = INVALID_SOCKET;
	ClientSocket = INVALID_SOCKET;
	result = NULL;
	iSendResult;
	debugTCP = false;
	init();
}

//***** Destructor
ServerAgent::~ServerAgent()
{
	closesocket(ListenSocket);
	closesocket(ClientSocket);
	WSACleanup();
}
//***** Init the connection
//		Call the 5 folowing functions
void ServerAgent::init() {
	int ret = 0;
	ret = startWindowsSocket();
	ret = initHints();
	ret = createListenerSocket();
	ret = assignPortToSocket();
	ret = listenAtSocket();
	if (ret == ERR)exit(ERR);
}

//***** Start the windows service for TCP/IP Sockets
int ServerAgent::startWindowsSocket() {
	// Initialize Winsock
	iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed with error: %d\n", iResult);
		return ERR;
	}
	return 0;
}

//***** Set the socket IP and Port
int ServerAgent::initHints() {
	ZeroMemory(&hints, sizeof(hints));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_protocol = IPPROTO_TCP;
	hints.ai_flags = AI_PASSIVE;

	// Resolve the server address and port
	iResult = getaddrinfo(NULL, DEFAULT_PORT, &hints, &result);
	if (iResult != 0) {
		printf("getaddrinfo failed with error: %d\n", iResult);
		return ERR;
	}
	return 0;
}

//***** Create a SOCKET for connecting to this server
int ServerAgent::createListenerSocket() {
	ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);
	if (ListenSocket == INVALID_SOCKET) {
		printf("socket failed with error: %ld\n", WSAGetLastError());
		freeaddrinfo(result);
		connected = false;
		return ERR;
	}
	return 0;
}

//***** Bind the listen socket
int ServerAgent::assignPortToSocket() {

	//resolve bind issues
	char enable = 1;
	if (setsockopt(ListenSocket, SOL_SOCKET, SO_REUSEADDR, &enable, sizeof(int)) < 0)
		printf("setsockopt(SO_REUSEADDR) failed on ListenSocket\n");

	// Setup the TCP listening socket
	iResult = bind(ListenSocket, result->ai_addr, (int)result->ai_addrlen);
	if (iResult == SOCKET_ERROR) {
		printf("bind failed with error: %d\n", WSAGetLastError());
		freeaddrinfo(result);
		connected = false;
		return ERR;
	}
	freeaddrinfo(result);
	return 0;
}

//***** Open the port for incoming connections
int ServerAgent::listenAtSocket() {
	iResult = listen(ListenSocket, SOMAXCONN);
	if (iResult == SOCKET_ERROR) {
		printf("listen failed with error: %d\n", WSAGetLastError());
		connected = false;
		return ERR;
	}
	return 0;
}
//End of init functions

//***** Accept any client on the listen socket and assign it to the client socket
//		This function blocks the thread.
void ServerAgent::acceptClient() {
	// Accept a client socket
	ClientSocket = accept(ListenSocket, (struct sockaddr*)&client_info, &addrsize);
	if (ClientSocket == INVALID_SOCKET) {
		serverAgentPrint("accept failed with error: "+ to_string(WSAGetLastError()) +"\n");
		connected = false;
		return;
	}
	// No longer need server socket

	connected = true;
}


void ServerAgent::shutdownConnection() {
	// shutdown the connection since we're done
	int iResult = shutdown(ClientSocket, SD_BOTH);
	if (iResult == SOCKET_ERROR) {
		if (iResult == WSAENOTCONN)
			serverAgentPrint("No connexion established...\n");
		serverAgentPrint("shutdown failed with error: "+ to_string (WSAGetLastError())+".\n");
		connected = false;
		
		return;
	}
	// cleanup
	connected = false;
}

int ServerAgent::setBlocking(bool blocking) {
	// Set the mode of the socket to be nonblocking
	u_long iMode = !blocking;
	if (ioctlsocket(ClientSocket, FIONBIO, &iMode) == SOCKET_ERROR) {
		serverAgentPrint("setting blocking mode with ioctlsocket failed with error: " + to_string(WSAGetLastError())+".\n");
		connected = false;
		return ERR;
	}
	return 0;
}

// ====== Communication Functions
int ServerAgent::receive(string * recvbuf) {
	char char_recvbuf[DEFAULT_BUFLEN]= "";
	int rcvCount = recv(ClientSocket, char_recvbuf, DEFAULT_BUFLEN, 0);
	if (rcvCount > 0) {
		//On a bien recu un truc, on enregistre
		*recvbuf = (string) char_recvbuf;
		return rcvCount;
	}
	int error = WSAGetLastError();
	if (error == WSAEWOULDBLOCK) {
		return 0;
	}
	if(error == WSAENOTCONN)
		serverAgentPrint("No connexion established yet!\n");
	else if( error == WSAECONNRESET)
		serverAgentPrint("Client has been disconnected. Need to close socket.\n");
	else 
		serverAgentPrint("recv failed with error: " + to_string(error) +"\n");
	connected = false;
	return ERR;
	
}


int ServerAgent::sendString(string str) {
	int sndCount = 0;
	sndCount = send(ClientSocket, str.c_str(), str.size(), 0);
	if (sndCount == WSAEWOULDBLOCK)
		return 0;
	else if (sndCount < 0 ){ 
		if (sndCount == WSAENOTCONN)
			serverAgentPrint("No connexion established yet!\n");
		else if (sndCount == WSAECONNRESET)
			serverAgentPrint("Client has been disconnected. Need to close socket.\n");
		else
			serverAgentPrint("send failed with error: " + to_string(sndCount) + "\n");
		return ERR;
	}
	return sndCount;
}

//***** Allow to put and header before each print. 
//		it makes easier to determine from which objet and/or thread come the print
void ServerAgent::serverAgentPrint(string str) {
	std::cout << "ServerAgent : " << str;
}

bool ServerAgent::getConnected() {
	return connected;
}
void ServerAgent::setConnected(bool connected) {
	ServerAgent::connected = connected;
}