package Editor_GUI;

import java.awt.* ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.* ;
import Components.Point;
import Entity.Monster;
import LevelEditor.LevelWriter;
import Main.SoundManager;

/** The frame JPanel containing the game. */

public final class GamePanel extends JPanel
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
					int x = e.getX();
					int y = e.getY();
					Point p = new Point(x, y);
					p.x = x;
					p.y = y;
					trajectory.add(p);
					
					//On dessine un carré au point du click
					Graphics g = canvas.getGraphics();
					g.setColor(Color.RED);
					g.fillRect(x, y, 5, 5);
					if (trajectory.size() > 1)
					{
						Point prev = trajectory.get(trajectory.size() - 2);
						g.setColor(Color.BLACK);
						g.drawLine(x, y, prev.x, prev.y);
					}
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
	private JButton createButton;
	private JButton returnButton;
	private JButton addNormalMobButton;
	private JButton addTriggerAreaButton;
	private JButton addSpeakerButton;
	private JButton addHommingMobButton;
	private JButton addEventButton;
	private JButton endEventButton;
	private JButton endButton;
	private JButton undoButton;
	private JButton NvalidateButton;
	private JButton SvalidateButton;
	private JButton HvalidateButton;
	private JButton AvalidateButton;

	//Monster fields
	private JTextField lifeTimeField;
	private JTextField radiusField;
	private JTextField lifeField;
	private JTextField posXField;
	private JTextField posYField;
	private JTextField posZField;
	private JTextField damageField;
	private JTextField vField;
	JComboBox<String> sound;
	JComboBox<Monster.Trigger> trigger;
	
	private JLabel lifeTimeLabel;
	private JLabel radiusLabel;
	private JLabel lifeLabel;
	private JLabel posXLabel;
	private JLabel posYLabel;
	private JLabel posZLabel;
	private JLabel damageLabel;
	private JLabel soundLabel;
	private JLabel vLabel;
	private JLabel triggerLabel;
	
	private Canvas canvas;
	myMouseListener ml;
	
	private JTextField FileNameField;
	private LevelWriter levelWriter;
	
	//Monsters' trajectory
	private ArrayList<Point> trajectory;
	
	//check if it's the first monster in the event
	private boolean isNotFirst;
	
	private GameFrame gameFrame;
	
	/** Constructor:
	 * @param gameController The game controller.
	 * @param gameWidth The width of the game in blocks.
	 * @param gameHeight The height of the game in blocks.
	 */
	public GamePanel(GameFrame g, int gameWidth, int gameHeight, SoundManager s)
	{
		super();
		gameFrame = g;
		isNotFirst = false;
		trajectory = new ArrayList<Point>();
		ml = new myMouseListener();
		
		canvas = new Canvas();
		canvas.setSize(800, 600);
		canvas.addMouseListener(ml);
		canvas.setBackground(Color.WHITE);
		
		addNormalMobButton = new JButton("addNormalMob");
		addHommingMobButton = new JButton("addHommingMob");
		addSpeakerButton = new JButton("addSpeaker");
		addTriggerAreaButton = new JButton("addTriggerArea");
		addEventButton = new JButton("addEvent");
		createButton = new JButton("create");
		returnButton = new JButton("Return");
		endButton = new JButton("End");
		endEventButton = new JButton("EndEvent");
		undoButton = new JButton("Undo");
		NvalidateButton = new JButton("O.K.");
		HvalidateButton = new JButton("O.K.");
		SvalidateButton = new JButton("O.K.");
		AvalidateButton = new JButton("O.K.");
		
		FileNameField = new JTextField(10);
		lifeTimeField = new JTextField(3);
		radiusField = new JTextField(3);
		lifeField = new JTextField(3);
		posXField = new JTextField(3);
		posYField = new JTextField(3);
		posZField = new JTextField(3);
		damageField = new JTextField(3);
		vField = new JTextField(3);
		
		lifeTimeLabel = new JLabel("lifeTime:");
		radiusLabel = new JLabel("radius:");
		lifeLabel = new JLabel("life:");
		posXLabel = new JLabel("posX:");
		posYLabel = new JLabel("posY:");
		posZLabel = new JLabel("posZ:");
		damageLabel = new JLabel("damage:");
		soundLabel = new JLabel("sound:");
		vLabel = new JLabel("velocity:");
		triggerLabel = new JLabel("trigger:");
		
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
		
		DefaultComboBoxModel<Monster.Trigger> model2 = new DefaultComboBoxModel<Monster.Trigger>();
		trigger = new JComboBox<Monster.Trigger>(model2);
		for (Monster.Trigger id: Monster.Trigger.values())
		{
			trigger.addItem(id);
		}
		trigger.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				//Object o = sound.getSelectedItem();
			}
		});
		
		add(FileNameField);
		add(createButton);
		
		validate();
		
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String levelName = FileNameField.getText();
				levelWriter = new LevelWriter("Levels/" + levelName + ".txt");
				levelWriter.writeLevelName(levelName);
				
				remove(FileNameField);
				remove(createButton);
						
				add(addEventButton);
				add(returnButton);
				add(endButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				trajectory = new ArrayList<Point>();
				
				removeAll();
				
				add(FileNameField);
				add(createButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		addNormalMobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				removeAll();
			
				add(radiusLabel);
				add(radiusField);
				
				add(lifeLabel);
				add(lifeField);
				
				add(posXLabel);
				add(posXField);
		
				add(posYLabel);
				add(posYField);
				
				add(posZLabel);
				add(posZField);
				
				add(damageLabel);
				add(damageField);
				
				add(soundLabel);
				add(sound);
			
				add(vLabel);
				add(vField);
				
				add(triggerLabel);
				add(trigger);
				
				add(undoButton);
				add(NvalidateButton);
				add(canvas);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		addHommingMobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				removeAll();

				add(radiusLabel);
				add(radiusField);
				
				add(lifeLabel);
				add(lifeField);
				
				add(posXLabel);
				add(posXField);
		
				add(posYLabel);
				add(posYField);
				
				add(posZLabel);
				add(posZField);
				
				add(damageLabel);
				add(damageField);
				
				add(soundLabel);
				add(sound);
			
				add(vLabel);
				add(vField);
				
				add(triggerLabel);
				add(trigger);
				
				add(lifeTimeLabel);
				add(lifeTimeField);
				
				add(HvalidateButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		addSpeakerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				removeAll();
				
				add(posXLabel);
				add(posXField);
		
				add(posYLabel);
				add(posYField);
				
				add(posZLabel);
				add(posZField);
				
				add(soundLabel);
				add(sound);
				
				add(triggerLabel);
				add(trigger);
				
				add(SvalidateButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		addTriggerAreaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				removeAll();
				
				add(radiusLabel);
				add(radiusField);
				
				add(posXLabel);
				add(posXField);
		
				add(posYLabel);
				add(posYField);
				
				add(posZLabel);
				add(posZField);
				
				add(damageLabel);
				add(damageField);
				
				add(soundLabel);
				add(sound);
				
				add(triggerLabel);
				add(trigger);
				
				add(AvalidateButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		NvalidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.writeMonster("N",
										radiusField.getText(),
										lifeField.getText(),
										posXField.getText(),
										posYField.getText(),
										posZField.getText(),
										String.valueOf(sound.getSelectedItem()),
										damageField.getText(),
										vField.getText(),
										String.valueOf(trigger.getSelectedItem()),
										trajectory, isNotFirst);
				
				if (!isNotFirst)
					isNotFirst = true;
				
				removeAll();
				
				add(addNormalMobButton);
				add(addHommingMobButton);
				add(addSpeakerButton);
				add(addTriggerAreaButton);
				add(endEventButton);
				add(returnButton);
				
				trajectory = new ArrayList<Point>();
				
				validate();
				gameFrame.repaint();
			}
		});
		
		HvalidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.writeMonster("H",
										radiusField.getText(),
										lifeField.getText(),
										posXField.getText(),
										posYField.getText(),
										posZField.getText(),
										String.valueOf(sound.getSelectedItem()),
										damageField.getText(),
										vField.getText(),
										String.valueOf(trigger.getSelectedItem()),
										trajectory, isNotFirst);
				
				if (!isNotFirst)
					isNotFirst = true;
				
				removeAll();
				
				add(addNormalMobButton);
				add(addHommingMobButton);
				add(addSpeakerButton);
				add(addTriggerAreaButton);
				add(endEventButton);
				add(returnButton);
				
				trajectory = new ArrayList<Point>();
				
				validate();
				gameFrame.repaint();
			}
		});
		
		AvalidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.writeMonster("A",
										"",
										"",
										posXField.getText(),
										posYField.getText(),
										posZField.getText(),
										String.valueOf(sound.getSelectedItem()),
										damageField.getText(),
										"",
										String.valueOf(trigger.getSelectedItem()),
										trajectory, isNotFirst);
				
				if (!isNotFirst)
					isNotFirst = true;
				
				removeAll();
				
				add(addNormalMobButton);
				add(addHommingMobButton);
				add(addSpeakerButton);
				add(addTriggerAreaButton);
				add(endEventButton);
				add(returnButton);
				
				trajectory = new ArrayList<Point>();
				
				validate();
				gameFrame.repaint();
			}
		});
		
		SvalidateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.writeMonster("S",
										"",
										"",
										posXField.getText(),
										posYField.getText(),
										posZField.getText(),
										String.valueOf(sound.getSelectedItem()),
										"",
										"",
										String.valueOf(trigger.getSelectedItem()),
										trajectory, isNotFirst);
				
				if (!isNotFirst)
					isNotFirst = true;
				
				removeAll();
				
				add(addNormalMobButton);
				add(addHommingMobButton);
				add(addSpeakerButton);
				add(addTriggerAreaButton);
				add(endEventButton);
				add(returnButton);
				
				trajectory = new ArrayList<Point>();
				
				validate();
				gameFrame.repaint();
			}
		});
		
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (trajectory.size() > 0)
				{
					trajectory.remove(trajectory.size() - 1);
					
					Graphics g = canvas.getGraphics();
					canvas.paint(g);
					
					for (int i = 0; i < trajectory.size(); i++)
					{
						Point p = trajectory.get(i);
						int x = p.x;
						int y = p.y;
						g.setColor(Color.RED);
						g.fillRect(x, y, 5, 5);
						if (i > 0)
						{
							Point prev = trajectory.get(i - 1);
							g.setColor(Color.BLACK);
							g.drawLine(x, y, prev.x, prev.y);
						}
					}
					
					if (trajectory.size() == 0)
						isNotFirst = false;
				}
				
				validate();
				gameFrame.repaint();
			}
		});
		
		addEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.beginEvent();
				
				removeAll();
				
				add(addNormalMobButton);
				add(addHommingMobButton);
				add(addSpeakerButton);
				add(addTriggerAreaButton);
				add(endEventButton);
				add(returnButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		endEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isNotFirst = false;
				
				levelWriter.endEvent();
				
				removeAll();
				
				add(addEventButton);
				add(returnButton);
				add(endButton);
				
				validate();
				gameFrame.repaint();
			}
		});
		
		endButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				levelWriter.end();
				
				removeAll();
				
				add(FileNameField);
				add(createButton);
				
				validate();
				gameFrame.repaint();	
			}
		});
		
	}
	
}
