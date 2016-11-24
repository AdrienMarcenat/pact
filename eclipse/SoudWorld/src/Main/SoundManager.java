package Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.jsfml.audio.*;
import org.jsfml.system.Vector3f;
import org.json.*;

import GameInterfaces.SoundManagerInterface;

public class SoundManager implements SoundManagerInterface
{
	//======================================[ Attributs ]===========================================//
	
	private HashMap<String, short[]> mSounds;
		
	//======================================[ Constructeur ]===========================================//
			
	public SoundManager() 
	{
		mSounds = new HashMap<String, short[]>();
	}

			
	//======================================[ Fonctions ]===========================================//
			
	@Override
	public void load(String filename)
	{
		try
		{
			InputStream ips = getClass().getResourceAsStream(filename);
			//in = new BufferedReader(new FileReader(filename));
		    JSONTokener reader = new JSONTokener(ips);
		    JSONObject value = (JSONObject)reader.nextValue();
		    JSONArray sounds = value.getJSONArray("sounds");
		    for(int i = 0; i < sounds.length(); i++)
		    {
		    	 JSONObject o = sounds.getJSONObject(i);
		    	 SoundBuffer sb = new SoundBuffer();
		    	 sb.loadFromStream(getClass().getResourceAsStream(o.getString("file")));
		    	 System.out.println("Ressource load: " + o.getString("file"));
		    	 mSounds.put(o.getString("name"),  sb.getSamples());
		    }
		}
		catch(JSONException e)
		{
			System.err.println("Error while reading file: " + e.getMessage());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
		    
	public short[] getSamples(String name)
    {
		return mSounds.get(name);
	}
	
	public SoundBuffer get(String name)
    {
		SoundBuffer sb = new SoundBuffer();
		try 
		{
			sb.loadFromSamples(mSounds.get(name), 1, 44100);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return sb;
	}
	
	public void clear()
	{
		mSounds.clear();
	}
		
	public HashMap<String, short[]> getmSounds() 
	{
		return mSounds;
	}




		//son simple (non 3D) sans boucle
		public void play(String id)
		{
			SoundBuffer sb = get(id);
			Sound s = new Sound(sb);
	
			//s.setVolume(100);
			s.setLoop(false);
			s.play();
		}
		
		//Son 3D
		public void play(String id, Vector3f pos)
		{
			SoundBuffer sb = get(id);
			Sound s = new Sound(sb);
			s.setPosition(pos.x, pos.z, pos.y);
			s.setLoop(false);
			s.setMinDistance(300);
			s.play();
		}
		
		//son simple (non 3D) avec boucle
		public void playWithLoop(String id)
		{
			
			SoundBuffer sb = get(id);
			Sound s = new Sound(sb);
			
			s.setLoop(true);
			s.play();
		}
}
