import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.awt.event.*;

public class Test extends JPanel implements Runnable {

  private JFrame frame;
  int seconds = 0;

  public Test() {
    frame = new JFrame("Test");
    frame.add(this);
    frame.setSize(500, 500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    Thread thread = new Thread(this);
    thread.start();

    /*timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        seconds++;
        System.out.println(seconds);
      }
    }, 1000, 1000);*/

  }

  public void run() {
    Timer timer = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        seconds++;
        System.out.println(seconds);
      }
    });
    timer.setRepeats(true);
    timer.start();

    delay(5);

    repaint();
  }

  public void delay(int milliseconds) {
		try {
			Thread.sleep(5);
		} catch(InterruptedException e){}
	}

  public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());
  }

  public static void main(String[] args) {
    Test app = new Test();
  }
}

/*	public void move(ArrayList<Wall> walls, ArrayList<Entity> doors, Hero hero) {
		int opp = hero.getY() - getY();
		int adj = hero.getX() - getX();

		if (adj == 0) {
			setY(getY() + opp);
			return;
		}

		double angle = Math.tan(opp/adj);

		if (getX() > hero.getX())
			angle += Math.PI;

		int shift = 1;
		double vx = shift * Math.cos(angle);
		double vy = shift * Math.sin(angle);

		setX(getX() + (int)vx);
		setY(getY() + (int)vy);
	}*/
