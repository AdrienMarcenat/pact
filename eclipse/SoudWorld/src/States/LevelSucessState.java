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

public class LevelSucessState extends State
{ 
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private SpriteElement leftArrowSprite;
	private SpriteElement rightArrowSprite;
	private SpriteElement hitSprite;
	private SpriteElement cristalSprite;
	private Font mFont;
    private Button nextLevelButton;
    private Button quitButton;
    private Label levelSucess;
    private Label hitNumber;
    private Label xp;
    private Label cristal;
    private Menu menu;
     
    //======================================[ Constructeur ]===========================================//
    
	public LevelSucessState(StateStack mystack, Context context)
	{
		super(mystack, context);
		menu = new Menu();
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelSelectionBackground));
		leftArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.leftArrow));
		rightArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.rightArrow));
		hitSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.HitBlue));
		cristalSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.Cristal));
		
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
		
		Color green = Color.GREEN;
		int charSize = 35;
		
		//Définition des boutons et de leur callBack
		nextLevelButton = new Button("Next Level", 260, 400, mFont, charSize, green) {
		@Override
			public void command()
			{
				mContext.levelReader.init("/level-" + Integer.toString(mContext.currentLevel + 1) + ".txt");
				mContext.currentLevel++;
				requestStackPop();
				requestStackPush(State.ID.Game);
			}
		};
		
		quitButton = new Button("return to level selection", 80, 500, mFont, charSize, green) {
		@Override
			public void command()
			{
				requestStackPop();
				requestStackPush(State.ID.LevelSelection);
			}
		};
	 	
		levelSucess = new Label("Level Completed", 60, 0, mFont, 60, green);
	 	hitNumber = new Label("", 300, 130, mFont, charSize, green);
	 	xp = new Label("", 350, 130, mFont, charSize, green);
	 	cristal = new Label("", 300, 260, mFont, charSize, green);
	 	hitNumber.setText(String.valueOf(mContext.player.getHitNumber()));
		xp.setText(" -> " + String.valueOf(10*(4-mContext.player.getHitNumber())) + " XP");
		cristal.setText(String.valueOf(0));
	 	
		menu.addButton(nextLevelButton);
		menu.addButton(quitButton);
		
		menu.addElement(mBackgroundSprite, 0);
		menu.addElement(hitNumber, 1);
		menu.addElement(hitSprite, 1);
		menu.addElement(leftArrowSprite, 1);
		menu.addElement(levelSucess, 1);
		menu.addElement(nextLevelButton, 1);
		menu.addElement(rightArrowSprite, 1);
		menu.addElement(quitButton, 1);
		menu.addElement(xp, 1);
	 	menu.addElement(cristalSprite, 1);
	 	
	 	hitSprite.attach(levelSucess);
	 	hitSprite.setPosition(new Vector2f(100,100));
	 	hitNumber.attach(hitSprite);
	 	hitNumber.setPosition(new Vector2f(150,30));
	 	xp.attach(hitNumber);
	 	xp.setPosition(new Vector2f(30,0));
	 	cristalSprite.attach(hitSprite);
	 	cristalSprite.setPosition(new Vector2f(0,150));
	 	cristal.attach(cristalSprite);
	 	cristal.setPosition(new Vector2f(150,30));
	 	quitButton.attach(nextLevelButton);
	 	quitButton.setPosition(new Vector2f(-180, 100));
	 	nextLevelButton.attach(cristal);
	 	nextLevelButton.setPosition(new Vector2f(-50,100));
	 	
	 	setArrow();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public void setArrow()
	{
		rightArrowSprite.attach(menu.getButton());
	 	leftArrowSprite.attach(menu.getButton());
	 	
	 	int l = menu.getButton().getNameLenght();
		rightArrowSprite.setPosition(new Vector2f(- 60,  - 20));
		leftArrowSprite.setPosition(new Vector2f( 26*l + 15, - 20));
	}
	
	@Override
	public void init()
	{
		menu.setCurrentButton(0);
		setArrow();
		mContext.window.setView(Context.getDefaultView());
		
		//*********************On met à jour les info joueur******************************//
		
		//Les cristaux récupérés
		mContext.playerInfo.setLevelInfo(mContext.currentLevel, mContext.player.getHitNumber(), mContext.cristals);
		cristal.setText(String.valueOf(mContext.playerInfo.getLevelInfo(mContext.currentLevel).getCristalsCollected()));
		
		//Les coup pris
		hitNumber.setText(String.valueOf(mContext.player.getHitNumber()));
		
		//Le nombre de niveaux complétés
		if (mContext.currentLevel == mContext.playerInfo.getLevelClearedNumber())
			mContext.playerInfo.increaseLevelClearedNumber();
		
		//Sauvegarde automatique
				mContext.saveManager.save(mContext.playerInfo);		
				
		mContext.cristals.clear();
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

