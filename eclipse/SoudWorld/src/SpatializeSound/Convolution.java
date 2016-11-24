package SpatializeSound;


import java.util.ArrayList;
import org.apache.commons.math3.complex.Complex;


public class Convolution 
{
	private static int two = (int) Math.pow(2, Math.floor(Math.log(512)/Math.log(2)));
			
	public static short[] convolution(short[] samples, short[] hrtf)
	{	
		ArrayList<Complex[]> sam = new ArrayList<Complex[]>();
		Complex[] chrtf = new Complex[two];
		boolean b = true;
		int j = 0;
		
		while(b)
		{
			Complex[] csamples = new Complex[two];
			for (int i = 0; i < two; i++)
			{
				if (j+i < samples.length)
					csamples[i] = new Complex(samples[j + i]);
				else
				{
					csamples[i] = new Complex(0);
					b = false;
				}
			}
			sam.add(csamples);
			j += two;
		}
		
		for (int i = 0; i < two; i++)
		{
			if (i < hrtf.length)
				chrtf[i] = new Complex(hrtf[i]);
			else
				chrtf[i] = new Complex(0);
		}
		
	    Complex[] csound = new Complex[two*(sam.size()+1)];
	    j = 0;
	    for (int i = 0; i < sam.size(); i++)
	    {
	    	Complex [] cc = FFT.convolve(sam.get(i), chrtf);
	    	for (int r = 0; r < cc.length; r++)
	    	{
	    		if (csound[j + r] != null)
	    			csound[j + r] = csound[j + r].add(cc[r]);
	    		else
	    			csound[j + r] = cc[r];
	    	}
	    	j += two;
	    }
		
	    
		double n = csound[0].getReal();
		for (int i = 0; i < csound.length; i++)
		{
			if (csound[i].getReal() > n)
				n = csound[i].getReal();
		}
		short[] sound = new short[csound.length];
		for (int i= 0; i < csound.length; i++)
		{
			sound[i] = (short) (32767*csound[i].getReal()/n);
		}
		
		return sound;
		
		/*double[] dsamples = new double[samples.length];
		double[] dhrtf = new double[hrtf.length];
		for (int i= 0; i < samples.length; i++)
		{
			dsamples[i] = (double) samples[i];
		}
		for (int i= 0; i < hrtf.length; i++)
		{
			dhrtf[i] = (double) hrtf[i];
		}
		double[] sound = MathArrays.convolve(dsamples, dhrtf);
		short[] dsound = new short[sound.length];
		double n = sound[0];
		for (int i= 0; i < dsound.length; i++)
		{
			if (sound[i] > n)
				n = sound[i];
		}
		for (int i= 0; i < dsound.length; i++)
		{
			dsound[i] = (short) (32767*sound[i]/n);
		}
		SoundBuffer res = new SoundBuffer();
		try 
		{
			res.loadFromSamples(dsound, 1, 44100);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		Sound sSound = new Sound(res);
		sSound.play();*/
	}
}
