package States;

import org.jsfml.audio.Listener;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
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
import MenuPanel.Label;
import MenuPanel.Menu;
import MenuPanel.SpriteElement;
import Save.PlayerInfo;
import Save.SaveManager;

public class SaveSelectionState extends State
{
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private SpriteElement upArrowSprite;
	private SpriteElement downArrowSprite;
	private SpriteElement squareSprite1;
	private SpriteElement squareSprite2;
	private SpriteElement squareSprite3;
    private Label cristal1;
    private Label pieces1;
    private Label cristal2;
    private Label pieces2;
    private Label cristal3;
    private Label pieces3;
	private int currentSave;
	private Font mFont;
	private Menu menu;
	     
	//======================================[ Constructeur ]===========================================//
	    
	public SaveSelectionState(StateStack mystack, Context context)
	{
		super(mystack, context);
		menu = new Menu();
		currentSave = 1;
			
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelSelectionBackground));
		upArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.upArrow));
		downArrowSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.downArrow));
		squareSprite1 = new SpriteElement(context.texture.get(TextureManagerInterface.ID.square), 50, 200);
		squareSprite2 = new SpriteElement(context.texture.get(TextureManagerInterface.ID.square), 300, 200);
		squareSprite3 = new SpriteElement(context.texture.get(TextureManagerInterface.ID.square), 550, 200);
		
		SaveManager sam = new SaveManager(1);
		PlayerInfo pi = sam.read();
		cristal1 = new Label(String.valueOf(pi.getCristalNumber()), 20, 0, mFont, 20, Color.GREEN);
		pieces1 = new Label(String.valueOf(pi.getLevelClearedNumber()), 20, 0, mFont, 20, Color.GREEN);
		sam.setSaveNumber(2);
		pi = sam.read();
		cristal2 = new Label(String.valueOf(pi.getCristalNumber()), 20, 0, mFont, 20, Color.GREEN);
		pieces2 = new Label(String.valueOf(pi.getLevelClearedNumber()), 20, 0, mFont, 20, Color.GREEN);
		sam.setSaveNumber(3);
		pi = sam.read();
		cristal3 = new Label(String.valueOf(pi.getCristalNumber()), 20, 0, mFont, 20, Color.GREEN);
		pieces3 = new Label(String.valueOf(pi.getLevelClearedNumber()), 20, 0, mFont, 20, Color.GREEN);
		
		menu.addElement(mBackgroundSprite, 0);
		menu.addElement(downArrowSprite, 1);
		menu.addElement(upArrowSprite, 1);
		menu.addElement(squareSprite1, 1);
		menu.addElement(squareSprite2, 1);
		menu.addElement(squareSprite3, 1);
		menu.addElement(cristal1, 2);
		menu.addElement(pieces1, 2);
		menu.addElement(cristal2, 2);
		menu.addElement(pieces2, 2);
		menu.addElement(cristal3, 2);
		menu.addElement(pieces3, 2);
		
		pieces1.attach(squareSprite1);
		pieces1.setPosition(new Vector2f(50, 50));
		cristal1.attach(pieces1);
		cristal1.setPosition(new Vector2f(0, 50));
		pieces2.attach(squareSprite2);
		pieces2.setPosition(new Vector2f(50, 50));
		cristal2.attach(pieces2);
		cristal2.setPosition(new Vector2f(0, 50));
		pieces3.attach(squareSprite3);
		pieces3.setPosition(new Vector2f(50, 50));
		cristal3.attach(pieces3);
		cristal3.setPosition(new Vector2f(0, 50));
		
		setArrow();
	}

	//======================================[ Fonctions ]===========================================//

	private void setArrow()
	{
		if (currentSave == 1)
		{	
			upArrowSprite.attach(squareSprite1);
			downArrowSprite.attach(squareSprite1);
		}
		else if (currentSave == 2)
		{
			upArrowSprite.attach(squareSprite2);
			downArrowSprite.attach(squareSprite2);
		}
		else
		{
			upArrowSprite.attach(squareSprite3);
			downArrowSprite.attach(squareSprite3);
		}
		upArrowSprite.setPosition(new Vector2f(55, 220));
		downArrowSprite.setPosition(new Vector2f(55, -65));
	}
	
	@Override
	public void init()
	{
		mContext.window.setView(Context.getDefaultView());
		currentSave = 1;
		SaveManager sam = new SaveManager(1);
		PlayerInfo pi = sam.read();
		cristal1.setText("Cristals: " + String.valueOf(pi.getCristalNumber()));
		pieces1.setText("Pieces: " + String.valueOf(pi.getLevelClearedNumber()));
		sam.setSaveNumber(2);
		pi = sam.read();
		cristal2.setText("Cristals: " + String.valueOf(pi.getCristalNumber()));
		pieces2.setText("Pieces: " + String.valueOf(pi.getLevelClearedNumber()));
		sam.setSaveNumber(3);
		pi = sam.read();
		cristal3.setText("Cristals: " + String.valueOf(pi.getCristalNumber()));
		pieces3.setText("Pieces: " + String.valueOf(pi.getLevelClearedNumber()));
		
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
	    		requestStackPush(State.ID.Menu);
	    	}
		    	
		    if (event.asKeyEvent().key == Key.LEFT)
		    {
		    	if (currentSave > 1)
		    		currentSave -= 1;
		    	else
		    		currentSave = 3;
		    	setArrow();
		    }
		    if (event.asKeyEvent().key == Key.RIGHT)
		    {
		    	if (currentSave < 3)
		    		currentSave += 1;
		    	else
		    		currentSave = 1;
		    	setArrow();
		    }
		       
		    if (event.asKeyEvent().key == Key.SPACE)
		    {
		    	mContext.saveManager = new SaveManager(currentSave);
		    	mContext.playerInfo = mContext.saveManager.read();
		    	requestStackPop();
		    	requestStackPush(State.ID.LevelSelection);
		    	requestStackPush(State.ID.KinectConnect);
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
