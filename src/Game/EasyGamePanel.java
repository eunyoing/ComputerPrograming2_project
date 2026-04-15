package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class EasyGamePanel extends JPanel {
    private Player player;
    private ArrayList<TriangleObstacle> obstacles;
    private ArrayList<Platform> platforms;
    private ArrayList<Item> items;
    private ArrayList<RotatingObstacle> rotatingObstacles;
    private ArrayList<FallingObstacle> fallingObstacles;
    private ArrayList<cloud> clouds;
    private final int WINDOW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final int FLOOR_HEIGHT = 100;
    private int fallingObstacleSpawnTimer = 0;
    private long startTime; // 게임 시작 시간
    private String playername;
    private boolean gameEnded = false; // 게임 종료 상태 플래그 추가

    private Image icon; // 배경 이미지

    public EasyGamePanel(String playername, String difficulty) {
        this.playername = playername;
        startTime = System.currentTimeMillis(); // 게임 시작 시간을 기록

        if(difficulty.equals("easy")) {
            player = new Player(50, WINDOW_HEIGHT - FLOOR_HEIGHT - 70);
            player.setGamePanel3(this);
            obstacles = new ArrayList<>();
            platforms = new ArrayList<>();
            items = new ArrayList<>();
            rotatingObstacles = new ArrayList<>();
            fallingObstacles = new ArrayList<>();
            clouds = new ArrayList<>();

            initMap(difficulty);
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

            fallingObstacleSpawnTimer++;
            if (fallingObstacleSpawnTimer > 50) {
                spawnFallingObstacle();
                fallingObstacleSpawnTimer = 0;
            }

            // 장애물과 충돌 여부 체크
            for (TriangleObstacle obstacle : obstacles) {
                if (obstacle.getBounds().intersects(player.getBounds())) {
                    if (!gameEnded) { // 이미 게임이 끝나지 않았으면
                        gameover();  // 충돌 시 게임 오버 처리
                        gameEnded = true; // 게임 종료 상태로 설정
                    }
                    return;  // 충돌 시 더 이상 진행하지 않음
                }
            }

            for (RotatingObstacle rotatingObstacle : rotatingObstacles) {
                if (rotatingObstacle.checkCollision(player.getBounds())) {
                    if (!gameEnded) { // 이미 게임이 끝나지 않았으면
                        gameover();  // 충돌 시 게임 오버 처리
                        gameEnded = true; // 게임 종료 상태로 설정
                    }
                    return;  // 충돌 시 더 이상 진행하지 않음
                }
            }

            for (FallingObstacle fallingObstacle : fallingObstacles) {
                if (fallingObstacle.getBounds().intersects(player.getBounds())) {
                    if (!gameEnded) { // 이미 게임이 끝나지 않았으면
                        gameover();  // 충돌 시 게임 오버 처리
                        gameEnded = true; // 게임 종료 상태로 설정
                    }
                    return;  // 충돌 시 더 이상 진행하지 않음
                }
            }

            // 탈출구와 충돌 여부 체크
            if (!gameEnded && isPlayerInExitZone()) { // 게임이 종료되지 않았을 때만 체크
                gameEnded = true; // 게임 종료 상태로 설정
                endGame(); // 게임 종료 처리
            }

            fallingObstacles.removeIf(fallingObstacle -> {
                fallingObstacle.update();
                return fallingObstacle.getY() > WINDOW_HEIGHT - FLOOR_HEIGHT - 80;
            });

            repaint();
        });

        timer.start();
    }

    private void initMap(String difficulty) {
        if(difficulty.equals("easy")) {
            platforms.add(new Platform(0, WINDOW_HEIGHT - FLOOR_HEIGHT , 1500, FLOOR_HEIGHT));
            platforms.add(new Platform(0, WINDOW_HEIGHT - FLOOR_HEIGHT * 3, 1250, 40));

            obstacles.add(new TriangleObstacle(200, WINDOW_HEIGHT - FLOOR_HEIGHT - 55, false));
            obstacles.add(new TriangleObstacle(400, WINDOW_HEIGHT - FLOOR_HEIGHT - 55, false));
            obstacles.add(new TriangleObstacle(600, WINDOW_HEIGHT - FLOOR_HEIGHT - 55, false));
            items.add(new Item(1300, WINDOW_HEIGHT - FLOOR_HEIGHT * 2, 50));

            rotatingObstacles.add(new RotatingObstacle(1000, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 30, 100));
            rotatingObstacles.add(new RotatingObstacle(750, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 30, 100));
            rotatingObstacles.add(new RotatingObstacle(500, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 30, 100));
            items.add(new Item(50, WINDOW_HEIGHT - FLOOR_HEIGHT * 4, 50));

            platforms.add(new Platform(250, WINDOW_HEIGHT - FLOOR_HEIGHT * 6, 1250, 40));
            obstacles.add(new TriangleObstacle(500, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 55, false));
            obstacles.add(new TriangleObstacle(700, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 55, false));
            obstacles.add(new TriangleObstacle(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 6 - 55, false));

            clouds.add(new cloud(100, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 40, 90, 60));
            clouds.add(new cloud(240, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 66, 44));
            clouds.add(new cloud(350, WINDOW_HEIGHT - FLOOR_HEIGHT * 8, 51, 34));
            clouds.add(new cloud(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 99, 66));
            clouds.add(new cloud(1200, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 10, 66, 44));
        }
    }

    private void spawnFallingObstacle() {
        int[] xPositions = {800, 950, 1100}; // 고정된 x좌표들
        int yPosition = WINDOW_HEIGHT - FLOOR_HEIGHT * 2 - 70;

        // x좌표 배열을 기반으로 낙하 장애물 생성 (1초 간격으로 생성)
        for (int i = 0; i < xPositions.length; i++) {
            final int x = xPositions[i];

            // 1초 간격으로 Timer 실행
            Timer timer = new Timer(i * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {fallingObstacles.add(new FallingObstacle(x, yPosition));}
            });
            timer.setRepeats(false); // 타이머가 한번만 실행되게 설정
            timer.start(); // 타이머 시작
        }
    }

    private boolean isPlayerInExitZone() {
        int exitX = 1300; // 탈출구의 x좌표
        int exitY = WINDOW_HEIGHT - FLOOR_HEIGHT * 7; // 탈출구의 y좌표
        int exitWidth = 80; // 탈출구의 너비
        int exitHeight = 80; // 탈출구의 높이

        // 탈출구와 플레이어의 경계를 비교하여 충돌 여부를 판단
        return player.getX() + player.getWidth() > exitX &&
                player.getX() < exitX + exitWidth &&
                player.getY() + player.getHeight() > exitY &&
                player.getY() < exitY + exitHeight;
    }

    private void endGame() {
        long endTime = System.currentTimeMillis(); // 현재 시간 기록
        long gameTime = endTime - startTime; // 게임이 시작된 이후 경과된 시간 계산

        long totalSeconds = gameTime / 1000; // 총 초
        long minutes = totalSeconds / 60; // 분
        long seconds = totalSeconds % 60; // 남은 초

        String formattedTime = minutes + "분 " + seconds + "초"; // "x분 x초" 형태로 포맷팅
        rank.saveUserData("easy", playername, formattedTime); // 난이도에 따른 데이터 저장

        System.out.println("Game Cleared! Returning to Main Screen...");

        ((Window) this.getTopLevelAncestor()).dispose(); // 현재 게임 창 닫기
        new Gameclear(); // 게임 클리어 화면 띄우기
    }

    public void gameover() {
        // 이미 게임 오버가 되었다면, 다시 호출되지 않도록 처리
        if (gameEnded) return;

        JFrame gameOverFrame = new Gameover();
        gameOverFrame.setVisible(true);

        // 현재 게임 창 닫기
        Window currentWindow = SwingUtilities.getWindowAncestor(this);
        if (currentWindow != null) {
            currentWindow.dispose(); // 현재 창 닫기
        }
        gameEnded = true; // 게임 종료 상태로 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        player.draw(g);

        for (Platform platform : platforms) platform.draw(g);
        for (TriangleObstacle obstacle : obstacles) obstacle.draw(g);
        for (Item item : items) item.draw(g);
        for (RotatingObstacle rotatingObstacle : rotatingObstacles) rotatingObstacle.draw(g);
        for (FallingObstacle fallingObstacle : fallingObstacles) fallingObstacle.draw(g);
        for (cloud clouding : clouds) clouding.draw(g);

        g.setColor(Color.YELLOW);
        ImageIcon icon5 = new ImageIcon("./src/Image/finish.png");
        icon = icon5.getImage();
        g.drawImage(icon, 1300, WINDOW_HEIGHT - FLOOR_HEIGHT * 7, 80, 80, null);
    }
}
