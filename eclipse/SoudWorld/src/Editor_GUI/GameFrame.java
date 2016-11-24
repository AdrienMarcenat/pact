package Editor_GUI;

import java.awt.* ;

import javax.swing.* ;

import Main.SoundManager;

/** This is the JFrame class for the main game window. */

public final class GameFrame extends JFrame
{
	static final long serialVersionUID = 201503101423L ;
	
	/** The game panel, ie. the content of the window. */
	private GamePanel gamePanel ;
	
	/** The constructor :
	 * @param gameController The game controller.
	 * @param name The game name to be displayed at the top of the window.
	 * @param gameWidth The width of the game in blocks.
	 * @param gameHeight The height of the game in blocks.
	 */
	public GameFrame(String name, int gameWidth, int gameHeight, SoundManager s)
	{
		super(name) ;
		
		// Widgets
		
		setContentPane(gamePanel = new GamePanel(this, gameWidth,gameHeight, s)) ;
		
		// Epilogue
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		
		int sizeW = gameWidth  ;
		int sizeH = gameHeight ;

		setSize(sizeW,sizeH) ;
		setVisible(true) ;
		
		Dimension dimension = gamePanel.getSize() ;
		
		int widthToAdd  = (int)(sizeW - dimension.getWidth()) ;
		int heightToAdd = (int)(sizeH - dimension.getHeight()) ;
		
		setSize(sizeW + widthToAdd,sizeH + heightToAdd) ;
		setResizable(false) ;	
		
	}
	
	public void resizeFrame(int gameWidth, int gameHeight, SoundManager s)
	{
		setContentPane(gamePanel = new GamePanel(this, gameWidth,gameHeight, s)) ;
		int sizeW =  gameWidth  ;
		int sizeH =  gameHeight ;
		
		setVisible(false) ;
		setSize(sizeW,sizeH) ;
		setVisible(true) ;

		Dimension dimension = gamePanel.getSize() ;
		
		int widthToAdd  = (int)(sizeW - dimension.getWidth()) ;
		int heightToAdd = (int)(sizeH - dimension.getHeight()) ;
		setVisible(false) ;
		setSize(sizeW + widthToAdd,sizeH + heightToAdd);			
		setVisible(true) ;
		repaint();
	}
	
	
	public final void quit()
	{
		   int response 
		   	= JOptionPane.showInternalOptionDialog(
		   			 gamePanel,
                   "Really quit ?",
                   "Quit application",
                   JOptionPane.YES_NO_OPTION,
                   JOptionPane.WARNING_MESSAGE,
                   null,null,null) ;
		   switch (response) 
		   {
		   case JOptionPane.OK_OPTION:
			   System.exit(0) ;
		   case JOptionPane.NO_OPTION:
			   break ;
		   }		
	}

}
