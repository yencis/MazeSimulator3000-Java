import java.awt.Graphics;
import java.util.*;

//import Display.Coordinate;
//import Display.compCoord;

//Search Algorithms Here

public class TreeAlg {
	
	boolean simulate = false;
	boolean iddfsFound = false;
	ArrayList<String> iddfsPath = null;
	double SIZEMAZE;
	Graphics g;
	
	TreeAlg(double SIZEMAZE, Graphics g){
		this.SIZEMAZE = SIZEMAZE;
		this.g = g;
		
	}
	
	
	public void dfs(int[][] maze, String cell, String end, int speed) {
		if (cell.contentEquals(end)) {
			System.out.println("Finished");
			simulate = false;
			return;
		}else if (simulate){	
			int x = Integer.parseInt(cell.split(" ")[0]);
			int y = Integer.parseInt(cell.split(" ")[1]);
			try {
				if (maze[y][x]==1)return;
			}catch(Exception e) {
				return;
			}
			maze[y][x] = 1;
			Display.drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0;i<4;i++) {
				switch(i) {
				case 3:dfs(maze,x+" "+(y-1),end,speed);break;
				case 1:dfs(maze,(x+1)+" "+(y),end,speed);break;
				case 0:dfs(maze,(x-1)+" "+(y),end,speed);break;
				case 2:dfs(maze,(x)+" "+(y+1),end,speed);break;				
				}
			}
			
		}else {
			return;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> bfs(int[][] maze, String cell, String end, int speed){
		Queue<ArrayList<String>> q = new LinkedList<ArrayList<String>>();
		ArrayList<String> path = new ArrayList<String>();
		path.add(cell);
		q.add(path);
		ArrayList<String> answer = new ArrayList<String>();
		while (!q.isEmpty()){
			ArrayList<String> curPath = q.remove();
			String curCell = curPath.get(curPath.size()-1);
			if (curCell.contentEquals(end)) {
				answer = curPath;
				break;
			}
			int x = Integer.parseInt(curCell.split(" ")[0]);
			int y = Integer.parseInt(curCell.split(" ")[1]);
			try {
				if (maze[y][x]==1)continue;
			}catch(Exception e) {
				continue;
			}
			maze[y][x] = 1;
			Display.drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
			
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0;i<4;i++) {
				switch(i) {
				case 0:curPath.add((x)+" "+(y-1));break;
				case 1:curPath.add((x+1)+" "+(y));break;
				case 2:curPath.add((x-1)+" "+(y));break;
				case 3:curPath.add((x)+" "+(y+1));break;				
				}
				q.add((ArrayList<String>) curPath.clone());
				curPath.remove(curPath.size()-1);
			}
		}
		System.out.println("BFS Done");
		return answer;
	}
	
	public void iddfs(int[][] maze, String cell, String end, int speed, int iterations, int maxIter,ArrayList<String> path) {
		if (cell.contentEquals(end)) {
			System.out.println("Finished");
			simulate = false;
			iddfsFound = true;
			iddfsPath = path;
			return;
		}else if(iterations==maxIter) {
			return;
		}else if (simulate){			
			int x = Integer.parseInt(cell.split(" ")[0]);
			int y = Integer.parseInt(cell.split(" ")[1]);
			try {
				if (maze[y][x]==1)return;
			}catch(Exception e) {
				return;
			}
			maze[y][x] = 1;
			Display.drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0;i<4;i++) {
				switch(i) {
				case 0:path.add(x+" "+(y-1));iddfs(maze,x+" "+(y-1),end,speed,iterations+1,maxIter,(ArrayList<String>) path.clone());break;
				case 1:path.add((x+1)+" "+(y));iddfs(maze,(x+1)+" "+(y),end,speed,iterations+1,maxIter,(ArrayList<String>) path.clone());break;
				case 2:path.add((x-1)+" "+(y));iddfs(maze,(x-1)+" "+(y),end,speed,iterations+1,maxIter,(ArrayList<String>) path.clone());break;
				case 3:path.add(x+" "+(y+1));iddfs(maze,(x)+" "+(y+1),end,speed,iterations+1,maxIter,(ArrayList<String>) path.clone());break;				
				}
				path.remove(path.size()-1);
			}
			
		}else {
			return;
		}		
	}
	
	public void greedySearch(int[][] maze, String cell, String end, int speed, boolean astarmode) {
		
		int endx = Integer.parseInt(end.split(" ")[0]);
		int endy = Integer.parseInt(end.split(" ")[1]);
		
		int startx = Integer.parseInt(cell.split(" ")[0]);
		int starty = Integer.parseInt(cell.split(" ")[1]);
		
		Queue<Coordinate> q = new LinkedList<Coordinate>();
		Coordinate start = new Coordinate(cell,Integer.MAX_VALUE,0);
		q.add(start);
		while (!q.isEmpty()) {
			Coordinate curCoord = q.remove();
			String curCell = curCoord.coord;
			if (curCell.contentEquals(end)) {
				System.out.println("Found");
				break;
			}
			int x = Integer.parseInt(curCell.split(" ")[0]);
			int y = Integer.parseInt(curCell.split(" ")[1]);
			try {
				if (maze[y][x]==1)continue;
			}catch(Exception e) {
				continue;
			}
			maze[y][x] = 1;
			Display.drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0;i<4;i++) {
				int dx = 0;
				int dy = 0;
				switch(i) {
				case 0:dx = x; dy = y-1;break;
				case 1:dx = x+1; dy = y;break;
				case 2:dx = x-1; dy = y;break;
				case 3:dx = x; dy = y+1;break;				
				}
				double score = calcDistance(endx,endy,dx,dy);
				if (astarmode) {
					score += curCoord.length+1;
				}
				q.add(new Coordinate(dx+" "+dy,score,curCoord.length+1));
			}
			Coordinate[] a = new Coordinate[q.size()];
			q.toArray(a);
			Arrays.sort(a,new compCoord());
			q = new LinkedList<Coordinate>(Arrays.asList(a));
		}
		System.out.println("Greedy done");
		return;	
	}
	
	class Coordinate{
		double score;
		String coord;
		int length;
		Coordinate(String content, double score, int length){
			this.score = score;
			this.coord = content;
			this.length = length;
		}
		
		public String toString() {
			return coord;
		}
	}
	
	class compCoord implements Comparator<Coordinate>{
	
		public int compare(Coordinate a, Coordinate b) {
			// TODO Auto-generated method stub
			return (int)((a.score-b.score)*100);
		}
		
	}

	public double calcDistance(int x, int y, int x1, int y1) {
		int xMode = Math.abs(x-x1);
		int yMode = Math.abs(y-y1);
		return xMode+yMode;
	}

}
