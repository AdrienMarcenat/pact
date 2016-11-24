#include <iostream>
#include "PlayerPositionThread.h"


PlayerPositionThread::PlayerPositionThread() :
	the_thread(),
	m_hNextSkeletonEvent(INVALID_HANDLE_VALUE),
	m_pSkeletonStreamHandle(INVALID_HANDLE_VALUE),
	m_pNuiSensor(NULL),
	KinectConnected(false),
	KinectDisconnected(false),
	mPosition()
{
	mPosition.x = 0;
	mPosition.y = 0;
	mPosition.z = 0;
	mPosition.w = 0;
}


PlayerPositionThread::~PlayerPositionThread()
{
	if (m_pNuiSensor)
	{
		m_pNuiSensor->NuiShutdown();
	}

	if (m_hNextSkeletonEvent && (m_hNextSkeletonEvent != INVALID_HANDLE_VALUE))
	{
		CloseHandle(m_hNextSkeletonEvent);
	}
	//Fermeture propre du thread
	stop_thread = true;
	if (the_thread.joinable()) the_thread.join();
	the_thread.~thread();
}


void PlayerPositionThread::clean()
{
	if (m_pNuiSensor)
	{
		m_pNuiSensor->NuiShutdown();
	}

	if (m_hNextSkeletonEvent && (m_hNextSkeletonEvent != INVALID_HANDLE_VALUE))
	{
		CloseHandle(m_hNextSkeletonEvent);
	}
	//SafeRelease(m_pNuiSensor);
}

void PlayerPositionThread::connect()
{
	// Look for a connected Kinect, and create it if found
	HRESULT hr = CreateFirstConnected();
	if (hr == S_OK)
		KinectConnected = true;

}

//***** Function to call after creating PlayerPositionThread object
void PlayerPositionThread::start() {
	
	// This will start the thread by running the serverRoutine function
	the_thread = std::thread(&PlayerPositionThread::serverRoutine, this);
}

//***** The function runned by the_thread
void PlayerPositionThread::serverRoutine() {


	MSG msg = { 0 };

	const int eventCount = 1;
	HANDLE hEvents[eventCount];

	// Main message loop
	while (WM_QUIT != msg.message && stop_thread != true)
	{
		hEvents[0] = m_hNextSkeletonEvent;

		// Check to see if we have either a message (by passing in QS_ALLEVENTS)
		// Or a Kinect event (hEvents)
		// Update() will check for Kinect events individually, in case more than one are signalled
		MsgWaitForMultipleObjects(eventCount, hEvents, FALSE, INFINITE, QS_ALLINPUT);

		// Explicitly check the Kinect frame event since MsgWaitForMultipleObjects
		// can return for other reasons even though it is signaled.
		Update();
		// Tempo de tour de boucle SUPER IMPORTANTE. Elle evite d'overload le CPU. 
		// Ne pas prendre de periode inférieur à 5ms!
		Sleep(periode);
	}

	//return static_cast<int>(msg.wParam);
	
}


//==========================================================================================================//
//==========================================[ Kinect Functions ]============================================//
//==========================================================================================================//



void PlayerPositionThread::Update()
{
	if (NULL == m_pNuiSensor)
	{
		return;
	}

	// Wait for 0ms, just quickly test if it is time to process a skeleton
	if (WAIT_OBJECT_0 == WaitForSingleObject(m_hNextSkeletonEvent, 0))
	{
		ProcessSkeleton();
	}
}

