package Save;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SaveWriter 
{
	//======================================[ Attributs ]===========================================//
	
    private FileWriter writer;
    
    //======================================[ Constructeur ]===========================================//
	
    public SaveWriter()
	{
		
	}
	
	//======================================[ Fonctions ]===========================================//
	
    public void init(String filename)
    {
    	try 
		{
			writer = new FileWriter(filename);
			writer.write("{");
			writer.write("\r\n");
		} 
		catch(FileNotFoundException e)
		{
			System.err.println("FileNotFoundException: " + e.getMessage());
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
	//write the saveFile
	public void Save(PlayerInfo p)
	{
			try 
			{
				writer.write('"' + "maxLife" +'"' +':' + '"' + p.getMaxLife() + '"' + ',');
				writer.write("\r\n");
				writer.write('"' + "level" +'"' +':' + '"' + p.getLevelClearedNumber() + '"' + ',');
				writer.write("\r\n");
				writer.write('"' + "cristalNumber" +'"' +':' + '"' + p.getCristalNumber() + '"' + ',');
				writer.write("\r\n");
				writer.write('"' + "levelStat" +'"' +':');
				writer.write("\r\n");
				writer.write('[');
				for (int i = 0; i < p.getLevelInfo().size(); i++)
				{
					LevelInfo l = p.getLevelInfo().get(i);
					
					writer.write('{');
					writer.write('"' + "cristalsCollected" +'"' +':' + '"' + l.getCristalsCollected() + '"' + ',');
					writer.write("\r\n");
					writer.write('"' + "hit" +'"' +':' + '"' + l.getHitNumber() + '"' + ',');
					writer.write("\r\n");
					
					writer.write('"' + "cristals" +'"' +':');
					writer.write("\r\n");
					writer.write('[');
					ArrayList<Integer> c = l.getCristalBooleans();
					for (int j = 0; j < c.size(); j++)
					{
						writer.write(String.valueOf(c.get(j)));
						if (i < c.size() - 1)
							writer.write(',');
					}
					writer.write(']');
					
					writer.write('}');
					
					if (i < p.getLevelInfo().size() - 1)
						writer.write(',');
				}
				writer.write(']');
				writer.write("\r\n");
				
				writer.write('}');
				
				writer.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
	
}
