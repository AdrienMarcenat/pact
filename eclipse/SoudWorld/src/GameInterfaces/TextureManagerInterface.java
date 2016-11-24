package GameInterfaces;

import org.jsfml.graphics.Texture;


public interface TextureManagerInterface 
{
	public enum ID
    {
		MenuBackground, 
		levelSelectionBackground, 
		HoloBackground, 
		levelZero, 
		levelOne, 
		levelTwo, 
		levelThree, 
		leftArrow, 
		rightArrow,
		Window,
		Teleporteur,
		LifeBar,
		Title, 
		HitBlue, 
		ProgressBar, 
		ProgressBarLine, 
		LifeBarLine, 
		Cristal,
		square, 
		upArrow, 
		downArrow
    };
    
	public void load(ID id , String filename);
	public Texture get(ID id);
}
