package States;

import org.jsfml.audio.Listener;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import GameInterfaces.FontManagerInterface;
import GameInterfaces.TextureManagerInterface;
import Main.Context;
import Main.State;
import Main.StateStack;
import MenuPanel.Button;
import MenuPanel.Menu;
import MenuPanel.SpriteElement;

public class MenuState extends State
{
	//======================================[ Attributs ]===========================================//
	
	private Font mFont;
    private SpriteElement mBackgroundSprite;
    private SpriteElement leftArrowSprite;
	private SpriteElement rightArrowSprite;
	private SpriteElement titleSprite;
	private Button newGameButton;
	private Button loadGameButton;
	private Button galleryButton;
	private Button quitButton;
	private Menu menu;
     
	//======================================[ Constructeur ]===========================================//
	
	public MenuState(StateStack mystack, Context context)
	{
		super(mystack, context);
		menu = new Menu();
		
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.MenuBackground), 400, 300);
		mBackgroundSprite.getSprite().setOrigin(960, 540);
		titleSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.Title), 150, 50);
		leftArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.leftArrow));
		rightArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.rightArrow));
		
		//Caractéristiques du texte: couleur et taille
		Color blue = Color.GREEN;
		int charSize = 40;
		
		//Définition des boutons et de leur callBack
		newGameButton = new Button("New Game", 100, 250, mFont, charSize, blue) {
			@Override
			public void command()
			{
				animation();
				requestStackPop();
				requestStackPush(State.ID.NewSave);
			}
		};
		
		loadGameButton = new Button("Load Game", 150, 320, mFont, charSize, blue) {
			@Override
			public void command()
			{
				animation();
				requestStackPop();
				requestStackPush(State.ID.Save);
			}
		};
		
		galleryButton = new Button("Sound Gallery", 200, 390, mFont, charSize, blue) {
			@Override
			public void command()
			{
				
			}
		};
		
		quitButton = new Button("Quit", 250, 460, mFont, charSize, blue) {
			@Override
			public void command()
			{
				requestStackPop();
			}
		};
	 	
		menu.addButton(newGameButton);
		menu.addButton(loadGameButton);
		menu.addButton(galleryButton);
		menu.addButton(quitButton);
		
		menu.addElement(galleryButton, 1);
		menu.addElement(leftArrowSprite, 1);
		menu.addElement(loadGameButton, 1);
		menu.addElement(mBackgroundSprite, 0);
		menu.addElement(newGameButton, 1);
		menu.addElement(rightArrowSprite, 1);
		menu.addElement(titleSprite, 1);
		menu.addElement(quitButton, 1);
		
	 	loadGameButton.attach(newGameButton);
	 	loadGameButton.setPosition(new Vector2f(50, 80));
	 	quitButton.attach(galleryButton);
	 	quitButton.setPosition(new Vector2f(50, 80));
	 	galleryButton.attach(loadGameButton);
	 	galleryButton.setPosition(new Vector2f(50, 80));
	 	
		setArrow();
	}

	//======================================[ Fonctions ]===========================================//
	
	//Animation lorsqu'on choisit New Game ou Load Game, effet de tunnel
	private void animation()
	{
		for(int a = 0; a < 500; a += 5)
		{
			mContext.rotation++;
			mBackgroundSprite.getSprite().setRotation(mContext.rotation);;
			
			View view = new View();
			view.setSize(800 - a, 600 - a);
			view.setCenter(400, 300);
			
			mContext.window.clear();
			mContext.window.setView(view);
		    mContext.window.draw(mBackgroundSprite.getSprite());
		    mContext.window.display();
		}
	}
	
	public void setArrow()
	{
		rightArrowSprite.attach(menu.getButton());
	 	leftArrowSprite.attach(menu.getButton());
	 	
	 	int l = menu.getButton().getNameLenght();
		rightArrowSprite.setPosition(new Vector2f(- 60,  - 10));
		leftArrowSprite.setPosition(new Vector2f( 32*l + 15, - 10));
	}
	
	@Override
	public void init()
	{
		menu.setCurrentButton(0);
		setArrow();
		mContext.window.setView(Context.getDefaultView());
	}
	
	@Override
	public boolean handleEvent(Event event)
	{
		if (event.type == Type.KEY_PRESSED)
	    {
			mContext.sound.play("Bip", Listener.getPosition());
	    	if (event.asKeyEvent().key == Key.ESCAPE)
	    	{
	    		requestStateClear();
	    		requestStackPush(State.ID.Title);
	    	}
	    	
	    	if (event.asKeyEvent().key == Key.UP)
	    	{
	    		menu.decreaseButton();
	    		setArrow();
	    	}
	    	
	    	if (event.asKeyEvent().key == Key.DOWN)
	    	{
	    		menu.increaseButton();
	    		setArrow();
	    	}
	    	
	    	if (event.asKeyEvent().key == Key.SPACE)
	    	{
	    		menu.command();
	    	}
	    }
		
	    if (event.type == Type.CLOSED)
	    	requestStateClear();
	    return true;
	}
	
	@Override
	public boolean update(Time dt)
	{
		mContext.rotation += 0.5;
		mBackgroundSprite.getSprite().setRotation(mContext.rotation);
	    return true;
	}

	@Override
	public void render()
	{
	    menu.draw(mContext.window);
	}
}
