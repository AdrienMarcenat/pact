package Components;

public class Life 
{
	//======================================[ Attributs ]===========================================//
	
	private int life;
	
	//======================================[ Constructeur ]===========================================//
	
	public Life(int life) 
	{
		this.life = life;
	}
	
	//======================================[ Fonctions ]===========================================//
	public int getLife() 
	{
		return life;
	}

	public void setLife(int life) 
	{
		this.life = life;
	}
	
	public void addToLife(int amount) 
	{
		life += amount;
	}
}
