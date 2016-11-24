package Main;

import org.jsfml.system.*;
import org.jsfml.window.event.*;


public abstract class State 
{
	//Les noms des state doivent être enregistrés ici
	//Ne pas oublier ensuite de compléter registerState dans Application
	public enum ID 
	{ 
		Title, 
		Menu, 
		Pause, 
		Game, 
		Speech, 
		GameOver, 
		KinectConnect, 
		LevelSelection, 
		LevelSucess, 
		Test, 
		Save, 
		NewSave, 
		NotVisiblePause 
	};
	
	//======================================[ Attributs ]===========================================//
	
		//La state peut donner des ordres (push, pop, clear) à la stack
		protected StateStack mStack;
		
		//Le contexte permet  à la state d'avoir accès à tous ce dont elle a besoin
		protected Context mContext;
		
	//======================================[ Constructeur ]===========================================//
	
	public State(StateStack mystack, Context context)
	{
		mStack = mystack;
		mContext = context;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Toutes les states partagent quatre méthodes indispensables:
	
	//L'initialisation, appelée quand on push la State
	public abstract void init();
	
	//La gestion des évènements de la frame
	public abstract boolean handleEvent(Event event);
	
	//La mise à jour en conséquences
	public abstract boolean update(Time dt);
	
	//Le rendu graphique/sonore
	public abstract void render();
	
	//La state peut donner trois ordres à la stack:
	
	//Empile une nouvelle state
	protected void requestStackPush(State.ID stateID)
	{
		 mStack.pushState(stateID);
	}
	
	//Dépile la state actuelle et revient à la précédente
	protected void requestStackPop()
	{
		mStack.popState();
	}
	
	//Vide la stack
	protected void requestStateClear()
	{
		mStack.clearStates();
	}
	
	protected final Context getContext() 
	{
		return mContext;
	}
}
