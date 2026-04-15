package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gameclear extends JFrame{
    public Gameclear() {
        setTitle("Jump Jump Game !");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel gameclear_backgroundLabel = new JLabel(new ImageIcon("./src/Image/gameclear_background.png"));
        gameclear_backgroundLabel.setBounds(0, 0, 1000, 600); // 이미지 크기와 위치 설정
        add(gameclear_backgroundLabel);


        JButton back_Main02 = new JButton(new ImageIcon("./src/Image/backbutton..png"));
        back_Main02.setBounds(450, 450, 100, 32);
        gameclear_backgroundLabel.add(back_Main02);

        back_Main02.addActionListener(new ActionListener() {
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


