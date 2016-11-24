package Components;

public class Damage 
{
	//======================================[ Attributs ]===========================================//
	
	private int damage;

	//======================================[ Constructeur ]===========================================//
	
	public Damage(int damage) 
	{
		this.damage = damage;
	}

	//======================================[ Fonctions ]===========================================//
	public int getDamage() 
	{
		return damage;
	}

	public void setDamage(int damage) 
	{
		this.damage = damage;
	}
}
