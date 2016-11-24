package MenuPanel;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class Label extends MenuElement
{
	//======================================[ Attributs ]===========================================//
	
	private Text text;
	
	//======================================[ Constructeur ]===========================================//
	
	public Label(String text, int x, int y, Font f, int charSize, Color color)
	{
		super(x, y);
		
		this.text = new Text();
	 	this.text.setString(text);
	 	this.text.setPosition(x, y);
	 	this.text.setFont(f);
	 	this.text.setCharacterSize(charSize);
	 	this.text.setColor(color);
	 	
	 	setElement(this.text);
	}
	
	//======================================[ Fonctions ]===========================================//
	public Text getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text.setString(text);
	}
	
	public int getTextLenght()
	{
		return text.getString().length();
	}

	@Override
	public void setPosition(Vector2f pos) 
	{
		setPos(pos);
		text.setPosition(getAbsolutePos());
		for (MenuElement son: sons)
		{
			son.setPosition(son.getPos());
		}
	}
}
