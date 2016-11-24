package Components;

import java.io.IOException;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource.Status;
import org.jsfml.system.Vector3f;
import Main.SoundManager;
import SpatializeSound.AudioPlayer3D;
import SpatializeSound.HRTFManager;

public class SoundPlayer 
{
	//======================================[ Attributs ]===========================================//
	
	private HRTFManager hm;
	private short[] sound;
	private short[] s;
	private int n;
	private AudioPlayer3D audio;
	private static int maxDist = (int) Math.sqrt(300*300 + 400*400);
	private static float frequency = 44100;
	private static int samplesPerFrame = (int) frequency/60;
	private static int smooth = 20*samplesPerFrame;
	int j = 0;
	
	//======================================[ Constructeur ]===========================================//
	
	public SoundPlayer(String id, SoundManager sm, HRTFManager hm)
	{
		this.hm = hm;
		sound = sm.getSamples(id);
    	s = new short[smooth];
    	n = sound.length;
    	audio = new AudioPlayer3D((int) frequency);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void update(Vector3f pos, Vector3f ppos)
	{
		if (audio.getRewrite())
    	{
			for (int i = 0; i < smooth; i ++)
	    	{
	    		s[i] = sound[(i + j*smooth)%n];
	    	}
	    	Vector3f angles = Interpolation.computeThetaPhi(ppos.x - pos.x,ppos.y - pos.y, pos.z - pos.z);
	    	int thetaL = (int) angles.x;
	    	int thetaR = (int) angles.y;
	    	int phi = (int) angles.z;
	    	
	    	String tL;
			if (thetaL < 10)
				tL = "00" + thetaL;
			else if (thetaL < 100)
				tL = "0" + thetaL;
			else
				tL = String.valueOf(thetaL);
	
			String tR;
			if (thetaR < 10)
				tR = "00" + thetaR;
			else if (thetaR < 100)
				tR = "0" + thetaR;
			else
				tR = String.valueOf(thetaR);
							
			//short[] spatial = hm.spatialize(s, "L" + phi + "_" + tL, "R" + phi + "_" + tR);
			short[] spatial = hm.spatialize(s, "L0_" + tL, "R0_" + tR);
			audio.setChunk(spatial, false);
			j++;
			
			float dist = Interpolation.getDistance(pos, ppos);
			audio.setVolume(100*(maxDist - dist)*(maxDist - dist)/(maxDist*maxDist));
    	}
		
		if (audio.getStatus() == Status.STOPPED)
		{
			audio.play();
		}
	}
	
	public void play()
	{
		audio.play();
	}
	
	public void pause()
	{
		audio.pause();
	}
	
	public void stop()
	{
		audio.stop();
	}
	
	public Status getStatus()
	{
		return audio.getStatus();
	}
	
	//Joue un son non 3D
	public void normalPlay(boolean loop)
	{
		SoundBuffer sb = new SoundBuffer();
		try 
		{
			sb.loadFromSamples(sound, 1, (int) frequency);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		Sound nSound = new Sound(sb);
		nSound.setLoop(loop);
		nSound.play();
	}
	
}
