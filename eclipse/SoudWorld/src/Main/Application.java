package Main;

import States.*;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.*;
   
public class Application 
{
	//======================================[ Attributs ]===========================================//
	private StateStack mStateStack;
    private RenderWindow mWindow;
    private Context mContext;
    
    //======================================[ Constructeur ]===========================================//
	public Application(Context context)
	{
		mStateStack = new StateStack();
		mWindow = context.window;
		mContext = context;
		
		registerState();
		
		//On commence par l'écran titre
	    mStateStack.pushState(State.ID.Title);
	    
	    Event event = new Event(0);
	    mStateStack.handleEvent(event);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//A compléter si l'on veut rajouter une state
	//Remplit le dictionnaire des states
	public void registerState()
	{
		TitleState tstate = new TitleState(mStateStack, mContext);
		mStateStack.registerState(State.ID.Title, tstate);
		
		MenuState mstate = new MenuState(mStateStack, mContext);
		mStateStack.registerState(State.ID.Menu, mstate);
		
		LevelSelectionState lstate = new LevelSelectionState(mStateStack, mContext);
		mStateStack.registerState(State.ID.LevelSelection, lstate);
		
		GameState gstate = new GameState(mStateStack, mContext);
		mStateStack.registerState(State.ID.Game, gstate);
		
		KinectConnectState kstate = new KinectConnectState(mStateStack, mContext);
		mStateStack.registerState(State.ID.KinectConnect, kstate);
		
		GameOverState gostate = new GameOverState(mStateStack, mContext);
		mStateStack.registerState(State.ID.GameOver, gostate);
		
		LevelSucessState lsstate = new LevelSucessState(mStateStack, mContext);
		mStateStack.registerState(State.ID.LevelSucess, lsstate);
		
		TestState teststate = new TestState(mStateStack, mContext);
		mStateStack.registerState(State.ID.Test, teststate);
		
		SaveSelectionState savestate = new SaveSelectionState(mStateStack, mContext);
		mStateStack.registerState(State.ID.Save, savestate);
		
		NewSaveState newSaveState = new NewSaveState(mStateStack, mContext);
		mStateStack.registerState(State.ID.NewSave, newSaveState);
		
		NotVisiblePauseState notVisiblePauseState = new NotVisiblePauseState(mStateStack, mContext);
		mStateStack.registerState(State.ID.NotVisiblePause, notVisiblePauseState);
	}

	
	public void processInput()
	{
	    Event event = mWindow.pollEvent();
	    while(event != null)
	    {
	        mStateStack.handleEvent(event);
	        event = mWindow.pollEvent();
	    }
	}

	public void update(Time dt)
	{
	    mStateStack.update(dt);
	}

	public void render()
	{
		mWindow.clear();
	    mStateStack.render();
	    mWindow.display();
	}
	
	//Boucle principale, s'arrete lorsque la Stack est vide, ce qui ferme le jeu
	public void run()
	{
	    Clock clock = new Clock();
	    Time dt = clock.restart();
	    while(!mStateStack.isEmpty())
	    {
	        dt = clock.restart();
	        processInput();
	        update(dt);
	        render();
	    }
	    mContext.client.destroyThreadAndConnection();
	    mWindow.close();
	}
}
