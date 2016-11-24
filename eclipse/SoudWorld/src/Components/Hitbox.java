package Components;

public class Hitbox 
{
	//======================================[ Attributs ]===========================================//
	
	private int radius;

	//======================================[ Constructeur ]===========================================//
	
	public Hitbox(int radius) 
	{
		this.radius = radius;
	}

	//======================================[ Fonctions ]===========================================//
	public int getRadius() 
	{
		return radius;
	}

	public void setRadius(int radius) 
	{
		this.radius = radius;
	}
}
