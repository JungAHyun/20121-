import java.awt.*;
import javax.swing.*;

//ImageIcon�� ��ǥ�� ��ġ�� �ο��ϱ����� ���
public class PosImageIcon extends ImageIcon  {
	
	    int imgX;  		 	//ImageIcon�� x��ǥ
		int imgY;      	 	//ImageIcon�� y��ǥ
		int imgWidth; 		//ImageIcon�� ����
		int imgHeight;      //ImageIcon�� ����
		
	//�̹����� �ʱ� ��ġ�� ũ�� ���ϴ� ������ 	
	public PosImageIcon (String img, int x, int y, int width, int height) {
		super(img);
		imgX = x;
		imgY = y;
		imgWidth = width;
		imgHeight = height;

	}
	
	//�̹��� �����̴� �Լ�
	public void move(int x, int y, int dir) {
		if(dir==0) {			 //������ ��
			imgX += x;		
			imgY -= y;
		}
		else if(dir==1) { 		 //������ �Ʒ�
			imgX += x;	
			imgY += y;
		} 
		else if(dir==2) {		 //���� ��
			imgX -= x;
			imgY -= y;
		}
		else if(dir==3) {		 //���� �Ʒ�
			imgX -= x;
			imgY += y;
		}
		else if (dir == 4) {	 //����
			imgX -= x;
		}
		else if (dir == 5) {	 //������
			imgX += x;
		}
	}
	
	//�̹��� �׸��� �Լ�
	public void draw(Graphics g) {
		g.drawImage(this.getImage(), imgX, imgY, imgWidth, imgHeight, null);
	}
	
	
	//�̹����� �߾��� ã��
	Point getCenter() {
		return new Point(imgX + (imgWidth/2), imgY + (imgHeight/2));
	}
	
	//�̹����� �Ʒ��� �߾� ã��
	Point getBottomCenter() {
		return new Point(imgX + (imgWidth/2), imgY + (imgHeight));
	}
	
	//�̹����� ���� �߾� ã��
	Point getTopCenter() {
		return new Point(imgX + (imgWidth/2), imgY );
	}
	
	//�̹����� ���� �߾� ã��
	Point getLeftCenter() {
		return new Point(imgX , imgY + (imgHeight/2));
	}
	
	//�̹����� ������ �߾� ã��
	Point getRightCenter() {
		return new Point(imgX + (imgWidth) , imgY + (imgHeight/2));
	}

}
