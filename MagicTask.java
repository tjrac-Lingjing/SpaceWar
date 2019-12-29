package  spacewar.task;

import java.util.TimerTask;

import  spacewar.MyPanel;

public class MagicTask extends TimerTask {
 
	@Override
	public void run() {
		if (MyPanel.myplane != null && !MyPanel.isPause && MyPanel.isStarted) {
			// �����ֺ�ս������û�򿪣�ħ��ֵ����
			if (!MyPanel.isProtect && !MyPanel.isUpdate) {
				MyPanel.magicCount++;
				if (MyPanel.magicCount > 10)
					MyPanel.magicCount = 10;
			}
			// �ж��Ƿ�򿪷�����
			if (MyPanel.isProtect) {
				// ����������ħ��ֵ�ݼ�
				MyPanel.magicCount--;
				if (MyPanel.magicCount <= 0) {
					MyPanel.magicCount = 0;
					MyPanel.isProtect = false;
				}
			}
			// �ж��Ƿ�����ս��
			if (MyPanel.isUpdate) {
				// ս��������ħ��ֵ�ݼ�
				MyPanel.magicCount--;
				if (MyPanel.magicCount <= 0) {
					MyPanel.magicCount = 0;
					MyPanel.isUpdate = false;
					MyPanel.myplane.isUpdate = MyPanel.isUpdate;
				}
			}
		}
	}
}
