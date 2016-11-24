package Main;

import java.io.IOException;
import java.util.HashMap;
import org.jsfml.graphics.Texture;

import GameInterfaces.TextureManagerInterface;

public class TextureManager implements TextureManagerInterface
{
	//======================================[ Attributs ]===========================================//
	
	private HashMap<ID, Texture> mTextures;
	
	//======================================[ Constructeur ]===========================================//
	
	public TextureManager()
	{
		mTextures = new HashMap<ID, Texture>();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void load(ID id , String filename)
	{
		Texture t = new Texture();
	    try 
	    {
	    	t.loadFromStream(getClass().getResourceAsStream(filename));
	    	System.out.println("Ressource load: " + filename);
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
		mTextures.put(id, t);
	}
	
	public Texture get(ID id)
	{
		return mTextures.get(id);
	}
}
