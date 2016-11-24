package MenuPanel;

import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;


public class SpriteElement extends MenuElement
{
	//======================================[ Attributs ]===========================================//
	
	private Sprite sprite;
		
	//======================================[ Constructeur ]===========================================//
		
	public SpriteElement(Texture t, int x, int y)
	{
		super(x, y);
			
		this.sprite = new Sprite();
	 	this.sprite.setTexture(t);
	 	this.sprite.setPosition(x, y);
	 	
		setElement(this.sprite);
	}
	
	public SpriteElement(Texture t)
	{
		super(0, 0);
			
		this.sprite = new Sprite();
	 	this.sprite.setTexture(t);
	 	
	 	setElement(this.sprite);
	}
		
	//======================================[ Fonctions ]===========================================//
	public Sprite getSprite()
	{
		return sprite;
	}

	@Override
	public void setPosition(Vector2f pos) 
	{
		setPos(pos);
		sprite.setPosition(getAbsolutePos());
		for (MenuElement son: sons)
		{
			son.setPosition(son.getPos());
		}
	}
}
