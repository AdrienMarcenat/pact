// Kinect_Server.cpp : définit le point d'entrée pour l'application console.
// Créé le 15/1215

#include "stdafx.h"
#include "ServerThread.h"
#include "KinectCommand.h"
#include "PlayerPositionThread.h"

//dans Project>>Propriétés de KinectServer>>RépertoiresVC++>>Répertoires Include
//inscrire $(JAVA_HOME)\include;$(JAVA_HOME)\include\win32;
//puis dans  Project>>Propriétés de KinectServer>>Assembleur de lien>>Général>> Répertoires de bibliothèques supplémentaires
// Ajouter C:\Program Files %28x86%29\Java\jdk1.8.0_73\lib;%(AdditionalLibraryDirectories)
//Puis dans Project>>Propriétés de KinectServer>>Assembleur de lien>>Entrée dans dépendances suppllémentaires
//ajouter jvm.lib;
//enfin dans %PATH%, ajouter C:\Program Files (x86)\Java\jdk1.8.0_73\jre\bin\server pour le fichier jvm.dll
//ou peut etre C:\Program Files (x86)\Java\jdk1.8.0_73\jre\bin\client, je sais pas encore
//Bien redémarrer le pc quand on touche à une variable d'envirronement comme %PATH% ou s'il on en créé une
//pour lancer le jar
#include <jni.h> 

void kinectServerPrint(string str);

int main()
{
	string recvbuf;
	KinectCommand command;


	//Code for launching thread
	ServerThread serverThread;
	serverThread.start();
	auto & game_thread = std::thread([]() {
		//===================== JNI JNI JNI JNI JNI ====================================
		JavaVM *jvm = NULL;
		JNIEnv *env;
		JavaVMInitArgs vm_args;
		JavaVMOption options;
		options.optionString = "-Djava.class.path=../../../../../eclipse/SoudWorld/Executable/soundWorld_KinectTest.jar";
		vm_args.version = JNI_VERSION_1_8;
		vm_args.nOptions = 1;
		vm_args.options = &options;
		vm_args.ignoreUnrecognized = 0;
		int ret = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
		if (ret == 0) {
			jclass cls = env->FindClass("Main/Main");
			if (cls != 0) {
				jmethodID meth = env->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
				jarray args = env->NewObjectArray(0, env->FindClass("java/lang/String"), 0);
				env->CallStaticVoidMethod(cls, meth, args);
			}
		}
	});
	//===================== JNI JNI JNI JNI JNI ====================================

	//Attente qu'un client se connecte
	while (!serverThread.getConnected())
		Sleep(5);

	PlayerPositionThread* ppt = new PlayerPositionThread();

	// Boucle infinie jusqu'a déconexion du client
	while (serverThread.getConnected()) {
		// Si quelque chose à lire
		if (serverThread.getSomethingToRead()) 
		{
			recvbuf = serverThread.threadRead();
			kinectServerPrint("Received : " + recvbuf + "\n");
			command = KinectCommand(recvbuf);
			if (command.getCmdName().length() > 0)
			{
				if (command.getCmdName() == "KinCon")
				{
					ppt = new PlayerPositionThread();
					ppt->connect();
					if (ppt->getKinectConnected())
					{
						command.clearAll();
						command.setCmdName("isConn");
						serverThread.threadSend(command.buildFormatedCommand());
						ppt->start();
					}
					else
					{
						ppt->~PlayerPositionThread(); 
						command.clearAll();
						command.setCmdName("NotCon");
						serverThread.threadSend(command.buildFormatedCommand());
					}
				}

				if (command.getCmdName() == "getPos")
				{
					if (!ppt->status())
					{
						ppt->~PlayerPositionThread();
						command.clearAll();
						command.setCmdName("DisCon");
						serverThread.threadSend(command.buildFormatedCommand());
					}
					else
					{
						Vector4 pos = ppt->getPosition();
						command.clearAll();
						command.setCmdName("posInf");
						command.add(to_string(pos.x));
						command.add(to_string(pos.y));
						command.add(to_string(pos.z));
						command.add(to_string(pos.w));
						serverThread.threadSend(command.buildFormatedCommand());
					}
				}
				
			}
			else 
			{
				kinectServerPrint("Unreadable command!\n");
			}
		}
		
		// Tempo de tour de boucle SUPER IMPORTANTE. Elle evite d'overload le CPU
		// Ne pas prendre de periode inférieur à 5ms!
		Sleep(1);
	}
	kinectServerPrint("Ending programme...\n");
	//Kill Java
	//jvm->DestroyJavaVM();
	//Wait for user to quit
	std::system("pause");
	return 0;

	

  
}

void kinectServerPrint(string str) {
	std::cout << "KinectServer :" + str + "\n";
}
