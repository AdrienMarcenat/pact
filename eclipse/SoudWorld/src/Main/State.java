package Main;

import org.jsfml.system.*;
import org.jsfml.window.event.*;


public abstract class State 
{
	//Les noms des state doivent �tre enregistr�s ici
	//Ne pas oublier ensuite de compl�ter registerState dans Application
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
	
		//La state peut donner des ordres (push, pop, clear) � la stack
		protected StateStack mStack;
		
		//Le contexte permet  � la state d'avoir acc�s � tous ce dont elle a besoin
		protected Context mContext;
		
	//======================================[ Constructeur ]===========================================//
	
	public State(StateStack mystack, Context context)
	{
		mStack = mystack;
		mContext = context;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Toutes les states partagent quatre m�thodes indispensables:
	
	//L'initialisation, appel�e quand on push la State
	public abstract void init();
	
	//La gestion des �v�nements de la frame
	public abstract boolean handleEvent(Event event);
	
	//La mise � jour en cons�quences
	public abstract boolean update(Time dt);
	
	//Le rendu graphique/sonore
	public abstract void render();
	
	//La state peut donner trois ordres � la stack:
	
	//Empile une nouvelle state
	protected void requestStackPush(State.ID stateID)
	{
		 mStack.pushState(stateID);
	}
	
	//D�pile la state actuelle et revient � la pr�c�dente
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
