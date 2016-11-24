package Components;

import org.jsfml.system.Vector3f;

import com.sun.javafx.geom.Vec4f;

import ClientServer.ClientThread;
import ClientServer.KinectCommand;
import Entity.Player;
import Exception.KinectDisconnectedException;

//Classe contenant des fonctions de manipulation de vecteur
public class Interpolation 
{
	public static Vec4f interpolation(int xA, int yA, int xB, int yB, int tanA, int tanB)
	{
		return null;
	}
	
	public static float length(Vector3f v)
	{
		float x = v.x;
		float y = v.y;
		float z = v.z;
		return (float)Math.sqrt(x*x+y*y+z*z);
	}
	
	public static Vector3f unit(Vector3f v)
	{
		float l = (float)Math.sqrt(length(v));
		return new Vector3f(v.x/l, v.y/l, v.z/l);
	}
	
	//On retourne deux angles: un pour l'oreille droite et un pour la gauche (strabisme)
	//La tête fait 20 cm de diamètre
	public static Vector3f computeThetaPhi(float x, float y, float z)
	{
		int thetaL = 0;
		int thetaR = 0;
		int phi = 0;
		float xR = x - 10;
		float xL = x + 10;
		float dist = (float) Math.sqrt(xR*xR + y*y + z*z);
		
		float cosL = (float) (y/(Math.sqrt(xL*xL + y*y)));
		float cosR = (float) (y/(Math.sqrt(xR*xR + y*y)));
		float acosL = (float) (Math.acos(cosL)/Math.PI);
		float acosR = (float) (Math.acos(cosR)/Math.PI);
		float cosPhi = (float) (y/dist);
		float acosPhi = (float) (Math.acos(cosPhi)/Math.PI);
		
		if (x < 0)
		{
			thetaL = (int) (-180*acosL + 360);
			thetaR = (int) (-180*acosR + 360);
		}
		else
		{
			thetaL = (int) (180*acosL);
			thetaR = (int) (180*acosR);
		}
		phi = (int) (130*acosPhi - 40);
		phi = phi - (phi%5);
		thetaL = thetaL - (thetaL%5);
		thetaR = thetaR - (thetaR%5);
		return new Vector3f(thetaL, thetaR, phi);
	}
	
	//Retourne la distance au carré entre deux points
    public static float getDistance(Vector3f a, Vector3f b)
    {
    	return ((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y) + (a.z - b.z)*(a.z - b.z));
    }

	public static Vector3f getPlayerPosition(ClientThread client) throws KinectDisconnectedException 
	{
		KinectCommand cmd = new KinectCommand();
		cmd.setCmdName("getPos");
		client.threadSend(cmd.buildFormatedCommand());
		while(!client.getSomethingToRead())
		{
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String pos = client.threadRead();
		if (pos == null)
			return new Vector3f(1,1,1);
		
		//On vérifie si la Kinect est toujours connectée
		if (pos.equals("#DisCon$"))
		{
			throw new KinectDisconnectedException();
		}
		
		cmd = new KinectCommand(pos);
		if (cmd.isEmpty())
			return new Vector3f(-1, -1, -1);
		
		return new Vector3f(Float.valueOf(cmd.get(0)),
							Float.valueOf(cmd.get(2)), //z est la profondeur
							Float.valueOf(cmd.get(1))); // y est la hauteur
	}

	//renvoie le carré de la distance entre A et B
	public static float squareDistance(Vector3f posA, Vector3f posB)
	{
		float x = posA.x - posB.x;
		float y = posA.y - posB.y;
		float z = posA.z - posB.z;
		return (x*x+y*y+z*z);
	}
		
	//vérifie si player et mob sont en collision
	public static boolean collisionPlayer(int mobRadius, Vector3f mobPosition, Player player)
	{
		int playerRadius = player.getHitboxRadius();
		int limit = mobRadius+playerRadius;
		float distance = squareDistance(mobPosition, player.getPosition());
		if ( distance < (limit*limit))
			return true;
		return false;
	}
}
