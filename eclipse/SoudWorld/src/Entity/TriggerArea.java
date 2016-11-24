package Entity;

import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class TriggerArea extends Monster
{
	//======================================[ Constructeur ]===========================================//
	
	public TriggerArea(int radius, Vector3f position,String id, SoundManager sm, HRTFManager hm, int damage, Monster.Trigger trigger) 
	{
		super(radius, 1, position, id, sm, hm, damage, trigger);
		this.id = Monster.ID.TriggerArea;
		this.position.setPos(new Vector3f(position.x, position.y, position.z));
		shape.setPosition(position.x, position.y);

	}
	
	//======================================[ Fonctions ]===========================================//
	
	//La TriggerArea ne bouge pas, et ne fais pas de dommage à priori
	@Override
	public void update(Vector3f newPlayerPos, Time dt) 
	{
		//Son
		Vector3f p = position.getPos();
		sound.update(new Vector3f(p.x, p.z, p.y), newPlayerPos);
	}
}
