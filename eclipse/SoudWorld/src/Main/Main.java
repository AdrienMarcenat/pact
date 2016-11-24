package Main;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector3f;
import org.jsfml.window.VideoMode;

import ClientServer.*;
import Entity.Player;
import GameInterfaces.FontManagerInterface;
import GameInterfaces.TextureManagerInterface;
import LevelEditor.LevelReader;
import Save.PlayerInfo;
import Save.SaveManager;
import SpatializeSound.HRTFManager;

public class Main 
{

	public static void main(String[] args)
	{	
		//======================================[ Creation de la fenetre de jeu ]===========================================//
		
		//800x600 pixels
		VideoMode v = new VideoMode(800, 600);
	    //RenderWindow window = new RenderWindow(v, "SoundWorld", WindowStyle.FULLSCREEN);
	    RenderWindow window = new RenderWindow(v, "SoundWorld");
	    //60 frames par seconde
	    window.setFramerateLimit(60);
	    
	    //======================================[ Chargement des ressources ]===========================================//
	    
	    FontManager f = new FontManager();
	    TextureManager t = new TextureManager();
	    SoundManager s = new SoundManager();
	    
	    //chargement des fonts
	    f.load(FontManagerInterface.ID.MenuText, "/Beware.ttf");
	    
	    //chargement des textures
	    t.load(TextureManagerInterface.ID.MenuBackground, "/background.png");
	    t.load(TextureManagerInterface.ID.HoloBackground, "/Holo.png");
	    t.load(TextureManagerInterface.ID.levelSelectionBackground, "/Level-selection-background.png");
	    t.load(TextureManagerInterface.ID.levelZero, "/level-0.png");
	    t.load(TextureManagerInterface.ID.levelOne, "/level-1.png");
	    t.load(TextureManagerInterface.ID.levelTwo, "/level-2.png");
	    t.load(TextureManagerInterface.ID.levelThree, "/level-3.png");
	    t.load(TextureManagerInterface.ID.rightArrow, "/rigth-arrow.png");
	    t.load(TextureManagerInterface.ID.leftArrow, "/left-arrow.png");
	    t.load(TextureManagerInterface.ID.upArrow, "/up-arrow.png");
	    t.load(TextureManagerInterface.ID.downArrow, "/down-arrow.png");
	    t.load(TextureManagerInterface.ID.Window, "/window.png");
	    t.load(TextureManagerInterface.ID.Teleporteur, "/teleporteur.png");
	    t.load(TextureManagerInterface.ID.Title, "/Title.png");
	    t.load(TextureManagerInterface.ID.HitBlue, "/hit-blue.png");
	    t.load(TextureManagerInterface.ID.ProgressBar, "/exp-bar.png");
	    t.load(TextureManagerInterface.ID.ProgressBarLine, "/exp-bar-line.png");
	    t.load(TextureManagerInterface.ID.LifeBar, "/life-bar.png");
	    t.load(TextureManagerInterface.ID.LifeBarLine, "/life-bar-line.png");
	    t.load(TextureManagerInterface.ID.Cristal, "/cristal.png");
	    t.load(TextureManagerInterface.ID.square, "/square.png");
	    
	    //chargement de sons
	    s.load("/SoundConfig.txt");
	    
	    //Config c = new Config();
	    //c.writeConfig("HRTFConfig.txt");
	    
	    SoundManager hrtf = new SoundManager();
	    hrtf.load("/HRTFConfig.txt");
	    HRTFManager hm = new HRTFManager(s, hrtf);
	   
	    
	  //======================================[ Creation et connexion du client ]===========================================//
	    
	    ClientThread client = new ClientThread();
	    
	    
		/*while (!client.getConnected())
		{
			client.connect("127.0.0.1", 20015);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		client.start();*/
		
	  //======================================[ Creation du levelReader, du player, et du contexte ]===========================================//
	    
	    LevelReader l = new LevelReader();
	    l.init("/Demo.txt");
	    //Listener.setGlobalVolume(100);
	    //Listener.setPosition(0, 0, 0);
	    Player p = new Player(20, 100, new Vector3f(400, 300, 0), s, hm, 100);
	    PlayerInfo pi = null;
	    SaveManager sam = null;
	    OrientationCaptor o = new OrientationCaptor();
	    
	    Context context = new Context(window, f, t, s, hrtf, o, client, l, 0, p, pi, sam);
	    
	  //======================================[ Creation et lancement de application ]===========================================//
	    Application app = new Application(context);
	    app.run();
	    
	}
}
