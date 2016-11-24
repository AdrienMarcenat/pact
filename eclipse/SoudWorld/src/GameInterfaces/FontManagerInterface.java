package GameInterfaces;

import org.jsfml.graphics.Font;

public interface FontManagerInterface 
{
	public enum ID
    {
		MenuText
    };
    
	public void load(ID id , String filename);
	public Font get(ID id);
}
