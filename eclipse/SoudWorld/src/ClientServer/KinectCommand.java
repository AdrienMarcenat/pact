package ClientServer;

import java.util.ArrayList;

public class KinectCommand extends ArrayList<String> {

	private static final long serialVersionUID = 154504022015L;

	private final int ERR = -1;
	public final static int TAILLE_ID_CMD 	= 6;
	public final static int NB_PARAM_MAX	= 10;
	private String cmdName;
	
	//Constructeur basique. tout est mit par defaut et vide. 
	public KinectCommand(){
		super();
		this.cmdName = "none";
	}
		
	// A utiliser par defaut
	//Constructeur a partir d'une chaine formatee selon notre protocol.
	public KinectCommand(String msg){
		
		super();
		if (isCmd(msg, TAILLE_ID_CMD) != ERR){
			
			int msg_len = msg.length();

			//on copie dans cmd les trois lettre décrivant la commande
			cmdName = msg.substring(1, TAILLE_ID_CMD+1); 

			//Om rempli le tableau de params
			int i = 1 + TAILLE_ID_CMD, param_n = 0;
			while (i< msg_len)
			{
				if (msg.charAt(i) == ';' || msg.charAt(i) == '$')
				{
					if(msg.charAt(i) == ';')
						add("");
					i++;
					while (i< msg_len-1 && msg.charAt(i) != ';' && msg.charAt(i) != '$' &&  param_n < NB_PARAM_MAX)
					{
						set(param_n,get(param_n)+msg.charAt(i++));
					}			
					param_n++;
				}
			}
		}
		//chaine incorrecte selon le protocole
		else{
			this.cmdName = "";
		}
	}
	
	//***** Called from the outside
	//	it print a cmd if it is valid
	public void printCmd() {
		
		System.out.println("==> Commande reconnue  --> " + cmdName);
		//Affichage des paramètres
		for (int i = 0; i < this.size(); i++)
		{
			if (get(i).length()>0)
				System.out.println("    Parametre " + i + " de " + cmdName + " --> " + get(i));
		}
		System.out.println("");
	}
	
	public String buildFormatedCommand(){
		int i=0;
		String cmd = "#" + cmdName;
		for(i=0;i<this.size();i++){
			cmd += ";" + this.get(i);
		}
		cmd+="$";
		return cmd;
	}
	
	@Override
	public boolean add(String str){
		if(size()<NB_PARAM_MAX){
			return super.add(str);
		}
		return false;
	}
	
	@Override
	public void add(int index,String str){
		if(size()<NB_PARAM_MAX && index < NB_PARAM_MAX ){
			super.add(index, str);
		}
	}
	
	public String getCmdName() {
		return cmdName;
	}
	public boolean setCmdName(String cmdName) {
		
		if(cmdName.length() == TAILLE_ID_CMD){
			this.cmdName = cmdName;
			return true;
		}
		return false;
	}
	
	public void clearAll(){
		this.clear();
		cmdName="";
	}
	//===== ===== TOOLS ===== =====//
	public int isCmd(String msg, int cmd_name_len) {
		if (msg.length() < 2 + cmd_name_len) return ERR;
		// les "1" dans 1 + cmd_name_len  + 1 sont dus au '#' et '$' de la fin 
		if (msg.charAt(0) == '#' && msg.length() >= 1 + cmd_name_len + 1)
		{
			// #1234cmd$
			// #1234cmd;param1;param2$
			if (msg.charAt(1 + cmd_name_len) == ';' || msg.charAt(1 + cmd_name_len) == '$')
			{
				int i;
				//Une commande commence toujours pas une lettre
				if (isLetter(msg.charAt(1)) == ERR) return ERR;
				for (i = 2; i < 1 + cmd_name_len; i++)
				{
					if (isLetter(msg.charAt(i)) == ERR && isNumber(msg.charAt(i)) == ERR)
						return ERR;
				}
				return 1;
			}
			else return ERR;
		}
		return ERR;
	}

	private int isLetter(char ch)
	{
		if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
			return 1;
		else
			return ERR;
	}
	
	int isNumber(char ch)
	{
		if (ch >= 48 && ch <= 57)
			return 1;
		else
			return ERR;
	}
}
