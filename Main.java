package spacewar;

import java.awt.Point;

import javax.swing.JDialog;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("spacewar.SpaceWar");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JDialog dialog = new JDialog();
			dialog.setTitle("��Ϸ��ʼ��ʧ��");
			dialog.setLocation(new Point(550, 300));
			dialog.setSize(200, 100);
			dialog.setVisible(true);
			System.out.println("��Ϸ��ʼ��ʧ��");
		}
	}
}
 