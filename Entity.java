import java.awt.*;

public class Entity {
	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;
	private boolean on;

	public Entity(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		on = false;
	}

	public Rectangle hitBox() {
		return new Rectangle(x, y, width, height);
	}
	public boolean collision(Rectangle hitBox) {
		if (hitBox().intersects(hitBox))
			return true;
		return false;
	}

	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setColor(int r, int g, int b) {
		this.color = new Color(r, g, b);
	}
	public void setState(boolean on) {
		this.on = on;
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
	public int getHeight() {
		return height;
	}
	public Color getColor() {
		return color;
	}
	public boolean isOn() {
		return on;
	}
}
