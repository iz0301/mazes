package mazes.main;

import java.util.ArrayList;
import java.util.List;

public class Map {

	int startX;
	int startY;
	List<Integer> xs = new ArrayList<Integer>();
	List<Integer> ys = new ArrayList<Integer>();
	List<Integer[]> solution = new ArrayList<Integer[]>();
	int width;
	int height;

	/**
	 * an array of an x and y value
	 * @param block
	 */
	public void addBlock(int x, int y) {
		if(!doesBlockExist(x,y)){
			xs.add(x);
			ys.add(y);
		}
	}

	/**
	 * returns true if it was removed, false if there was no block at the location
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean removeBlock(int x, int y) {
		int i = 0;
		while(i < xs.size()){
			if(xs.get(i) == x && ys.get(i) == y){
				xs.remove(i);
				ys.remove(i);
				return true;
			}
			i++;
		}
		return false;
	}

	public boolean doesBlockExist(int[] coords){
		return doesBlockExist(coords[0], coords[1]);
	}

	public boolean doesBlockExist(int x, int y){
		int i = 0;
		while(i < xs.size()){
			if(xs.get(i) == x && ys.get(i) == y){
				return true;
			}
			i++;
		}
		return false;
	}

	public int numberOfBorderingBlocks(int x, int y){
		int num = 0;
		if(doesBlockExist(x+1, y)){
			num++;
		}
		if(doesBlockExist(x-1, y)){
			num++;
		}
		if(doesBlockExist(x, y+1)){
			num++;
		}
		if(doesBlockExist(x, y-1)){
			num++;
		}
		return num;
	}

	/**
	 * returns the x,y coordinates of a random adjacent coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public int[] randomStepFrom(int x, int y){
		if(randomOneOrZero() == 1){
			if(randomOneOrZero() == 1){
				return new int[]{x+1, y};
			} else {
				return new int[]{x-1, y};
			}
		} else {
			if(randomOneOrZero() == 1){
				return new int[]{x, y+1};
			} else {
				return new int[]{x, y-1};
			}
		}
	}

	public int randomOneOrZero(){
		return Math.round(Math.round(Math.random()));
	}

	/**
	 * generates a new random maze with exactly 1 solution with the specifed width and height and returns the result
	 * minimum dimension is 3x3
	 * @param width
	 * @param height
	 * @return
	 */
	public static Map generateRandomMaze(int width, int height){
		//populate all blocks
		Map map = new Map();
		map.width = width;
		map.height = height;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				map.addBlock(x,y);
			}
		}

		boolean finishExists = false;
		int sx = 0;
		int sy = Math.round(Math.round(1+Math.random()*(height-2)));
		map.removeBlock(sx, sy);

		int[] lastPos = new int[]{sx, sy};
		List<Integer[]> solution = new ArrayList<Integer[]>();
		while(!finishExists){
			int[] testStep = map.randomStepFrom(lastPos);
			int numberOfTestTries = 0;
			while((!(map.doesBlockExist(testStep) && 
					((map.numberOfBorderingBlocks(testStep) == 3) || 
							(testStep[0] == width-1) && map.numberOfBorderingBlocks(testStep) == 2))) && numberOfTestTries < 20){
				testStep = map.randomStepFrom(lastPos);
				numberOfTestTries++;

			}
			if(numberOfTestTries < 20){
				map.removeBlock(testStep);
				lastPos = testStep;
				solution.add(new Integer[]{testStep[0], testStep[1]});
				if(map.numberOfBorderingBlocks(testStep) == 2){
					finishExists = true;
				}
			} else {
				for(int x = 0; x < width; x++){
					for(int y = 0; y < height; y++){
						map.addBlock(x,y);
					}
				}
				sx = 0;
				sy = Math.round(Math.round(1+Math.random()*(height-2)));
				solution = new ArrayList<Integer[]>();
				lastPos = new int[]{sx, sy};
				map.removeBlock(sx, sy);
			}
		}

		map.startX = sx;
		map.startY = sy;
		for(Integer[] i : solution){
			System.out.print("(");
			System.out.print(i[0]+","+i[1]);
			System.out.print("),");
		}
		map.solution = solution;
		Integer[] lp = solution.get(Math.round(Math.round(Math.random()*(solution.size()-1))));
		lastPos = new int[]{lp[0], lp[1]};
		//while(!map.allBlocksUsed()){
		int pathTries = 0;
		List<List<Integer[]>> paths = new ArrayList<List<Integer[]>>();
		paths.add(solution);
		while(pathTries < 2000){
			List<Integer[]> randPath = paths.get(Math.round(Math.round((Math.random()*(paths.size()-1)))));
			lp = randPath.get(Math.round(Math.round((Math.random()*(randPath.size()-1)))));
			lastPos = new int[]{lp[0], lp[1]};
			List<Integer[]> path = new ArrayList<Integer[]>();
			path.add(new Integer[]{lastPos[0], lastPos[1]});
			boolean giveUp = false;
			while(!giveUp){
				int[] testStep = map.randomStepFrom(lastPos);
				int numberOfTestTries = 0;
				while(!(map.doesBlockExist(testStep) && (map.numberOfBorderingBlocks(testStep)) == 3) && numberOfTestTries < 20){
					testStep = map.randomStepFrom(lastPos);
					numberOfTestTries++;
				}
				if(numberOfTestTries < 20){
					map.removeBlock(testStep);
					lastPos = testStep;
					path.add(new Integer[]{lastPos[0], lastPos[1]});
				} else {
					giveUp = true;
					paths.add(path);
				}
			}
			pathTries++;
		} 

		int randomFailures = 0;
		while(randomFailures < 200000){
			int y = Math.round(Math.round(1+Math.random()*(height-2)));
			int x = Math.round(Math.round(1+Math.random()*(width-2)));
			if(map.doesBlockExist(x, y) && map.numberOfBorderingBlocks(x, y) == 3 && x != 0 && x != width-1 && y!=0 && y!=height-1){
				map.removeBlock(x, y);
			} else {
				randomFailures++;
			}
		}

		/*	for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					if(map.doesBlockExist(x, y) && map.numberOfBorderingBlocks(x, y) >= 3 && x != 0 && x!=width-1 && y!=0 && y!=height-1){
						map.removeBlock(x, y);
					}
				}
		//	}

		}*/
		return map;
	}

	private int[] randomStepFrom(int[] coords) {
		return randomStepFrom(coords[0], coords[1]);
	}

	/**
	 * returns true if no block borders exatly 3
	 * false if there is a block that borders 3 others
	 * @return
	 */
	public boolean allBlocksUsed(){
		for(int i = 0; i < xs.size(); i++){
			if(numberOfBorderingBlocks(xs.get(i), ys.get(i)) == 3){
				return false;
			}
		}
		return true;
	}

	public void removeBlock(int[] coords) {
		removeBlock(coords[0], coords[1]);		
	}

	public int numberOfBorderingBlocks(int[] coords) {
		return numberOfBorderingBlocks(coords[0], coords[1]);
	}

}
