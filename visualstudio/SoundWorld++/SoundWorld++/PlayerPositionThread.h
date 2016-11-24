#pragma once

#include "stdafx2.h"
#include <thread>         // std::thread
#include <string>
#include <iostream>
#include <windows.h>
#include <NuiApi.h>
#ifndef DEFAULT_PERIODE
#define DEFAULT_PERIODE 5
#endif
using namespace std;


class PlayerPositionThread
{
private:
	int periode = DEFAULT_PERIODE;
	bool stop_thread = false;
	std::thread the_thread;

	//===== Kinect Functions=====//

	// Position
	Vector4 mPosition;

	// Current Kinect
	INuiSensor*             m_pNuiSensor;

	HANDLE                  m_pSkeletonStreamHandle;
	HANDLE                  m_hNextSkeletonEvent;
	bool					KinectConnected;
	bool					KinectDisconnected;

	//===== Functions=====//

	//***** Allow to put and header before each print. 
	//		it makes easier to determine from which objet and/or thread come the print
	void	threadPrint(string str);

	//***** The function runned by the_thread
	void	serverRoutine();
public:
	//***** Constructor
	PlayerPositionThread();

	//***** Destructor
	~PlayerPositionThread();

	//***** Function to call after creating PlayerPositionThread object
	void	start();
	void connect();
	void clean();
	bool status();

	void Update();
	void ProcessSkeleton();
	Vector4 shiftCoordinates(Vector4);
	HRESULT CreateFirstConnected();
	Vector4 getPosition();
	bool getKinectConnected();
	bool getKinectDisconnected();

	// ********** Thread functions *********//
	//***** Set the thread period (in ms)
	void	setPeriod(int newPeriod);

	//***** Give the thread period (in ms)
	int 	getPeriod();
};