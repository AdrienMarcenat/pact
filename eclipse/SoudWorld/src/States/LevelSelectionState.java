package States;

import java.util.ArrayList;

import org.jsfml.audio.Listener;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;
import GameInterfaces.TextureManagerInterface;
import Main.*;
import MenuPanel.Menu;
import MenuPanel.SpriteElement;

public class LevelSelectionState extends State
{
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private ArrayList<SpriteElement> levelSprites;
	private SpriteElement leftArrowSprite;
	private SpriteElement rightArrowSprite;
    private int currentLevel;
    private Menu menu;
     
    //======================================[ Constructeur ]===========================================//
    
	public LevelSelectionState(StateStack mystack, Context context)
	{
		super(mystack, context);
		menu = new Menu();
		currentLevel = 0;
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelSelectionBackground));
		leftArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.leftArrow), 190, 250);
		leftArrowSprite.setVisible(false);
		rightArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.rightArrow), 570, 250);
		
		levelSprites = new ArrayList<SpriteElement>();
		levelSprites.add( new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelZero), 240, 140));
		levelSprites.add( new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelOne), 240, 140));
		levelSprites.add( new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelTwo), 240, 140));
		levelSprites.add( new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelThree), 240, 140));	
		
		menu.addElement(mBackgroundSprite, 0);
		menu.addElement(rightArrowSprite, 1);
		menu.addElement(leftArrowSprite, 1);
		menu.addElement(levelSprites.get(currentLevel), 1);
	}

	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mContext.window.setView(Context.getDefaultView());
		menu.removeElement(levelSprites.get(currentLevel));
		currentLevel = mContext.currentLevel;
		menu.addElement(levelSprites.get(currentLevel), 1);
		
		if (currentLevel == levelSprites.size() -1)
			rightArrowSprite.setVisible(false);
		else if (currentLevel == 0)
			leftArrowSprite.setVisible(false);
		else
		{
			leftArrowSprite.setVisible(true);
			rightArrowSprite.setVisible(true);
		}
	}
	
	@Override
	public boolean handleEvent(Event event)
	{
	    if (event.type == Type.KEY_PRESSED)
	    {
	    	mContext.sound.play("Bip", Listener.getPosition());
	    	if (event.asKeyEvent().key == Key.ESCAPE)
	    	{
	    		//Sauvegarde automatique
	    		mContext.saveManager.save(mContext.playerInfo);
	    		
	    		requestStateClear();
	    		requestStackPush(State.ID.Title);
	    	}
	    	
	    	if (event.asKeyEvent().key == Key.LEFT)
	    	{
	    		menu.removeElement(levelSprites.get(currentLevel));
	    		if (currentLevel > 0)
	    			currentLevel -= 1;
	    		menu.addElement(levelSprites.get(currentLevel), 1);
	    	}
	    	if (event.asKeyEvent().key == Key.RIGHT)
	    	{
	    		menu.removeElement(levelSprites.get(currentLevel));
	    		if (currentLevel < 3)
	    			currentLevel += 1;
	    		menu.addElement(levelSprites.get(currentLevel), 1);
	    	}
	        
	    	if (event.asKeyEvent().key == Key.SPACE)
	    	{
	    		mContext.currentLevel = currentLevel;
	    		mContext.levelReader.init("/level-" + Integer.toString(currentLevel) + ".txt");
	    		//mContext.levelReader.init("/Demo.txt");
	    		requestStackPop();
	    		requestStackPush(State.ID.Test);
	    	}
	    	
	    	if (currentLevel == levelSprites.size() -1)
    			rightArrowSprite.setVisible(false);
    		else if (currentLevel == 0)
    			leftArrowSprite.setVisible(false);
    		else
    		{
    			leftArrowSprite.setVisible(true);
    			rightArrowSprite.setVisible(true);
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
