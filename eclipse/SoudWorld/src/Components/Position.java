package Components;

import org.jsfml.system.Vector3f;

public class Position 
{
	//======================================[ Attributs ]===========================================//
	
	private Vector3f pos;
	
	//======================================[ Constructeur ]===========================================//
	
	public Position(Vector3f pos)
	{
		this.pos = pos;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public Vector3f getPos() 
	{
		return pos;
	}

	public void setPos(Vector3f pos) 
	{
		this.pos = pos;
	}
}
