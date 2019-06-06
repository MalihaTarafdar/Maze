import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

public class Maze extends JPanel implements KeyListener, Runnable {
	private ArrayList<Wall> walls;
	private ArrayList<Monster> monsters;
	private ArrayList<Entity> doors;
	private ArrayList<Entity> switches;

	private String[] mazes = {"Maze1","Maze2","Maze3"};

	private int width = 20;
	private int height = 20;

	private JFrame frame;
	private Hero hero;
	private Monster monster;
	private Thread thread;

	private boolean gameOn = true;
	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;

	public Maze() {
		frame = new JFrame("Maze");
		frame.add(this);

		createMaze("Maze1.txt");
		hero = new Hero(20, 50, width, height, Color.GREEN);
		monster = new Monster(300, 50, width, height, Color.RED);

		frame.addKeyListener(this);
		frame.setSize(1300,750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	public void createMaze(String fileName) {
		walls = new ArrayList<Wall>();
		File name = new File(fileName);

		try {
			BufferedReader input = new BufferedReader(new FileReader(name));
			int x = 0;
			int y = 0;
			String text;
			while((text = input.readLine()) != null) {
				for(int i = 0; i < text.length(); i++) {
					if(text.charAt(i) == '-')
						walls.add(new Wall(x, y, width, height, Color.BLUE));
					x += width;
				}
				y += height;
				x = 0;
			}
		}
		catch (IOException io) {
			System.err.println("File does not exist");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,frame.getWidth(),frame.getHeight());

		for(Wall wall : walls) {
			g2.setColor(wall.getColor());
			g2.fill(wall.hitBox());
			g2.setColor(Color.ORANGE);
			g2.draw(wall.hitBox());
		}

		g2.setColor(hero.getColor());
		g2.fill(hero.getEllipse());

		g2.setColor(monster.getColor());
		g2.fill(monster.getEllipse());

		if(!gameOn) {
			g2.setColor(Color.RED);
			g2.setFont(new Font("Elephant",Font.PLAIN,30));
			g2.drawString("Game Over",frame.getWidth()/2,frame.getHeight()/2);
		}
	}

	public void run() {
		while(true) {
			if(gameOn) {
				if(up)
					hero.move('W', walls);
				if(right)
					hero.move('D', walls);
				if(down)
					hero.move('S', walls);
				if(left)
					hero.move('A', walls);

				//move monster based on time
				monster.move(1, walls);

				if (hero.getX() >= frame.getWidth() || hero.getY() >= frame.getHeight())
					gameOn = false;
				if (hero.collision(monster.hitBox()))
					gameOn = false;
			}
			try {
				thread.sleep(10);
			} catch(InterruptedException e){}
			repaint();
		}
	}

	public void keyPressed(KeyEvent e)  {
		if(e.getKeyCode() == KeyEvent.VK_W)
			up = true;
		if(e.getKeyCode() == KeyEvent.VK_D)
			right = true;
		if(e.getKeyCode() == KeyEvent.VK_S)
			down = true;
		if(e.getKeyCode() == KeyEvent.VK_A)
			left = true;
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
			up = false;
		if(e.getKeyCode() == KeyEvent.VK_D)
			right = false;
		if(e.getKeyCode() == KeyEvent.VK_S)
			down = false;
		if(e.getKeyCode() == KeyEvent.VK_A)
			left = false;
	}

	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		Maze app = new Maze();
	}
}
