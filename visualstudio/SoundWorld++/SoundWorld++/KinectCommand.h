#pragma once
#include <string>

#define TAILLE_ID_CMD 6
#define NB_PARAM_MAX 10
#define ERR -1
using namespace std;

class KinectCommand
{
private:
	 string cmdName;
	 string params[NB_PARAM_MAX];
	 int isLetter(char ch);
	 int isNumber(char ch);
	 int size;
	 

public:
	KinectCommand();
	KinectCommand(string msg);
	~KinectCommand();
	int isCmd(string msg, char cmd_name_len);
	bool setCmdName(string cmdName);
	string getCmdName();
	void clearAll();
	bool add(string str);
	bool add(int index, string str);
	int getSize();
	bool remove(int index);
	void printCmd();
	string buildFormatedCommand();

};

