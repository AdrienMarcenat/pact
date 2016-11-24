package States;


import org.jsfml.audio.Listener;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.*;
import org.jsfml.window.event.Event.Type;

import Editor_GUI.GameFrame;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.TextureManagerInterface;
import HRTFTest.HRTFTestFrame;
import Main.*;
import MenuPanel.Label;
import MenuPanel.SpriteElement;

public class TitleState extends State
{
	//======================================[ Attributs ]===========================================//
	
	private SpriteElement mBackgroundSprite;
	private SpriteElement titleSprite;
	private Font mFont;
    private Label mText;
    private Time mTextEffectTime;
     
    //======================================[ Constructeur ]===========================================//
    
	public TitleState(StateStack mystack, Context context)
	{
		super(mystack, context);
		
		mBackgroundSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.MenuBackground), 400, 300);
		mBackgroundSprite.getSprite().setOrigin(960, 540);
		
		titleSprite = new SpriteElement(context.texture.get(TextureManagerInterface.ID.Title), 150, 50);
		
		mFont = context.font.get(FontManagerInterface.ID.MenuText);
	 	
		mText = new Label("Press any Key", 220, 450, mFont, 40, Color.GREEN);
	 	mTextEffectTime = Time.ZERO;
	}

	//======================================[ Fonctions ]===========================================//
	
	@Override
	public void init()
	{
		mTextEffectTime = Time.ZERO;
		mText.setVisible(true);
		mContext.window.setView(Context.getDefaultView());
	}
	
	@Override
	public boolean handleEvent(Event event)
	{
	    if (event.type == Type.KEY_PRESSED)
	    {
	    	if (event.asKeyEvent().key == Key.ESCAPE)
	    	{	
	    		requestStateClear();
	    		return true;
	    	}
	    	
	    	mContext.sound.play("Bip", Listener.getPosition());
	        requestStackPop();
	        requestStackPush(State.ID.Menu);
	        //GameFrame g = new GameFrame("Editor", 1000,1000);
	        //HRTFTestFrame g = new HRTFTestFrame("HRTF test", 1000,1000, mContext.sound, mContext.hrtf);
	    }
	    if (event.type == Type.CLOSED)
	    	requestStateClear();
	    return true;
	}

	@Override
	public boolean update(Time dt)
	{
		//On fait clignoter le texte
	    mTextEffectTime = Time.add(mTextEffectTime, dt);
	    if (mTextEffectTime.asSeconds() >= 0.7f)
	    {
	        mText.setVisible(!mText.isVisible());
	        mTextEffectTime = Time.ZERO;
	    }
	    
	    //On fait tourner le fond d'écran
	    mContext.rotation += 0.5;
	    mBackgroundSprite.getSprite().setRotation(mContext.rotation);
	    
	    return true;
	}

	@Override
	public void render()
	{
	    mContext.window.draw(mBackgroundSprite.getSprite());
	    mContext.window.draw(titleSprite.getSprite());
	    if (mText.isVisible())
	        mContext.window.draw(mText.getText());
	}

}
