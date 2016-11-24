package Entity;

import org.jsfml.audio.SoundSource.Status;
import org.jsfml.system.Time;
import org.jsfml.system.Vector3f;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

public class Speaker extends Monster
{
	//======================================[ Constructeur ]===========================================//
	
	public Speaker(Vector3f position, String id, SoundManager sm, HRTFManager hm, Monster.Trigger trigger) 
	{
		super(1, 1, position, id, sm, hm, 0, trigger);
		this.position.setPos(new Vector3f(-50, 50, 0));
		this.id = Monster.ID.Speaker;
		shape.setPosition(-50, -50);
		sound.play(); // le speaker parle au joueur
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//Le speaker guide le joueur par des messages vocaux
	@Override
	public void update(Vector3f newPlayerPos, Time dt) 
	{
		sound.normalPlay(false);
		if (sound.getStatus() == Status.STOPPED)
			life.setLife(0);
	}
	
}
