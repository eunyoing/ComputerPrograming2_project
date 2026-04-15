package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


class TutorialGamePanel extends JPanel {
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
    private int cameraX = 0; // 카메라의 x좌표
    private String playername;
    private boolean gameEnded = false; // 게임 종료 상태 플래그 추가

    public TutorialGamePanel() {
        this.playername = playername;

        player = new Player(50, WINDOW_HEIGHT - FLOOR_HEIGHT - 70);
        player.setGamePanel1(this);
        obstacles = new ArrayList<>();
        platforms = new ArrayList<>();
        items = new ArrayList<>();
        rotatingObstacles = new ArrayList<>();
        fallingObstacles = new ArrayList<>();
        clouds = new ArrayList<>();
        initMap();

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

            // 탈출구와 충돌 여부 체크
            if (!gameEnded && isPlayerInExitZone()) { // 게임이 종료되지 않았을 때만 체크
                gameEnded = true; // 게임 종료 상태로 설정
                endGame(); // 게임 종료 처리
            }

            fallingObstacles.removeIf(fallingObstacle -> {
                fallingObstacle.update();
                return fallingObstacle.getY() > WINDOW_HEIGHT - FLOOR_HEIGHT * 3 - 50;
            });

            repaint();
        });

        timer.start();
    }

    private void initMap() {
        platforms.add(new Platform(0, WINDOW_HEIGHT - FLOOR_HEIGHT - 20, 1500, FLOOR_HEIGHT));
        platforms.add(new Platform(380, WINDOW_HEIGHT - FLOOR_HEIGHT * 3, 1120, 40));

        obstacles.add(new TriangleObstacle(500,WINDOW_HEIGHT - FLOOR_HEIGHT * 3 - 55,false));
        items.add(new Item(1300, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 , 50));

        rotatingObstacles.add(new RotatingObstacle(1100, WINDOW_HEIGHT - FLOOR_HEIGHT * 4 - 30 , 100));

        platforms.add(new Platform(300, WINDOW_HEIGHT - FLOOR_HEIGHT * 2, 50, 20, 3, 150, 350));

        clouds.add(new cloud(100, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 40, 90, 60));
        clouds.add(new cloud(240, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 66, 44));
        clouds.add(new cloud(350, WINDOW_HEIGHT - FLOOR_HEIGHT * 8, 51, 34));
        clouds.add(new cloud(900, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 80, 99, 66));
        clouds.add(new cloud(1200, WINDOW_HEIGHT - FLOOR_HEIGHT * 8 - 10, 66, 44));
    }

    private void spawnFallingObstacle() {
        int[] xPositions = {700,800}; // 고정된 x좌표들
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

        ImageIcon icon5 = new ImageIcon("./src/Image/finish.png");
        Image icon = icon5.getImage();
        g.drawImage(icon, 1300, WINDOW_HEIGHT - FLOOR_HEIGHT * 7, 80, 80, null);
    }

}

