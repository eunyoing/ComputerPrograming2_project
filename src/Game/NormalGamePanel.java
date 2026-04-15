package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class NormalGamePanel extends JPanel {
    private Player player;
    private ArrayList<TriangleObstacle> obstacles;
    private ArrayList<Platform> platforms;
    private ArrayList<Item> items;
    private ArrayList<RotatingObstacle> rotatingObstacles;
    private ArrayList<FallingObstacle> fallingObstacles;
    private ArrayList<cloud> clouds;

    private final int WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final int FLOOR_HEIGHT = 100;
    private int obstacleSpawnTimer = 0;
    private int fallingObstacleSpawnTimer = 0;
    private int cameraX = 0; // 카메라의 x좌표
    private long startTime; // 게임 시작 시간
    private String playername;
    private boolean gameEnded = false; // 게임 종료 상태 플래그 추가

    public NormalGamePanel(String playername, String difficulty) {
        this.playername = playername;
        startTime = System.currentTimeMillis(); // 게임 시작 시간을 기록

        // 난이도에 따라 게임 초기화
        if (difficulty.equals("normal")) {
            player = new Player(50, WINDOW_HEIGHT - FLOOR_HEIGHT - 70);
            player.setGamePanel(this);
            obstacles = new ArrayList<>();
            platforms = new ArrayList<>();
            items = new ArrayList<>();
            rotatingObstacles = new ArrayList<>();
            fallingObstacles = new ArrayList<>();
            clouds = new ArrayList<>();

            // Hard 난이도에 맞는 맵 설정
            initMap(difficulty);
        } else {
            // Normal 또는 Easy 난이도를 선택한 경우, 게임 초기화 하지 않음
            JOptionPane.showMessageDialog(this, difficulty + " 난이도 맵은 아직 구현되지 않았습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // 생성자에서 더 이상 진행하지 않음
        }

        Color color = new Color(0x00A2E8);
        setBackground(color);

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        Timer timer = new Timer(20, e -> {
            player.update(platforms, obstacles, items, rotatingObstacles, fallingObstacles);

            for (RotatingObstacle rotatingObstacle : rotatingObstacles) {
                rotatingObstacle.rotate();
            }

            for (TriangleObstacle obstacle : obstacles) {
                if (obstacle.isOnFirstFloor()) {
                    obstacle.moveLeft();
                }
            }

            for (Platform platform : platforms) {
                platform.update();
            }

            obstacleSpawnTimer++;
            if (obstacleSpawnTimer > 35) {
                spawnObstacle();
                obstacleSpawnTimer = 0;
            }

            fallingObstacleSpawnTimer++;
            if (fallingObstacleSpawnTimer > 50) {
                spawnFallingObstacle();
                fallingObstacleSpawnTimer = 0;
            }

            // 탈출구와 충돌 여부 체크
            if (!gameEnded && isPlayerInExitZone()) {
                gameEnded = true; // 게임 종료 상태로 설정
                endGame(); // 게임 종료 처리
            }

            fallingObstacles.removeIf(fallingObstacle -> {
                fallingObstacle.update();
                return fallingObstacle.getY() > WINDOW_HEIGHT - FLOOR_HEIGHT * 3 - 50;
            });

            // 카메라 이동 로직
            if (player.getX() > getWidth() / 2) {
                cameraX = player.getX() - getWidth() / 2;
            }

            repaint(); // 화면 갱신
        });

        timer.start();
    }

    // 난이도에 따라 맵 초기화
    private void initMap(String difficulty) {
        if (difficulty.equals("normal")) {
            // Hard 난이도에 맞는 맵 설정
            platforms.add(new Platform(0, WINDOW_HEIGHT - FLOOR_HEIGHT - 20, 2450, FLOOR_HEIGHT)); // 바닥 1층 플랫폼
            platforms.add(new Platform(1300, WINDOW_HEIGHT - FLOOR_HEIGHT * 2, 300, 20)); // 1층 2층 사이 바닥
            platforms.add(new Platform(0, WINDOW_HEIGHT - FLOOR_HEIGHT * 3, 2600, 40)); // 2층

            obstacles.add(new TriangleObstacle(2500, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2550, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2600, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2650, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2700, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2750, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2850, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false));
            obstacles.add(new TriangleObstacle(2800, WINDOW_HEIGHT - FLOOR_HEIGHT - 35, false)); // 1층 바닥 장애물

            obstacles.add(new TriangleObstacle(200, WINDOW_HEIGHT - FLOOR_HEIGHT * 3 - 56, false)); // y: 2층 바닥 y좌표 - 장애물 height
            obstacles.add(new TriangleObstacle(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 3 - 56, false)); // 2층 바닥 장애물

            platforms.add(new Platform(200, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 800, 20)); // 4층
            obstacles.add(new TriangleObstacle(400, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 56, false));
            obstacles.add(new TriangleObstacle(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 56, false));
            obstacles.add(new TriangleObstacle(700, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 56, false)); // y: 4층 바닥 y좌표 - 장애물 height

            items.add(new Item(300, WINDOW_HEIGHT - FLOOR_HEIGHT * 5 - 20, 50)); // 아이템 위치

            platforms.add(new Platform(400, WINDOW_HEIGHT - FLOOR_HEIGHT * 4, 300, 20)); // 3층
            obstacles.add(new TriangleObstacle(500, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 56, false)); // y: 3층 바닥 y좌표 - 장애물 height

            rotatingObstacles.add(new RotatingObstacle(100, WINDOW_HEIGHT - FLOOR_HEIGHT * 5, 100)); // 3층 4층 사이 돌아가는 불
            rotatingObstacles.add(new RotatingObstacle(1400, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 110));
            rotatingObstacles.add(new RotatingObstacle(1800, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 110));
            rotatingObstacles.add(new RotatingObstacle(2200, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 110)); // 4층 오른쪽 돌아가는 불

            platforms.add(new Platform(2400, WINDOW_HEIGHT - FLOOR_HEIGHT - 20 - 56, 50, 20, 3, 2370, 2600));
            platforms.add(new Platform(2650, WINDOW_HEIGHT - FLOOR_HEIGHT * 2, 50, 20, 3, 2650, 2900)); // 1층 2층 사이 움직이는 블럭
            platforms.add(new Platform(1000, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 100, 30, 3, 1000, 2700)); // 4층 움직이는 블럭

            clouds.add(new cloud(100, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 40, 90, 60));
            clouds.add(new cloud(240, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 66, 44));
            clouds.add(new cloud(350, WINDOW_HEIGHT - FLOOR_HEIGHT * 8, 51, 34));
            clouds.add(new cloud(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 99, 66));
            clouds.add(new cloud(1400, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 10, 66, 44));
            clouds.add(new cloud(1600, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 60, 90, 60));
            clouds.add(new cloud(2100, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 10, 60, 40));
            clouds.add(new cloud(2300, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 66, 44));
            clouds.add(new cloud(2600, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 60, 51, 34));
            clouds.add(new cloud(2800, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 90, 90, 60));
        }
        // Easy 및 Normal 난이도에 대한 설정은 비워둠
    }

    private void spawnFallingObstacle() {
        int[] xPositions = {1350, 1100, 2100, 1650, 1500, 1950, 1800, 2250}; // 고정된 x좌표들
        int yPosition = WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 70;

        // x좌표 배열을 기반으로 낙하 장애물 생성 (1초 간격으로 생성)
        for (int i = 0; i < xPositions.length; i++) {
            final int x = xPositions[i];

            // 1초 간격으로 Timer 실행
            Timer timer = new Timer(i * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fallingObstacles.add(new FallingObstacle(x, yPosition));
                }
            });
            timer.setRepeats(false); // 타이머가 한번만 실행되게 설정
            timer.start(); // 타이머 시작
        }
    }

    private void spawnObstacle() { // 움직이는 장애물
        int xPosition = 3000;
        int yPosition = WINDOW_HEIGHT - FLOOR_HEIGHT - 20 - 56; // Y : 1층 Y축 - 장애물 HEIGHT

        obstacles.add(new TriangleObstacle(xPosition, yPosition, true));
    }

    private boolean isPlayerInExitZone() {
        int exitX = 2500; // 탈출구의 x좌표
        int exitY = WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 80; // 탈출구의 y좌표
        int exitWidth = 80; // 탈출구의 너비
        int exitHeight = 80; // 탈출구의 높이

        // 탈출구와 플레이어의 경계를 비교하여 충돌 여부를 판단
        return player.getX() + player.getWidth() > exitX &&
                player.getX() < exitX + exitWidth &&
                player.getY() + player.getHeight() > exitY &&
                player.getY() < exitY + exitHeight;
    }

    private void endGame() {
        // 게임 종료 시 시간 기록
        long endTime = System.currentTimeMillis(); // 현재 시간 기록
        long gameTime = endTime - startTime; // 게임이 시작된 이후 경과된 시간 계산

        // 게임 시간을 초 단위로 변환
        long totalSeconds = gameTime / 1000; // 총 초
        long minutes = totalSeconds / 60; // 분
        long seconds = totalSeconds % 60; // 남은 초

        // 사용자 데이터 저장 (난이도에 따라)
        String formattedTime = minutes + "분 " + seconds + "초"; // "x분 x초" 형태로 포맷팅
        rank.saveUserData("normal", playername, formattedTime); // 하드 난이도 데이터 저장

        // 메인 스크린으로 돌아가기
        System.out.println("Game Cleared! Returning to Main Screen...");

        // 현재 게임 창 닫기
        ((Window) this.getTopLevelAncestor()).dispose(); // 현재 게임 창 닫기
        new Gameclear();
    }

    public void gameover() {
        // Gameover 창 표시
        JFrame gameOverFrame = new Gameover();
        gameOverFrame.setVisible(true);

        // 현재 게임 창 닫기
        Window currentWindow = SwingUtilities.getWindowAncestor(this);
        if (currentWindow != null) {
            currentWindow.dispose(); // GamePanel1이 포함된 창 닫기
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 카메라 위치를 고려하여 그리기
        g.translate(-cameraX, 0);
        player.draw(g);

        for (Platform platform : platforms) platform.draw(g);
        for (TriangleObstacle obstacle : obstacles) obstacle.draw(g);
        for (Item item : items) item.draw(g);
        for (RotatingObstacle rotatingObstacle : rotatingObstacles) rotatingObstacle.draw(g);
        for (FallingObstacle fallingObstacle : fallingObstacles) fallingObstacle.draw(g);
        for (cloud clouding : clouds) clouding.draw(g);

        // 탈출구 그리기
        g.setColor(Color.YELLOW);
        ImageIcon icon5 = new ImageIcon("./src/Image/finish.png");
        Image icon = icon5.getImage();
        g.drawImage(icon, 2500, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 80, 80, 80, null);
    }
}


