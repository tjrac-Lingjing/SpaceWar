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

import spacewar.detail.GameObject;

import  spacewar.SpaceWar;
import  spacewar.MyPanel;
import  spacewar.utils.ImageUtil;
public class Bomb extends GameObject {

	public static final int BOMB_HEIGHT = 60;
	public static final int BOMB_WIDTH = 30;
	public static final int BOMB_SPEED = 30;
 
	// ս���ӵ�ͼ������
	public static List<BufferedImage> imagesUpdate = new ArrayList<BufferedImage>();
	public static List<BufferedImage> images = new ArrayList<BufferedImage>();
	public int direction;// ȡֵ1���ϣ�-1����
	public int bombSpeed;// �����ӵ��ķ����ٶ�
	public boolean isUpdate;// ����Ƿ�Ϊ�����ڵ�
	private int currentIndex; 
 
	public Bomb(int x, int y, int direction, Boolean isUpdate) {
		super(x, y);
		// ����ս���ӵ��ķ����ٶ�
		bombSpeed = BOMB_SPEED;
		this.direction = direction;
		this.isUpdate = isUpdate;
	}

	@Override
	public boolean draw(Graphics g, JPanel panel, boolean pause) {
		if (!pause) {
			// �ӵ�ֻ��Ҫ��������
			point.y -= bombSpeed * direction;// bombSpeed�����ӵ����ٶȣ�Ҳ�����ӵ��ƶ��Ĳ���
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.bombList.remove(currentIndex);
				return false;
			}
			if (isUpdate == true) {
				int index = new Random().nextInt(18);
				return g.drawImage(imagesUpdate.get(index), point.x, point.y,panel);
			} else {
				int index = new Random().nextInt(15);
				return g.drawImage(images.get(index), point.x, point.y, panel);
			}
		} else
			return false;
	}

	public static boolean loadImage() {
		try {
			BufferedImage temp = ImageIO.read(new File("images/bomb1.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 15; i++) {
				BufferedImage image = temp.getSubimage(i * BOMB_WIDTH, 0,BOMB_WIDTH, BOMB_WIDTH);
				images.add(image);
			}
			temp = ImageIO.read(new File("images/bomb.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 18; i++) {
				BufferedImage image = temp.getSubimage(i * BOMB_WIDTH, 0,
						BOMB_WIDTH, BOMB_HEIGHT);
				imagesUpdate.add(image);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(point.x, point.y, BOMB_WIDTH, BOMB_HEIGHT);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getBombSpeed() {
		return bombSpeed;
	}

	public void setBombSpeed(int bombSpeed) {
		this.bombSpeed = bombSpeed;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

}
