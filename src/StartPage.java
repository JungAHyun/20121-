import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JButton;

public class StartPage extends JFrame {

	private JPanel contentPane;
	JFrame startFrame = new JFrame();
	public static void main(String[] args) {
		new StartPage();
	}


	public StartPage() {
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 200, 565, 625);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 10, 521, 555);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("�� ���� ����");
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setBounds(137, 125, 281, 65);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("�����̽��� ��ũ������ü", Font.ITALIC, 50));
		
		JLabel lblBrickOutGame = new JLabel("Brick out Game");
		lblBrickOutGame.setFont(new Font("Agency FB", Font.ITALIC, 34));
		lblBrickOutGame.setBackground(Color.WHITE);
		lblBrickOutGame.setBounds(267, 175, 281, 65);
		panel.add(lblBrickOutGame);
		
		//�����ư
		JButton ENDbtn = new JButton("END"); 
		ENDbtn.setFont(new Font("Baskerville Old Face", Font.PLAIN, 20));
		ENDbtn.setBounds(350, 425, 130, 48);
		panel.add(ENDbtn);
		
		//���۹�ư
		JButton STARTbtn = new JButton("START");
		STARTbtn.setFont(new Font("Baskerville Old Face", Font.PLAIN, 20));
		STARTbtn.setBounds(350, 339, 130, 48);
		panel.add(STARTbtn);
		
		//��ư�� �׼Ǹ����� �ޱ�
		STARTbtn.addActionListener(new MyActionListener());
		ENDbtn.addActionListener(new MyActionListener());
		
		
		setVisible(true);
		
		
	}
	
	//start��ư�� ������ ���ӽ���, end��ư�� ������ �����ϴ� �׼� ������
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("START")) {
				new GamePage().go();
			}
			else {
				System.exit(0);
			}
			
		}
		
	}
}
