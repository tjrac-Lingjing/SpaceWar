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

	public static final int ENEMY_HEIGHT = 90;// 默认敌人高度
	public static final int ENEMY_WIDTH = 120;// 默认敌人宽度
	public static final int ENEMY_SPEED = 3;// 默认敌人速度
	public static final int ENEMY_LIFE = 2;// 默认敌人生命值
	public static final int WAIT = 0;// 默认敌人生命值

	public int life = ENEMY_LIFE;// 生命
	public int speed;// 速度
	public int direction;// 方向 -1向上和1向下
	public int imageIndex;//图片下标
	private int currentIndex;//现在下标
	public static List<BufferedImage> imagesDown = new ArrayList<BufferedImage>();
	public static List<BufferedImage> imagesUp = new ArrayList<BufferedImage>();

	public Enemy(int speed, int direction) {
		super(0, 0);
		this.speed = speed;
		this.direction = direction;
		this.life = ENEMY_LIFE;
		int y;
		// 纵坐标在窗口高范围内
		if (direction == -1)// 控制敌机速度方向敌机向上飞
		{
			y = SpaceWar.WINDOWS_HEIGHT;
			imageIndex = 1;
		} else// 敌机向下飞
		{
			y = 0;
			imageIndex = 0;
		}
		point.x = new Random().nextInt(SpaceWar.WINDOWS_WIDTH - ENEMY_WIDTH);//随机出现上端的X坐标
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
				// 向下飞
				g.drawImage(imagesDown.get(0), point.x, point.y, panel);
			} else {
				// 向上飞
				g.drawImage(imagesUp.get(0), point.x, point.y, panel);
			}
			return true;
		
	}

	
	public boolean draw(Graphics g, JPanel panel, int passNum) {
	
			int index = passNum % 5;
			// 敌机位置随机变化,只改变纵坐标，随机数为了让敌机不匀速飞行
			
			point.y += (speed) * direction+(new Random().nextInt(5)%10);//敌机速度下降快慢控制
			if (point.y < 0 || point.y > SpaceWar.WINDOWS_HEIGHT) {
				MyPanel.enemyList.remove(currentIndex);
				return false;
			}
			// imageIndex为0代表向下飞的敌机，为1代表向上飞的敌机
			if (imageIndex == 0)
				g.drawImage(imagesDown.get(index), point.x, point.y, panel);//出现向下的敌机图案
			else
				g.drawImage(imagesUp.get(index), point.x, point.y, panel);//出现向上的敌机图案
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
