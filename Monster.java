import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Monster extends Entity {

	private int dir;

	public Monster(int x, int y, int width, int height, Color color) {
		super(x, y, width, height, color);
		dir = 1;
	}

	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	public boolean onPath(ArrayList<Entity> monsterPath) {
		for (Entity path : monsterPath) {
			if (collision(path.hitBox()))
				return true;
		}
		return false;
	}

	public void move(ArrayList<Entity> monsterPath) {
		if (onPath(monsterPath)) {
			if (dir == 0)
				setY(getY() - 1);
			else if (dir == 1)
				setX(getX() + 1);
			else if (dir == 2)
				setY(getY() + 1);
			else if (dir == 3)
				setX(getX() - 1);
		} else {
			dir++;
			if (dir == 4)
				dir = 0;
		}
	}
}
