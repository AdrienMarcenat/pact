package Entity;

import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;
import Components.*;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public abstract class Monster 
{
	public enum ID 
	{
		Normal,
		Homming,
		TriggerArea,
		Speaker
	}
	
	public enum Trigger
	{
		EndEvent,
		Cristal,
		None,
	}
	
	//======================================[ Attributs ]===========================================//
	
	protected Hitbox hitbox;
	protected Life life;
	protected Position position;
	protected SoundPlayer sound;
	protected Damage damage;
	protected CircleShape shape;
	protected Monster.ID id;
	private Monster.Trigger trigger;
	
	//======================================[ Constructeur ]===========================================//
	
	public Monster(int radius, int life, Vector3f position, String id, SoundManager sm, HRTFManager hm, int damage, Monster.Trigger trigger)
	{
		this.hitbox = new Hitbox(radius);
		this.life = new Life(life);
		this.position = new Position(position);
		this.sound = new SoundPlayer(id, sm, hm);
		this.damage = new Damage(damage);
		this.trigger = trigger;
		shape = new CircleShape();
		shape.setRadius(radius);
		shape.setFillColor(Color.RED);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public abstract void update(Vector3f newPlayerPos, Time dt);
	
	public ID getID()
	{
		return id;
	}
	
	public CircleShape getShape() 
	{
		return shape;
	}
	
	public SoundPlayer getSoundPlayer()
	{
		return sound;
	}

	public int getDamage() 
	{
		return damage.getDamage();
	}

	public void setDamage(int damage) 
	{
		this.damage.setDamage(damage);
	}

	public int getHitboxRadius() 
	{
		return hitbox.getRadius();
	}

	public void setHitboxRadius(int radius) 
	{
		hitbox.setRadius(radius);
	}

	public int getLife() 
	{
		return life.getLife();
	}

	public void setLife(int newLife) 
	{
		life.setLife(newLife);
	}
	
	public void addToLife(int amount) 
	{
		life.addToLife(amount);
	}

	public Vector3f getPosition() 
	{
		return position.getPos();
	}

	public void setPosition(Vector3f pos) 
	{
		position.setPos(pos);
	}
	
	public Monster.Trigger getTrigger()
	{
		return trigger;
	}
}
