import java.awt.geom.Ellipse2D;
import java.awt.*;
import java.util.ArrayList;

public class Monster {
	private int x;
	private int y;
	private int width;
	private int shift;
	private Color color;

	public Monster(int x, int y, int width, int shift, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.shift = shift;
		this.color = color;
	}
	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(x, y, width, shift);
	}
	public Rectangle getRect() {
		return new Rectangle(x,y,width,shift);
	}
	public boolean collides(ArrayList<Wall> walls) {
		for (Wall wall : walls) {
			if (getRect().intersects(wall.getRect()))
				return true;
		}
		return false;
	}
	public void move(int dir, ArrayList<Wall> walls, Hero hero) {
		if (!collides(walls)) {
			if (dir == 0)
				y -= 2;
			else if (dir == 1)
				x += 2;
			else if (dir == 2)
				y += 2;
			else if (dir == 3)
				x -= 2;
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
}