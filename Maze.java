import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

public class Maze extends JPanel implements KeyListener, MouseListener, Runnable {
	private static final long serialVersionUID = 42l;

	private ArrayList<Wall> walls;
	private ArrayList<Entity> doors;
	private ArrayList<Entity> switches;
	private ArrayList<Entity> portals;
	private ArrayList<Monster> monsters;
	private ArrayList<Integer> switchDoorNumbers;

	private String[] mazes = {"Maze1", "Maze2", "Maze3"};

	private int entityWidth = 20;
	private int entityHeight = 20;
	private int wallWidth = 30;
	private int wallHeight = 30;
	private int gameOn = 3;
	private int randPortal = 0;

	private JFrame frame;
	private Thread thread;
	private Hero hero1;
	private Hero hero2;
	private Monster monster;
	private Entity end;
	private Entity start;
	private Menu menu;

	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;
	private boolean teleported = false;

	public Maze() {
		frame = new JFrame("Maze");
		frame.add(this);

		hero1 = new Hero(0, 0, entityWidth, entityHeight, Color.GREEN);
		createMaze("Maze2.txt");
		menu = new Menu();

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
		portals = new ArrayList<Entity>();
		monsters = new ArrayList<Monster>();
		switchDoorNumbers = new ArrayList<Integer>();
		end = new Entity(frame.getWidth(), frame.getHeight(), entityWidth, entityHeight, Color.MAGENTA);
		start = new Entity(0, 0, entityWidth, entityHeight, Color.ORANGE);

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
						walls.add(new Wall(x, y, wallWidth, wallHeight, Color.BLUE));

					if (c == '-')
						doors.add(new Entity(x, y, wallWidth, wallHeight, Color.GRAY));

					if (c >= 48 && c <= 57) {
						switches.add(new Entity(x + 10, y + 10, 10, 10, Color.YELLOW));
						switchDoorNumbers.add(Character.getNumericValue(text.charAt(i)));
						portals.add(new Entity(x + 10, y + 10, 15, 15, Color.MAGENTA));
					}

					if (c == 'S') {
						hero1.setX(x + entityWidth / 2);
						hero1.setY(y + entityHeight / 2);
					}
					if (c == 'M') {
						monsters.add(new Monster(x + entityWidth / 2, y + entityHeight / 2, entityWidth, entityHeight, Color.RED, 1));
					}
					if (c == 'E') {
						end.setX(x);
						end.setY(y);
					}

					x += wallWidth;
				}
				y += wallHeight;
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
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		if (!menu.isOnScreen()) {
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
			for (Entity p : portals) {
				g2.setColor(p.getColor());
				if (p.isOn())
					g2.draw(p.hitBox());
			}

			g2.setColor(hero1.getColor());
			g2.fill(hero1.getEllipse());

			for (Monster m : monsters) {
				g2.setColor(m.getColor());
				g2.fill(m.getEllipse());
			}

			g2.setColor(Color.RED);
			g2.setFont(new Font("Helvetica", Font.PLAIN, 50));

			if(gameOn == 1) {
				g2.drawString("You win!", frame.getWidth() / 2 - 100, frame.getHeight() / 2);
			} else if (gameOn == 0) {
				g2.drawString("You lose!", frame.getWidth() / 2 - 100, frame.getHeight() / 2);
			}
		} else {
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Helvetica", Font.BOLD, 50));
			g2.drawString("MAZE", frame.getWidth() / 2 - 70, frame.getHeight() / 3);
			g2.setFont(new Font("Helvetica", Font.PLAIN, 30));
			g2.drawString("Press SPACE to start", frame.getWidth() / 2 - 140, frame.getHeight() / 2);
		}
	}

	public void run() {
		while(true) {
			if(gameOn == 2) {
				if(up)
					hero1.move('W', walls, doors);
				if(right)
					hero1.move('D', walls, doors);
				if(down)
					hero1.move('S', walls, doors);
				if(left)
					hero1.move('A', walls, doors);

				for (Monster m : monsters)
					m.move(walls, doors, monsters);

				for (int i = 0; i < switches.size(); i++) {
					if (!switches.get(i).isOn() && hero1.collision(switches.get(i).hitBox())) {
						doors.get(switchDoorNumbers.get(i)).setState(true);
						switches.get(i).setState(true);
					}
					if (!hero1.collision(portals.get(i).hitBox()) && switches.get(i).isOn())
						portals.get(i).setState(true);
				}

				for (int i = 0; i < portals.size(); i++) {
					if (hero1.collision(portals.get(i).hitBox()) && portals.get(i).isOn()) {
						int count = 0;
						for (Entity p : portals) {
							if (p.isOn())
								count++;
						}
						if (count > 1 && !teleported) {
							do {
								randPortal = (int)(Math.random() * portals.size());
							} while (hero1.collision(portals.get(randPortal).hitBox()) || !portals.get(randPortal).isOn());
							hero1.setX(portals.get(randPortal).getX());
							hero1.setY(portals.get(randPortal).getY());
							teleported = true;
						}
					}
				}
				if (!hero1.collision(portals.get(randPortal).hitBox()))
					teleported = false;

				if (hero1.collision(end.hitBox()))
					gameOn = 1;
				for (Monster m : monsters) {
					if (hero1.collision(m.hitBox()))
						gameOn = 0;
				}
			}

			try {
				Thread.sleep(6);
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
			menu.setOnScreen(false);
			gameOn = 2;
		}
	}
	public void keyTyped(KeyEvent e) {}

	public void mouseExited(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {

	}
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public static void main(String[] args) {
		Maze app = new Maze();
	}
}
