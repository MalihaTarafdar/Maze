import java.awt.*;

public class Wall {
	private int x;
	private int y;
	private int width;
	private int shift;
	private Color color;

	public Wall(int x, int y, int width, int shift, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.shift = shift;
		this.color = color;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, width, shift);
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
	public int getShift() {
		return shift;
	}
	public Color getColor() {
		return color;
	}
}
