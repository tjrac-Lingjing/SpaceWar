package details;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
public class Bomb {

	public static final int BOMB_HEIGHT = 60;
	public static final int BOMB_WIDTH = 30;
	public static final int BOMB_SPEED = 30;
	public int direction;// 取值1向上，-1向下
	public int bombSpeed;// 设置子弹的飞行速度
	public boolean isUpdate;// 标记是否为升级炮弹
	private int currentIndex;

	public Bomb(int x, int y, int direction, Boolean isUpdate) {
		// 设置战机子弹的飞行速度
		bombSpeed = BOMB_SPEED;
		this.direction = direction;
		this.isUpdate = isUpdate;
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

}
