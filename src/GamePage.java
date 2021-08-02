import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import java.util.TimerTask;

public class GamePage extends JFrame {
	public GamePage() {
		

	}

        //GUI���ۿ� ���Ǵ� ���(��ư, �� ��)
       // GamePage myframe = new GamePage();
		static DrawPanel drawP =null; //���� ū �г�
		playPDraw playPD = null; 
		static JButton suspendB = new JButton("�Ͻ�����");//�Ͻ����� ��ư
		static JButton startB = new JButton("����");//���� ��ư
		static JButton contB = new JButton("���");// ��ӹ�ư
		static JButton endB = new JButton("����");//�����ư
		static JLabel timeLb = new JLabel("�ð�: 0�� 0��");//�ð��� ��Ÿ���� ���� ��
		static JPanel playP = new JPanel();  //���� ȭ�� �г�
		static JLabel scoreLb = new JLabel("SCORE: ");  //������
		
		
	    //������ �ʴ� ���(���� �ƴ� ������ �����Ͽ� ����ϴ� ����: ���� ��ȭ��ų���� ����)
		static int BALL_WIDTH = 30; //���� ���γ���
		static int BALL_HEIGHT = 30; //���� ���γ���
		static int BLOCK_WIDTH = 50; //���� ���γ���
		static int BLOCK_HEIGHT = 30; //���� ���γ���
		static int BLOCK_ROWS = 5; //���� �� ��
		static int BLOCK_COLUMNS = 10; //���� �� ��
		static int BLOCK_GAP = 4; //���� ������ ����
		static int BAR_WIDTH = 80; //�÷��̾ ����ϴ� ����(bar)�� ���γ���
		static int BAR_HEIGHT = 10;//�÷��̾ ����ϴ� ����(bar)�� ���γ���
		static int PLAYSCREEN_WIDTH= 565; //���ӽ���ȭ���� ���γ���
		static int PLAYSCREEN_HEIGHT= 547; //���ӽ���ȭ���� ���γ���
		static int BLOCK_INTERVAL = 20; //�� ���� ���� �ð�
		static ClockListener myClockListener ; //�ð�����ϴ� �ð�������
        static int aniSpeed =50; //�ִϸ��̼��� �ӵ�
      
		
		//PosImageIcon Ŭ������ �̿��ؼ� �̹��� ��ü ����
	    static PosImageIcon ball_Icon = new PosImageIcon("C:/DEV/eclipse-workspace/GameProject/src/�����.png", 
	    													250, 300, BALL_WIDTH, BALL_HEIGHT); //�� �̹���
	    static PosImageIcon bar_Icon = new PosImageIcon("C:/DEV/eclipse-workspace/GameProject/src/�����.png",
	    													250,380, BAR_WIDTH, BAR_HEIGHT);     //bar �̹���
	
	   
	   //���ϴ� ���
	    static int sumScore= 0;  //������
	    static Timer goAni;
		static Timer goClock; 
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];  //�ʱ����ü���� ������ ���۷��� �迭���� �� ����(������ ����)
		static Block[] onelineblock = new Block[BLOCK_COLUMNS]; // ���� ���� ���� ���پ� �� �����Ҷ� ���
		static int barXTarget = bar_Icon.imgX; ; // ���� x���� ������ ���� ����
		static int dir = 0; // ���� ���� ���� - 0:��������, 1:�����ʾƷ�, 2: ������, 3:���ʾƷ�
		static int ballSpeed = 5; // ���� �ӵ�
	    static int blockCount = 0;
	    static int state = 0; //���� ���� ���� - 0: ����, 1: ����, 2: �Ͻ�����  
	     static int result;  //���̾�α��� ��� ����
	    
	    
		//����ü�� ������ ������ Ŭ����
		static class Block{
			//x, y :���� ��ġ(�������̹Ƿ� for���� Ȱ���Ͽ� ����)
			//width, height: ���� ũ��(������ ������ ���� ���)
			//color: ���� ��
			//isHidden: ���� �浹 �� true�� ���ϸ鼭 ���� ������� ��. �ٽ�*
			int x = 0 ; 
			int y = 0; 
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; 			// 0:�ʷϻ�, 1: ��ȫ��, 2: �Ķ���, 3:��Ȳ��, 4:������ 
			boolean isHidden = false; 
			int score = 0;
			
		}


		
		//�� ��ü �����ϴ� �Լ�
		public void makeBlocks() {
			//�ʱ�� ��ü �����ϱ�
			for(int i = 0; i < BLOCK_ROWS; i++) {
				for(int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); 
					blocks[i][j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; //*�ٽ������ϱ� ���� x��ġ ����(���� ����*j + �������� ����*j)
					blocks[i][j].y = 50 + BLOCK_HEIGHT*i + BLOCK_GAP*i; //���� y��ġ ����
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = (int)(Math.random() * 4) ; // ���� ��: 0~4�� ���� �����ϰ� ����
					blocks[i][j].isHidden = false; //�浹 ������ false, �浹�ϸ� true�� ���� �� ���������� ���� �� ��.
				}
			}
		}
		
		public void makeOneLineBlocks() {
			for(int j = 0; j < BLOCK_COLUMNS; j++) {
				onelineblock[j] = new Block(); 
				onelineblock[j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; //*�ٽ������ϱ� ���� x��ġ ����(���� ����*j + �������� ����*j)
				onelineblock[j].y = 50 + BLOCK_GAP; //���� y��ġ ����
				onelineblock[j].width = BLOCK_WIDTH;
				onelineblock[j].height = BLOCK_HEIGHT;
				onelineblock[j].color = (int)(Math.random() * 4) ; // ���� ��: 0~4�� ���� �����ϰ� ����
				onelineblock[j].isHidden = false; //�浹 ������ false, �浹�ϸ� true�� ���� �� ���������� ���� �� ��.
			    System.out.println(onelineblock[j].color);
			}
		   
		   }
		
		
	
	public void go() {
		//GUI����
		this.setTitle("���������");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700, 200, 565, 625);

		//�ʱ� ���� �������� ��������, ������ ���ϱ�
		int randomdir = (int)(Math.random() * 10);
		if(randomdir%2==0)
			dir = 0;
		else
			dir=1;
		
		//GUI �׸���
		drawP = new DrawPanel();
		this.add(drawP);
		this.setVisible(true);
 		
 		makeBlocks();
 		
 		this.setFocusable(true);
		this.addKeyListener(new MyKeyListener());
	
		myClockListener.reset();
		goClock.start();
	    startTimer();
			
	
	}
	
	class MyKeyListener extends KeyAdapter {
			//Ű �������� �̺�Ʈ
			public void keyPressed(KeyEvent e) {
				 //���� Ű�� ������ ��
				if(e.getKeyCode() == KeyEvent.VK_LEFT) { 
					System.out.println("����");
					barXTarget-=15;
					// Ű�� �����ؼ� ���� ���(���� ���� ��ġ�� Ÿ�ٰ� ���� ���� ���)
					if(bar_Icon.imgX < barXTarget) {
						barXTarget = bar_Icon.imgX;
					}
				}
				
				//������ Ű�� ������ ��
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {  
					System.out.println("������");
					barXTarget+=15; 
					// Ű�� �����ؼ� ���� ���(���� ���� ��ġ�� Ÿ�ٰ� ���� ���� ���)
					if(bar_Icon.imgX > barXTarget) {
						barXTarget = bar_Icon.imgX;
					}
				}		
			}
	}
	
	
	//GUI �׸������� �г�
		class DrawPanel extends JPanel{
			public DrawPanel() {
	            //���� ū �г�
				this.setSize(543, 530);
				this.setLayout(null);
				this.setBorder(new EmptyBorder(5, 5, 5, 5));
				
				// ���� ���� ��ư �� ���� �� �г�
				JPanel buttomP = new JPanel();
				buttomP.setBackground(SystemColor.controlHighlight);
				buttomP.setBounds(5, 531, 528, 42);
				buttomP.setLayout(null);
				
				suspendB.setBounds(122, 10, 85, 23);
				buttomP.add(suspendB);
				
				
				startB.setBounds(37, 10, 63, 23);
				buttomP.add(startB);
			
				
				contB.setBounds(227, 10, 63, 23);
				buttomP.add(contB);
				
				

				endB.setBounds(308, 10, 63, 23);
				buttomP.add(endB);
			
				
				timeLb.setBounds(400, 14, 92, 15);
				buttomP.add(timeLb);		
				this.add(buttomP);
				
				//�׼Ǹ����� �ޱ�
				suspendB.addActionListener(new MyButtonListener());
				contB.addActionListener(new MyButtonListener());
				startB.addActionListener(new MyButtonListener());
				endB.addActionListener(new MyButtonListener());	
					
				
				myClockListener = new  ClockListener();
				goClock = new Timer(1000, myClockListener);
			
				goAni = new Timer(aniSpeed, new AnimeListener());
				
				scoreLb.setBounds(417, 20, 100, 15);			
				this.add(scoreLb);
				
				
				//����ȭ���� �ֿ� ��� �׸���
				playPD = new playPDraw();
				this.add(playPD);

				
			}
		}
	
		
	// ��ư �̺�Ʈ�� ������ Ŭ������ �ۼ� - 0: ����, 1: ����, 2: �Ͻ����� 

	class MyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			if(b.getText().equals("����")) {
				//state = 1;
				//go();
				
			}
			else if(b.getText().equals("����")) 
				state = 0;
			else 
				state = 2;
			
		}
		
	}
	
		
	class ClockListener implements ActionListener{
		int times = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
			times++;
			timeLb.setText("�ð� : " + times/60 + "�� "+ times%60 + "��");
			
			//���ʸ��� �� ���پ� ������
			if(times % BLOCK_INTERVAL == 0) {
				for(int i = 0; i < BLOCK_ROWS; i++) {
					for(int j = 0; j < BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) { 			//isHidden�� true�� �׸��ʿ� ����.
							continue;
						}
						else {
							blocks[i][j].y += BLOCK_HEIGHT + BLOCK_GAP*2;  //�Ʒ��� �� �̵� 
							
							//���� �Ʒ��� y���� ���� ���� ���� �Ʒ��� �������ԵǸ� �Ǹ� ����
							if(blocks[i][j].y + BLOCK_HEIGHT  >=  bar_Icon.getTopCenter().y) {
								//������̾�α� ����
								result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + "\n�ٽ� �����ϰڽ��ϱ�?"
											, "Confirm", JOptionPane .YES_NO_OPTION);
								
								if(result == JOptionPane.CLOSED_OPTION)
									System.exit(0);
								else if(result ==JOptionPane.YES_OPTION ) {
									go();
								}
								else
									System.exit(0);
							}
						}
					}
				}
				//makeOneLineBlocks();
				//blockCount++;
			}
		}
		
		public void reset() {
			times = 0;
		}
		public int getElaspedTime() {
			return times;
		}
		
	}
	
		
	public class AnimeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {		
		}
		
	}
	
	
		
	//������ ����Ǵ� ȭ���� ��� �׸��� Ŭ����
	class playPDraw extends JPanel{
		public playPDraw() {
			this.setBounds(5, 45, 558, 472);
			this.setBackground(Color.WHITE);	
			this.setLayout(null);
		}
			public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			
		    ball_Icon.draw(g); //���̹��� �׸���	
		    bar_Icon.draw(g);  //bar�̹��� �׸���
		    
		    //���׸���
			drawBlocks(g2d);
			
			/*
			if(blockCount > 0)
				drawOneLineBlocks(g2d);
				*/
		}
		
		
		//�� �׸��� �Լ�
		public void drawBlocks(Graphics2D g2d) {			
			
			//�ʱ� �� �ֱ�
			for(int i = 0; i < BLOCK_ROWS; i++) {
				for(int j = 0; j < BLOCK_COLUMNS; j++) {
					if(blocks[i][j].isHidden) {			//isHidden�� true�� �׸��ʿ� ����.
						continue;
					}
					if(blocks[i][j].color ==0 ){ 		//color�� ���� 0�̸� �ʷϻ�
						g2d.setColor(Color.GREEN);
						blocks[i][j].score = 10;
					}
					else if(blocks[i][j].color == 1){ 	//color�� ���� 1�̸� ��ȫ��
						g2d.setColor(Color.PINK);
						blocks[i][j].score = 20;
					}
					else if(blocks[i][j].color == 2){	 //color�� ���� 2�̸� �Ķ���
						g2d.setColor(Color.BLUE);
						blocks[i][j].score = 30;
					}
					else if(blocks[i][j].color == 3){ 	//color�� ���� 3�̸� ��Ȳ��
						g2d.setColor(Color.ORANGE);
						blocks[i][j].score = 40;
					}
					else if(blocks[i][j].color == 3){ 	//color�� ���� 4�̸� ������
						g2d.setColor(Color.RED);
						blocks[i][j].score = 100;
					}	
					g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
				}
			}
		}
	
		/* �����ٸ� �׸��� �Լ� 
		public void drawOneLineBlocks(Graphics2D g2d) {			
			System.out.println("paint3");
	
			for(int i = 0; i < BLOCK_COLUMNS; i++) {
				
					if(onelineblock[i].isHidden) { //isHidden�� true�� �׸��ʿ� ����.
						continue;
					}
					if(onelineblock[i].color ==0 ){ //color�� ���� 0�̸� �ʷϻ�
						g2d.setColor(Color.GREEN);
						onelineblock[i].score = 10;
					}
					else if(onelineblock[i].color == 1){ //color�� ���� 1�̸� ��ȫ��
						g2d.setColor(Color.PINK);
						onelineblock[i].score = 20;
					}
					else if(onelineblock[i].color == 2){ //color�� ���� 2�̸� �Ķ���
						g2d.setColor(Color.BLUE);
						onelineblock[i].score = 30;
					}
					else if(onelineblock[i].color == 3){ //color�� ���� 3�̸� ��Ȳ��
						g2d.setColor(Color.ORANGE);
						onelineblock[i].score = 40;
					}
					else if(onelineblock[i].color == 3){ //color�� ���� 4�̸� ������
						g2d.setColor(Color.RED);
						onelineblock[i].score = 100;
					}	
					g2d.fillRect(onelineblock[i].x, onelineblock[i].y, onelineblock[i].width, onelineblock[i].height);
				}
			}*/
		
	}
	


	public void startTimer() {
		goClock = new Timer(20, new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				moverment(); 			//�����̴� �Լ�
				checkCollision(); 		//���� ���� �ٿ� �浹�������� �Լ�
				checkCollisionBlock();	 //���� ���� �浹�������� �Լ�
				playPD.repaint(); //redraw
			}
		});
		
		goClock.start(); //start timer
	}
	
	
	//�����̴� �Լ�
	public void moverment() {
		//�� �����̱�
		if(bar_Icon.imgX > barXTarget) {
			bar_Icon.move(5, 0, 4);
		}
		else if (bar_Icon.imgX < barXTarget) {
			bar_Icon.move(5, 0, 5);
		}
		
		//�� �밢������ �����̱�
		if(dir==0) { 		//������ ��
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}
		else if(dir==1) { 	//������ �Ʒ�
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		} 
		else if(dir==2) { 	//���� ��
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}
		else if(dir==3) { 	//���� �Ʒ�
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}

	}
	
	//���� �� �Ǵ� ������ �浹�ߴ��� Ȯ���ϴ� �Լ�
	public boolean duplRECT(Rectangle rect1, Rectangle rect2) {
		return rect1.intersects(rect2);
	}

	
	//���� ���� �ٿ� �浹�������� �Լ�
	public void checkCollision() {
		
		//���� ������ ���� �̵��� ���� ���
		if(dir==0) { 
			//����, �Ʒ��� ���� �浹�� ��� ����. �ٿ� �浹�� ���� ����.
			//���� ���� �����Ͽ� ������ �Ʒ��� �̵�
			if(ball_Icon.imgY < 0) {
			  dir = 1;
			}
			//������ ���� �浹�Ͽ� ���� ���� �̵�
			if(ball_Icon.imgX > PLAYSCREEN_WIDTH-BALL_WIDTH*2) {                                              //�������δ� (*2)�� �ʿ������ *2�� �־�� ��Ȯ�� �̵��Ѵ�. 
				dir = 2;
			}
		}
		
		//���� ������ �Ʒ��� �̵��� ���� ���
		else if(dir==1) { 
			//����, ���� ���� ������ ���� ����
			//�Ʒ��� ���� �浹�� ���**
			if(ball_Icon.imgY > PLAYSCREEN_HEIGHT - BALL_HEIGHT*3) {
				dir = 0;
				
				result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + 
						"\n�ٽ� �����ϰڽ��ϱ�?",  "Confirm", JOptionPane .YES_NO_OPTION);
				
				// yes�� ������ ���� ������ġ�� ���� �ٽ� ����, �׷��� ������ ����
				if(result == JOptionPane.CLOSED_OPTION)
					System.exit(0);
				else if(result ==JOptionPane.YES_OPTION ) {
					ball_Icon.imgX = 250;    
					ball_Icon.imgY = 300;
					go();
				}
				else
					System.exit(0);
			}
			
			//������ ���� �ε������� ���� �Ʒ��� �̵�
			if(ball_Icon.imgX > PLAYSCREEN_WIDTH-BALL_WIDTH*2) {
				dir = 3;
			}
			//�ٿ� �浹������ ������ ���� �̵�
			if(ball_Icon.getBottomCenter().y >= bar_Icon.imgY) {
				if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(bar_Icon.imgX, bar_Icon.imgY, bar_Icon.imgWidth, bar_Icon.imgHeight))) {
					dir = 0;
				}
			}
		} 
		
		//���� ���� ���� �̵��� ���� ���
		else if(dir==2) { 
			//������, �Ʒ��� ���� �浹�� ��� ����. 
			//�ٿ� �浹�� ���� ����.
			
			//���� ���� �����Ͽ� ���� �Ʒ��� �̵�
			if(ball_Icon.imgY < 0) {
				dir = 3;
			}
			//���� ���� �浹�Ͽ� ������ ���� �̵�
			if(ball_Icon.imgX < 0) {
				dir = 0;
			}
			
		}
		
		//���� ���� �Ʒ��� �̵��� ���� ���
		else if(dir==3) { 
			//����, ������ ���� ������ ���� ����
			
			//�Ʒ��� ���� �浹�� ���**
			if(ball_Icon.imgY > PLAYSCREEN_HEIGHT - BALL_HEIGHT*3) {
				dir = 2;
				
				//������̾�α� ����
				result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + "\n�ٽ� �����ϰڽ��ϱ�?"
							, "Confirm", JOptionPane .YES_NO_OPTION);
				
				if(result == JOptionPane.CLOSED_OPTION)
					System.exit(0);
				else if(result ==JOptionPane.YES_OPTION ) {
					go();
				}
				else
					System.exit(0);

			}
			
			//���� ���� �ε������� ������ �Ʒ��� �̵�
			if(ball_Icon.imgX < 0) {
				dir = 1;
			}
			//�ٿ� �浹������ ���� ���� �̵�
			if(ball_Icon.getBottomCenter().y >= bar_Icon.imgY) {
				if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(bar_Icon.imgX, bar_Icon.imgY, bar_Icon.imgWidth, bar_Icon.imgHeight))) {
					dir = 2;
				}
			}
		}
	}
	



	//���� ���� �浹�� üũ�ϴ� �Լ�
	public void checkCollisionBlock(){
		//���� ���� �浹�ߴ��� ���� �ľ��ϱ�
		for(int i=0; i<BLOCK_ROWS; i++) {
			for(int j=0; j<BLOCK_COLUMNS; j++) {
				Block block = blocks[i][j];
				if(block.isHidden == false) {
					//���� ������ ���� �̵��Ҷ� 
					if(dir==0) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// ���� ���� �Ʒ��� �浹������ ������ �Ʒ��� �̵�
							if(ball_Icon.imgX > block.x + 1 && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 1;
							}
							//���� ���� ���ʿ� �浹������ ���� ���� �̵�
							else {
								dir = 2;
							}
							//���� ������� ��.
							block.isHidden = true;
							breakSound();  //�Ҹ�����
							sumScore += block.score;   //�� ���� �ش��ϴ� ���� ����
							scoreLb.setText("SCORE: " + sumScore);  //���� ��Ÿ����
						}
					}
					
					
					//���� ������ �Ʒ��� �̵��Ҷ� 
					if(dir==1) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// ���� ���� ���� �浹������ ������ ���� �̵�
							if(ball_Icon.imgX > block.x+1  && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 0;
							}
							//���� ���� ���ʿ� �浹������ ���� �Ʒ��� �̵�
							else {
								dir = 3;
							}
							//���� ������� ��.
							block.isHidden = true; 
							breakSound();  //�Ҹ�����
							sumScore += block.score;   //�� ���� �ش��ϴ� ���� ����
							scoreLb.setText("SCORE: " + sumScore);  //���� ��Ÿ����
						}
					} 
					
					//���� ���� ���� �̵��Ҷ� 
					else if(dir==2) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// ���� ���� �Ʒ��� �浹������ ���� �Ʒ��� �̵�
							if(ball_Icon.imgX > block.x + 1 && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 3;
							}
							//���� ���� �����ʿ� �浹������ ������ ���� �̵�
							else {
								dir = 0;
							}
							//���� ������� ��.
							block.isHidden = true;
							breakSound();  //�Ҹ�����
							sumScore += block.score;   //�� ���� �ش��ϴ� ���� ����
							scoreLb.setText("SCORE: " + sumScore);  //���� ��Ÿ����
						}
					} 
					
					//���� ���� �Ʒ��� �̵��Ҷ� 
					if(duplRECT(new Rectangle(ball_Icon.imgX,  ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(block.x, block.y, block.width, block.height))) {
						// ���� ���� ���� �浹������ ���� ���� �̵�
						if(ball_Icon.imgX > block.x + 1 && 
								ball_Icon.getRightCenter().x <= block.x + block.width-1) {
							dir = 2;
						}
						//���� ���� �����ʿ� �浹������ ������ �Ʒ��� �̵�
						else {
							dir = 1;
						}
						//���� ������� ��.
						block.isHidden = true;
						breakSound();  //�Ҹ�����
						sumScore += block.score;   //�� ���� �ش��ϴ� ���� ����
						scoreLb.setText("SCORE: " + sumScore);  //���� ��Ÿ����
					}
					
					}

				}
			}

		}
	
	
	//���� ���� �浹�Ҷ� �Ҹ����� �ϴ� �Լ�
	public void breakSound() {
		
        File file = new File("C:\\Users\\USER\\Desktop\\��������������Ʈ\\��.wav");
        System.out.println(file.exists()); //true
        
        try {
            
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
            
        } catch(Exception e) {
            
            e.printStackTrace();
        }

	}
	
	
	
	public static void main(String[] args) {
	new GamePage().go();
	}


}


