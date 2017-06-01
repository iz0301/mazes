package mazes.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MazesMain {

	static int WIDTH = 30;
	static int HEIGHT = 30;
	static boolean showSolution = false;
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		int size = 10;
		try{
			WIDTH = Integer.parseInt(args[0]);
			HEIGHT = Integer.parseInt(args[1]);
			size = Integer.parseInt(args[2]);
		} catch (NullPointerException e){}catch(ArrayIndexOutOfBoundsException e){}
		PlayPanel panel = new PlayPanel(Map.generateRandomMaze(WIDTH, HEIGHT), size);
		frame.add(panel);
		frame.setVisible(true);
		frame.requestFocus();
		panel.requestFocus();
		JButton regen = new JButton("Regen");
		regen.setBounds(0, 0, 10, 10);
		regen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.map = Map.generateRandomMaze(WIDTH,HEIGHT);
				panel.path.clear();
				panel.path.add(new Integer[]{panel.map.startX, panel.map.startY});
				panel.path.add(new Integer[]{panel.map.startX, panel.map.startY});
				showSolution = false;
				panel.repaint();
				panel.requestFocus();
				frame.revalidate();
				frame.repaint();
			}
		});
		
		JButton solution = new JButton("Solve");
		solution.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				showSolution = true;
				panel.repaint();
				frame.revalidate();
				frame.repaint();
			}
		});
		panel.add(solution);
		
		panel.add(regen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		//frame.setUndecorated(true);
		frame.setSize(700,700);
		frame.setVisible(true);
		panel.repaint();
		frame.revalidate();
		frame.repaint();
		
	}

}
