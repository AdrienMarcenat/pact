package Entity;

import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;

import Components.Interpolation;
import Components.Point;
import Components.Trajectory;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class NormalMob extends Monster
{
	//======================================[ Attributs ]===========================================//
	
	private Trajectory trajectory;
	private Point nextPoint;
	private Point prevPoint;
	private int currentPoint;
	private float velocity;
	
	//======================================[ Constructeur ]===========================================//
	
	public NormalMob(int radius, int life, Vector3f position, String id, SoundManager sm, HRTFManager hm, 
					 Trajectory trajectory, int damage, float velocity, Monster.Trigger trigger) 
	{
		super(radius, life, position, id, sm, hm, damage, trigger);
		this.id = Monster.ID.Normal;
		this.trajectory = trajectory;
		this.velocity = velocity;
		prevPoint = trajectory.getPosition(0);
		currentPoint = 1;
		nextPoint = trajectory.getPosition(1);
		this.position.setPos(new Vector3f(prevPoint.x, prevPoint.y, position.z));
		shape.setPosition(position.x, position.y);
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Le normal mob suit la trajectoire préenregistrée trajectory
	@Override
	public void update(Vector3f newPlayerPos, Time dt) 
	{
		Vector3f direction = Interpolation.unit(new Vector3f(nextPoint.x-prevPoint.x, nextPoint.y-prevPoint.y, 0));
		
		Vector3f v = Vector3f.mul(direction, velocity);
		
		Vector3f pos = new Vector3f(position.getPos().x+v.x, position.getPos().y+v.y, position.getPos().z);
		position.setPos(pos);
		shape.setPosition(position.getPos().x, position.getPos().y);
		
		//On vérifie si on à atteint nextPoint
		if (direction.x > 0 && pos.x > nextPoint.x
			|| direction.x < 0 && pos.x < nextPoint.x
			|| direction.y < 0 && pos.y <= nextPoint.y
			|| direction.y < 0 && pos.y <= nextPoint.y)
		{
			//On regarde si la trajectoire est terminée, si oui le monstre meurt
			if (currentPoint < trajectory.size()-1)
			{
				prevPoint = trajectory.getPosition(currentPoint);
				currentPoint++;
				nextPoint = trajectory.getPosition(currentPoint);
			}
			else
			{
				life.setLife(0);
			}
		}
		
		//Son
		Vector3f p = position.getPos();
		sound.update(new Vector3f(p.x, p.z, p.y), newPlayerPos);
	}
}
