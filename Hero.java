import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Hero extends Entity {

	public Hero(int x, int y, int width, int height, Color color) {
		super(x, y, width, height, color);
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

	public void move(char dir, ArrayList<Wall> walls, ArrayList<Entity> doors) {
		if (dir == 'W')
			setY(getY() - 1);
		else if (dir == 'D')
			setX(getX() + 1);
		else if (dir == 'S')
			setY(getY() + 1);
		else if (dir == 'A')
			setX(getX() - 1);

		if (collision(walls, doors)) { //if collides, undo
			if (dir == 'W')
				setY(getY() + 1);
			else if (dir == 'D')
				setX(getX() - 1);
			else if (dir == 'S')
				setY(getY() - 1);
			else if (dir == 'A')
				setX(getX() + 1);
		}
	}
}
