package States;


import org.jsfml.audio.Listener;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.*;
import org.jsfml.window.event.Event.Type;

import ClientServer.ClientThread;
import ClientServer.KinectCommand;
import Components.Interpolation;
import Exception.KinectDisconnectedException;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.SoundManagerInterface;
import Main.*;
import MenuPanel.Label;

public class NotVisiblePauseState extends State
{
	//======================================[ Attributs ]===========================================//
	
	private CircleShape area;
	private RectangleShape rect;
	private Font mFont;
    private Label mText;
    private Time mCircleEffectTime;
    private boolean isVisible;
    private Vector3f areaPos;
    
    //======================================[ Constructeur ]===========================================//
    
	public NotVisiblePauseState(StateStack mystack, Context context)
	{
		super(mystack, context);
		
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
	 	
		mText = new Label("You are out of the game area, please enter the red circle", 50, 100, mFont, 20, Color.WHITE);
	 	mCircleEffectTime = Time.ZERO;
	 	
	 	area = new CircleShape();
	 	area.setRadius(20);
	 	area.setFillColor(Color.RED);
	 	
	 	rect = new RectangleShape();
	 	rect.setSize(new Vector2f(800, 600));
	 	rect.setFillColor(new Color(0,0,0,100));
	}

	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mCircleEffectTime = Time.ZERO;
		mContext.window.setView(Context.getDefaultView());
		Vector3f playerPos = mContext.player.getPosition();
		//area.setPosition(playerPos.x, playerPos.y);
		area.setPosition(200, 20);
		//areaPos = playerPos;
		areaPos = new Vector3f(200, 20, 1);
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
	    }
    	
	    if (event.type == Type.CLOSED)
	    	requestStateClear();
	    return false;
	}

	@Override
	public boolean update(Time dt)
	{
		//On fait clignoter le cercle
	    mCircleEffectTime = Time.add(mCircleEffectTime, dt);
	    if (mCircleEffectTime.asSeconds() >= 0.5f)
	    {
	    	isVisible = !isVisible;
	        mCircleEffectTime = Time.ZERO;
	    }
	    
	    Vector3f pos = new Vector3f(1, 1, 1);
		try 
		{
			pos = Interpolation.getPlayerPosition(mContext.client);
		} 
		catch (KinectDisconnectedException e) 
		{
	    	requestStackPush(State.ID.KinectConnect);
		}
	   
	    mContext.player.setPosition(pos);
	    if (Interpolation.collisionPlayer((int)area.getRadius(), areaPos, mContext.player))
	    	requestStackPop();
	    
	    return false;
	}

	@Override
	public void render()
	{
	    mContext.window.draw(rect);
	    mContext.window.draw(mText.getText());
	    if (isVisible)
	        mContext.window.draw(area);
	    mContext.window.draw(mContext.player.getShape());
	}

	
}

