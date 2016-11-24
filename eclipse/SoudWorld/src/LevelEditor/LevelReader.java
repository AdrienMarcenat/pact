package LevelEditor;

import java.io.InputStream;
import java.util.ArrayList;

import org.jsfml.system.Vector3f;
import org.json.*;

import Components.*;
import Entity.*;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class LevelReader 
{
	//======================================[ Attributs ]===========================================//
	
    private JSONTokener reader;
    private JSONObject value;
    private JSONArray events;
    private int currentEvent;
    private boolean endLevel;
    
    //======================================[ Constructeur ]===========================================//
	
    public LevelReader()
	{

	}
	
    //======================================[ Fonctions ]===========================================//
	
    //Commence à lire le fichier filename
    public void init(String filename)
	{
		try
		{
			InputStream ips = getClass().getResourceAsStream(filename);
			//in = new BufferedReader(new FileReader(filename));
		    reader = new JSONTokener(ips);
		    value = (JSONObject)reader.nextValue();
		    events = value.getJSONArray("events");
		    currentEvent = 0;
		    endLevel = false;
		    
		}
		catch(JSONException e)
		{
			System.err.println("Error while reading file: " + e.getMessage());
		}
		
	}
    
    public boolean levelCleared()
    {
    	return endLevel;
    }
	
	//read the field "levelName" in the file
	public String getLevelName()
	{
		String name = new String();
		
		try
		{
		    name = value.getString("levelName");
		}
		catch(JSONException e)
		{
			System.err.println("Error while reading file: " + e.getMessage());
		}
		
		return name;
	}
	
	public int getNumberOfEvent()
	{
		return events.length();
	}
	
	public int getCurrentEvent()
	{
		return currentEvent;
	}
	
	//read the next event and provide a list of all the monsters in this event
	public ArrayList<Monster> getNextEvent(SoundManager sound, HRTFManager hm)
	{
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		
		if(currentEvent == events.length())
		{
			endLevel = true;
			return monsters;
		}
		
		try
		{
		    JSONArray e = events.getJSONArray(currentEvent);
		    
		    for(int i = 0; i < e.length(); i++)
		    {
		    	JSONObject o = e.getJSONObject(i);
		    	
			   	String type = o.getString("type");
			 	Monster.Trigger trigger = Monster.Trigger.valueOf(o.getString("trigger"));
			 	
			   	//Position
			   	float posX = Float.valueOf(o.getString("posX"));
			   	float posY = Float.valueOf(o.getString("posY"));
			   	float posZ = Float.valueOf(o.getString("posZ"));
			   	
				//Sound
			   	String id = o.getString("sound");
			   
			   	if (type.equals("N"))
			   	{
			   	 	int radius = Integer.valueOf(o.getString("radius"));
			   		int damage = Integer.valueOf(o.getString("damage"));
			   		int life = Integer.valueOf(o.getString("life"));
			   		
			   		//Speed
				   	float v = Float.valueOf(o.getString("velocity"));
				   	
				   	//Trajectory
				   	JSONArray trajectory = o.getJSONArray("trajectory");
				   	ArrayList<Point> t = new ArrayList<Point>();
				   	for(int j = 0; j < trajectory.length(); j++)
				   	{
				   		JSONObject p = trajectory.getJSONObject(j);
				   		int x = Integer.valueOf(p.getString("X"));
				   		int y = Integer.valueOf(p.getString("Y"));
				   		t.add( new Point(x, y));
				   	}
				   	
				   	monsters.add(new NormalMob(radius, life, new Vector3f(posX, posY, posZ), 
				   			     id, sound, hm, new Trajectory(t), damage, v, trigger));
			   	}
			   	else if (type.equals("H"))
			   	{
			   	 	int radius = Integer.valueOf(o.getString("radius"));
			   		int damage = Integer.valueOf(o.getString("damage"));
			   		int life = Integer.valueOf(o.getString("life"));
			   		int lifeTime = Integer.valueOf(o.getString("lifeTime"));
			   		
			   		//Speed
				   	float v = Float.valueOf(o.getString("velocity"));
				   	monsters.add(new HommingMob(radius, life, new Vector3f(posX, posY, posZ), 
				   			     id, sound, hm, damage, v, trigger, lifeTime));
				   	
			   	}
			   	else if (type.equals("A"))
				{
			   	 	int radius = Integer.valueOf(o.getString("radius"));
			   		int damage = Integer.valueOf(o.getString("damage"));
			   		
			   		monsters.add(new TriggerArea(radius, new Vector3f(posX, posY, posZ), 
			   				     id, sound, hm, damage, trigger));
				}
			   	else if (type.equals("S"))
					monsters.add(new Speaker(new Vector3f(posX, posY, posZ), 
							     id, sound, hm, trigger));
			   	
			}
		}
		catch(JSONException e)
		{
			System.err.println("Error while reading file: " + e.getMessage());
		}
		
		currentEvent++;
		
		return monsters;
	}
	
	public Ambience getAmbience(SoundManager sm, HRTFManager hm)
	{
		ArrayList<SoundPlayer> sounds = new ArrayList<SoundPlayer>();
		ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
		
		try
		{
			JSONArray e = value.getJSONArray("ambience");
		    
		    for(int i = 0; i < e.length(); i++)
		    {
		    	JSONObject o = e.getJSONObject(i);
		    	
		    	String id = o.getString("sound");
		    	sounds.add(new SoundPlayer(id, sm, hm));

		    	float posX = Float.valueOf(o.getString("posX"));
			   	float posY = Float.valueOf(o.getString("posY"));
			   	float posZ = Float.valueOf(o.getString("posZ"));
			   	positions.add(new Vector3f(posX, posY, posZ));
		    }
		}
		catch(JSONException e)
		{
			System.err.println("Error while reading file: " + e.getMessage());
		}
		
		return new Ambience(sounds, positions);
	}
}
