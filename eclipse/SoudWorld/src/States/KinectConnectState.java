package States;

import org.jsfml.audio.Listener;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.Event.Type;

import ClientServer.*;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.TextureManagerInterface;
import Main.*;
import MenuPanel.Label;
import MenuPanel.SpriteElement;

public class KinectConnectState extends State
{ 
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private Font mFont;
    private Label mText;
    private Time mTextEffectTime;
    
    //======================================[ Constructeur ]===========================================//
    
	public KinectConnectState(StateStack mystack, Context context)
	{
		super(mystack, context);
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.levelSelectionBackground));
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
	 	mText = new Label("Please connect a Kinect device and press space", 50, 300, mFont, 22, Color.WHITE);
	 	mTextEffectTime = Time.ZERO;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mContext.window.setView(Context.getDefaultView());
		mTextEffectTime = Time.ZERO;
		mText.setVisible(true);
	}
	
	@Override
	public boolean handleEvent(Event event) 
	{
		if (event.type == Type.KEY_PRESSED)
	    {
			mContext.sound.play("Bip", Listener.getPosition());
			if (event.asKeyEvent().key == Key.SPACE)
			{
				boolean c = false;
				try 
				{
					c = connection();
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				
				if(c)
				{
					requestStackPop();
				}
			}
			else if (event.asKeyEvent().key == Key.ESCAPE)
			{
				requestStateClear();
				requestStackPush(State.ID.Menu);
			}
	    }
		if (event.type == Type.CLOSED)
			requestStateClear();
	    return false;
	}

	@Override
	public boolean update(Time dt) 
	{
		mTextEffectTime = Time.add(mTextEffectTime, dt);
		if (mTextEffectTime.asSeconds() >= 1.f)
		{
			mText.setVisible(!mText.isVisible());
			mTextEffectTime = Time.ZERO;
	    }
		return false;	
	}

	@Override
	public void render() 
	{	
	    mContext.window.draw(mBackgroundSprite.getSprite());
	    if (mText.isVisible())
	    	mContext.window.draw(mText.getText());
	}
	
	//Essaie de connecter la kinect en envoyant l'ordre KinCon au C++
	private boolean connection() throws InterruptedException
	{
		ClientThread client = mContext.client;
		
		if (client.getConnected())
		{
			KinectCommand cmd = new KinectCommand();
		
			cmd.setCmdName("KinCon");
			client.threadSend(cmd.buildFormatedCommand());
			while(!client.getSomethingToRead())
			{
				Thread.sleep(5);
			}
			String isConnect = client.threadRead();
			
			if (isConnect.equals("#isConn$"))
				return true;
		}
			
		return false;	
	}

}
