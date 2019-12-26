package spacewar.detail;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import spacewar.SpaceWar;
import spacewar.MyPanel;
import spacewar.utils.ImageUtil;

public class Ball extends GameObject {

	public static final int BALL_SPEED = 6;
	public static final int BALL_HEIGHT = 40;
	public static final int BALL_WIDTH = 15;
	// ���Ϻ����µĵл��ڵ�
	public static List<BufferedImage> imagesUp = new ArrayList<BufferedImage>();
	public static List<BufferedImage> imagesDown = new ArrayList<BufferedImage>();
	public int direction;// ����ը���ķ��з���
	public int ballSpeed;// ը�����ٶ�
	private int currentIndex;

	public Ball(int x, int y, int direction) {
		super(x, y);
		this.direction = direction;
		this.ballSpeed = BALL_SPEED;
	}

	@Override
	public boolean draw(Graphics g, JPanel panel) {
		
			// �ڵ�������У�ֻ��Ҫ���������꣬��ֻʵ���ڵ����ϵ��·���
			point.y += ballSpeed * this.direction;
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.ballList.remove(currentIndex);
				return false;
			}
			// Boss�ڵ�����仯
			int index = new Random().nextInt(5);
			if (direction == 1) {
				g.drawImage(imagesDown.get(0), point.x, point.y, panel);
			} else {
				g.drawImage(imagesUp.get(0), point.x, point.y, panel);
			}
			return true;

	}

	public boolean draw(Graphics g, JPanel panel, int passNum) {
		// �л��ڵ���ؿ��仯
		int index = (passNum - 1) / 2;
			// �ڵ�������У�ֻ��Ҫ���������꣬��ֻʵ���ڵ����ϵ��·���
			point.y += ballSpeed * this.direction;
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.ballList.remove(currentIndex);
				return false;
			}
			if (direction == 1) {
				g.drawImage(imagesDown.get(index), point.x, point.y, panel);
			} else {
				g.drawImage(imagesUp.get(index), point.x, point.y, panel);
			}
			return true;
	}

	public static boolean loadImage() {
		try {
			BufferedImage temp = ImageIO.read(new File("images/balldown.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 5; i++) {
				BufferedImage image = temp.getSubimage(i * BALL_WIDTH, 0,
						BALL_WIDTH, BALL_HEIGHT);
				imagesDown.add(image);
			}
			temp = ImageIO.read(new File("images/ballup.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 5; i++) {
				BufferedImage image = temp.getSubimage(i * BALL_WIDTH, 0,
						BALL_WIDTH, BALL_HEIGHT);
				imagesUp.add(image);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(point.x, point.y, BALL_WIDTH, BALL_HEIGHT);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getBallSpeed() {
		return ballSpeed;
	}

	public void setBallSpeed(int ballSpeed) {
		this.ballSpeed = ballSpeed;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
