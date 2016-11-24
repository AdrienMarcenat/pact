package HRTFTest;

import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.* ;

import org.jsfml.audio.Listener;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource.Status;
import org.jsfml.system.Vector3f;

import Components.Interpolation;
import Components.Point;
import Main.SoundManager;
import SpatializeSound.HRTFManager;

/** The frame JPanel containing the game. */

public final class HRTFTestPanel extends JPanel
{
	static final long serialVersionUID = 201503101423L ;
	
	public class myMouseListener implements MouseListener
	{
		@Override
		public final void mouseClicked(MouseEvent e)
		{
				if(e.getButton() == MouseEvent.BUTTON1)
				{
					//clic gauche
					int X = e.getX();
					int Y = e.getY();
					Point p = new Point(X, Y);
					p.x = X;
					p.y = Y;
					points.add(p);
					
					//On dessine un carré au point du click
					Graphics g = canvas.getGraphics();
					g.setColor(Color.RED);
					g.fillRect(X, Y, 5, 5);
				}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	//Buttons
	private JButton play;
	private JButton clear;
	private JButton move;

	//Labels
	private JLabel thetaLabel;
	
	JComboBox<String> sound;
	
	private Canvas canvas;
	myMouseListener ml;
	
	//Sound localisation
	private ArrayList<Point> points;
	
	private HRTFTestFrame gameFrame;
	
	private HRTFManager hrtf;
	private Sound so;
	
	/** Constructor:
	 * @param gameController The game controller.
	 * @param gameWidth The width of the game in blocks.
	 * @param gameHeight The height of the game in blocks.
	 */
	public HRTFTestPanel(HRTFTestFrame g, int gameWidth, int gameHeight, SoundManager s, SoundManager h)
	{
		super();
		
		Listener.setPosition(new Vector3f(400, 0, 300));
		Listener.setGlobalVolume(100);
		
		so = new Sound();
		hrtf = new HRTFManager(s, h);
		gameFrame = g;
		points = new ArrayList<Point>();
		ml = new myMouseListener();
		
		canvas = new Canvas();
		canvas.setSize(800, 600);
		canvas.addMouseListener(ml);
		canvas.setBackground(Color.WHITE);
		
		play = new JButton("play");
		clear = new JButton("clear");
		move = new JButton("move");
		thetaLabel = new JLabel();
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		sound = new JComboBox<String>(model);
		for (String id: s.getmSounds().keySet())
		{
			sound.addItem(id);
		}
		sound.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				//Object o = sound.getSelectedItem();
			}
		});
		
		add(play);
		add(move);
		add(clear);
		add(thetaLabel);
		add(canvas);
		add(sound);
		
		validate();
		
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				points.clear();
				Graphics g = canvas.getGraphics();
				canvas.paint(g);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				int maxDist = (int) Math.sqrt(300*300 + 400*400);
				for(Point p : points)
				{
					float x = - p.x + 400;
					float y = - p.y + 300;
					float dist = (float) Math.sqrt(x*x + y*y);
					Vector3f angles = Interpolation.computeThetaPhi(x, y, 0);
					int thetaL = (int) angles.x;
					int thetaR = (int) angles.y;

					String tL;
					if (thetaL < 10)
						tL = "00" + thetaL;
					else if (thetaL < 100)
						tL = "0" + thetaL;
					else
						tL = String.valueOf(thetaL);

					String tR;
					if (thetaR < 10)
						tR = "00" + thetaR;
					else if (thetaR < 100)
						tR = "0" + thetaR;
					else
						tR = String.valueOf(thetaR);
					
					short[] spatial = hrtf.spatialize(s.getSamples((String) sound.getSelectedItem()), "L0_" + tL, "R0_" + tR);
					SoundBuffer sb = new SoundBuffer();
					try {
						sb.loadFromSamples(spatial, 2, 44100);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					so.setBuffer(sb);
					so.setVolume(100*(maxDist - dist)*(maxDist - dist)/(maxDist*maxDist));
					so.play();
					while (so.getStatus() == Status.PLAYING)
					{
						
					}
				}
				
				//Comparaison avec SFML
				/*for(Point p : points)
				{
					SoundBuffer sb = s.get((String)sound.getSelectedItem());
					Sound son = new Sound(sb);
					son.setPosition(p.x, 0, p.y);
					son.setLoop(false);
					son.setMinDistance(300);
					son.play();
					while (son.getStatus() == Status.PLAYING)
					{
						
					}
				}*/
				
				validate();
				gameFrame.repaint();
			}
		});
		
		move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				hrtf.move((String) sound.getSelectedItem(), points);
			}
		});
	}
}
