package SpatializeSound;

import java.util.ArrayList;

import org.jsfml.audio.SoundSource.Status;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;
import Components.Interpolation;
import Components.Point;
import Main.SoundManager;

public class HRTFManager
{
	//======================================[ Attributs ]===========================================//
	
	private SoundManager sm;
	private SoundManager hrtf;
	private static float frequency = 44100;
	private static int samplesPerFrame = (int) frequency/60;
	
	//======================================[ Constructeur ]===========================================//
	
	public HRTFManager(SoundManager sm, SoundManager hrtf) 
    {
		this.sm = sm;
		this.hrtf = hrtf;
	}

	//======================================[ Fonctions ]===========================================//
	
    public short[] spatialize(short[] sound, String left_hrtf, String right_hrtf)
	{
    	short[] left = Convolution.convolution(sound, hrtf.getSamples(left_hrtf));
    	short[] right = Convolution.convolution(sound, hrtf.getSamples(right_hrtf));
    	short[] res = new short[left.length + right.length];
    	for (int i = 0; i < left.length; i++)
    	{
    		res[2*i + 1] = left[i]; 
    		res[2*i] = right[i];
    	}
    	
    	return res;
   	}
    
    public void move(String name, ArrayList<Point> trajectory)
    {	
    	short[] sound = sm.getSamples(name);
    	int smooth = 20*samplesPerFrame;
    	short[] s = new short[smooth];
    	int n = sound.length;
    	int maxDist = (int) Math.sqrt(300*300 + 400*400);
    	AudioPlayer3D audio = new AudioPlayer3D((int) frequency);
    	int currentPoint = 0;    
    	Vector3f dep = new Vector3f(trajectory.get(0).x, trajectory.get(0).y, 0);
    	Vector3f end = new Vector3f(trajectory.get(1).x, trajectory.get(1).y, 0);
    	Vector3f pos = new Vector3f(dep.x, dep.y, dep.z);
    	
    	Time dt = Time.ZERO;
		Clock clock = new Clock();
		int j = 0;
		
		while(currentPoint < trajectory.size() )
	    {
	    	Vector3f direction = Interpolation.unit(new Vector3f( -dep.x + end.x, -dep.y + end.y, -dep.z + end.z));
			Vector3f v = Vector3f.mul(direction, 0.05f);
			
			if (direction.x > 0 && pos.x > end.x
	    			|| direction.x < 0 && pos.x < end.x
	    			|| direction.y < 0 && pos.y <= end.y
	    			|| direction.y < 0 && pos.y <= end.y)
    		{
    			if (currentPoint < trajectory.size()-1)
    			{
    				dep = new Vector3f(trajectory.get(currentPoint).x, trajectory.get(currentPoint).y, 0);
    				currentPoint++;
    				end = new Vector3f(trajectory.get(currentPoint).x, trajectory.get(currentPoint).y, 0);
    			}
    			else
    				currentPoint++;
    		}
    	
    		if (audio.getRewrite())
	    	{
    			for (int i = 0; i < smooth; i ++)
		    	{
		    		s[i] = sound[(i + j*smooth)%n];
		    	}
		    	Vector3f angles = Interpolation.computeThetaPhi(400 - pos.x,300 - pos.y, 0);
		    	int thetaL = (int) angles.x;
		    	int thetaR = (int) angles.y;
		    	
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
								
				short[] spatial = spatialize(s, "L0_" + tL, "R0_" + tR);
				audio.setChunk(spatial, false);
				j++;
	    	}
    		
    		dt = clock.getElapsedTime();
    		if (dt.asSeconds() > 1/60.f)
    		{
    			pos = new Vector3f(pos.x + v.x, pos.y + v.y, pos.z + v.z);
    			clock.restart();
    			float dist = (float) Math.sqrt((400-pos.x)*(400-pos.x) + (300-pos.y)*(300-pos.y));
				audio.setVolume(100*(maxDist - dist)*(maxDist - dist)/(maxDist*maxDist));
    		}
			
			if (audio.getStatus() == Status.STOPPED)
			{
				audio.play();
			}
			
	    }
    	audio.stop();
    }
    
    //Retourne la durée du son en seconde 
    public float getLenght(short[] sound, float frequency)
    {
    	return (sound.length)/frequency;
    }
   
}
