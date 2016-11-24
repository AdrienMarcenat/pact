package GameInterfaces;

import org.jsfml.audio.SoundBuffer;
import org.jsfml.system.Vector3f;


public interface SoundManagerInterface 
{   
	public void load(String filename);
	public short[] getSamples(String name);
	public SoundBuffer get(String name);
	public void play(String id);
	public void play(String id, Vector3f pos);
	public void playWithLoop(String id);
}