HRESULT PlayerPositionThread::CreateFirstConnected()
{
	INuiSensor * pNuiSensor;

	int iSensorCount = 0;
	
	HRESULT hr = NuiGetSensorCount(&iSensorCount);
	printf("NuiGetSensorCount return : %i, sensorCount : %i\n", hr, iSensorCount);
	if (S_OK !=hr)
	{
		printf("NuiGetSensorCount return : %i\n", hr);
		return hr;
	}
	
	// Look at each Kinect sensor
	for (int i = 0; i < iSensorCount; ++i)
	{
		// Create the sensor so we can check status, if we can't create it, move on to the next
		hr = NuiCreateSensorByIndex(i, &pNuiSensor);
		if (FAILED(hr))
		{
			continue;
		}

		// Get the status of the sensor, and if connected, then we can initialize it
		hr = pNuiSensor->NuiStatus();
		if (S_OK == hr)
		{
			m_pNuiSensor = pNuiSensor;
			break;
		}

		// This sensor wasn't OK, so release it since we're not using it
		pNuiSensor->Release();
	}

	if (NULL != m_pNuiSensor)
	{
		// Initialize the Kinect and specify that we'll be using skeleton
		hr = m_pNuiSensor->NuiInitialize(NUI_INITIALIZE_FLAG_USES_SKELETON);
		if (SUCCEEDED(hr))
		{
			// Create an event that will be signaled when skeleton data is available
			m_hNextSkeletonEvent = CreateEventW(NULL, TRUE, FALSE, NULL);

			// Open a skeleton stream to receive skeleton data
			hr = m_pNuiSensor->NuiSkeletonTrackingEnable(m_hNextSkeletonEvent, 0);
		}
	}

	if (NULL == m_pNuiSensor || FAILED(hr))
	{
		return E_FAIL;
	}

	return hr;
}

void PlayerPositionThread::ProcessSkeleton()
{
	NUI_SKELETON_FRAME skeletonFrame = { 0 };

	HRESULT hr = m_pNuiSensor->NuiSkeletonGetNextFrame(0, &skeletonFrame);
	if (FAILED(hr))
	{
		return;
	}

	// smooth out the skeleton data
	m_pNuiSensor->NuiTransformSmooth(&skeletonFrame, NULL);

	bool isTrack = false;
	for (int i = 0; i < NUI_SKELETON_COUNT; ++i)
	{
		NUI_SKELETON_TRACKING_STATE trackingState = skeletonFrame.SkeletonData[i].eTrackingState;
		//std::cout << trackingState << std::endl;

		if (NUI_SKELETON_TRACKED == trackingState)
		{	
			mPosition = shiftCoordinates(skeletonFrame.SkeletonData[i].Position);
			isTrack = true;
		}
		else if (NUI_SKELETON_POSITION_ONLY == trackingState)
		{
			// we've only received the center point of the skeleton, draw that
			mPosition = shiftCoordinates(skeletonFrame.SkeletonData[i].Position);
			isTrack = true;
		}
		
	}

	if (!isTrack)
	{
		mPosition.x = -1;	
		mPosition.y = -1;
		mPosition.z = -1;
		mPosition.w = -1;
	}

	/*std::cout << mPosition.x << std::endl;
	std::cout << mPosition.y << std::endl;
	std::cout << mPosition.z << std::endl;
	std::cout << "" << std::endl;*/

}

//Transforme les coordonnées de kinnect en coordonnées our un écran 800x600
//elles sont en double centimètres
Vector4 PlayerPositionThread::shiftCoordinates(Vector4 pos)
{
	//On centre la kinnect dans un écran 800x600
	pos.x = 200 * pos.x + 400;
	//Hauteur
	pos.y = 200 * pos.y;
	//z ne change pas car c'est la profondeur
	pos.z = 200 * pos.z - 200;

	return pos;
}

bool PlayerPositionThread::status()
{
	if (m_pNuiSensor == NULL)
		return true;

	//Kinect status
	HRESULT co = m_pNuiSensor->NuiStatus();
	if (S_OK != co)
	{
		return false;
	}
	return true;
}

Vector4 PlayerPositionThread::getPosition()
{
	return mPosition;
}

bool PlayerPositionThread::getKinectConnected()
{
	return KinectConnected;
}

bool PlayerPositionThread::getKinectDisconnected()
{
	return KinectDisconnected;
}

//==========================================================================================================//
//==========================================[ Thread Functions ]============================================//
//==========================================================================================================//

//****** Allow to put and header before each print. 
//		it makes easier to determine from which objet and/or thread come the print
void PlayerPositionThread::threadPrint(string str) {
	std::cout << "PlayerPositionThread : " << str;
}

//***** Set the thread period (in ms)
void PlayerPositionThread::setPeriod(int newPeriod) {
	periode = newPeriod;
}

//***** Give the thread period (in ms)
int PlayerPositionThread::getPeriod() {
	return periode;
}