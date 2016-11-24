package Save;

import java.util.ArrayList;

public class LevelInfo 
{
		//======================================[ Attributs ]===========================================//
	
			private int cristalsCollected;
			private int hitNumber;
			private ArrayList<Integer> cristalBooleans;
			
			//======================================[ Constructeur ]===========================================//
			
			public LevelInfo(int cristalsCollected, int hitNumber, ArrayList<Integer> cristalBooleans) 
			{	
				this.cristalsCollected = cristalsCollected;
				this.hitNumber = hitNumber;
				this.cristalBooleans = cristalBooleans;
			}
		
			//======================================[ Fonctions ]===========================================//
			
			public int getCristalsCollected() 
			{
				return cristalsCollected;
			}

			public void setCristalsCollected(int cristalsCollected) 
			{
				this.cristalsCollected = cristalsCollected;
			}

			public int getHitNumber() 
			{
				return hitNumber;
			}

			public void setHitNumber(int hitNumber) 
			{
				this.hitNumber = hitNumber;
			}

			public ArrayList<Integer> getCristalBooleans() 
			{
				return cristalBooleans;
			}

			public void setCristalBooleans(int i, int bool) 
			{
				this.cristalBooleans.set(i, bool);
			}

}
