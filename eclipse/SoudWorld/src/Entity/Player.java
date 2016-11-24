package Entity;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector3f;

import Components.*;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class Player 
{
	//======================================[ Attributs ]===========================================//
	
	private Hitbox hitbox;
	private Life life;
	private int maxLife;
	private Position position;
	private CircleShape shape;
	private SoundPlayer s;
	private boolean lowLife;
	private int hitNumber;
	
	//======================================[ Constructeur ]===========================================//
	
	public Player(int radius, int life, Vector3f position, SoundManager sm, HRTFManager hm, int maxLife) 
	{
		this.hitbox = new Hitbox(radius);
		this.life = new Life(life);
		this.position = new Position(position);
		this.s = new SoundPlayer("LowLife", sm, hm);
		this.maxLife = maxLife;
		this.lowLife = false;
		this.hitNumber = 0;
		
		shape = new CircleShape();
		shape.setFillColor(Color.GREEN);
		shape.setRadius(radius);
		shape.setPosition(position.x, position.y);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	public CircleShape getShape() 
	{
		return shape;
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
		if (newLife >= 0 && newLife <= maxLife)
			life.setLife(newLife);
	}
	
	public void addToLife(int amount) 
	{
		if (life.getLife() + amount > maxLife)
			life.setLife(maxLife);
		else if (life.getLife() + amount < 0)
			life.setLife(0);
		else
			life.setLife(life.getLife() + amount);
	}

	public Vector3f getPosition() 
	{
		return position.getPos();
	}

	public void setPosition(Vector3f pos) 
	{
		position.setPos(pos);
	}
	//Joue le son de vie basse si on ets à 20% ou moins de vie
	public void update()
	{
		if((float)(life.getLife())/maxLife <= 0.2f)
		{
			if (!lowLife)
			{	s.normalPlay(true);
				lowLife = true;
			}
		}
		else
		{
			s.pause();
			lowLife = false;
		}
	}

	public void stop() 
	{
		s.stop();
	}
	
	public void setHitNumber(int amount)
	{
		hitNumber = amount;
	}
	
	public int getHitNumber()
	{
		return hitNumber;
	}
	
	public void increaseHitNumber()
	{
		hitNumber++;
	}
	
	public int getMaxLife()
	{
		return maxLife;
	}
}
