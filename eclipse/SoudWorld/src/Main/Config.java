package Main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Config 
{
	//======================================[ Attributs ]===========================================//
	
    private FileWriter writer;
    
    //======================================[ Constructeur ]===========================================//
	
    public Config()
	{
		
	}
	
	//======================================[ Fonctions ]===========================================//
	
    public void writeConfig(String filename)
    {
    	try 
		{
			writer = new FileWriter(filename);
			writer.write("{");
			writer.write("\r\n");
			writer.write('"' + "sounds" + '"' + ":" + "\r\n");
			writer.write("[");
			writer.write("\r\n");
			int k = 5;
			for (int elev = -40; elev <= 90; elev += 10)
			{
				if (Math.abs(elev) == 30)
					k = 6;
				else if (Math.abs(elev) == 40)
					k = 6;
				else if (elev == 50)
					k = 8;
				else if (elev == 60)
					k = 10;
				else if (elev == 70)
					k = 15;
				else if (elev == 80)
					k = 30;
				else if (elev == 90)
					k = 360;
				else
					k = 5;
				
				for (int theta = 0; theta < 360; theta += k)
				{
					String t;
					if (theta < 10)
						t = "00" + theta;
					else if (theta < 100)
						t = "0" + theta;
					else
						t = String.valueOf(theta);
					
					writer.write("{" + '"' + "name" + '"' + ":" 
									+ '"' + "L" + elev + "_" + theta + '"' + ","
									+ '"' + "file" + '"' + ":"
									+ '"' + "/" + "L" + elev + "e" + t + "a.wav" + '"' + "}");
					
					writer.write(",");
					writer.write("\r\n");
					
					writer.write("{" + '"' + "name" + '"' + ":" 
							+ '"' + "R" + elev + "_" + t + '"' + ","
							+ '"' + "file" + '"' + ":"
							+ '"' + "/" + "R" + elev + "e" + t + "a.wav" + '"' + "}");
					
					if (elev != 90 || theta != 355)
						writer.write(",");
					writer.write("\r\n");
				}
				writer.write("\r\n");
				
			}
			writer.write("\r\n");
			writer.write("]");
			writer.write("\r\n");
			writer.write("}");
			writer.close();
			
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
	
}

