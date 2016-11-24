package Components;

import java.util.ArrayList;


public class Trajectory 
{
	//======================================[ Attributs ]===========================================//
	
	private ArrayList<Point> trajectory;

	//======================================[ Constructeur ]===========================================//
	
	public Trajectory(ArrayList<Point> trajectory) 
	{
		this.trajectory = trajectory;
	}

	//======================================[ Fonctions ]===========================================//
	
	public Point getPosition(int i) 
	{
		return trajectory.get(i);
	}

	public void addPosition(Point pos) 
	{
		trajectory.add(pos);
	}
	
	public int size() 
	{
		return trajectory.size();
	}
}
