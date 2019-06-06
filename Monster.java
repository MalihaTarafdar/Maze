import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Monster extends Entity {

	public Monster(int x, int y, int width, int height, Color color) {
		super(x, y, width, height, color);
	}

	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	public boolean collision(ArrayList<Wall> walls) {
		for (Wall wall : walls) {
			if (hitBox().intersects(wall.hitBox()))
				return true;
		}
		return false;
	}

	public void move(int dir, ArrayList<Wall> walls, Hero hero) {
		if (!collision(walls)) {
			if (dir == 0)
				setY(getY() - 1);
			else if (dir == 1)
				setX(getX() + 1);
			else if (dir == 2)
				setY(getY() + 1);
			else if (dir == 3)
				setX(getX() - 1);
		}

	}
}
