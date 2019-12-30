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
import spacewar.utils.ImageUtil;

public class Blood extends GameObject {

	public static final int BLOOD_SPEED = 3;
	public static final int BLOOD_HEIGHT = 60;

	public static List<BufferedImage> images = new ArrayList<BufferedImage>();
	public int bloodSpeed;// Ѫ�����ٶ�
	public int directionX;// Ѫ�����з���1Ϊ���ң�-1Ϊ����
	public int directionY;// Ѫ�����з���1Ϊ���£�-1Ϊ����

	public Blood() {
		super(0, 0);
		point.x = new Random().nextInt(SpaceWar.WINDOWS_WIDTH);
		point.y = 0;
		directionY = 1;
		if (new Random().nextBoolean())
			directionX = 1;
		else
			directionX = -1;
		this.bloodSpeed = BLOOD_SPEED;
	}

	@Override
	public boolean draw(Graphics g, JPanel panel) {
			// Ѫ��������ı�
			point.y += bloodSpeed * this.directionY;
			// �߽紦��
			if (point.y < 0) {
				point.y = 0;
				this.directionY = 1;
			}
			// �߽紦��
			if (point.y > SpaceWar.WINDOWS_HEIGHT) {
				point.y = SpaceWar.WINDOWS_HEIGHT;
				this.directionY = -1;
			}
			// Ѫ��������ı�
			point.x += bloodSpeed * this.directionX;
			// �߽紦��
			if (point.x > SpaceWar.WINDOWS_WIDTH) {
				point.x = SpaceWar.WINDOWS_WIDTH;
				this.directionX = -1;
			}
			// �߽紦��
			if (point.x < 0) {
				point.x = 0;
				this.directionX = 1;
			}
			g.drawImage(images.get(3), point.x, point.y, panel);
			return true;

	}

	public static boolean loadImage() {
		try {
			BufferedImage temp = ImageIO.read(new File("images/blood.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 6; i++) {
				BufferedImage image = temp.getSubimage(i * BLOOD_HEIGHT, 0,
						BLOOD_HEIGHT, BLOOD_HEIGHT);
				images.add(image);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(point.x, point.y, BLOOD_HEIGHT, BLOOD_HEIGHT);
	}

	public int getBloodSpeed() {
		return bloodSpeed;
	}

	public void setBloodSpeed(int bloodSpeed) {
		this.bloodSpeed = bloodSpeed;
	}

	public int getDirectionX() {
		return directionX;
	}

	public void setDirectionX(int directionX) {
		this.directionX = directionX;
	}

	public int getDirectionY() {
		return directionY;
	}

	public void setDirectionY(int directionY) {
		this.directionY = directionY;
	}

}
