package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class  MainScreen extends JFrame {
    private JTextField nameField; // 이름 입력 필드

    public MainScreen() {
        setTitle("Jump Jump Game !");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // 배경 이미지 설정
        JLabel backgroundLabel = new JLabel(new ImageIcon("./src/Image/mainscreen_background.png"));
        backgroundLabel.setBounds(0, 0, 1000, 600);
        add(backgroundLabel);

        // 사용자 이름 입력 패널
        JPanel inputPanel = new JPanel(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel(new ImageIcon("./src/Image/signbutton.png"), JLabel.CENTER);
        nameLabel.setBounds(350,460,165,43);
        nameField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.setBounds(350, 460, 330, 43);
        backgroundLabel.add(inputPanel);

        // 게임 시작 버튼 패널
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setLayout(new BorderLayout());
        JButton startButton = new JButton(new ImageIcon("./src/Image/startbutton.png"));
        startButtonPanel.add(startButton, BorderLayout.CENTER);
        startButton.setBounds(430, 400,150,43);
        startButtonPanel.setOpaque(false);
        startButtonPanel.setBounds(430, 400,150,43);
        backgroundLabel.add(startButtonPanel);

        JLabel tutorialtxt = new JLabel("tutorial");
        tutorialtxt.setBounds(293,395,38,28);
        tutorialtxt.setFont(new Font("Arial", Font.BOLD, 11));
        backgroundLabel.add(tutorialtxt);
        // 튜토리얼 버튼 패널
        JPanel tutorialButtonPanel = new JPanel();
        tutorialButtonPanel.setLayout(new BorderLayout());
        JButton tutorialButton = new JButton(new ImageIcon("./src/Image/tutorialbutton.png"));
        tutorialButtonPanel.add(tutorialButton, BorderLayout.CENTER);
        tutorialButton.setBounds(290,420,50,36);
        tutorialButtonPanel.setOpaque(false);
        tutorialButtonPanel.setBounds(290,420,50,36);
        backgroundLabel.add(tutorialButtonPanel);

        JLabel ranktxt = new JLabel("Rank");
        ranktxt.setBounds(715,378,38,28);
        ranktxt.setFont(new Font("Arial", Font.BOLD, 11));
        backgroundLabel.add(ranktxt);

        // 랭크 버튼 패널
        JPanel rankButtonPanel = new JPanel();
        rankButtonPanel.setLayout(new BorderLayout());
        JButton rankButton = new JButton(new ImageIcon("./src/Image/rankbutton.png"));
        rankButtonPanel.add(rankButton, BorderLayout.CENTER);
        rankButton.setBounds(700,400,65,61);
        rankButtonPanel.setOpaque(false);
        rankButtonPanel.setBounds(700,400,65,61);
        backgroundLabel.add(rankButtonPanel);

        // 게임 시작 버튼 클릭 시 이벤트 처리
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    showDifficultySelection(playerName); // 난이도 선택 화면 표시
                } else {
                    JOptionPane.showMessageDialog(MainScreen.this,
                            "User name 을 입력하세요.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 튜토리얼 버튼 클릭 시 이벤트 처리
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 튜토리얼 화면을 여는 코드 (Game1 클래스를 호출)
                dispose(); //  화면 닫기
                new TutorialGame(); // Game1 클래스는 튜토리얼을 담당
            }
        });

        // 랭크 버튼 클릭 시 이벤트 처리
        rankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRankWindow(); // 랭크 창 표시
            }
        });

        setLocationRelativeTo(null); // 화면 중앙에 배치
        setVisible(true);
    }

    // 난이도 선택 다이얼로그 표시
    private void showDifficultySelection(String playerName) {
        JDialog difficultyDialog = new JDialog(this, "Select Difficulty", true);
        difficultyDialog.setLayout(new FlowLayout());
        difficultyDialog.setSize(300, 150);

        // 난이도 선택 라디오 버튼
        JRadioButton easyButton = new JRadioButton("Easy");
        JRadioButton normalButton = new JRadioButton("Normal");
        JRadioButton hardButton = new JRadioButton("Hard");

        // 기본적으로 아무것도 선택되지 않은 상태로 설정
        easyButton.setSelected(false);
        normalButton.setSelected(false);
        hardButton.setSelected(false);

        ButtonGroup group = new ButtonGroup();
        group.add(easyButton);
        group.add(normalButton);
        group.add(hardButton);

        // 확인 버튼
        JButton okButton = new JButton("OK");
        difficultyDialog.add(easyButton);
        difficultyDialog.add(normalButton);
        difficultyDialog.add(hardButton);
        difficultyDialog.add(okButton);

        // 확인 버튼 클릭 시 이벤트 처리
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택하지 않은 경우 처리
                if (!easyButton.isSelected() && !normalButton.isSelected() && !hardButton.isSelected()) {
                    JOptionPane.showMessageDialog(difficultyDialog,
           "난이도를 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // 선택이 없으면 종료
                }

                difficultyDialog.dispose(); // 다이얼로그 닫기
                String selectedDifficulty; // 선택한 난이도 변수

                // 선택된 난이도에 따라 변수 설정
                if (easyButton.isSelected()) {
                    selectedDifficulty = "easy"; // Easy 선택
                    dispose(); //  화면 닫기
                    new EasyGame(selectedDifficulty, playerName);
                } else if (normalButton.isSelected()) {
                    selectedDifficulty = "normal"; // Normal 선택
                    dispose(); //  화면 닫기
                    new NormalGame(selectedDifficulty, playerName); // Normal 난이도로 게임 시작
                } else {
                    selectedDifficulty = "hard"; // Hard 선택
                    dispose(); //  화면 닫기
                    new HardGame(selectedDifficulty, playerName);
                }
            }
        });

        difficultyDialog.setLocationRelativeTo(this); // 다이얼로그 중앙 배치
        difficultyDialog.setVisible(true); // 다이얼로그 표시
    }




    // 랭크 창 표시 메소드
    private void showRankWindow() {
        JDialog rankDialog = new JDialog(this, "View Ranks", true);
        rankDialog.setLayout(new BorderLayout());
        rankDialog.setSize(400, 300);

        // 난이도 선택 패널
        JPanel difficultyPanel = new JPanel();
        JRadioButton easyButton = new JRadioButton("Easy");
        JRadioButton normalButton = new JRadioButton("Normal");
        JRadioButton hardButton = new JRadioButton("Hard");
        hardButton.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(easyButton);
        group.add(normalButton);
        group.add(hardButton);

        difficultyPanel.add(easyButton);
        difficultyPanel.add(normalButton);
        difficultyPanel.add(hardButton);

        // 랭크 표시 패널
        JTextArea rankArea = new JTextArea();
        rankArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(rankArea);

        // 랭크 업데이트 버튼
        JButton updateButton = new JButton("Update Ranks");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDifficulty; // 기본값
                if (easyButton.isSelected()) {
                    selectedDifficulty = "easy"; // Easy 선택
                    loadRanks(selectedDifficulty, rankArea); // 선택한 난이도의 랭크 로드
                } else if (normalButton.isSelected()) {
                    selectedDifficulty = "normal"; // Normal 선택
                    loadRanks(selectedDifficulty, rankArea); // 선택한 난이도의 랭크 로드
                }
                else if (hardButton.isSelected()) {
                    selectedDifficulty = "hard";
                    loadRanks(selectedDifficulty, rankArea); // 선택한 난이도의 랭크 로드
                }
            }
        });

        rankDialog.add(difficultyPanel, BorderLayout.NORTH);
        rankDialog.add(scrollPane, BorderLayout.CENTER);
        rankDialog.add(updateButton, BorderLayout.SOUTH);

        rankDialog.setLocationRelativeTo(this); // 중앙 배치
        rankDialog.setVisible(true); // 랭크 창 표시
    }

    // 선택한 난이도의 랭크 로드 메소드
    private void loadRanks(String difficulty, JTextArea rankArea) {
        rankArea.setText(""); // 기존 텍스트 지우기
        List<rank.Rank> rankings = rank.loadRanking(difficulty); // 랭크 로드

        for (rank.Rank r : rankings) {
            rankArea.append(r.toString() + "\n"); // 랭크를 텍스트 영역에 추가
        }
    }

    public static void main(String[] args) {
        new MainScreen(); // 메인 화면 실행
    }
}
