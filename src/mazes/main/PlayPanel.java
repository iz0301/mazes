package mazes.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class PlayPanel extends JPanel implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4904001329579896158L;
	Map map = Map.generateRandomMaze(10, 10);
	int wallSize = 10;
	public static final int YBUFF = 50;
	List<Integer[]> path = new ArrayList<Integer[]>();
	
	public PlayPanel(Map map, int wallSize){
		this.map = map;
		this.wallSize = wallSize;
		path.add(new Integer[]{map.startX, map.startY});
		path.add(new Integer[]{map.startX, map.startY});
		this.addKeyListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.clearRect(0, 0, (wallSize+1)*map.width, (wallSize+1)*map.height);
		for(int x = 0; x < map.width; x++){
			for(int y = 0; y < map.height; y++){
				if(map.doesBlockExist(x, y)){
					g.setColor(new Color(255,0,0));
					g.fillRect(x*wallSize+wallSize, y*wallSize+wallSize+YBUFF, wallSize, wallSize);
				}
			}
		}
		for(Integer[] p : path){
			g.setColor(new Color(0,0,255));
			g.fillRect(p[0]*wallSize+Math.round(Math.round(wallSize*1.25)), YBUFF+p[1]*wallSize+Math.round(Math.round(wallSize*1.25)), wallSize/2, wallSize/2);
		}
		for(Integer[] p : map.solution){
			g.setColor(new Color(0,255,0));
			if(MazesMain.showSolution){
				g.fillRect(p[0]*wallSize+Math.round(Math.round(wallSize*1.25)), YBUFF+p[1]*wallSize+Math.round(Math.round(wallSize*1.25)), wallSize/2, wallSize/2);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Integer[] lastPos = path.get(path.size()-1);
		Integer[] newPos = lastPos;
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			newPos = new Integer[]{lastPos[0], lastPos[1]-1};
			break;
		case KeyEvent.VK_DOWN:
			newPos = new Integer[]{lastPos[0], lastPos[1]+1};
			break;
		case KeyEvent.VK_LEFT:
			newPos = new Integer[]{lastPos[0]-1, lastPos[1]};
			break;
		case KeyEvent.VK_RIGHT:
			newPos = new Integer[]{lastPos[0]+1, lastPos[1]};
			break;
		}
		boolean backtracking = false;
		//if going backward
		if(path.size() > 2){
			if(newPos[0] == path.get(path.size()-2)[0] && newPos[1] == path.get(path.size()-2)[1]){
				path.remove(path.get(path.size()-1));
				backtracking = true;
			} 
		}
		if(newPos != lastPos && !map.doesBlockExist(newPos[0], newPos[1]) && !backtracking && newPos[0]>0){
			path.add(newPos);
		}
		this.repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
