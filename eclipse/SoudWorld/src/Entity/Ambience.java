package Entity;

import java.util.ArrayList;
import org.jsfml.system.Vector3f;
import Components.SoundPlayer;


public class Ambience 
{
	private ArrayList<SoundPlayer> sounds;
	private ArrayList<Vector3f> positions;
	private boolean isPlaying;
	
	public Ambience(ArrayList<SoundPlayer> sounds, ArrayList<Vector3f> positions)
	{
		this.sounds = sounds;
		this.positions = positions;
		this.isPlaying = false;
	}
	
	public void update(Vector3f newPlayerPos)
	{
		for(int i = 0; i < sounds.size(); i++)
		{
			this.sounds.get(i).update(positions.get(i), newPlayerPos);
		}
		
		isPlaying = true;
	}
	
	public void stop()
	{
		for(int i = 0; i < sounds.size(); i++)
		{
			this.sounds.get(i).stop();
		}
		
		isPlaying = false;
	}
	
	public boolean isPlaying()
	{
		return isPlaying;
	}
}
