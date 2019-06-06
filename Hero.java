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
	public boolean collision(Rectangle hitBox) {
		if (hitBox(4).intersects(hitBox))
			return true;
		return false;
	}
	public Rectangle hitBox(int dir) {
		if (dir == 0)
			return new Rectangle(getX(), getY() - 2, getWidth(), getHeight());
		else if (dir == 1)
			return new Rectangle(getX() + 2, getY(), getWidth(), getHeight());
		else if (dir == 2)
			return new Rectangle(getX(), getY() + 2, getWidth(), getHeight());
		else if (dir == 3)
			return new Rectangle(getX() - 2, getY(), getWidth(), getHeight());
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}
	public void move(int dir, ArrayList<Wall> walls) {
		boolean collision = false;
		for (Wall wall : walls) {
			if (hitBox(dir).intersects(wall.hitBox()))
				collision = true;
		}
		if (!collision) {
			if (dir == 0)
				setY(getY() - 2);
			else if (dir == 1)
				setX(getX() + 2);
			else if (dir == 2)
				setY(getY() + 2);
			else if (dir == 3)
				setX(getX() - 2);
		}
	}
}
