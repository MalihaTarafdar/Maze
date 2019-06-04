import java.awt.Rectangle;

public class Wall {
	private int x;
	private int y;
	private int width;
	private int shift;

	public Wall(int x, int y, int width, int shift) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.shift = shift;
	}
	public Rectangle getRect() {
		return new Rectangle(x, y, width, shift);
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
}
