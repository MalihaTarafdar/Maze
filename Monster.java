import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Monster extends Entity {

	private int dir;

	public Monster(int x, int y, int width, int height, Color color, int dir) {
		super(x, y, width, height, color);
		this.dir = dir;
	}

	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	public boolean collision(ArrayList<Wall> walls, ArrayList<Entity> doors) {
		boolean w = false;
		boolean d = false;
		for (Wall wall : walls) {
			if (collision(wall.hitBox()))
				w = true;
		}
		for (Entity door : doors) {
			if (collision(door.hitBox()) && !door.isOn())
				d = true;
		}
		return w || d;
	}

	public void changePos() {
		if (dir == 0)
			setY(getY() - 1);
		else if (dir == 1)
			setX(getX() + 1);
		else if (dir == 2)
			setY(getY() + 1);
		else if (dir == 3)
			setX(getX() - 1);
	}
	public void reversePos() {
		if (dir == 0)
			setY(getY() + 1);
		else if (dir == 1)
			setX(getX() - 1);
		else if (dir == 2)
			setY(getY() - 1);
		else if (dir == 3)
			setX(getX() + 1);
	}

	public void move(ArrayList<Wall> walls, ArrayList<Entity> doors) {
		changePos();

		while (collision(walls, doors)) {
			reversePos();

			dir++;
			if (dir == 4)
				dir = 0;

			changePos();
		}
	}

	public void setDir(int dir) {
		this.dir = dir;
	}
	public int getDir() {
		return dir;
	}
}
