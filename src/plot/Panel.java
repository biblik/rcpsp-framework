package plot;

import rcpsp.Instance;
import rcpsp.Solution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int LEFT_OFFSET = 50;
	private static final int NB_VERTICAL_TICK = 4;
	private static final int NB_HORIZONTAL_TICK = 10;
	
	private Instance instance;
	private Integer[] solution;
	private int objectiveValue;
	private int xMax;
	private int[] yMax;
	private Color[] colors;
	private ArrayList<RectangleActivity> plotActivities; //Liste des tâches côté IHM (une même tâche peut avoir plusieurs tâches IHM) 

	
	public Panel(Instance instance, Solution sol) throws Exception
	{ 
		this.instance = instance;
		this.objectiveValue = sol.getObjectiveValue();
		this.solution = sol.getBeginTimeActivities(); 
		
		// Axes size
		xMax = objectiveValue;
		int nbRessources = instance.getNbResources();
		yMax = new int[nbRessources];
		for(int i = 0; i < nbRessources; i++)
		{
			yMax[i] = instance.getCapacityResource(i);
		}
		
		// Color definition
		int nbActivities = instance.getNbActivities();
		colors = new Color[nbActivities];
		for(int j = 0; j < nbActivities; j++)
		{
			colors[j] = RandomColor.getRandomColor();
		}
	}

	public void paintComponent(Graphics g)
	{
		plotActivities = new ArrayList<RectangleActivity>();
		Graphics2D g2d = (Graphics2D)g; 
		float[] dotStyle = {5,5};
		float[] normalStyle = {1,0};
		BasicStroke dot = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dotStyle, 0);
		BasicStroke bold = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, normalStyle, 0);
		BasicStroke standard = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, normalStyle, 0);
		
		int nbResources = instance.getNbResources();
		int nbActivities = instance.getNbActivities();
		int width = getWidth();
		int height = getHeight() - 50;
		
		// Print makespan
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
		g.drawString("Makespan : " + objectiveValue, width/2-40, 20);
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
		
		// Loop on resources (one plot per resource)
		for(int i = 0; i < nbResources; i++)
		{
			g2d.setStroke(bold);
			
			int heightBase = height/nbResources*(i+1);
			g.drawLine(0, heightBase, width, heightBase);	

			
			int stepV=(int) ((height-150)/(nbResources*yMax[i])); 
			int heightBis = stepV*yMax[i];
			
			g.drawLine(LEFT_OFFSET, heightBase-heightBis, width, heightBase-heightBis);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 16)); 
			g.drawString("Resource "+i, width/2-20, heightBase-heightBis-5);
			g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
	
			g.drawLine(LEFT_OFFSET, heightBase, LEFT_OFFSET, heightBase-heightBis);
			int stepH = (width-LEFT_OFFSET)/xMax;
			
			// Trace rectangle of consumption
			// For each step time
			for(int t = 0; t < xMax; t++)
			{
				int nbResourcesTemp = 0;
				for(int j = 0; j < nbActivities; j++)
				{
					int begin = solution[j];
					int end = -1;
					try
					{
						end = begin + instance.getDurationActivity(j);
					}
					catch (Exception e)
					{ 
						e.printStackTrace();
					}
					if(begin <= t && end > t)
					{
						int consumption = 0;
						try
						{
							consumption = instance.getConsumptionResourceActivity(i, j);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						nbResourcesTemp += consumption;
						g.setColor(colors[j]);
						int x1 = LEFT_OFFSET+t*stepH;
						int widthR = stepH;
						int y1 = heightBase-nbResourcesTemp*stepV;
						int heightR = consumption*stepV;
						plotActivities.add(new RectangleActivity(x1, x1+widthR, y1, y1+heightR, j));
						g.fillRect(x1, y1, widthR, heightR);
					}
				}
			}
			
			// Tick on axes
			// 4 marks on vertical axes
			int stepTickV = yMax[i] / NB_VERTICAL_TICK;
			g2d.setStroke(standard);	
			g.setColor(Color.BLACK);
			for(int k = 0; k < NB_VERTICAL_TICK; k++)
			{
				g.drawLine(LEFT_OFFSET-10, heightBase-(k+1)*stepTickV*stepV, LEFT_OFFSET, heightBase-(k+1)*stepTickV*stepV);
				g.drawString(String.valueOf((k+1)*stepTickV), LEFT_OFFSET-25, heightBase-(k+1)*stepTickV*stepV+5);
			}
			
			// 10 marks on horizontal axes
			int stepTickH = xMax / NB_HORIZONTAL_TICK;
			for(int k=0;k<=xMax;k++)
			{	
				if(k%stepTickH==0)
				{
					// --- Long tick
					g.drawString(String.valueOf(k), LEFT_OFFSET+(k)*stepH-7, heightBase+25);
					g.drawLine(LEFT_OFFSET+k*stepH, heightBase, LEFT_OFFSET+k*stepH, heightBase+10);
				}
				else
				{
					// --- Short tick
					g.drawLine(LEFT_OFFSET+k*stepH, heightBase, LEFT_OFFSET+k*stepH, heightBase+5);
				}
			}
			
			// --- Dot on vertival grid
			g2d.setStroke(dot);	
			for(int y = 0; y < yMax[i]; y++)
			{
				if (y%stepTickV==0)
				g.drawLine(LEFT_OFFSET, heightBase-y*stepV, width, heightBase-y*stepV);
			}
			
		}		
		
		addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent arg0)
			{
				
				int x=arg0.getX();
				int y=arg0.getY();
				for (RectangleActivity rec:plotActivities)
				{
					if (rec.getX1()<=x && rec.getX2()>=x && rec.getY1()<=y && rec.getY2()>= y)
						setToolTipText("Activity "+ rec.getIndexActivity());
				}
			}  
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});

	}

}
