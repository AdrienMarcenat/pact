package MenuPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.jsfml.graphics.*;


public class Menu 
{
	//======================================[ Attributs ]===========================================//
	
	private HashMap<Integer, ArrayList<MenuElement> > elements;
	private ArrayList<Button> buttons;
	private int currentButton;
	
	//======================================[ Constructeur ]===========================================//
	
	public Menu()
	{
		elements = new HashMap<Integer, ArrayList<MenuElement> >();
		buttons = new ArrayList<Button>();
		currentButton = 0;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void addElement(MenuElement e, int layer)
	{
		ArrayList<MenuElement> le = elements.get(layer);
		if (le == null)
		{	
			le = new ArrayList<MenuElement>();
			le.add(e);
			elements.put(layer, le);
		}
		else
		{
			le.add(e);
		}
		
		e.setLayer(layer);
	}
	
	public void removeElement(MenuElement e)
	{
		for (Iterator<MenuElement> iter = elements.get(e.getLayer()).listIterator(); iter.hasNext(); )
		{
			MenuElement me = iter.next();
			if (e.equals(me))
				iter.remove();
		}
	}
	
	public void setLayer(MenuElement e, int layer)
	{
		removeElement(e);
		addElement(e, layer);
	}
	
	public void addButton(Button b)
	{
		buttons.add(b);
	}
	
	public Button getButton()
	{
		return buttons.get(currentButton);
	}
	
	public void command()
	{
		buttons.get(currentButton).command();
	}
	
	public int getCurrentButton()
	{
		return currentButton;
	}
	
	public void setCurrentButton(int n)
	{
		currentButton = n;
	}
	
	public void increaseButton()
	{
		if (currentButton < buttons.size() - 1)
			currentButton++;
		else 
			currentButton = 0;
	}
	
	public void decreaseButton()
	{
		if(currentButton > 0)
			currentButton--;
		else
			currentButton = buttons.size() -1;
	}	
	
	
	//Les couches sont dessinées dans l'ordre croissant
	public void draw(RenderWindow window)
	{
		for (int i = 0; i < elements.size(); i++)
		{
			for (MenuElement e : elements.get(i))
			{
				if(e.isVisible())
					e.draw(window);
			}
		}
	}
}
