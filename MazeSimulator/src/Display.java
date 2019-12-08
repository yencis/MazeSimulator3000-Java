import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JApplet;
import javax.swing.*;

import java.io.*;
import java.util.*;

/*Imagine refactoring code to make it readable
 * Made by Yencis/Api 
 * Insert Github Link
 */



public class Display extends JApplet{
	
	static int WIDTHMAZE = 50;
	static int HEIGHTMAZE = 50;
	static double SIZEMAZE = 0.2;
	
	static int SIDELENGTH = 40;
	static int INNERSIDELENGTH = 35;
	static String FONTSTYLE = "Dialog";
	static boolean mazeDrawn = false;
	static JButton inputMaze;
	static CustomMouse ev;
	static CustomKey kc;
	CustomItem ci;
	int[][] customMaze;
	boolean customMazeHasStart = false;
	boolean customMazeHasEnd = false;
	static boolean home = true;
	boolean random = false;
	boolean draw = false;
	static boolean simulate = false;
	static boolean doPath = false;
	static boolean iddfsFound = false;
	ArrayList<String> iddfsPath = null;
	TextField tf;
	TextField tf2;
	TextField size;
	TextField imports;
	JComboBox<String> cb;
	JPanel panel;
	TreeAlg t;
	
	int[][] simulateMaze = new int[HEIGHTMAZE][WIDTHMAZE];
	
	
	@SuppressWarnings("unchecked")
	public void init() {
		setName("Maze Simulator 3000");
		setSize(500,600);
		
		String[] items = {"DFS","BFS","IDDFS","Greedy","A*"};
		tf = new TextField(Integer.toString(WIDTHMAZE));
		tf2 = new TextField(Integer.toString(HEIGHTMAZE));
		size = new TextField(Double.toString(SIZEMAZE));
		imports = new TextField("File Pathname Here");
		cb = new JComboBox<String>(items);
		panel = new JPanel();
		panel.setBackground(Color.red);
		//add(inputMaze);
		//Evenet Listeners
		ev = new CustomMouse();
		addMouseListener(ev);
		kc = new CustomKey();
		ci = new CustomItem();
		cb.addItemListener(ci);
		//getContentPane().addKeyListener(kc);
		tf.addKeyListener(kc);
		tf2.addKeyListener(kc);
		size.addKeyListener(kc);
		setLayout(null);
		tf.setBounds(10,getHeight()-100,100,20);
		tf2.setBounds(10,getHeight()-140,100,20);
		size.setBounds(10,getHeight()-180,100,20);
		cb.setBounds(300, getHeight()-200, 100, 100);
		panel.setBounds(300,getHeight()-200,100,100);
		imports.setBounds(100,getHeight()-50,200, 30);
		//panel.add(cb);
		//panel.setOpaque(false);
		add(cb);
		add(panel);
		add(tf2);
		add(tf);
		add(size);
		add(imports);
		//add(cb);
		repaint();
		//cb.revalidate();
	}
	
