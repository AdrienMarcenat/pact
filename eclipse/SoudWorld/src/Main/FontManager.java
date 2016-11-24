package Main;

import org.jsfml.graphics.Font;

import java.io.IOException;
import java.util.HashMap;
import GameInterfaces.FontManagerInterface;

public class FontManager implements FontManagerInterface
{
	//======================================[ Attributs ]===========================================//
	
	private HashMap<ID, Font> mFonts;
	
	//======================================[ Constructeur ]===========================================//
	
	public FontManager()
	{
		mFonts = new HashMap<ID, Font>();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void load(ID id , String filename)
	{
		Font f = new Font();
	    try 
	    {
			f.loadFromStream(getClass().getResourceAsStream(filename));
			System.out.println("Ressource load: " + filename);
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
		mFonts.put(id, f);
	}
	
	public Font get(ID id)
	{
		return mFonts.get(id);
	}
}
