package Entity;

import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;

import Components.*;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class HommingMob extends Monster
{
	//======================================[ Attributs ]===========================================//
	
	private Time time;
	private int lifeTime;
	private float velocity;
	
	//======================================[ Constructeur ]===========================================//
	
	public HommingMob(int radius, int life, Vector3f position, String id, SoundManager sm, HRTFManager hm, 
					  int damage, float velocity, Monster.Trigger trigger, int lifeTime) 
	{
		super(radius, life, position, id, sm, hm, damage, trigger);
		this.id = Monster.ID.Homming;
		this.time = Time.ZERO;
		this.velocity = velocity;
		this.lifeTime = lifeTime;	
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Le HommingMob suit le joueur où qu'il soit, il ne suit pas de trajectoire précise
	@Override
	public void update(Vector3f newPlayerPos, Time dt) 
	{
		Vector3f newSpeed = Interpolation.unit(new Vector3f(newPlayerPos.x-position.getPos().x,
															newPlayerPos.y-position.getPos().y,
															newPlayerPos.z-position.getPos().z));
		
		newSpeed = Vector3f.mul(newSpeed, velocity);
		
		Vector3f pos = new Vector3f(position.getPos().x + newSpeed.x, position.getPos().y + newSpeed.y, position.getPos().z + newSpeed.z);
		position.setPos(pos);
		shape.setPosition(pos.x, pos.y);
		
		Vector3f p = position.getPos();
		sound.update(new Vector3f(p.x, p.z, p.y), newPlayerPos);
		
		time = Time.add(time,  dt);
		if (time.asSeconds() > lifeTime)
			life.setLife(0);
	}
}
