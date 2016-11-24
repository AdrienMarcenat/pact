package Save;

import java.util.ArrayList;

public class PlayerInfo 
{
		//======================================[ Attributs ]===========================================//
	
		//La vie max du joueur
		private int maxLife;
		//Le nombre de niveaux terminés
		private int levelClearedNumber;
		//Le nombre de cristaux collectés
		private int cristalNumber;
		private ArrayList<LevelInfo> levelInfo;
		
		//======================================[ Constructeur ]===========================================//
		
		public PlayerInfo(int maxLife, int levelClearedNumber, int cristalNumber, ArrayList<LevelInfo> levelInfo) 
		{
			this.maxLife = maxLife;
			this.levelClearedNumber = levelClearedNumber;
			this.cristalNumber = cristalNumber;
			this.levelInfo = levelInfo;
		}

		//======================================[ Fonctions ]===========================================//
		
		public int getMaxLife() 
		{
			return maxLife;
		}

		public void setMaxLife(int maxLife) 
		{
			this.maxLife = maxLife;
		}

		public int getLevelClearedNumber() 
		{
			return levelClearedNumber;
		}

		public void setLevelClearedNumber(int levelClearedNumber) 
		{
			this.levelClearedNumber = levelClearedNumber;
		}

		public void increaseLevelClearedNumber() 
		{
			this.levelClearedNumber++;
		}
		
		public int getCristalNumber() 
		{
			return cristalNumber;
		}

		public void setCristalNumber(int cristalNumber) 
		{	
			this.cristalNumber = cristalNumber;
		}
		
		public LevelInfo getLevelInfo(int i) 
		{
			return levelInfo.get(i);
		}

		public void setLevelInfo(int hit, int level, ArrayList<Integer> newCristals) 
		{
			int cristalsCollected = 0;
			
			if (level < levelClearedNumber)
			{
				ArrayList<Integer> prevCristals = levelInfo.get(level).getCristalBooleans();
				ArrayList<Integer> diff = new ArrayList<Integer>();
				
				for (int j = 0; j < newCristals.size(); j++)
				{
					if (prevCristals.get(j) > 0 || newCristals.get(j) > 0)
					{
						diff.add(1);
						cristalsCollected++;
					}
					else
						diff.add(0);
				}
				
				levelInfo.set(level, new LevelInfo(cristalsCollected, hit, diff));
			}
			else
			{
				for (int j = 0; j < newCristals.size(); j++)
				{
					if (newCristals.get(j) > 0)
					{
						cristalsCollected++;
					}
				}
				
				levelInfo.add(new LevelInfo(cristalsCollected, hit, newCristals));
			}
		}

		public ArrayList<LevelInfo> getLevelInfo() 
		{	
			return levelInfo;
		}

		
}
