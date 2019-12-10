package spacewar.listener;


import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import spacewar.*;
import spacewar.detail.*;


public class MyKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (MyPanel.myplane != null ) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				int x = MyPanel.myplane.getPoint().x - MyPanel.DEFAULT_SPEED;
				if (x < 0)
					x = 0;
				MyPanel.myplane.setPoint(new Point(x, MyPanel.myplane
						.getPoint().y));
				break;
			case KeyEvent.VK_RIGHT:
				int x1 = MyPanel.myplane.getPoint().x + MyPanel.DEFAULT_SPEED;
				if (x1 > SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH)
					x1 = SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH;
				MyPanel.myplane.setPoint(new Point(x1, MyPanel.myplane
						.getPoint().y));
				break;
			case KeyEvent.VK_UP:
				int y = MyPanel.myplane.getPoint().y - MyPanel.DEFAULT_SPEED;
				if (y < 0)
					y = 0;
				MyPanel.myplane.setPoint(new Point(
						MyPanel.myplane.getPoint().x, y));
				break;
			case KeyEvent.VK_DOWN:
				int y1 = MyPanel.myplane.getPoint().y + MyPanel.DEFAULT_SPEED;
				if (y1 > SpaceWar.WINDOWS_HEIGHT)
					y1 = SpaceWar.WINDOWS_HEIGHT;
				MyPanel.myplane.setPoint(new Point(
						MyPanel.myplane.getPoint().x, y1));
				break;
			case KeyEvent.VK_SPACE:
				Bomb bomb1 = new Bomb(MyPanel.myplane.getPoint().x + 10,
						MyPanel.myplane.getPoint().y, 1);
				MyPanel.bombList.add(bomb1);
				Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x
						+ MyPlane.PLANE_WIDTH - 40,
						MyPanel.myplane.getPoint().y, 1);
				MyPanel.bombList.add(bomb2);
				break;

			default:
				break;
			}
		}
	}

}
