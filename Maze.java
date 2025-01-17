import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Maze extends JPanel implements KeyListener, Runnable {
	private static final long serialVersionUID = 42l;
	private ArrayList<Wall> walls;
	private ArrayList<Monster> monsters;
	private ArrayList<Entity> doors;
	private ArrayList<Entity> keys;
	private ArrayList<Entity> portals;
	private ArrayList<Integer> keyDoorNumbers;

	private int entityWidth = 20;
	private int entityHeight = 20;
	private int wallWidth = 30;
	private int wallHeight = 30;
	private int gameOn = 3;
	private int randPortal = 0;
	private int tickSpeed = 3;
	private int milliseconds = 0;
	private String map = "";

	private JFrame frame;
	private Thread thread;
	private Hero hero;
	private Entity start;
	private Entity end;
	private Menu menu;
	private BufferedImage mazeImg;

	private boolean right = false;
	private boolean left = false;
	private boolean up = false;
	private boolean down = false;
	private boolean teleported = false;

	public Maze() {
		frame = new JFrame("Maze");
		frame.add(this);

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/Positive System.otf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/Square.ttf")));
		} catch (IOException|FontFormatException e) {}

		frame.addKeyListener(this);
		frame.setSize(1300, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		thread = new Thread(this);
		thread.start();

		menu = new Menu();
		hero = new Hero(0, 0, entityWidth, entityHeight, Color.GREEN);
	}

	public void createMaze(String fileName) {
		walls = new ArrayList<Wall>();
		monsters = new ArrayList<Monster>();
		doors = new ArrayList<Entity>();
		keys = new ArrayList<Entity>();
		portals = new ArrayList<Entity>();
		keyDoorNumbers = new ArrayList<Integer>();
		start = new Entity(0, 0, entityWidth, entityHeight, Color.ORANGE);
		end = new Entity(frame.getWidth(), frame.getHeight(), entityWidth, entityHeight, Color.MAGENTA);

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
						keys.add(new Entity(x + 10, y + 10, 10, 10, Color.YELLOW));
						keyDoorNumbers.add(Character.getNumericValue(text.charAt(i)));
						portals.add(new Entity(x + 8, y + 10, 15, 15, Color.MAGENTA));
					}
					if (c == 'M')
						monsters.add(new Monster(x + entityWidth / 2, y + entityHeight / 2, entityWidth, entityHeight, Color.RED, 1));
					if (c == 'S') {
						hero.setX(x + entityWidth / 2);
						hero.setY(y + entityHeight / 2);
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

		Font title = new Font("Positive System", Font.PLAIN, 120);
		FontMetrics tm = g2.getFontMetrics(title);
		Font main = new Font("SquareFont", Font.PLAIN, 30);
		FontMetrics om = g2.getFontMetrics(main);
		Font font = new Font("SquareFont", Font.PLAIN, 40);
		FontMetrics fm = g2.getFontMetrics(font);

		if (menu.isOnScreen()) { //on menu
			if (menu.isOnStart()) { //on start screen
				g2.setFont(title);
				int titleX = frame.getWidth() / 2 - tm.stringWidth("MAZE") / 2;
				int titleY = frame.getHeight() / 3;
				g2.setPaint(new GradientPaint(titleX, titleY, Color.BLUE, titleX + tm.stringWidth("MAZE"), titleY + tm.getHeight(), Color.CYAN));
				g2.drawString("MAZE", titleX, titleY);

				g2.setColor(Color.WHITE);
				g2.setFont(main);
				int optionY = frame.getHeight() / 2;

				for (String option : menu.getStartNames()) {
					g2.drawString(option, frame.getWidth() / 2 - om.stringWidth(option) / 2, optionY);
					optionY += 50;
				}
				for (int i = 0; i < menu.getStartOptions().length; i++) {
					if (menu.getStartOptions()[i]) {
						optionY = frame.getHeight() / 2 - 20 + 52 * i;
						g2.setColor(Color.BLUE);
						g2.fillRect(frame.getWidth() / 2 - 150, optionY, 10, 10);
					}
				}
			} else if (menu.isOnMap()) { //on map selection
				int titleX = frame.getWidth() / 2 - fm.stringWidth("Choose A Maze") / 2;
				int titleY = frame.getHeight() / 4;
				g2.setPaint(new GradientPaint(titleX, titleY, Color.BLUE, titleX + fm.stringWidth("Choose A Maze"), titleY + fm.getHeight(), Color.CYAN));
				g2.setFont(font);
				g2.drawString("Choose A Maze", titleX, titleY);

				g2.setFont(main);
				g2.setColor(Color.WHITE);
				int optionX = frame.getWidth() / 4;
				for (String option : menu.getMapNames()) {
					g2.drawString(option, optionX, frame.getHeight() * 2 / 3);
					try {
						mazeImg = ImageIO.read(new File("./img/" + option + ".png"));
					} catch (IOException e) {}
					g2.drawImage(mazeImg, optionX - 18, frame.getHeight() / 2 - 33, this);
					optionX += 300;
				}
				for (int i = 0; i < menu.getMapOptions().length; i++) {
					if (menu.getMapOptions()[i]) {
						optionX = frame.getWidth() / 4 - 55 + 300 * i;
						g2.setColor(Color.BLUE);
						g2.drawRect(optionX, frame.getHeight() / 2 - 60, 200, 200);
					}
				}
			} else if (menu.isOnHow()) { //on how to play
				int titleX = frame.getWidth() / 2 - fm.stringWidth("How to Play") / 2;
				int titleY = frame.getHeight() / 6;
				g2.setPaint(new GradientPaint(titleX, titleY, Color.BLUE, titleX + fm.stringWidth("How to Play"), titleY + fm.getHeight(), Color.CYAN));
				g2.setFont(font);
				g2.drawString("How to Play", titleX, titleY);

				g2.setFont(main);
				int boxX = frame.getWidth() / 6;
				int boxY = frame.getHeight() / 4;
				g2.setPaint(new GradientPaint(boxX, boxY, Color.BLUE, boxX + 900, boxY + 450, Color.CYAN));

				g2.drawRect(boxX, boxY, 900, 450);
				g2.setColor(Color.WHITE);
				boxX += 30;
				int shift = 52;
				boxY += shift;
				g2.drawString("Avoid monsters (red)", boxX, boxY);
				boxY += shift;
				g2.drawString("Move with W, A, S, D", boxX, boxY);
				boxY += shift;
				g2.drawString("Press ESC to pause", boxX, boxY);
				boxY += shift;
				g2.drawString("Yellow keys open gray doors", boxX, boxY);
				boxY += shift;
				g2.drawString("After a key is collected, a portal appears", boxX, boxY);
				boxY += shift;
				g2.drawString("Portals can be used for fast travel", boxX, boxY);
				boxY += shift;
				g2.drawString("Reach the exit to win", boxX, boxY);
				boxY += shift;
				g2.drawString("Maze 3 is not impossible", boxX, boxY);
			}

			if (!menu.isOnStart()) { //back button
				g2.setFont(main);
				g2.setPaint(new GradientPaint(40, 40, Color.BLUE, 72, 50, Color.CYAN));
				g2.drawRect(30, 30, 72, 50);
				g2.drawString("ESC", 40, 65);
			}
		} else { //in game
			for(Wall wall : walls) {
				g2.setColor(wall.getColor());
				g2.fill(wall.hitBox());
			}
			for (Entity key : keys) {
				g2.setColor(key.getColor());
				if (!key.isOn())
					g2.fill(key.hitBox());
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
			g2.setColor(hero.getColor());
			g2.fill(hero.getEllipse());
			for (Monster m : monsters) {
				g2.setColor(m.getColor());
				g2.fill(m.getEllipse());
			}

			if (gameOn != 2) { //game end
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("SquareFont", Font.PLAIN, 50));
				if(gameOn == 1) {
					g2.drawString("YOU WIN", frame.getWidth() / 2 - 100, frame.getHeight() / 2);
				} else if (gameOn == 0) {
					g2.drawString("GAME OVER", frame.getWidth() / 2 - 100, frame.getHeight() / 2);
				}
			}
			if (menu.isPaused()) { //pause menu
				g2.setColor(Color.WHITE);
				g2.fillRect(frame.getWidth() / 6 - 5, frame.getHeight() / 6 - 5, 210, 160);
				g2.setColor(Color.BLACK);
				g2.fillRect(frame.getWidth() / 6, frame.getHeight() / 6, 200, 150);

				g2.setFont(main);
				g2.setColor(Color.WHITE);
				int optionY = frame.getHeight() / 5 + 30;
				for (String option : menu.getPauseNames()) {
					g2.drawString(option, frame.getWidth() / 5 + 65 - om.stringWidth(option) / 2, optionY);
					optionY += 50;
				}
				for (int i = 0; i < menu.getPauseOptions().length; i++) {
					if (menu.getPauseOptions()[i]) {
						optionY = frame.getHeight() / 5 + 10 + 52 * i;
						g2.setColor(Color.BLUE);
						g2.fillRect(frame.getWidth() / 5 - 15, optionY, 10, 10);
					}
				}
			}
		}
	}

	public void run() {
		while(true) {
			if(gameOn == 2 && !menu.isPaused()) {
				if(up)
					hero.move('W', walls, doors);
				if(right)
					hero.move('D', walls, doors);
				if(down)
					hero.move('S', walls, doors);
				if(left)
					hero.move('A', walls, doors);

				for (Monster m : monsters) {
					if (milliseconds % 2 == 0) //so hero faster than monster
						m.move(walls, doors, monsters, end);
				}

				for (int i = 0; i < keys.size(); i++) {
					if (!keys.get(i).isOn() && hero.collision(keys.get(i).hitBox())) {
						doors.get(keyDoorNumbers.get(i)).setState(true);
						keys.get(i).setState(true);
					}
					if (!hero.collision(portals.get(i).hitBox()) && keys.get(i).isOn())
						portals.get(i).setState(true);
				}

				teleport();

				if (hero.collision(end.hitBox()))
					gameOn = 1;
				for (Monster m : monsters) {
					if (hero.collision(m.hitBox()))
						gameOn = 0;
				}
			} else if (gameOn == 0 || gameOn == 1) {
				delay(1500);
				gameOn = 3;
				portals.clear();
				walls.clear();
				monsters.clear();
				doors.clear();
				keys.clear();
				keyDoorNumbers.clear();
				randPortal = 0;
				menu.setOnScreen(true);
				menu.setOnStart(true);
			}
			if (milliseconds % tickSpeed == 0 && milliseconds > 30) //so milliseconds doesn't get too large
				milliseconds = 0;

			milliseconds += tickSpeed;
			delay(tickSpeed);
			repaint();
		}
	}

	public void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch(InterruptedException e){}
	}

	public void teleport() { //teleportation through portals
		for (int i = 0; i < portals.size(); i++) {
			if (hero.collision(portals.get(i).hitBox()) && portals.get(i).isOn()) {
				int numOpenPortals = 0;
				for (Entity p : portals) {
					if (p.isOn())
						numOpenPortals++;
				}
				if (numOpenPortals > 1 && !teleported) {
					do {
						randPortal = (int)(Math.random() * portals.size());
					} while (hero.collision(portals.get(randPortal).hitBox()) || !portals.get(randPortal).isOn());
					hero.setX(portals.get(randPortal).getX());
					hero.setY(portals.get(randPortal).getY());
					teleported = true;
				}
			}
		}
		if (!hero.collision(portals.get(randPortal).hitBox()))
			teleported = false;
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

		if (menu.isOnStart()) { //on start screen
			if (e.getKeyCode() == KeyEvent.VK_UP)
				menu.moveBackward(0);
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				menu.moveForward(0);

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu.setOnStart(false);
				if (menu.getStartOptions()[0]) //select map
					menu.setOnMap(true);
				else if (menu.getStartOptions()[1]) //how to play
					menu.setOnHow(true);
				else if (menu.getStartOptions()[2]) //quit
					System.exit(0);
			}
		} else if (menu.isOnMap()) { //on map selection
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				menu.moveForward(1);
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				menu.moveBackward(1);

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (menu.getMapOptions()[0])
					map = "./mazes/Maze1.txt";
				else if (menu.getMapOptions()[1])
					map = "./mazes/Maze2.txt";
				else if (menu.getMapOptions()[2])
					map = "./mazes/Maze3.txt";
				menu.setOnMap(false);
				menu.resetOptions();
				menu.setOnScreen(false);
				createMaze(map);
				gameOn = 2;
				milliseconds = 0;
			}

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //go back to start
				menu.setOnMap(false);
				menu.resetOptions();
				menu.setOnStart(true);
			}
		} else if (menu.isOnHow()) { //on how to play
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //go back to start
				menu.setOnHow(false);
				menu.resetOptions();
				menu.setOnStart(true);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameOn == 2)
			menu.setPaused(true);
		if (menu.isPaused()) { //on pause menu
			if (e.getKeyCode() == KeyEvent.VK_UP)
				menu.moveBackward(2);
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				menu.moveForward(2);

			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (menu.getPauseOptions()[1]) { //go back to start
					gameOn = 3;
					menu.setOnScreen(true);
					menu.setOnStart(true);
				}
				menu.setPaused(false); //resume
				menu.resetOptions();
			}
		}
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
