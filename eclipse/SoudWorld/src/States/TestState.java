package States;

// Classe spéciale pour réaliser des tests
// Elle simule GameState avec un niveau "test.txt" qui contient un monstre type N
// Faisant des cercles, puis un de type H
// Elle affiche le monde vue de dessus (en 2D donc), les cercles rouges sont les monstres, 
// le vert est le joueur, on peut le bouger avec les flèches

//Test pouvant être réalisés pour l'instant: 
// -collisions détectées, 
// -son correctement joué (quand il faut et à la bonne position
// -déplacement correct des monstres
// -lecture correct du fichier de niveau
// -mise à jour des informations du joueur (vie, nombre de coups...)
// -disparition des monstres et de leur son

import java.util.ArrayList;

import org.jsfml.audio.Listener;

import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import Entity.Monster;
import Main.Context;
import Main.GameWorld;
import Main.State;
import Main.StateStack;
import SpatializeSound.HRTFManager;

public class TestState extends State
{
	//======================================[ Attributs ]===========================================//
	
    private GameWorld gameworld;
	private Vector3f pos;
	private boolean entry;
     
	//======================================[ Constructeur ]===========================================//
	
	public TestState(StateStack mystack, Context context)
	{
		super(mystack, context);
		
		pos = new Vector3f(200, 200, 0);
		HRTFManager hm= new HRTFManager(mContext.sound, mContext.hrtf);
		gameworld = new GameWorld(mContext, mContext.levelReader, mContext.sound, hm);
		entry = false;
	}

	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mContext.window.setView(Context.getDefaultView());
		//mContext.levelReader.init("/test.txt");
		HRTFManager hm= new HRTFManager(mContext.sound, mContext.hrtf);
		gameworld = new GameWorld(mContext, mContext.levelReader, mContext.sound, hm);
		gameworld.init();
		entry = true;
		mContext.player.setHitNumber(0);
	}
	
	@Override
	public boolean handleEvent(Event event)
	{
	    if (event.type == Type.CLOSED)
	    	requestStateClear();
	    
		if (event.type == Type.KEY_PRESSED)
		{
			if (event.asKeyEvent().key == Key.ESCAPE)
	    	{
				//IMPORTANT: on réinitialise le gameworld
				gameworld.clean();
				entry = false;
				//On retourne au menu
				requestStateClear();
	    		requestStackPush(State.ID.Title);
	    	}
			if (event.asKeyEvent().key == Key.UP)
	    	{
				pos = new Vector3f(pos.x, pos.y - 5, pos.z);
	    	}
			if (event.asKeyEvent().key == Key.DOWN)
	    	{
				pos = new Vector3f(pos.x, pos.y + 5, pos.z);
	    	}
			if (event.asKeyEvent().key == Key.LEFT)
	    	{
				pos = new Vector3f(pos.x - 5, pos.y, pos.z);
	    	}
			if (event.asKeyEvent().key == Key.RIGHT)
	    	{
				pos = new Vector3f(pos.x + 5, pos.y, pos.z);
	    	}
		}
		return true;
	}

	@Override
	public boolean update(Time dt)
	{
		if (!entry)
			init();
		
		Listener.setPosition(pos.x, pos.z, pos.y);
		//Listener.setDirection((float) (1/Math.sqrt(2)), 0.f, (float) (1/Math.sqrt(2)));
		Listener.setDirection(mContext.orientationCaptor.getOrientation());
		GameState.LevelState levelState = gameworld.update(pos , dt);
		mContext.player.getShape().setPosition(pos.x, pos.y);
		if (levelState == GameState.LevelState.GameOver)
		{
			//IMPORTANT: on réinitialise le gameworld
			gameworld.clean();
			entry = false;
			//On affiche l'écran de Game Over
			requestStackPop();
			requestStackPush(State.ID.GameOver);
		}
		if (levelState == GameState.LevelState.Sucess)
		{
			//IMPORTANT: on réinitialise le gameworld
			gameworld.clean();
			entry = false;
			//On affiche l'écran de succès
    		requestStackPop();
    		requestStackPush(State.ID.LevelSucess);
		}
		return true;
	}

	@Override
	public void render()
	{
	    ArrayList<Monster> m = gameworld.getMonsters();
	    for (Monster mob : m)
	    {
	    	mContext.window.draw(mob.getShape());
	    }
	    mContext.window.draw(mContext.player.getShape());
	}
}
