package States;

import java.util.ArrayList;

import org.jsfml.audio.Listener;
import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import Components.Interpolation;
import Entity.Monster;
import Exception.KinectDisconnectedException;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.SoundManagerInterface;
import GameInterfaces.TextureManagerInterface;

import Main.*;
import MenuPanel.Label;
import MenuPanel.Menu;
import MenuPanel.SpriteElement;
import SpatializeSound.HRTFManager;

public class GameState extends State
{ 
	public enum LevelState
	{
		GameOver,
		Sucess,
		Pause,
		Normal,
	}
	
	//======================================[ Attributs ]===========================================//
	
	private GameWorld gameworld;
	private Label levelName;
	private Label life;
	private Label progression;
	private Label cristal;
	private SpriteElement mBackgroundSprite;
	private SpriteElement teleporteurSprite;
	private SpriteElement lifeBarSprite;
	private SpriteElement lifeBarLineSprite;
	private SpriteElement progressBarSprite;
	private SpriteElement progressBarLineSprite;
	private SpriteElement cristalSprite;
	private Font mFont;
	private Menu menu;
	
	//======================================[ Constructeur ]===========================================//
	
	public GameState(StateStack mystack, Context context) 
	{
		super(mystack, context);
		menu = new Menu();

		mBackgroundSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.HoloBackground));
		teleporteurSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.Teleporteur), 200, 150);
		lifeBarSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.LifeBar));
		lifeBarLineSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.LifeBarLine));
		progressBarSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.ProgressBar));
		progressBarLineSprite = new SpriteElement(mContext.texture.get(TextureManagerInterface.ID.ProgressBarLine));
		cristalSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.Cristal), 50, 50);

		mFont = mContext.font.get(FontManagerInterface.ID.MenuText);
		
		levelName = new Label("", 350, 500, mFont, 35, Color.WHITE);
	 	life = new Label("", 510, 40, mFont, 35, Color.GREEN);
	 	progression = new Label("", 510, 90, mFont, 35, Color.CYAN);
	 	cristal = new Label("0", 160, 70, mFont, 35, Color.BLUE);
	 	
		menu.addElement(mBackgroundSprite, 0);
	 	menu.addElement(cristalSprite, 1);
	 	menu.addElement(levelName, 1);
	 	menu.addElement(life, 3);
	 	menu.addElement(lifeBarLineSprite, 1);
	 	menu.addElement(lifeBarSprite, 2);
	 	menu.addElement(progressBarLineSprite, 1);
	 	menu.addElement(progressBarSprite, 2);
	 	menu.addElement(progression, 3);
	 	menu.addElement(teleporteurSprite, 1);
	 	menu.addElement(cristal, 1);
	 	
	 	levelName.attach(teleporteurSprite);
	 	levelName.setPosition(new Vector2f(130, 350));
	 	
	 	lifeBarLineSprite.setPosition(new Vector2f(500, 50));
	 	lifeBarSprite.attach(lifeBarLineSprite);
	 	lifeBarSprite.setPosition(new Vector2f(5, 5));
	 	life.attach(lifeBarLineSprite);
	 	life.setPosition(new Vector2f(10, -15));
	 	progressBarSprite.attach(progressBarLineSprite);
	 	progressBarSprite.setPosition(new Vector2f(5, 5));
	 	progression.attach(progressBarLineSprite);
	 	progression.setPosition(new Vector2f(10, -15));
	 	progressBarLineSprite.attach(lifeBarLineSprite);
	 	progressBarLineSprite.setPosition(new Vector2f(0, 50));
	}

	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mContext.cristals.clear();
		mContext.window.setView(Context.getDefaultView());
		//mContext.levelReader.init("/test.txt");
		levelName.setText(mContext.levelReader.getLevelName());
		if (mContext.currentLevel < mContext.playerInfo.getLevelClearedNumber())
			cristal.setText(String.valueOf(mContext.playerInfo.getLevelInfo(mContext.currentLevel).getCristalsCollected()));
		else
			cristal.setText("0");
		mContext.player.setHitNumber(0);
		HRTFManager hm = new HRTFManager(mContext.sound, mContext.hrtf);
		gameworld = new GameWorld(mContext, mContext.levelReader, mContext.sound, hm);
		gameworld.init();
	}
	
	@Override
	public boolean handleEvent(Event event) 
	{
		if (event.type == Type.CLOSED)
			requestStateClear();
		
		if (event.type == Type.KEY_PRESSED)
		{
			mContext.sound.play("Bip", Listener.getPosition());
			if (event.asKeyEvent().key == Key.ESCAPE)
	    	{
				//IMPORTANT: on réinitialise le gameworld
				gameworld.clean();
				//On retourne au menu
	    		requestStackPop();
	    		requestStackPush(State.ID.Title);
	    	}
		}
		return true;
	}

	@Override
	public boolean update(Time dt) 
	{	
		
		Vector3f pos = new Vector3f(400, 300, 1);
		try 
		{
			pos = Interpolation.getPlayerPosition(mContext.client);
		} 
		catch (KinectDisconnectedException e) 
		{
	    	requestStackPush(State.ID.KinectConnect);
		}
		
		if (pos.x == -1)
		{
			requestStackPush(State.ID.NotVisiblePause);
		}
		Listener.setPosition(pos.x, pos.z, pos.y); //ne pas oublier d'inverser y et z!!!
		//Listener.setDirection((float) (1/Math.sqrt(2)), 0.f, (float) (1/Math.sqrt(2)));
		Listener.setDirection(mContext.orientationCaptor.getOrientation());
	
		LevelState levelState = gameworld.update(pos , dt);
		if (levelState == LevelState.GameOver)
		{
			//IMPORTANT: on réinitialise le gameworld
			gameworld.clean();
			//On affiche l'écran de Game Over
			requestStackPop();
			requestStackPush(State.ID.GameOver);
		}
		else if (levelState == LevelState.Sucess)
		{
			//IMPORTANT: on réinitialise le gameworld
			gameworld.clean();
			//On affiche l'écran de succès
    		requestStackPop();
    		requestStackPush(State.ID.LevelSucess);
		}
		
		//Vie et progression
		life.setText(String.valueOf(mContext.player.getLife()));
		lifeBarSprite.getSprite().setScale((float)(mContext.player.getLife())/100, 0.7f);
		
		float progress =(float)(mContext.levelReader.getCurrentEvent())/mContext.levelReader.getNumberOfEvent();
		progression.setText(String.valueOf((int)(100*progress)));
		progressBarSprite.getSprite().setScale(progress, 0.7f);
		
		return true;
	}

	@Override
	public void render() 
	{
	    menu.draw(mContext.window);
	    
	    ArrayList<Monster> m = gameworld.getMonsters();
	    for (Monster mob : m)
	    {
	    	mContext.window.draw(mob.getShape());
	    }
	    mContext.window.draw(mContext.player.getShape());
	}
	
}
