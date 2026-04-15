package Game;

import javax.swing.*;

class NormalGame extends JFrame {
    private NormalGamePanel normalGamePanel;

    public NormalGame(String difficulty, String playername) {

        setTitle("Jump Map Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        normalGamePanel = new NormalGamePanel(playername, difficulty); // playername과 난이도 전달
        add(normalGamePanel);
        setVisible(true);
    }
}

class HardGame extends JFrame {
    private HardGamePanel hardGamePanel;

    public HardGame(String difficulty, String playername) {

        setTitle("Jump Map Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hardGamePanel = new HardGamePanel(playername, difficulty); // playername 전달
        add(hardGamePanel);
        setVisible(true);
    }
}

class TutorialGame extends JFrame {
    private TutorialGamePanel tutorialGamePanel; // GamePanel1을 추가해야 합니다.

    public TutorialGame() {
        setTitle("Jump Map Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tutorialGamePanel = new TutorialGamePanel(); // GamePanel1 인스턴스를 생성해야 합니다.
        add(tutorialGamePanel);

        setVisible(true);
    }
}

class EasyGame extends JFrame {
    private EasyGamePanel easyGamePanel;

    public EasyGame(String difficulty, String playername) {

        setTitle("Jump Map Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // 전체 화면
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        easyGamePanel = new EasyGamePanel(playername, difficulty); // playername 전달
        add(easyGamePanel);
        setVisible(true);
    }
}