	public void paint(Graphics g) {
		
		//Text Label for Text Field
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight()-200);
		g.fillRect(0, 0, 300, getHeight());
		g.fillRect(0, 500, getWidth(), getHeight());
		g.fillRect(400, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.fillRect(0, 0, (int)(WIDTHMAZE*SIZEMAZE)*40, (int)(HEIGHTMAZE*SIZEMAZE)*40);
		g.drawString("Width of Maze", 10, getHeight()-100);
		g.drawString("Height of Maze", 10, getHeight()-140);
		g.drawString("Size of Maze",10,getHeight()-180);
		g.drawString("Choose Your Algorithm",300,getHeight()-200);
		g.drawString("Errors: ",100,getHeight()-50);
		//Mouse Listener:
		int[] xy = ev.lastMousePress();
		boolean leftClick = ev.leftPress();
		System.out.println(xy[0]+" "+xy[1]);
		
		//Custom Buttons:
		
		//Repaint button (Dev Mode)
		if (inRectangle(xy[0],xy[1],400,100,100,40)) {
			drawButton(g,400,100,100,30,"Home",Color.GRAY);
			random = false;
			draw = false;
			simulate = false;
			drawBoard(g,simulateMaze,SIZEMAZE,false);
		}else {
			drawButton(g,400,100,100,30,"Home",Color.LIGHT_GRAY);
		}
		//Generate Random Map
		if (inRectangle(xy[0],xy[1],400,140,100,40)) {
			drawButton(g,400,140,100,30,"Generate\nRandom",Color.GRAY);
			random = true;
			draw = false;
			simulate = false;
		}else {
			drawButton(g,400,140,100,30,"Generate\nRandom",Color.LIGHT_GRAY);
		}
		
		if (inRectangle(xy[0],xy[1],400,180,100,40)) {
			drawButton(g,400,180,100,30,"Custom\nMaze",Color.GRAY);
			draw = true;
			random = false;
			simulate = false;
		}else {
			drawButton(g,400,180,100,30,"Custom\nMaze",Color.LIGHT_GRAY);
		}
		
		if (inRectangle(xy[0],xy[1],400,220,100,40)) {
			drawButton(g,400,220,100,30,"Simulate",Color.GRAY);
			draw = false;
			random = false;
			simulate = true;
		}else {
			drawButton(g,400,220,100,30,"Simulate",Color.LIGHT_GRAY);
		}
		
		if (inRectangle(xy[0],xy[1],400,260,100,40)) {
			drawButton(g,400,260,100,30,"UseCustomMaze",Color.GRAY);
			simulateMaze = copyMatrix(customMaze, HEIGHTMAZE,WIDTHMAZE);
		}else {
			drawButton(g,400,260,100,30,"UseCustomMaze",Color.LIGHT_GRAY);
		}
		
		if (inRectangle(xy[0],xy[1],400,300,100,40)) {
			drawButton(g,400,300,100,30,"ExportMaze",Color.GRAY);
			drawBoard(g,simulateMaze,SIZEMAZE,false);
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("maze.txt")));
				out.print(WIDTHMAZE+" "+HEIGHTMAZE+"\n");
				for (int r = 0;r<HEIGHTMAZE;r++) {
					for (int c = 0;c<WIDTHMAZE;c++) {
						out.print(simulateMaze[r][c]);
					}
					out.print("\n");
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error exporting maze");
			}
			System.out.println("Done exporting maze. Make a copy as the file will be overridden next export.\n"
					+ "You can find the file in the bin directory.");

		}else {
			drawButton(g,400,300,100,30,"ExportMaze",Color.LIGHT_GRAY);
		}
		
		if (inRectangle(xy[0],xy[1],400,340,100,40)) {
			drawButton(g,400,340,100,30,"ImportMaze",Color.GRAY);
			String file = imports.getText();
			try {
				customMaze = getTextMaze(file);
				System.out.println("Successfully imported");
				draw = true;
				HEIGHTMAZE = customMaze.length;
				WIDTHMAZE = customMaze[0].length;
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				g.drawString("Errors: Invalid FilePathName",100,getHeight()-50);
				System.out.println("Remember to correct your file '\\' to '\\\\'");
			}
		}else {
			drawButton(g,400,340,100,30,"ImportMaze",Color.LIGHT_GRAY);
		}
		
		
		
		
		
		if (draw) {
			if (inRectangle(xy[0],xy[1],0,0,(int)(WIDTHMAZE*SIZEMAZE)*40,(int)(HEIGHTMAZE*SIZEMAZE)*40)) {
				//drawButton(g,400,100,100,30,null,Color.GREEN);
				int heckers = (int)(40*SIZEMAZE);
				int val = customMaze[xy[1]/heckers][xy[0]/heckers];
				if (leftClick) {
					if (val==0) {
						customMaze[xy[1]/heckers][xy[0]/heckers]=1;
					}else if (val!=0) {
						customMaze[xy[1]/heckers][xy[0]/heckers]= 0;
						if (val==2)customMazeHasStart = false;
						if (val==3)customMazeHasEnd = false;
					}
				}else {
					if(!customMazeHasStart) {
						customMaze[xy[1]/heckers][xy[0]/heckers]=2;
						customMazeHasStart = true;
					}else if (!customMazeHasEnd){
						customMaze[xy[1]/heckers][xy[0]/heckers]=3;
						customMazeHasEnd = true;
					}
				}
				
			}
			drawCustomMaze(g, WIDTHMAZE,HEIGHTMAZE,SIZEMAZE);
		}
		//removeAll();
		if (random) {
			Maze r = new Maze();
			
			int[][] maze = r.generateMaze(WIDTHMAZE,HEIGHTMAZE);
			while(r.quality()) {
				maze = r.generateMaze(WIDTHMAZE,HEIGHTMAZE);
				//break;
			}
			drawBoard(g,maze,SIZEMAZE,false);
			simulateMaze = maze;
			random = false;
		}
		
		
		if (simulate) {
			simulate = false;
			drawBoard(g,simulateMaze,SIZEMAZE,false);
			simulate = true;
			String goal = "";
			String end = "";
			for (int r = 0;r<HEIGHTMAZE;r++) {
				for (int c = 0;c<WIDTHMAZE;c++) {
					//System.out.println(simulateMaze[r][c]);
					if (simulateMaze[r][c] == 2) {
						goal = c+" "+r;
					}else if (simulateMaze[r][c]==3) {
						end = c+" "+r;
					}
					
				}
			}
			int index = cb.getSelectedIndex();
			if (index==0) {
				int[][] tempMaze = copyMatrix(simulateMaze,HEIGHTMAZE,WIDTHMAZE);
				t = new TreeAlg(SIZEMAZE,g);
				t.simulate = true;
				t.dfs(tempMaze, goal, end, (int)(1000/(HEIGHTMAZE+WIDTHMAZE)));
				//dfs(tempMaze, goal, end, (int)(1000/(HEIGHTMAZE+WIDTHMAZE)), g);
			}else if (index == 1) {
				int[][] tempMaze = copyMatrix(simulateMaze,HEIGHTMAZE,WIDTHMAZE);
				t = new TreeAlg(SIZEMAZE,g);
				t.simulate = true;
				ArrayList<String> solution = t.bfs(tempMaze, goal, end, (int)(1000/(HEIGHTMAZE+WIDTHMAZE)));
				for (int i = 0;i<solution.size();i++) {
					String cell = solution.get(i);
					int x = Integer.parseInt(cell.split(" ")[0]);
					int y = Integer.parseInt(cell.split(" ")[1]);
					doPath = true;
					drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
					doPath = false;
				}
				System.out.println(solution);
			}else if (index == 2) {
				t = new TreeAlg(SIZEMAZE,g);
				iddfsFound = false;
				int iter = 0;
				int[][] originalMaze = copyMatrix(simulateMaze,HEIGHTMAZE,WIDTHMAZE);
				while (!iddfsFound) {
					
					ArrayList<String> path = new ArrayList<String>();
					path.add(goal);
					iter++;
					t.iddfs(simulateMaze, goal, end, (int)(1000/(HEIGHTMAZE+WIDTHMAZE)), 0, iter,path);
					
					simulateMaze = copyMatrix(originalMaze,HEIGHTMAZE,WIDTHMAZE);
					if (!iddfsFound) {
						g.setColor(Color.BLACK);
						g.fillRect(0,0,(int)(WIDTHMAZE*40*SIZEMAZE),(int)(HEIGHTMAZE*40*SIZEMAZE));
						simulate = false;
						drawBoard(g, originalMaze, SIZEMAZE, false);
					}
					/*try {
						Thread.sleep((int)(100/(HEIGHTMAZE+WIDTHMAZE)));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					simulate = true;
					//iddfsFound = true;
				}
				System.out.println(iddfsPath);
				if (iddfsPath!=null) {
					for (int i = 0;i<iddfsPath.size();i++) {
						String cell = iddfsPath.get(i);
						int x = Integer.parseInt(cell.split(" ")[0]);
						int y = Integer.parseInt(cell.split(" ")[1]);
						doPath = true;
						drawSquare(g,(int)(x*40*SIZEMAZE),(int)(y*40*SIZEMAZE),SIZEMAZE);
						doPath = false;
					}
					iddfsPath = null;
				}
				System.out.println("IDDFS Done");
				iddfsPath = null;
			}else if (index  == 3) { //greedy search
				int[][] tempMaze = copyMatrix(simulateMaze,HEIGHTMAZE,WIDTHMAZE);
				t = new TreeAlg(SIZEMAZE,g);
				t.greedySearch(tempMaze, goal, end, (1000/(HEIGHTMAZE+WIDTHMAZE)), false);
			}else if (index == 4) { //a* search
				int[][] tempMaze = copyMatrix(simulateMaze,HEIGHTMAZE,WIDTHMAZE);
				t = new TreeAlg(SIZEMAZE,g);
				t.greedySearch(tempMaze, goal, end, (1000/(HEIGHTMAZE+WIDTHMAZE)), true);
			}
			
			
			
			//paintComponent();
		}
		/*
		try {
			maze = getTextMaze("C:\\Users\\jfyen\\eclipse-workspace\\MazeSimulator\\src\\Maze1.txt");
			drawBoard(g, maze,1);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid Maze File");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No File Found");
			e.printStackTrace();
		}*/
		
		//removeAll();
		
	}
	
	public double calcDistance(int x, int y, int x1, int y1) {
		int xMode = Math.abs(x-x1);
		int yMode = Math.abs(y-y1);
		return xMode+yMode;
	}
	
	
	public int[][] copyMatrix(int[][] original, int r, int c){
		int[][] h = new int[r][c];
		for (int row = 0;row<r;row++) {
			for (int col = 0;col<c;col++) {
				h[row][col] = original[row][col];
			}
		}
		return h;
	}
	
	
	public void drawCustomMaze(Graphics g, int width, int height, double size) {
		if (customMaze == null) {
			customMaze = new int[height][width];
		}
		drawBoard(g,customMaze,size,true);
	}
	
	
	
	public int[][] getTextMaze(String textFile) throws IOException,NumberFormatException{
		BufferedReader f = new BufferedReader(new FileReader(textFile));
		StringTokenizer st = new StringTokenizer(f.readLine());
		int W = Integer.parseInt(st.nextToken());
		int H = Integer.parseInt(st.nextToken());
		int[][] maze = new int[H][W];
		for (int r = 0;r<H;r++) {
			st = new StringTokenizer(f.readLine());
			String line = st.nextToken();
			for (int c = 0;c<W;c++) {
				maze[r][c] = Integer.parseInt(line.substring(c,c+1));	
			}
		}
		return maze;
	}
	
	
	
	public static void drawSquare(Graphics g, int x, int y, double size) {    //default size 1
		int sideLength = (int) ((int)SIDELENGTH*size);
		int sideLengthInner = (int) ((int)INNERSIDELENGTH*size);
		if (doPath) {
			g.setColor(Color.green);
			g.fillRect(x, y, sideLength, sideLength);
		}else if(simulate) {
			g.setColor(Color.yellow);
			g.fillRect(x, y, sideLength, sideLength);
		}else {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x, y, sideLength, sideLength);
			g.setColor(Color.gray);
			g.fillRect(x+(sideLength-sideLengthInner)/2, y+(sideLength-sideLengthInner)/2,sideLengthInner, sideLengthInner);
		}
	}
	
	
	public void drawGrid(Graphics g, int x, int y, double size) {    //default size 1
		int sideLength = (int) ((int)SIDELENGTH*size);
		int sideLengthInner = (int) ((int)INNERSIDELENGTH*size);
		g.setColor(Color.black);
		g.fillRect(x, y, sideLength, sideLength);
		g.setColor(Color.white);
		g.fillRect(x+(sideLength-sideLengthInner)/2, y+(sideLength-sideLengthInner)/2,sideLengthInner, sideLengthInner);
		
	}
	
	public void drawStart(Graphics g, int x, int y, double size) {
		int sideLength = (int) ((int)SIDELENGTH*size);
		int sideLengthInner = (int) ((int)INNERSIDELENGTH*size);
		g.setColor(Color.green);
		g.fillRect(x, y, sideLength, sideLength);
		g.setColor(Color.white);
		Font f = new Font("Dialog",Font.PLAIN,(int) (SIDELENGTH*size));
		g.setFont(f);
		g.drawString("S", (int)(x+(sideLength-sideLengthInner)), (int)(y+sideLengthInner));
	}
	
	public void drawEnd(Graphics g, int x, int y, double size) {
		int sideLength = (int) ((int)SIDELENGTH*size);
		int sideLengthInner = (int) ((int)INNERSIDELENGTH*size);
		g.setColor(Color.red);
		g.fillRect(x, y, sideLength, sideLength);
		g.setColor(Color.white);
		Font f = new Font("Dialog",Font.PLAIN,(int) (SIDELENGTH*size));
		g.setFont(f);
		g.drawString("E", (int)(x+(sideLength-sideLengthInner)), (int)(y+sideLengthInner));
	}
	
	
	public void drawBoard(Graphics g, int[][] maze, double size, boolean grid) {
		int drawAtX = 0;
		int drawAtY = 0;
		int width = maze[0].length;
		int height = maze.length;
		for (int r = 0;r<height;r++) {
			for (int c = 0;c<width;c++) {
				if (maze[r][c]==1) {
					drawSquare(g,drawAtX,drawAtY,size);
				}else if (maze[r][c] == 2) {
					drawStart(g,drawAtX,drawAtY,size);
				}else if (maze[r][c] == 3) {
					drawEnd(g,drawAtX,drawAtY,size);
				}else if (maze[r][c]==0 && grid) {
					drawGrid(g,drawAtX,drawAtY,size);
				}
				
				drawAtX += SIDELENGTH*size;
			}
			drawAtX = 0;
			drawAtY += SIDELENGTH*size;
		}
	}
	
	public void drawButton(Graphics g, int x, int y, int width, int height, String text, Color color) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x, y, width, height);
		g.setColor(color);
		g.fillRect(x+2,y+2,width-4,height-4);
		g.setColor(Color.black);
		//g.drawString("BRUHAUHSHDSHHDa", 300, 300);
		if (text!= null) {
			//System.out.println("J");
			Font f = new Font("Dialog",Font.PLAIN,12);
			g.setFont(f);
			g.drawString(text, x, y+height/2);
		}
		
	}
	
	
	
	public boolean inRectangle(int x1, int y1, int x, int y, int width, int height) {
		if (x1>x&&x1<x+width&&y1>y&&y1<y+height) {
			return true;
		}
		return false;
	}
	
	
	class CustomMouse implements MouseListener{

		int x;
		int y;
		boolean leftClick;
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int mask = e.getModifiers();
			if (mask == 16) {
				leftClick = true;
			}else if (mask == 4) {
				leftClick = false;
			}
			x = e.getX();
			y = e.getY();
			repaint();
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
		
		public int[] lastMousePress() {
			int[] n = {x,y};
			return n;
		}
		
		public boolean leftPress() {
			return leftClick;
		}
		
	}
	
	class CustomKey implements KeyListener{
		
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar()=='\n') {
				if (e.getSource()==tf) {
					try {
						int width = Integer.parseInt(tf.getText());
						WIDTHMAZE = width;
					}catch (Exception e1) {
						tf.setText(Integer.toString(WIDTHMAZE));
					}
					repaint();
				}
				if (e.getSource()==tf2) {
					try {
						int height = Integer.parseInt(tf2.getText());
						HEIGHTMAZE = height;
					}catch (Exception e1) {
						tf2.setText(Integer.toString(HEIGHTMAZE));
					}
					repaint();
				}
				if (e.getSource()==size) {
					try {
						double size1 = Double.parseDouble(size.getText());
						SIZEMAZE = size1;
					}catch (Exception e1) {
						size.setText(Double.toString(SIZEMAZE));
					}
					repaint();
				}
			}
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			//System.out.println("XD");
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			//System.out.println("XD");
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	class CustomItem implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			//System.out.println(cb.getSelectedIndex());
		}
	}
}
