package LevelEditor;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Components.Point;


public class LevelWriter 
{
	//======================================[ Attributs ]===========================================//
	
    private int currentEvent;
    private PrintWriter writer;
    
    //======================================[ Constructeur ]===========================================//
	
    public LevelWriter(String filename)
	{
		try 
		{
			writer = new PrintWriter(filename);
		} 
		catch(FileNotFoundException e)
		{
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
		
		writer.append("{");
		writer.println();
		currentEvent = 0;
	}
	
	//======================================[ Fonctions ]===========================================//
	
	//write the field "levelName" in the file
	public void writeLevelName(String levelName)
	{
			writer.append('"' + "levelName" +'"' +':' + '"' + levelName + '"' + ',');
			writer.println();
			writer.append('"' + "events" +'"' +':' + '[');
			writer.println();
	}
	
	//write a monster in the current event
	public void writeMonster(String type, String radius, String life,
							 String posX, String posY, String posZ, String sound,
							 String damage, String v, String trigger,
							 ArrayList<Point> trajectory, boolean isNotFirst)
	{
		if (isNotFirst)
			writer.append(',');
		
		writer.append("{" + '"' + "type" +'"' +':' + '"' + type + '"' + ','
						+ '"' + "radius" +'"' +':' + '"' + radius + '"' + ','
						+ '"' + "life" +'"' +':' + '"' + life + '"' + ','
						+ '"' + "posX" +'"' +':' + '"' + posX + '"' + ','
						+ '"' + "posY" +'"' +':' + '"' + posY + '"' + ','
						+ '"' + "posZ" +'"' +':' + '"' + posZ + '"' + ','
						+ '"' + "sound" +'"' +':' + '"' + sound + '"' + ','
						+ '"' + "damage" +'"' +':' + '"' + damage + '"' + ','
						+ '"' + "velocity" +'"' +':' + '"' + v + '"' + ','
						+ '"' + "trigger" +'"' +':' + '"' + trigger + '"' + ',');
		
		writer.append('"' + "trajectory" + '"' + ':' + '[');
		int n =trajectory.size();
		for (int i = 0; i < n; i++)
		{
			Point p = trajectory.get(i);
			writer.append("{" + '"' + "X" + '"' + ':' + '"' + Integer.toString(p.x) + '"' + ','
							+ '"' + "Y" + '"' + ':' + '"' + Integer.toString(p.y) + '"' + "}");
			if (i < n-1)
				writer.append(",");
		}
		writer.append(']');
		writer.append("}");	
		writer.println();
	}
	
	public void beginEvent()
	{
		if (currentEvent == 0)
			writer.append("[");
		else
			writer.append(",[");
		writer.println();
		
		currentEvent += 1;
	}
	
	public void endEvent()
	{
		writer.append("]");
		writer.println();
	}
	
	public void end()
	{
		writer.append("]");
		writer.println();
		writer.append("}");
		writer.close();
	}
}
