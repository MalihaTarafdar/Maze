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
	public Rectangle hitBox(char dir) {
		if (dir == 'W')
			return new Rectangle(getX(), getY() - 2, getWidth(), getHeight());
		else if (dir == 'D')
			return new Rectangle(getX() + 2, getY(), getWidth(), getHeight());
		else if (dir == 'S')
			return new Rectangle(getX(), getY() + 2, getWidth(), getHeight());
		else if (dir == 'A')
			return new Rectangle(getX() - 2, getY(), getWidth(), getHeight());
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public boolean collision(ArrayList<Wall> walls) {
		for (Wall wall : walls) {
			if (hitBox().intersects(wall.hitBox()))
				return true;
		}
		return false;
	}

	public void move(char dir, ArrayList<Wall> walls) {
		if (dir == 'W')
			setY(getY() - 1);
		else if (dir == 'D')
			setX(getX() + 1);
		else if (dir == 'S')
			setY(getY() + 1);
		else if (dir == 'A')
			setX(getX() - 1);

		if (collision(walls)) {
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
