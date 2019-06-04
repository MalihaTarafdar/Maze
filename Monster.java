import java.awt.geom.Ellipse2D;

public class Monster {

	private int x;
	private int y;
	private int width;
	private int shift;

	public Monster(int x, int y, int width, int shift) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.shift = shift;
	}
	public Ellipse2D.Double getEllipse() {
		return new Ellipse2D.Double(x, y, width, shift);
	}

	public void move(int dir) {
		if (dir == 0)
			y -= 2;
		else if (dir == 1)
			x += 2;
		else if (dir == 2)
			y += 2;
		else if (dir == 3)
			x -= 2;
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
