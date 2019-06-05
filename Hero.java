import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.*;

public class Hero {
	private int x;
	private int y;
	private int width;
	private int height;
	Color color;

	public Hero(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(x, y, width, height);
	}
	public boolean collides(Rectangle collisionBox) {
		if (getRect(4).intersects(collisionBox))
			return true;
		return false;
	}
	public Rectangle getRect(int dir) {
		if (dir == 0)
			return new Rectangle(x,y-2,width,height);
		else if (dir == 1)
			return new Rectangle(x+2,y,width,height);
		else if (dir == 2)
			return new Rectangle(x,y+2,width,height);
		else if (dir == 3)
			return new Rectangle(x-2,y,width,height);
		return new Rectangle(x,y,width,height);
	}
	public void move(int dir, ArrayList<Wall> walls) { //receive key code
		boolean collision = false;
		for (Wall wall : walls) {
			if (getRect(dir).intersects(wall.getRect()))
				collision = true;
		}
		if (!collision) {
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
	public int getheight() {
		return height;
	}
	public Color getColor() {
		return color;
	}
}
