import java.awt.*;

public class Wall {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;

	public Wall(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	} //x*width
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
