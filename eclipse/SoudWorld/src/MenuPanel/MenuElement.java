package MenuPanel;

import java.util.ArrayList;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public abstract class MenuElement 
{
	//======================================[ Attributs ]===========================================//
	
	private Drawable element;
	private MenuElement father = null;
	protected ArrayList<MenuElement> sons;
	private int layer;
	private Vector2f pos;
	private boolean visible;
	
	//======================================[ Constructeur ]===========================================//

	public MenuElement(int x , int y)
	{
		this.layer = 0;
		this.pos = new Vector2f(x, y);
		visible = true;
		sons = new ArrayList<MenuElement>();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void draw(RenderWindow window)
	{
		window.draw(element);	
	}
	
	public abstract void setPosition(Vector2f pos);
	
	public void attach(MenuElement father)
	{
		this.father = father;
		father.addSon(this);
		setPosition(this.pos);	
	}
	
	public void addSon(MenuElement e)
	{
		sons.add(e);
	}
	
	public void removeSon()
	{
		sons.remove(this);
	}
	
	public void detach()
	{
		father.removeSon();
		father = null;
	}

	public int getLayer() 
	{
		return layer;
	}

	public void setLayer(int layer) 
	{
		this.layer = layer;
	}
	
	public Drawable getElement() 
	{
		return element;
	}

	public void setElement(Drawable element) 
	{
		this.element = element;
	}
	
	public Vector2f getPos() 
	{
		return pos;
	}
	
	public Vector2f getAbsolutePos()
	{
		if (father != null)
			return (Vector2f.add(pos, father.getAbsolutePos()));
		
		return pos;
	}

	public void setPos(Vector2f pos) 
	{
		this.pos = pos;
	}
	
	public boolean isVisible() 
	{
		return visible;
	}

	public void setVisible(boolean visible) 
	{
		this.visible = visible;
	}
	
	@Override
	public boolean equals(Object e)
	{
		MenuElement me = (MenuElement) e;
		
		if (father != null)
			return (element.equals(me.getElement()) 
					&& layer == me.getLayer() 
					&& father.equals(me) 
					&& pos.equals(me.getPos()));
		else
			return (element.equals(me.getElement()) 
					&& layer == me.getLayer()  
					&& pos.equals(me.getPos()));
	}
}
