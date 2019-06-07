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
	private ArrayList<Entity> monsterPath;

	private String[] mazes = {"Maze1","Maze2","Maze3"};

	private int width = 20;
	private int height = 20;
	private int gameOn = 2;

	private JFrame frame;
	private Hero hero;
	private Monster monster;
	private Thread thread;
	private Entity end;

	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;

	public Maze() {
		frame = new JFrame("Maze");
		frame.add(this);

		createMaze("Maze1.txt");
		hero = new Hero(100, 38, width, height, Color.GREEN);
		monster = new Monster(10, 38, width, height, Color.RED);

		frame.addKeyListener(this);
		frame.setSize(1300,750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	public void createMaze(String fileName) {
		walls = new ArrayList<Wall>();
		doors = new ArrayList<Entity>();
		switches = new ArrayList<Entity>();
		monsterPath = new ArrayList<Entity>();
		end = new Entity(frame.getWidth(), frame.getHeight(), 30, 30, Color.MAGENTA);

		File name = new File(fileName);

		try {
			BufferedReader input = new BufferedReader(new FileReader(name));
			int x = 0;
			int y = 0;
			String text;
			while((text = input.readLine()) != null) {
				for(int i = 0; i < text.length(); i++) {
					if(text.charAt(i) == '*')
						walls.add(new Wall(x, y, 30, 30, Color.BLUE));
					if (text.charAt(i) == '-')
						doors.add(new Entity(x, y, 30, 30, Color.GRAY));
					if (text.charAt(i) == 'X')
						switches.add(new Entity(x + 10, y + 10, 10, 10, Color.YELLOW));
					if (text.charAt(i) == ' ')
						monsterPath.add(new Entity(x + 15, y + 15, 5, 5, Color.RED));
					if (text.charAt(i) == 'E') {
						end.setX(x);
						end.setY(y);
					}

					x += 30;
				}
				y += 30;
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
		}
		for (Entity s : switches) {
			g2.setColor(s.getColor());
			if (!s.isOn())
				g2.fill(s.hitBox());
		}
		for (Entity door : doors) {
			g2.setColor(door.getColor());
			if (!door.isOn())
				g2.fill(door.hitBox());
		}
		for (Entity path : monsterPath) {

		}

		g2.setColor(hero.getColor());
		g2.fill(hero.getEllipse());

		g2.setColor(monster.getColor());
		g2.fill(monster.getEllipse());

		g2.setColor(Color.RED);
		g2.setFont(new Font("Helvetica", Font.PLAIN, 30));

		if(gameOn == 1) {
			g2.drawString("You win!", frame.getWidth()/2, frame.getHeight()/2);
		} else if (gameOn == 0) {
			g2.drawString("You lose!", frame.getWidth()/2, frame.getHeight()/2);
		}
	}

	public void run() {
		while(true) {
			if(gameOn == 2) {
				if(up)
					hero.move('W', walls, doors);
				if(right)
					hero.move('D', walls, doors);
				if(down)
					hero.move('S', walls, doors);
				if(left)
					hero.move('A', walls, doors);

				//monster.move(monsterPath);

				if (!switches.get(0).isOn() && hero.collision(switches.get(0).hitBox())) {
					doors.get(3).setState(true);
					switches.get(0).setState(true);
				} else if (!switches.get(1).isOn() && hero.collision(switches.get(1).hitBox())) {
					doors.get(0).setState(true);
					switches.get(1).setState(true);
				} else if (!switches.get(2).isOn() && hero.collision(switches.get(2).hitBox())) {
					doors.get(2).setState(true);
					switches.get(2).setState(true);
				} else if (!switches.get(3).isOn() && hero.collision(switches.get(3).hitBox())) {
					doors.get(1).setState(true);
					switches.get(3).setState(true);
				}


				if (hero.collision(end.hitBox()))
					gameOn = 1;
				if (hero.collision(monster.hitBox()))
					gameOn = 0;
			}
			try {
				thread.sleep(6);
			} catch(InterruptedException e){}
			repaint();
		}
	}

	public void keyPressed(KeyEvent e)  {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = true;
		else if (e.getKeyCode() == KeyEvent.VK_D)
			right = true;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			down = true;
		else if (e.getKeyCode() == KeyEvent.VK_A)
			left = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = false;
	}

	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		Maze app = new Maze();
	}
}
