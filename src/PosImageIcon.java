import java.awt.*;
import javax.swing.*;

//ImageIcon에 좌표와 위치를 부여하기위해 사용
public class PosImageIcon extends ImageIcon  {
	
	    int imgX;  		 	//ImageIcon의 x좌표
		int imgY;      	 	//ImageIcon의 y좌표
		int imgWidth; 		//ImageIcon의 넓이
		int imgHeight;      //ImageIcon의 높이
		
	//이미지의 초기 위치와 크기 정하는 생성자 	
	public PosImageIcon (String img, int x, int y, int width, int height) {
		super(img);
		imgX = x;
		imgY = y;
		imgWidth = width;
		imgHeight = height;

	}
	
	//이미지 움직이는 함수
	public void move(int x, int y, int dir) {
		if(dir==0) {			 //오른쪽 위
			imgX += x;		
			imgY -= y;
		}
		else if(dir==1) { 		 //오른쪽 아래
			imgX += x;	
			imgY += y;
		} 
		else if(dir==2) {		 //왼쪽 위
			imgX -= x;
			imgY -= y;
		}
		else if(dir==3) {		 //왼쪽 아래
			imgX -= x;
			imgY += y;
		}
		else if (dir == 4) {	 //왼쪽
			imgX -= x;
		}
		else if (dir == 5) {	 //오른쪽
			imgX += x;
		}
	}
	
	//이미지 그리는 함수
	public void draw(Graphics g) {
		g.drawImage(this.getImage(), imgX, imgY, imgWidth, imgHeight, null);
	}
	
	
	//이미지의 중앙점 찾기
	Point getCenter() {
		return new Point(imgX + (imgWidth/2), imgY + (imgHeight/2));
	}
	
	//이미지의 아래쪽 중앙 찾기
	Point getBottomCenter() {
		return new Point(imgX + (imgWidth/2), imgY + (imgHeight));
	}
	
	//이미지의 위쪽 중앙 찾기
	Point getTopCenter() {
		return new Point(imgX + (imgWidth/2), imgY );
	}
	
	//이미지의 왼쪽 중앙 찾기
	Point getLeftCenter() {
		return new Point(imgX , imgY + (imgHeight/2));
	}
	
	//이미지의 오른쪽 중앙 찾기
	Point getRightCenter() {
		return new Point(imgX + (imgWidth) , imgY + (imgHeight/2));
	}

}
