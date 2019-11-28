import java.util.*;


public class Maze {
	
	int[][] maze;
	int HEIGHT;
	int WIDTH;
	
	public int[][] generateMaze(int width, int height){
		maze = new int[height][width];
		HEIGHT = height;
		WIDTH = width;
		
		//Make maze out of all blocks
		for (int r = 0;r<height;r++) {
			for (int c = 0;c<width;c++) {
				maze[r][c] = 1;
			}
		}
		
		int x = Math.abs(((int)(Math.random()*100))%width);
		int y = Math.abs(((int)(Math.random()*100))%height);
		//System.out.println(x+" "+y);
		String startCell = x+" "+y+" "+0+" "+0;
		Queue<String> q = new LinkedList<String>();
		q.add(startCell);
		while(!q.isEmpty()) {
			//System.out.println(q);
			String cell = q.remove();
			int xCell = Integer.parseInt(cell.split(" ")[0]);
			int yCell = Integer.parseInt(cell.split(" ")[1]);
			int xD = Integer.parseInt(cell.split(" ")[2]);
			int yD = Integer.parseInt(cell.split(" ")[3]);
			try {
				maze[yCell][xCell] = 0;
				maze[yCell-yD][xCell-xD] = 0;
			}catch(Exception e1) {
				continue;
			}
			int iter = 0;
			int iterations = (int)(Math.random()*100)%4+1;
			if (width<20||height<20) {
				iterations = 4;
			}
			for (int i = 0;i<iterations;i++) {
				int dX = 0;
				int dY = 0;
				
				int dir = Math.abs(((int)(Math.random()*100))%4);
				
				switch(dir) {
				case 0:dY = -1;break;
				case 1:dX = 1; break;
				case 2:dY = 1; break;
				case 3:dX = -1; break;
				}
				try {
					//if (maze[yCell+dY][xCell+dX]==0||maze[yCell+dY*2][xCell+dX*2]==0||(int)(Math.random()*100)%5==5) {
					if (twoNeighbors(yCell+dY*2,xCell+dX*2)||twoNeighbors(yCell+dY,xCell+dX)||maze[yCell+dY][xCell+dX]==0||maze[yCell+dY*2][xCell+dX*2]==0) {	
						continue;
					}else if (maze[yCell+dY*2][xCell+dX*2]==1&&maze[yCell+dY][xCell+dX]==1){
						//System.out.println(q);
						iter++;
						if ((int)(Math.random()*100)%5!=1)
							q.add((xCell+dX*2)+" "+(yCell+dY*2)+" "+dX+" "+dY);
						if (width<50&&height<50) {
							if (iter!=2) {
								continue;
							}else {
								break;
							}
						}
						//break;
						
					}
				}catch(Exception e) {
					continue;
				}
				
			}
			
		}
		
		maze[y][x] = 2;
		
		while(true) {
			int xEnd = Math.abs(((int)(Math.random()*100))%width);
			int yEnd = Math.abs(((int)(Math.random()*100))%height);
			if (maze[yEnd][xEnd]==0) {
				maze[yEnd][xEnd]=3;
				break;
			}else if (maze[yEnd][xEnd]==2) {
				break;
			}
		}
			
		return maze;
	}
	
	public boolean twoNeighbors(int yCell, int xCell) { //checks if cell has enough neighbors
		int neighbors = 0;
		for (int i = 0;i<4;i++) {
			try {
				switch (i){
				case 0: if (maze[yCell-1][xCell]==0)neighbors++;break;
				case 1: if (maze[yCell][xCell+1]==0)neighbors++;break;
				case 2: if (maze[yCell+1][xCell]==0)neighbors++;break;
				case 3: if (maze[yCell][xCell-1]==0)neighbors++;break;
				}
			}catch(Exception e) {
				continue;
			}
		}
		if (neighbors<2)return false;
		return true;
	}
	
	public boolean quality() {
		double walls = 0;
		double space = 0;
		for (int r = 0;r<HEIGHT;r++) {
			for (int c = 0;c<WIDTH;c++) {
				if (maze[r][c]==0) {
					space++;
				}else {
					walls++;
				}
			}
		}
		double ratio = walls/space;
		//System.out.println(ratio);
		if (WIDTH<4||HEIGHT<4) {
			//System.out.println("ok you can go");
			return false;
		}
		return !(ratio<2);
	}
	

}
