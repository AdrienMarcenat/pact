package States;


import org.jsfml.audio.Listener;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import Main.*;
import MenuPanel.Button;
import MenuPanel.Label;
import MenuPanel.Menu;
import MenuPanel.SpriteElement;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.TextureManagerInterface;

public class GameOverState extends State
{ 
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private SpriteElement leftArrowSprite;
	private SpriteElement rightArrowSprite;
	private Font mFont;
    private Button retryButton;
    private Button quitButton;
    private Label gameOver;
    private Menu menu;
     
    //======================================[ Constructeur ]===========================================//
    
	public GameOverState(StateStack mystack, Context context)
	{
		super(mystack, context);
		menu = new Menu();
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelSelectionBackground));
		leftArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.leftArrow));
		rightArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.rightArrow));
		
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
		Color green = Color.GREEN;
		int charSize = 40;
		
		//Définition des boutons et de leur callBack
		retryButton = new Button("Retry", 300, 250, mFont, charSize, green) {
		@Override
			public void command()
			{
				mContext.levelReader.init("/level-" + Integer.toString(mContext.currentLevel) + ".txt");
				requestStackPop();
				requestStackPush(State.ID.Game);
			}
		};
		
		quitButton = new Button("Quit", 300, 350, mFont, charSize, green) {
		@Override
			public void command()
			{
				requestStackPop();
				requestStackPush(State.ID.Menu);
			}
		};
				
	 	gameOver = new Label("Game Over", 120, 100, mFont, 80, green);
	 	
	 	menu.addElement(mBackgroundSprite, 0);
	 	menu.addElement(retryButton, 1);
	 	menu.addElement(quitButton, 1);
	 	menu.addElement(gameOver, 1);
	 	menu.addElement(leftArrowSprite, 1);
	 	menu.addElement(rightArrowSprite, 1);
	 	
	 	menu.addButton(retryButton);
	 	menu.addButton(quitButton);
	 	
	 	quitButton.attach(retryButton);
	 	quitButton.setPosition(new Vector2f(0, 100));
	 	
	 	retryButton.attach(gameOver);
		retryButton.setPosition(new Vector2f(180, 150));
	 	
		setArrow();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void setArrow()
	{
		rightArrowSprite.attach(menu.getButton());
		leftArrowSprite.attach(menu.getButton());
		
		int l = menu.getButton().getNameLenght();
		rightArrowSprite.setPosition( new Vector2f(- 60,  - 20));
		leftArrowSprite.setPosition( new Vector2f(32*l + 10, - 20));
	}
	
	@Override
	public void init()
	{
		mContext.window.setView(Context.getDefaultView());
		menu.setCurrentButton(0);
		setArrow();
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
	    	
	    	if (event.asKeyEvent().key == Key.DOWN)
	    	{
	    		menu.increaseButton();
	    		setArrow();
	    	}
	    	if (event.asKeyEvent().key == Key.UP)
	    	{
	    		menu.decreaseButton();
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
	    return true;
	}

	@Override
	public void render()
	{
	    menu.draw(mContext.window);
	}

}

