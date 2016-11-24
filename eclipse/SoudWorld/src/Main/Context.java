package Main;

import java.util.ArrayList;

import org.jsfml.graphics.*;

import ClientServer.*;
import Entity.Player;
import LevelEditor.LevelReader;
import Save.PlayerInfo;
import Save.SaveManager;

public class Context
{		
	
	//======================================[ Attribut ]===========================================//
	
	//Tous public par souci d'ergonomie, car on y accede souvent sans les modifier
	public RenderWindow window;
	public FontManager font;
	public TextureManager texture;
	public SoundManager sound;
	public SoundManager hrtf;
	public ClientThread client;
	public LevelReader levelReader;
	public OrientationCaptor orientationCaptor;
	public int currentLevel;
	public float rotation;
	public Player player;
	public PlayerInfo playerInfo;
	public SaveManager saveManager;
	public ArrayList<Integer> cristals;
		
	//======================================[ Constructeur ]===========================================//
	
	public Context(RenderWindow win, FontManager f, TextureManager t, 
			SoundManager s, SoundManager h, OrientationCaptor o, ClientThread c, 
			LevelReader l, int n, Player p, PlayerInfo pi, SaveManager sam)
	{
		window = win;
		font = f;
		texture = t;
		client = c;
		levelReader = l;
		sound = s;
		hrtf = h;
		currentLevel = n;
		rotation = 0;
		player = p;
		orientationCaptor = o;
		playerInfo = pi;
		saveManager = sam;
		cristals = new ArrayList<Integer>();
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Renvoie la vue initiale de la fenetre
	public static View getDefaultView()
	{
		View view = new View();
		view.setSize(800, 600);
		view.setCenter(400, 300);
		return view;
	}
}

