package Main;

import java.util.ArrayList;
import java.util.Iterator;

import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;

import Components.Interpolation;
import Entity.*;
import LevelEditor.LevelReader;
import SpatializeSound.HRTFManager;
import States.GameState;

public class GameWorld 
{
	//======================================[ Attributs ]===========================================//
	
	private Context mContext;
	Player player;
	private ArrayList<Monster> monsters;
	private LevelReader levelReader;
	private SoundManager sound;
	private HRTFManager hm;
	private Time wait;
	private Ambience ambience;
	
	
	//======================================[ Constructeur ]===========================================//
	
	public GameWorld(Context mContext, LevelReader levelReader, SoundManager sound, HRTFManager hm) 
	{
		this.mContext = mContext;
		this.player = mContext.player;
		this.levelReader = levelReader;
		this.monsters = new ArrayList<Monster>();
		this.sound = sound;
		this.hm = hm;
		this.wait = Time.ZERO;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//réinitialise la liste de monstres et on stoppe les sons
	public void clean()
	{
		for (Iterator<Monster> iter = monsters.listIterator(); iter.hasNext(); ) 
		{
		    Monster mob = iter.next();
			mob.getSoundPlayer().stop();
			iter.remove();
		}
		
		ambience.stop();
		player.stop();
		player.setLife(player.getMaxLife());
		
		//System.out.println("clean");
	}
	
	public void init()
	{
		ambience = levelReader.getAmbience(sound, hm);
		
		//System.out.println("init");
	}
	
	public ArrayList<Monster> getMonsters() 
	{
		return monsters;
	}
	
	//Met à jour la position des monstres et du joueur, et vérifie leurs points de vie
	public GameState.LevelState update(Vector3f newPlayerPos, Time dt)
	{	
		ambience.update(newPlayerPos);
		
		//si la liste est vide on passe à l'event suivant après un délai de 2secondes
		if (monsters.isEmpty())
		{
			if (wait.asSeconds() < 2)
			{
				//on met à jour la position de la shape du joueur
				player.setPosition(newPlayerPos);
				player.getShape().setPosition(newPlayerPos.x, newPlayerPos.y);
				player.update();
				
				wait = Time.add(dt, wait);
				return GameState.LevelState.Normal;
			}
			
			wait = Time.ZERO;
			monsters = levelReader.getNextEvent(sound, hm);
		}
		
		//on met à jour la position de la shape du joueur
		player.setPosition(newPlayerPos);
		player.getShape().setPosition(newPlayerPos.x, newPlayerPos.y);
		player.update();
		
		for (Iterator<Monster> iter = monsters.listIterator(); iter.hasNext(); ) 
		{
		    Monster mob = iter.next();
		    
		    //On tue les monstres qui n'ont plus de vie
			if (mob.getLife() == 0)
			{
				mob.getSoundPlayer().stop();
				if (mob.getTrigger() == Monster.Trigger.Cristal)
				{
					iter.remove();
					mContext.cristals.add(0);
				}
				else
					iter.remove();
			}
			else
			{
				//mise à jour de la position du mob en fonction de celle du joueur
				mob.update(newPlayerPos, dt);
				//gestion des collisions
				if (Interpolation.collisionPlayer(mob.getHitboxRadius(), mob.getPosition(), player))
				{
					player.addToLife(mob.getDamage());
					mob.getSoundPlayer().stop();
					
					if(mob.getDamage() > 0)
					{
						sound.play("SFWarp", newPlayerPos);
					}
					else if(mob.getDamage() < 0)
					{
						player.increaseHitNumber();
						sound.play("DamageReceived", newPlayerPos);
					}
					
					Monster.Trigger t = mob.getTrigger();
					if (t.equals(Monster.Trigger.EndEvent))
					{
						for (Monster m: monsters) 
						{
							if (m.getTrigger().equals(Monster.Trigger.Cristal))
								mContext.cristals.add(0);
						}
						monsters.clear();
						return GameState.LevelState.Normal;
					}
					else if (t.equals(Monster.Trigger.Cristal))
					{
						mContext.cristals.add(1);
					}
					
					iter.remove();
				}
			}
		}
		
		//on vérifie si le joueur a toujours de la vie
		if (player.getLife() <= 0)
			return GameState.LevelState.GameOver;
		
		if (levelReader.getCurrentEvent() == levelReader.getNumberOfEvent() && monsters.isEmpty())
		{
			sound.play("PieceFound", newPlayerPos);
			return GameState.LevelState.Sucess;
		}
					
		return GameState.LevelState.Normal;
	}
	
}
