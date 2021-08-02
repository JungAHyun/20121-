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

        //GUI제작에 사용되는 요소(버튼, 라벨 등)
       // GamePage myframe = new GamePage();
		static DrawPanel drawP =null; //제일 큰 패널
		playPDraw playPD = null; 
		static JButton suspendB = new JButton("일시중지");//일시중지 버튼
		static JButton startB = new JButton("시작");//시작 버튼
		static JButton contB = new JButton("계속");// 계속버튼
		static JButton endB = new JButton("종료");//종료버튼
		static JLabel timeLb = new JLabel("시간: 0분 0초");//시간을 나타내기 위한 라벨
		static JPanel playP = new JPanel();  //게임 화면 패널
		static JLabel scoreLb = new JLabel("SCORE: ");  //점수라벨
		
		
	    //변하지 않는 요소(값이 아닌 변수를 설정하여 사용하는 이유: 값을 변화시킬때에 용이)
		static int BALL_WIDTH = 30; //공의 가로넓이
		static int BALL_HEIGHT = 30; //공의 세로높이
		static int BLOCK_WIDTH = 50; //블럭의 가로넓이
		static int BLOCK_HEIGHT = 30; //블럭의 세로높이
		static int BLOCK_ROWS = 5; //블럭의 행 수
		static int BLOCK_COLUMNS = 10; //블럭의 열 수
		static int BLOCK_GAP = 4; //블럭들 사이의 간격
		static int BAR_WIDTH = 80; //플레이어가 사용하는 바의(bar)의 가로넓이
		static int BAR_HEIGHT = 10;//플레이어가 사용하는 바의(bar)의 세로높이
		static int PLAYSCREEN_WIDTH= 565; //게임실행화면의 가로넓이
		static int PLAYSCREEN_HEIGHT= 547; //게임실행화면의 세로높이
		static int BLOCK_INTERVAL = 20; //블러 생성 지연 시간
		static ClockListener myClockListener ; //시간사용하는 시간리스터
        static int aniSpeed =50; //애니매이션의 속도
      
		
		//PosImageIcon 클래스를 이용해서 이미지 객체 생성
	    static PosImageIcon ball_Icon = new PosImageIcon("C:/DEV/eclipse-workspace/GameProject/src/색깔공.png", 
	    													250, 300, BALL_WIDTH, BALL_HEIGHT); //공 이미지
	    static PosImageIcon bar_Icon = new PosImageIcon("C:/DEV/eclipse-workspace/GameProject/src/막대기.png",
	    													250,380, BAR_WIDTH, BAR_HEIGHT);     //bar 이미지
	
	   
	   //변하는 요소
	    static int sumScore= 0;  //총점수
	    static Timer goAni;
		static Timer goClock; 
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];  //초기블럭객체들을 저장할 레퍼런스 배열선언 및 생성(공간만 생성)
		static Block[] onelineblock = new Block[BLOCK_COLUMNS]; // 게임 진행 도중 한줄씩 블럭 생성할때 사용
		static int barXTarget = bar_Icon.imgX; ; // 바의 x값의 보관을 위한 변수
		static int dir = 0; // 공의 방향 설정 - 0:오른쪽위, 1:오른쪽아래, 2: 왼쪽위, 3:왼쪽아래
		static int ballSpeed = 5; // 공의 속도
	    static int blockCount = 0;
	    static int state = 0; //현재 진행 상태 - 0: 종료, 1: 시작, 2: 일시정지  
	     static int result;  //다이어로그의 결과 저장
	    
	    
		//블럭객체의 정보를 가지는 클래스
		static class Block{
			//x, y :블럭의 위치(여러개이므로 for문을 활용하여 설정)
			//width, height: 블럭의 크기(위에서 설정한 변수 사용)
			//color: 블럭의 색
			//isHidden: 공과 충돌 후 true로 변하면서 블럭을 사라지게 함. 다시*
			int x = 0 ; 
			int y = 0; 
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; 			// 0:초록색, 1: 분홍색, 2: 파란색, 3:주황색, 4:빨간색 
			boolean isHidden = false; 
			int score = 0;
			
		}


		
		//블럭 객체 생성하는 함수
		public void makeBlocks() {
			//초기블럭 객체 생성하기
			for(int i = 0; i < BLOCK_ROWS; i++) {
				for(int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); 
					blocks[i][j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; //*다시이해하기 블럭의 x위치 설정(블럭의 넓이*j + 블럭사이의 간격*j)
					blocks[i][j].y = 50 + BLOCK_HEIGHT*i + BLOCK_GAP*i; //블럭의 y위치 설정
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = (int)(Math.random() * 4) ; // 블럭의 색: 0~4의 숫자 랜덤하게 결정
					blocks[i][j].isHidden = false; //충돌 전에는 false, 충돌하면 true로 변경 후 숨겨지도록 설정 할 것.
				}
			}
		}
		
		public void makeOneLineBlocks() {
			for(int j = 0; j < BLOCK_COLUMNS; j++) {
				onelineblock[j] = new Block(); 
				onelineblock[j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; //*다시이해하기 블럭의 x위치 설정(블럭의 넓이*j + 블럭사이의 간격*j)
				onelineblock[j].y = 50 + BLOCK_GAP; //블럭의 y위치 설정
				onelineblock[j].width = BLOCK_WIDTH;
				onelineblock[j].height = BLOCK_HEIGHT;
				onelineblock[j].color = (int)(Math.random() * 4) ; // 블럭의 색: 0~4의 숫자 랜덤하게 결정
				onelineblock[j].isHidden = false; //충돌 전에는 false, 충돌하면 true로 변경 후 숨겨지도록 설정 할 것.
			    System.out.println(onelineblock[j].color);
			}
		   
		   }
		
		
	
	public void go() {
		//GUI세팅
		this.setTitle("블럭깨기게임");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(700, 200, 565, 625);

		//초기 방향 랜덤으로 오른쪽위, 왼쪽위 정하기
		int randomdir = (int)(Math.random() * 10);
		if(randomdir%2==0)
			dir = 0;
		else
			dir=1;
		
		//GUI 그리기
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
			//키 누를때의 이벤트
			public void keyPressed(KeyEvent e) {
				 //왼쪽 키를 눌렀을 때
				if(e.getKeyCode() == KeyEvent.VK_LEFT) { 
					System.out.println("왼쪽");
					barXTarget-=15;
					// 키를 연속해서 누를 경우(바의 실제 위치가 타겟값 보다 작은 경우)
					if(bar_Icon.imgX < barXTarget) {
						barXTarget = bar_Icon.imgX;
					}
				}
				
				//오늘쪽 키를 눌렀을 때
				if(e.getKeyCode() == KeyEvent.VK_RIGHT) {  
					System.out.println("오를쪽");
					barXTarget+=15; 
					// 키를 연속해서 누를 경우(바의 실제 위치가 타겟값 보다 작은 경우)
					if(bar_Icon.imgX > barXTarget) {
						barXTarget = bar_Icon.imgX;
					}
				}		
			}
	}
	
	
	//GUI 그리기위한 패널
		class DrawPanel extends JPanel{
			public DrawPanel() {
	            //가장 큰 패널
				this.setSize(543, 530);
				this.setLayout(null);
				this.setBorder(new EmptyBorder(5, 5, 5, 5));
				
				// 게임 조정 버튼 및 라벨이 들어갈 패널
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
				
				//액션리스너 달기
				suspendB.addActionListener(new MyButtonListener());
				contB.addActionListener(new MyButtonListener());
				startB.addActionListener(new MyButtonListener());
				endB.addActionListener(new MyButtonListener());	
					
				
				myClockListener = new  ClockListener();
				goClock = new Timer(1000, myClockListener);
			
				goAni = new Timer(aniSpeed, new AnimeListener());
				
				scoreLb.setBounds(417, 20, 100, 15);			
				this.add(scoreLb);
				
				
				//게임화면의 주요 요소 그리기
				playPD = new playPDraw();
				this.add(playPD);

				
			}
		}
	
		
	// 버튼 이벤트를 독립된 클래스로 작성 - 0: 종료, 1: 시작, 2: 일시정지 

	class MyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			
			if(b.getText().equals("시작")) {
				//state = 1;
				//go();
				
			}
			else if(b.getText().equals("종료")) 
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
			timeLb.setText("시간 : " + times/60 + "분 "+ times%60 + "초");
			
			//몇초마다 블럭 한줄씩 내리기
			if(times % BLOCK_INTERVAL == 0) {
				for(int i = 0; i < BLOCK_ROWS; i++) {
					for(int j = 0; j < BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) { 			//isHidden이 true면 그릴필요 없다.
							continue;
						}
						else {
							blocks[i][j].y += BLOCK_HEIGHT + BLOCK_GAP*2;  //아래로 블럭 이동 
							
							//블럭의 아래면 y값이 바의 윗면 보다 아래로 내려가게되면 되면 종료
							if(blocks[i][j].y + BLOCK_HEIGHT  >=  bar_Icon.getTopCenter().y) {
								//종료다이어로그 띄우기
								result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + "\n다시 시작하겠습니까?"
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
	
	
		
	//게임이 진행되는 화면의 요소 그리는 클래스
	class playPDraw extends JPanel{
		public playPDraw() {
			this.setBounds(5, 45, 558, 472);
			this.setBackground(Color.WHITE);	
			this.setLayout(null);
		}
			public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D)g;
			
		    ball_Icon.draw(g); //공이미지 그리기	
		    bar_Icon.draw(g);  //bar이미지 그리기
		    
		    //블럭그리기
			drawBlocks(g2d);
			
			/*
			if(blockCount > 0)
				drawOneLineBlocks(g2d);
				*/
		}
		
		
		//블럭 그리는 함수
		public void drawBlocks(Graphics2D g2d) {			
			
			//초기 블럭 넣기
			for(int i = 0; i < BLOCK_ROWS; i++) {
				for(int j = 0; j < BLOCK_COLUMNS; j++) {
					if(blocks[i][j].isHidden) {			//isHidden이 true면 그릴필요 없다.
						continue;
					}
					if(blocks[i][j].color ==0 ){ 		//color의 값이 0이면 초록색
						g2d.setColor(Color.GREEN);
						blocks[i][j].score = 10;
					}
					else if(blocks[i][j].color == 1){ 	//color의 값이 1이면 분홍색
						g2d.setColor(Color.PINK);
						blocks[i][j].score = 20;
					}
					else if(blocks[i][j].color == 2){	 //color의 값이 2이면 파란색
						g2d.setColor(Color.BLUE);
						blocks[i][j].score = 30;
					}
					else if(blocks[i][j].color == 3){ 	//color의 값이 3이면 주황색
						g2d.setColor(Color.ORANGE);
						blocks[i][j].score = 40;
					}
					else if(blocks[i][j].color == 3){ 	//color의 값이 4이면 빨간색
						g2d.setColor(Color.RED);
						blocks[i][j].score = 100;
					}	
					g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
				}
			}
		}
	
		/* 블럭한줄만 그리는 함수 
		public void drawOneLineBlocks(Graphics2D g2d) {			
			System.out.println("paint3");
	
			for(int i = 0; i < BLOCK_COLUMNS; i++) {
				
					if(onelineblock[i].isHidden) { //isHidden이 true면 그릴필요 없다.
						continue;
					}
					if(onelineblock[i].color ==0 ){ //color의 값이 0이면 초록색
						g2d.setColor(Color.GREEN);
						onelineblock[i].score = 10;
					}
					else if(onelineblock[i].color == 1){ //color의 값이 1이면 분홍색
						g2d.setColor(Color.PINK);
						onelineblock[i].score = 20;
					}
					else if(onelineblock[i].color == 2){ //color의 값이 2이면 파란색
						g2d.setColor(Color.BLUE);
						onelineblock[i].score = 30;
					}
					else if(onelineblock[i].color == 3){ //color의 값이 3이면 주황색
						g2d.setColor(Color.ORANGE);
						onelineblock[i].score = 40;
					}
					else if(onelineblock[i].color == 3){ //color의 값이 4이면 빨간색
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
				moverment(); 			//움직이는 함수
				checkCollision(); 		//공이 벽과 바에 충돌했을때의 함수
				checkCollisionBlock();	 //공이 블럭에 충돌했을때의 함수
				playPD.repaint(); //redraw
			}
		});
		
		goClock.start(); //start timer
	}
	
	
	//움직이는 함수
	public void moverment() {
		//바 움직이기
		if(bar_Icon.imgX > barXTarget) {
			bar_Icon.move(5, 0, 4);
		}
		else if (bar_Icon.imgX < barXTarget) {
			bar_Icon.move(5, 0, 5);
		}
		
		//공 대각선으로 움직이기
		if(dir==0) { 		//오른쪽 위
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}
		else if(dir==1) { 	//오른쪽 아래
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		} 
		else if(dir==2) { 	//왼쪽 위
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}
		else if(dir==3) { 	//왼쪽 아래
			ball_Icon.move(ballSpeed, ballSpeed, dir);
		}

	}
	
	//공이 바 또는 벽돌과 충돌했는지 확인하는 함수
	public boolean duplRECT(Rectangle rect1, Rectangle rect2) {
		return rect1.intersects(rect2);
	}

	
	//공이 벽과 바에 충돌했을때의 함수
	public void checkCollision() {
		
		//공이 오른쪽 위로 이동할 때의 경우
		if(dir==0) { 
			//왼쪽, 아래쪽 벽에 충돌할 경우 없음. 바에 충돌할 경우는 없음.
			//위쪽 벽에 층돌하여 오른쪽 아래로 이동
			if(ball_Icon.imgY < 0) {
			  dir = 1;
			}
			//오른쪽 벽에 충돌하여 왼쪽 위로 이동
			if(ball_Icon.imgX > PLAYSCREEN_WIDTH-BALL_WIDTH*2) {                                              //계산상으로는 (*2)가 필요없지만 *2가 있어야 정확히 이동한다. 
				dir = 2;
			}
		}
		
		//공이 오른쪽 아래로 이동할 때의 경우
		else if(dir==1) { 
			//위쪽, 왼쪽 벽에 층돌할 경우는 없음
			//아래쪽 벽에 충돌할 경우**
			if(ball_Icon.imgY > PLAYSCREEN_HEIGHT - BALL_HEIGHT*3) {
				dir = 0;
				
				result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + 
						"\n다시 시작하겠습니까?",  "Confirm", JOptionPane .YES_NO_OPTION);
				
				// yes를 누르면 공을 원래위치로 놓고 다시 시작, 그렇지 않으면 종료
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
			
			//오른쪽 벽에 부딧혔을때 왼쪽 아래로 이동
			if(ball_Icon.imgX > PLAYSCREEN_WIDTH-BALL_WIDTH*2) {
				dir = 3;
			}
			//바에 충돌했을때 오른쪽 위로 이동
			if(ball_Icon.getBottomCenter().y >= bar_Icon.imgY) {
				if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(bar_Icon.imgX, bar_Icon.imgY, bar_Icon.imgWidth, bar_Icon.imgHeight))) {
					dir = 0;
				}
			}
		} 
		
		//공이 왼쪽 위로 이동할 때의 경우
		else if(dir==2) { 
			//오른쪽, 아래쪽 벽에 충돌할 경우 없음. 
			//바에 충돌할 경우는 없음.
			
			//위쪽 벽에 층돌하여 왼쪽 아래로 이동
			if(ball_Icon.imgY < 0) {
				dir = 3;
			}
			//왼쪽 벽에 충돌하여 오른쪽 위로 이동
			if(ball_Icon.imgX < 0) {
				dir = 0;
			}
			
		}
		
		//공이 왼쪽 아래로 이동할 때의 경우
		else if(dir==3) { 
			//위쪽, 오른쪽 벽에 층돌할 경우는 없음
			
			//아래쪽 벽에 충돌할 경우**
			if(ball_Icon.imgY > PLAYSCREEN_HEIGHT - BALL_HEIGHT*3) {
				dir = 2;
				
				//종료다이어로그 띄우기
				result = JOptionPane.showConfirmDialog(null, "SCORE: " + sumScore + "\n다시 시작하겠습니까?"
							, "Confirm", JOptionPane .YES_NO_OPTION);
				
				if(result == JOptionPane.CLOSED_OPTION)
					System.exit(0);
				else if(result ==JOptionPane.YES_OPTION ) {
					go();
				}
				else
					System.exit(0);

			}
			
			//왼쪽 벽에 부딧혔을때 오른쪽 아래로 이동
			if(ball_Icon.imgX < 0) {
				dir = 1;
			}
			//바에 충돌했을때 왼쪽 위로 이동
			if(ball_Icon.getBottomCenter().y >= bar_Icon.imgY) {
				if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(bar_Icon.imgX, bar_Icon.imgY, bar_Icon.imgWidth, bar_Icon.imgHeight))) {
					dir = 2;
				}
			}
		}
	}
	



	//공과 블럭의 충돌을 체크하는 함수
	public void checkCollisionBlock(){
		//블럭이 공과 충돌했는지 여부 파악하기
		for(int i=0; i<BLOCK_ROWS; i++) {
			for(int j=0; j<BLOCK_COLUMNS; j++) {
				Block block = blocks[i][j];
				if(block.isHidden == false) {
					//공이 오른쪽 위로 이동할때 
					if(dir==0) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// 공이 블럭의 아래에 충돌했을때 오른쪽 아래로 이동
							if(ball_Icon.imgX > block.x + 1 && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 1;
							}
							//공이 블럭의 왼쪽에 충돌했을때 왼쪽 위로 이동
							else {
								dir = 2;
							}
							//블럭이 사라지게 함.
							block.isHidden = true;
							breakSound();  //소리내기
							sumScore += block.score;   //블럭 색깔에 해당하는 점수 증가
							scoreLb.setText("SCORE: " + sumScore);  //점수 나타내기
						}
					}
					
					
					//공이 오른쪽 아래로 이동할때 
					if(dir==1) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// 공이 블럭의 위에 충돌했을때 오른쪽 위로 이동
							if(ball_Icon.imgX > block.x+1  && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 0;
							}
							//공이 블럭의 왼쪽에 충돌했을때 왼쪽 아래로 이동
							else {
								dir = 3;
							}
							//블럭이 사라지게 함.
							block.isHidden = true; 
							breakSound();  //소리내기
							sumScore += block.score;   //블럭 색깔에 해당하는 점수 증가
							scoreLb.setText("SCORE: " + sumScore);  //점수 나타내기
						}
					} 
					
					//공이 왼쪽 위로 이동할때 
					else if(dir==2) { 
						if(duplRECT(new Rectangle(ball_Icon.imgX, ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
							    new Rectangle(block.x, block.y, block.width, block.height))) {
							// 공이 블럭의 아래에 충돌했을때 왼쪽 아래로 이동
							if(ball_Icon.imgX > block.x + 1 && 
									ball_Icon.getRightCenter().x <= block.x + block.width-1) {
								dir = 3;
							}
							//공이 블럭의 오른쪽에 충돌했을때 오른쪽 위로 이동
							else {
								dir = 0;
							}
							//블럭이 사라지게 함.
							block.isHidden = true;
							breakSound();  //소리내기
							sumScore += block.score;   //블럭 색깔에 해당하는 점수 증가
							scoreLb.setText("SCORE: " + sumScore);  //점수 나타내기
						}
					} 
					
					//공이 왼쪽 아래로 이동할때 
					if(duplRECT(new Rectangle(ball_Icon.imgX,  ball_Icon.imgY, ball_Icon.imgWidth, ball_Icon.imgHeight), 
						    new Rectangle(block.x, block.y, block.width, block.height))) {
						// 공이 블럭의 위에 충돌했을때 왼쪽 위로 이동
						if(ball_Icon.imgX > block.x + 1 && 
								ball_Icon.getRightCenter().x <= block.x + block.width-1) {
							dir = 2;
						}
						//공이 블럭의 오른쪽에 충돌했을때 오른쪽 아래로 이동
						else {
							dir = 1;
						}
						//블럭이 사라지게 함.
						block.isHidden = true;
						breakSound();  //소리내기
						sumScore += block.score;   //블럭 색깔에 해당하는 점수 증가
						scoreLb.setText("SCORE: " + sumScore);  //점수 나타내기
					}
					
					}

				}
			}

		}
	
	
	//블럭과 공이 충돌할때 소리나게 하는 함수
	public void breakSound() {
		
        File file = new File("C:\\Users\\USER\\Desktop\\게임제작프로젝트\\뽁.wav");
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


