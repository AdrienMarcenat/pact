package Main;

import org.jsfml.system.Vector3f;

import GameInterfaces.OrientationCaptorInterface;

public class OrientationCaptor implements OrientationCaptorInterface
{
	//======================================[ Attributs ]===========================================//
	
	private float origin;
	
	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void setOrigin(float origin) 
	{
		this.origin = origin;
	}

	@Override
	public Vector3f getOrientation() 
	{
		//TO DO
		return (new Vector3f(0, 0, -1)); //Looking toward the negative Y axis
	}

}
