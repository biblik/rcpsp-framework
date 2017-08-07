package plot;
import rcpsp.Instance;
import rcpsp.Solution;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame(Instance instance, Solution solution) throws Exception{   
		
		build(instance,solution);
		
	}

	private void build(Instance instance, Solution solution) throws Exception { 
		setTitle("RCPSPS");
		 
		setSize(800, 600);
		setResizable(true);
		
		this.setLocationRelativeTo(null); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    this.setContentPane(new Panel(instance, solution));
	    this.setVisible(true);
	}
}
