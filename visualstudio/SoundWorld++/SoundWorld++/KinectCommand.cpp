#include "stdafx.h"
#include "KinectCommand.h"

KinectCommand::KinectCommand() {
	this->cmdName = "none";
	this->size = 0;
}


KinectCommand::KinectCommand(string msg)
{

	if (isCmd(msg, TAILLE_ID_CMD) != ERR) {

		int msg_len = msg.length();

		//on copie dans cmd les trois lettre décrivant la commande
		cmdName = msg.substr(1, TAILLE_ID_CMD);

		//Om rempli le tableau de params
		int i = 1 + TAILLE_ID_CMD, param_n = 0;
		while (i< msg_len)
		{
			if (msg[i] == ';' || msg[i] == '$')
			{
				if (msg[i] == ';')
					param_n++;
				i++;

				while (i< msg_len && msg[i] != ';' && msg[i] != '$' &&  param_n-1 < NB_PARAM_MAX)
				{
					params[param_n-1] += msg[i++];
				}
			}
		}
		this->size = param_n;
	}
	//chaine incorrecte selon le protocole
	else {
		this->cmdName = "";
		this->size = 0;
	}
}


KinectCommand::~KinectCommand()
{
}

string KinectCommand::buildFormatedCommand() {
	int i = 0;
	string cmd = "#" + cmdName;
	for (i = 0;i<this->size;i++) {
		cmd += ";" + params[i];
	}
	cmd += "$";
	return cmd;
}

//***** Called from the outside
//	it print a cmd if it is valid
 void KinectCommand::printCmd() {

	cout << "==> Commande reconnue  --> " + cmdName + "\n";
	//Affichage des paramètres
	for (int i = 0; i < this->size; i++)
	{
		if (params[i].length()>0)
			cout << "    Parametre " + to_string(i) + " de " + cmdName + " --> " + params[i] + "\n";
	}
	cout << ("\n");
}

bool KinectCommand::add(string str) {
	if (str.length() <= 0 || this->size >= NB_PARAM_MAX ) {
		return false;
	}
	params[this->size++] = str;
}



bool KinectCommand::add(int index, string str) {
	int i;
	string tmp;
	string tmp2;
	if (str.length() <= 0 || this->size == NB_PARAM_MAX) {
		return false;
	}
	this->size++;
	tmp = params[index];
	params[index] = str;
	for (i = index +1 ;i < NB_PARAM_MAX;i++) {
		if (params[i].length() > 0) {
			tmp2 = params[i];
			params[i] = tmp;
			tmp = tmp2;
		}
		else {
			params[i] = tmp;
			return true;
		}
	}
	return true;

}

bool KinectCommand::remove(int index) {
	int i;

	if (index <0 || index > this->size || index >NB_PARAM_MAX)
		return false;

	this->size--;
	if (index == NB_PARAM_MAX - 1) {
		params[index] == "";
		return true;
	}
	for (i = index + 1;i < NB_PARAM_MAX;i++) {
		if (params[i].length() > 0) {
			params[i-1] = params[i];
		}
		else {
			return true;
		}
	}
	return true;
}

string KinectCommand::getCmdName() {
	return cmdName;
}

bool KinectCommand::setCmdName(string cmdName) {

	if (cmdName.length() == TAILLE_ID_CMD) {
		this->cmdName = cmdName;
		return true;
	}
	return false;
}

int KinectCommand::getSize() {
	return this->size;
}

 void KinectCommand::clearAll() {
	cmdName = "";
	int i = 0;
	for (i = 0; i < NB_PARAM_MAX;i++) {
		params[i] = "";
	}
	size = 0;
}

//===== ===== TOOLS ===== =====//
int 	KinectCommand::isCmd(string msg, char cmd_name_len) {
	if (msg.length() < 2 + cmd_name_len) return ERR;
	// les "1" dans 1 + cmd_name_len  + 1 sont dus au '#' et '$' de la fin 
	if (msg[0] == '#' && msg.length() >= (size_t)1 + cmd_name_len + 1)
	{
		// #1234cmd$
		// #1234cmd;param1;param2$
		if (msg[1 + cmd_name_len] == ';' || msg[1 + cmd_name_len] == '$')
		{
			int i;
			//Une commande commence toujours pas une lettre
			if (isLetter(msg[1]) == ERR) return ERR;
 			for (i = 2; i < 1 + cmd_name_len; i++)
			{
				if (isLetter(msg[i]) == ERR && isNumber(msg[i]) == ERR)
					return ERR;
			}
			return 1;
		}
		else return ERR;
	}
	return ERR;
}

int KinectCommand::isLetter(char ch)
{
	if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
		return 1;
	else
		return ERR;
}

int KinectCommand::isNumber(char ch)
{
	if (ch >= 48 && ch <= 57)
		return 1;
	else
		return ERR;
}