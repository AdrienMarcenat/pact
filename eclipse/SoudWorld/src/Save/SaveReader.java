package Save;

import java.io.FileReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SaveReader 
{
	//======================================[ Attributs ]===========================================//
	
    private JSONTokener reader;
    private JSONObject value;
    private FileReader fr;
    
    //======================================[ Constructeur ]===========================================//
	
    public SaveReader()
	{

	}
	
    //======================================[ Fonctions ]===========================================//
	
    //Commence à lire le fichier filename
    public void init(String filename) throws Exception
	{
		fr = new FileReader(filename);
		reader = new JSONTokener(fr);
		value = (JSONObject)reader.nextValue();
	}
    
    public PlayerInfo read() throws Exception
    {
    	PlayerInfo p = null;
    	
		int maxLife = value.getInt("maxLife");
		int levelClearedNumber = value.getInt("level");
		int cristalNumber = value.getInt("cristalNumber");
			
		ArrayList<LevelInfo> levelInfo = new ArrayList<LevelInfo>();
		JSONArray levelStat = value.getJSONArray("levelStat");
		for (int i = 0; i < levelStat.length(); i++)
		{
			JSONObject o = levelStat.getJSONObject(i);
			int cristalsCollected = o.getInt("cristalsCollected");
			int hitNumber = o.getInt("hit");
			
			ArrayList<Integer> cristalBooleans = new ArrayList<Integer>();
			JSONArray cristals = o.getJSONArray("cristals");
			for (int j = 0; j < cristals.length(); j++)
			{
				cristalBooleans.add(cristals.getInt(j));
			}
			
			levelInfo.add(new LevelInfo(cristalsCollected, hitNumber, cristalBooleans));
		}
		
		p = new PlayerInfo(maxLife, levelClearedNumber, cristalNumber, levelInfo); 
  	
		fr.close();
		
    	return p;
    }
}
