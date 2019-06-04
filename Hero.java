import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Hero {
	private int x;
	private int y;
	private int width;
	private int shift;
	boolean collision;
	Color color;

	public Hero(int x, int y, int width, int shift, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.shift = shift;
		this.color = color;
		collision = false;
	}
	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(x, y, width, shift);
	}
	public void move(int direction, ArrayList<Wall> walls) {
		collision = false;
		Wall collisionWall = walls.get(0);
		int collisionDir = 5;
		for (Wall wall : walls) {
			if ((new Rectangle(x,y,width,shift)).intersects(wall.getRect())) {
				collision = true;
				collisionWall = wall;
				collisionDir = direction;
			}
		}
		if (!collision) {
			if (direction == 0)
				y -= shift;
			else if (direction == 1)
				x += width;
			else if (direction == 2)
				y += shift;
			else if (direction == 3)
				x -= width;
		} else {
			while (collision) {
			if (collisionDir % 2 == 0) {
				if (direction == 0)
					y += shift;
				else if (direction == 2)
					y -= shift;
			} else {
				if (direction == 1)
					x -= shift;
				else if (direction == 3)
					x += width;
			}
			if (!(new Rectangle(x,y,width,shift)).intersects(collisionWall.getRect()))
				collision = false;
			}
		}
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getShift() {
		return shift;
	}
	public Color getColor() {
		return color;
	}
	public boolean intersects() {
		return collision;
	}
}
