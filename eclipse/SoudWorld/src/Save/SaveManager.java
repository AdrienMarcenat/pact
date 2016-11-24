package Save;


public class SaveManager 
{
	//======================================[ Attributs ]===========================================//
	
	private SaveWriter sw;
	private SaveReader sr;
	private int saveNumber;
			
	//======================================[ Constructeur ]===========================================//
			
	public SaveManager(int saveNumber)
	{
		this.saveNumber = saveNumber;
		this.sw = new SaveWriter();
		this.sr = new SaveReader();
	}

	//======================================[ Fonctions ]===========================================//
					
	public void save(PlayerInfo p)
	{
		//On écrit d'abord dans TempSave
		sw = new SaveWriter();
		sw.init("SaveFiles/TempSave" + String.valueOf(saveNumber) + ".txt");
		sw.Save(p);
		
		//Puis dans Save
		sw = new SaveWriter();
		sw.init("SaveFiles/Save" + String.valueOf(saveNumber) + ".txt");
		sw.Save(p);
	}
	
	public PlayerInfo read()
	{
		PlayerInfo p = null;
		
		try 
		{
			//On essaie de lire Save
			sr = new SaveReader();
			sr.init("SaveFiles/Save" + String.valueOf(saveNumber) + ".txt");
			p = sr.read();
		} 
		catch (Exception e) 
		{
			//Si on y arrive pas on va lire TempSave que l'on recopie dans Save
			try 
			{
				sr = new SaveReader();
				sr.init("SaveFiles/TempSave" + String.valueOf(saveNumber) + ".txt");
				p = sr.read();
			} 
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
			sw = new SaveWriter();
			sw.init("SaveFiles/Save" + String.valueOf(saveNumber) + ".txt");
			sw.Save(p);
		}
		
		return p;
	}
	
	public int getSaveNumber() 
	{
		return saveNumber;
	}

	public void setSaveNumber(int saveNumber) 
	{
		this.saveNumber = saveNumber;
	}
}
