package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gameover extends JFrame {
    public Gameover() {
        setTitle("Jump Jump Game !");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel gameover_backgroundLabel = new JLabel(new ImageIcon("./src/Image/gameoverscreen.png"));
        gameover_backgroundLabel.setBounds(0,0,1000,600); // 이미지 크기와 위치 설정
        add(gameover_backgroundLabel);

        JButton back_Main01 = new JButton(new ImageIcon("./src/Image/backbutton..png"));
        back_Main01.setBounds(450,450,100,32);
        back_Main01.setBounds(450, 450, 100, 32);
        gameover_backgroundLabel.add(back_Main01);

        back_Main01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); //  화면 닫기
                new MainScreen(); // 이름과 함께 Game 실행
            }
        });

        setLocationRelativeTo(null); // 화면 중앙에 배치
        setVisible(true);
    }

}

