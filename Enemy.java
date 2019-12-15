package spacewar.detail;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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

public class Enemy extends GameObject {

	public static final int ENEMY_HEIGHT = 90;// Ĭ�ϵ��˸߶�
	public static final int ENEMY_WIDTH = 120;// Ĭ�ϵ��˿��
	public static final int ENEMY_SPEED = 3;// Ĭ�ϵ����ٶ�
	public static final int ENEMY_LIFE = 2;// Ĭ�ϵ�������ֵ
	public static final int WAIT = 0;// Ĭ�ϵ�������ֵ

	public int life = ENEMY_LIFE;// ����
	public int speed;// �ٶ�
	public int direction;// ���� -1���Ϻ�1����
	public int imageIndex;//ͼƬ�±�
	private int currentIndex;//�����±�
	public static List<BufferedImage> imagesDown = new ArrayList<BufferedImage>();
	public static List<BufferedImage> imagesUp = new ArrayList<BufferedImage>();

	public Enemy(int speed, int direction) {
		super(0, 0);
		this.speed = speed;
		this.direction = direction;
		this.life = ENEMY_LIFE;
		int y;
		// �������ڴ��ڸ߷�Χ��
		if (direction == -1)// ���Ƶл��ٶȷ���л����Ϸ�
		{
			y = SpaceWar.WINDOWS_HEIGHT;
			imageIndex = 1;
		} else// �л����·�
		{
			y = 0;
			imageIndex = 0;
		}
		point.x = new Random().nextInt(SpaceWar.WINDOWS_WIDTH - ENEMY_WIDTH);//��������϶˵�X����
		point.y = y;
	}

	@Override
	public boolean draw(Graphics g, JPanel panel) {
	
			point.y += direction * speed;
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.enemyList.remove(currentIndex);
				return false;
			}
			if (direction == 1) {
				// ���·�
				g.drawImage(imagesDown.get(0), point.x, point.y, panel);
			} else {
				// ���Ϸ�
				g.drawImage(imagesUp.get(0), point.x, point.y, panel);
			}
			return true;
		
	}

	
	public boolean draw(Graphics g, JPanel panel, int passNum) {
	
			int index = passNum % 5;
			// �л�λ������仯,ֻ�ı������꣬�����Ϊ���õл������ٷ���
			
			point.y += (speed) * direction+(new Random().nextInt(5)%10);//�л��ٶ��½���������
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.enemyList.remove(currentIndex);
				return false;
			}
			// imageIndexΪ0�������·ɵĵл���Ϊ1�������Ϸɵĵл�
			if (imageIndex == 0)
				g.drawImage(imagesDown.get(index), point.x, point.y, panel);//�������µĵл�ͼ��
			else
				g.drawImage(imagesUp.get(index), point.x, point.y, panel);//�������ϵĵл�ͼ��
			return true;
		
	}

	public static boolean loadImage() {
		try {
			BufferedImage temp = ImageIO.read(new File("images/enemyDown.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 5; i++) {
				BufferedImage image = temp.getSubimage(i * ENEMY_WIDTH, 0,
						ENEMY_WIDTH, ENEMY_HEIGHT);
				imagesDown.add(image);
			}
			temp = ImageIO.read(new File("images/enemyUp.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 5; i++) {
				BufferedImage image = temp.getSubimage(i * ENEMY_WIDTH, 0,
						ENEMY_WIDTH, ENEMY_HEIGHT);
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
		return new Rectangle(point.x, point.y, ENEMY_WIDTH, ENEMY_HEIGHT);
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

}
