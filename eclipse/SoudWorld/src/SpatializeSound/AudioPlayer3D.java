package SpatializeSound;

import org.jsfml.audio.SoundStream;
import org.jsfml.system.Time;

public class AudioPlayer3D extends SoundStream
{
	//======================================[ Attributs ]===========================================//

	private Chunk chunk;
	private boolean rewrite = true;
	
	//======================================[ Constructeur ]===========================================//

	public AudioPlayer3D(int frequency)
	{
		this.initialize(2, frequency);
	}
	
	//======================================[ Fonctions ]===========================================//

	public Chunk getChunk() 
	{
		return chunk;
	}
	
	public boolean getRewrite()
	{
		return rewrite;
	}

	public void setChunk(short[] samples, boolean end) 
	{
		chunk = new Chunk(samples, end);
		rewrite = false;
	}
	
	@Override
	protected Chunk onGetData() 
	{	
		if(!rewrite)
		{
			rewrite = true;
			return chunk;
		}
		else
		{
			return null;
		}
	}

	@Override
	protected void onSeek(Time dt) 
	{
		
	}

}
