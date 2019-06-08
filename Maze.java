import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

public class Maze extends JPanel implements KeyListener, Runnable {
	private ArrayList<Wall> walls;
	private ArrayList<Entity> doors;
	private ArrayList<Entity> switches;
	private ArrayList<Integer> switchDoorNumbers;

	private String[] mazes = {"Maze1", "Maze2", "Maze3"};

	private int width = 20;
	private int height = 20;
	private int gameOn = 3;

	private JFrame frame;
	private Thread thread;
	private Hero hero;
	private Monster monster;
	private Entity end;
	private Entity start;

	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;
	private boolean onMenu = true;

	public Maze() {
		frame = new JFrame("Maze");
		frame.add(this);

		hero = new Hero(100, 38, width, height, Color.GREEN);
		monster = new Monster(5, 38, width, height, Color.RED);
		createMaze("Maze1.txt");

		frame.addKeyListener(this);
		frame.setSize(1300, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		thread = new Thread(this);
		thread.start();
	}

	public void createMaze(String fileName) {
		walls = new ArrayList<Wall>();
		doors = new ArrayList<Entity>();
		switches = new ArrayList<Entity>();
		switchDoorNumbers = new ArrayList<Integer>();
		end = new Entity(frame.getWidth(), frame.getHeight(), width, height, Color.MAGENTA);
		start = new Entity(0, 0, width, height, Color.ORANGE);

		File name = new File(fileName);

		try {
			BufferedReader input = new BufferedReader(new FileReader(name));
			int x = 0;
			int y = 0;
			String text;
			while((text = input.readLine()) != null) {
				for(int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);

					if(c == '*')
						walls.add(new Wall(x, y, 30, 30, Color.BLUE));

					if (c == '-')
						doors.add(new Entity(x, y, 30, 30, Color.GRAY));

					if (c >= 48 && c <= 57) {
						switches.add(new Entity(x + 10, y + 10, 10, 10, Color.YELLOW));
						switchDoorNumbers.add(Character.getNumericValue(text.charAt(i)));
					}

					if (c == 'S') {
						hero.setX(x + width / 2);
						hero.setY(y + height / 2);
					}
					if (c == 'M') {
						monster.setX(x + width / 2);
						monster.setY(y + height / 2);
					}
					if (c == 'E') {
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

		if (!onMenu) {
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

			g2.setColor(hero.getColor());
			g2.fill(hero.getEllipse());

			g2.setColor(monster.getColor());
			g2.fill(monster.getEllipse());

			g2.setColor(Color.RED);
			g2.setFont(new Font("Helvetica", Font.PLAIN, 30));

			if(gameOn == 1) {
				g2.drawString("You win!", frame.getWidth() / 2, frame.getHeight() / 2);
			} else if (gameOn == 0) {
				g2.drawString("You lose!", frame.getWidth() / 2, frame.getHeight() / 2);
			}
		} else {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Helvetica", Font.BOLD, 50));
			g2.drawString("MAZE", frame.getWidth() / 2 - 70, frame.getHeight() / 3);
			g2.setFont(new Font("Helvetica", Font.PLAIN, 30));
			g2.drawString("Press SPACE to start.", frame.getWidth() / 2 - 140, frame.getHeight() / 3 * 2);
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

				monster.move(walls, doors);

				for (int i = 0; i < switches.size(); i++) {
					if (!switches.get(i).isOn() && hero.collision(switches.get(i).hitBox())) {
						doors.get(switchDoorNumbers.get(i)).setState(true);
						switches.get(i).setState(true);
					}
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
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			onMenu = false;
			gameOn = 2;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		Maze app = new Maze();
	}
}
