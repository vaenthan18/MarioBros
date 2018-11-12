import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Animation extends JPanel implements ActionListener, KeyListener {

	private static ImageIcon[] smallRunning = new ImageIcon[6];
	private static ImageIcon[] smallJump = new ImageIcon[2];
	private static ImageIcon[] smallStill = new ImageIcon[2];
	private static ImageIcon background = new ImageIcon("map.png");

	private static JLabel label = new JLabel("\t\tSuper Mario Bros: Use Arrow Keys to Move / Adjust Speed with Slider\t\t\t\t\t\t\t\t\t\t//VAENTHAN JEEVARAJAH");
	private static JSlider speed = new JSlider(JSlider.HORIZONTAL, 1, 50, 1);
	private static int i = 0, direction = 0, arrayLength = 1, currentDirection = 0, counter = -10, x = 400, y = 333,
			velx = 0, vely = 0;
	private static String folder = new String("Still");
	private static Timer timer;

	private static class SliderChange implements ChangeListener {

		public void stateChanged(ChangeEvent event) {
			JSlider source = (JSlider) event.getSource();

			if (!source.getValueIsAdjusting()) {
				timer.setDelay(150 / source.getValue());
			}
		}
	}

	public Animation() {
		timer = new Timer(50, this);
		timer.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.setLayout(new BorderLayout());
	}

	public static void imageInitialize(ImageIcon[] images, String folder) {
		for (int i = 0; i < images.length; i++) {
			images[i] = new ImageIcon("./mario/" + folder + "/File" + i + ".png");
		}
	}

	public void checkForChange() {
		if (currentDirection != direction) {
			currentDirection = direction;
			i = (direction == 0) ? arrayLength / 2 : 0;
		}
	}

	public void actionPerformed(ActionEvent event) {
		speed.setFocusable(false);
		if (i >= (arrayLength - (direction * arrayLength / 2) - 1)) {
			i = (direction == 0) ? arrayLength / 2 : 0;
		} else {
			i++;
		}
		if (x + velx < 800 && x + velx > 0) {
			x += velx;
		}
		y += vely;
		if (counter != -10) {
			folder = "Jump";
		}
		if (folder == "Jump") {
			up();
		}
		if (counter == 10) {
			counter = -10;
			still();
			folder = "Still";
			arrayLength = smallStill.length;
		}
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(background.getImage(), 0, 50, 800, 350, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		imageInitialize(smallStill, "smallStill");
		imageInitialize(smallRunning, "smallRunning");
		imageInitialize(smallJump, "smallJump");

		if (folder == "Still") {
			g2.drawImage(smallStill[i].getImage(), x, y, 30, 30, null);
		} else if (folder == "Run") {
			g2.drawImage(smallRunning[i].getImage(), x, y, 30, 30, null);
		} else if (folder == "Jump") {
			g2.drawImage(smallJump[i].getImage(), x, y, 30, 30, null);
		}
		g2.dispose();
	}

	public static void up() {
		counter++;
		vely = counter;
		velx = (direction == 0) ? 4 : -4;
	}

	public static void right() {
		if (counter == -10) {
			vely = 0;
			velx = 4;
			direction = 0;
		}
	}

	public static void left() {
		if (counter == -10) {
			vely = 0;
			velx = -4;
			direction = 1;
		}
	}

	public static void still() {
		if (counter == -10) {
			vely = 0;
			velx = 0;
		}
	}

	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		if (counter == -10) {
			if (key == KeyEvent.VK_UP) {
				up();
				folder = "Jump";
				arrayLength = smallJump.length;
			} else if (key == KeyEvent.VK_LEFT) {
				left();
				folder = "Run";
				arrayLength = smallRunning.length;
			} else if (key == KeyEvent.VK_RIGHT) {
				right();
				folder = "Run";
				arrayLength = smallRunning.length;
			} else {
				still();
				folder = "Still";
				arrayLength = smallStill.length;
			}
		}
	}

	public void keyTyped(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
		int key = event.getKeyCode();
		if (key != KeyEvent.VK_UP) {
			still();
			folder = "Still";
			arrayLength = smallStill.length;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Animation");
		Animation animation = new Animation();

		speed.setMajorTickSpacing(2);
		speed.setMinorTickSpacing(1);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.addChangeListener(new SliderChange());

		frame.add(speed, BorderLayout.NORTH);
		frame.add(label, BorderLayout.SOUTH);
		frame.add(animation);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 550);
	}
}
