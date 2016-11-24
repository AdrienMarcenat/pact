package MenuPanel;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

public abstract class Button extends MenuElement
{
	//======================================[ Attributs ]===========================================//
	
	private Text name;
	public abstract void command();
	
	//======================================[ Constructeur ]===========================================//
	
	public Button(String name, int x, int y, Font f, int charSize, Color color)
	{
		super(x, y);
		
		this.name = new Text();
	 	this.name.setString(name);
	 	this.name.setPosition(x, y);
	 	this.name.setFont(f);
	 	this.name.setCharacterSize(charSize);
	 	this.name.setColor(color);
	 	
	 	this.setElement(this.name);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public Text getName()
	{
		return name;
	}
	
	public int getNameLenght()
	{
		return name.getString().length();
	}
	
	@Override
	public void setPosition(Vector2f pos)
	{
		setPos(pos);
		name.setPosition(getAbsolutePos());
		for (MenuElement son: sons)
		{
			son.setPosition(son.getPos());
		}	
	}
}
