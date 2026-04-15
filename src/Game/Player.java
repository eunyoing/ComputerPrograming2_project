package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    private int x, y;
    private int width = 50, height = 50;
    private int velocityY = 0;
    private boolean jumping = false;
    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private final int FLOOR_HEIGHT = 100;
    private boolean canDoubleJump = false;
    private boolean canTripleJump = false;
    private int jumpCount = 0;
    private long doubleJumpTime = 0;
    private static final int DOUBLE_JUMP_DURATION = 4000;
    private boolean spacePressed = false;
    private boolean isFacingRight = true; // 초기 방향: 오른쪽
    private Image icon;
    private NormalGamePanel normalGamePanel;
    private TutorialGamePanel tutorialGamePanel;
    private HardGamePanel hardGamePanel;
    private EasyGamePanel easyGamePanel;
    // 게임 종료 플래그
    private boolean gameEnded = false;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void setGamePanel(NormalGamePanel gamePanel) {
        this.normalGamePanel = gamePanel;
    }
    public void setGamePanel1(TutorialGamePanel gamePanel) {
        this.tutorialGamePanel = gamePanel;
    }
    public void setGamePanel2(HardGamePanel gamePanel) {
        this.hardGamePanel = gamePanel;
    }
    public void setGamePanel3(EasyGamePanel gamepanel) {this.easyGamePanel = gamepanel;}

    // 키 입력 처리
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !spacePressed) {
            spacePressed = true;
            if (!jumping) {
                jumping = true;
                velocityY = JUMP_STRENGTH;
                jumpCount = 1;
            } else if (canDoubleJump && jumpCount == 1) {
                velocityY = JUMP_STRENGTH;
                jumpCount = 2;
            } else if (canTripleJump && jumpCount == 2) {
                velocityY = JUMP_STRENGTH;
                jumpCount = 3;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
            isFacingRight = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
            isFacingRight = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) spacePressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
    }

    // 업데이트 메소드
    public void update(ArrayList<Platform> platforms, ArrayList<TriangleObstacle> obstacles,
                       ArrayList<Item> items, ArrayList<RotatingObstacle> rotatingObstacles,
                       ArrayList<FallingObstacle> fallingObstacles) {
        int nextX = x;
        if (rightPressed) nextX += 5;
        if (leftPressed) nextX -= 5;

        // 수평 충돌 체크
        boolean horizontalCollision = false;
        for (Platform platform : platforms) {
            if (nextX + width > platform.getX() && nextX < platform.getX() + platform.getWidth() &&
                    y + height > platform.getY() && y < platform.getY() + platform.getHeight()) {
                horizontalCollision = true;
                break;
            }
        }
        if (!horizontalCollision) x = nextX;

        // 중력 적용
        y += velocityY;
        velocityY += GRAVITY;

        boolean onPlatform = false;
        for (Platform platform : platforms) {
            Rectangle platformBounds = new Rectangle(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            Rectangle playerBounds = getBounds();

            if (playerBounds.intersects(platformBounds) && y + height <= platform.getY() + velocityY) {
                y = platform.getY() - height;
                velocityY = 0;
                jumping = false;
                jumpCount = 0;
                onPlatform = true;
                break;
            }

            // 아래에서 플랫폼에 충돌하면 미끄러짐 처리
            if (playerBounds.intersects(platformBounds) && y < platform.getY() + platform.getHeight() && velocityY < 0) {
                y = platform.getY() + platform.getHeight();
                velocityY = 0;
            }
        }

        // 바닥에 충돌 처리
        if (!onPlatform && y >= Toolkit.getDefaultToolkit().getScreenSize().height - FLOOR_HEIGHT - height) {
            y = Toolkit.getDefaultToolkit().getScreenSize().height - FLOOR_HEIGHT - height;
            velocityY = 0;
            jumping = false;
            jumpCount = 0;
        }

        if (canDoubleJump && System.currentTimeMillis() - doubleJumpTime > DOUBLE_JUMP_DURATION) {
            canDoubleJump = false;
        }

        // 아이템 충돌 처리
        for (Item item : items) {
            if (!item.isCollected() && getBounds().intersects(item.getBounds())) {
                item.collect();
                canDoubleJump = true; // 아이템을 획득하면 이중 점프 가능
                canTripleJump = true; // 삼중 점프 가능
                doubleJumpTime = System.currentTimeMillis();
            }
        }

        // 장애물과의 충돌 감지
        for (TriangleObstacle obstacle : obstacles) {
            if (obstacle.getBounds().intersects(getBounds())) {
                if (!gameEnded) {
                    gameEnded = true;
                    System.out.println("Game Over!");
                    if (normalGamePanel != null) {
                        normalGamePanel.gameover();
                    }
                    if (tutorialGamePanel != null) {
                        tutorialGamePanel.gameover();
                    }
                    if (hardGamePanel != null) {
                        hardGamePanel.gameover();
                    }
                    if(easyGamePanel != null){
                        easyGamePanel.gameover();
                    }
                }
                return;
            }
        }

        // 회전하는 장애물과의 충돌 감지
        for (RotatingObstacle rotatingObstacle : rotatingObstacles) {
            if (rotatingObstacle.checkCollision(getBounds())) {
                if (!gameEnded) {
                    gameEnded = true;
                    System.out.println("Game Over!");
                    if (normalGamePanel != null) {
                        normalGamePanel.gameover();
                    }
                    if (tutorialGamePanel != null) {
                        tutorialGamePanel.gameover();
                    }
                    if (hardGamePanel != null) {
                        hardGamePanel.gameover();
                    }
                }
                return;
            }
        }

        // 낙하하는 장애물과의 충돌 감지
        for (FallingObstacle fallingObstacle : fallingObstacles) {
            if (fallingObstacle.getBounds().intersects(getBounds())) {
                if (!gameEnded) {
                    gameEnded = true;
                    System.out.println("Game Over!");
                    if (normalGamePanel != null) {
                        normalGamePanel.gameover();
                    }
                    if (tutorialGamePanel != null) {
                        tutorialGamePanel.gameover();
                    }
                    if (hardGamePanel != null) {
                        hardGamePanel.gameover();
                    } 
                }
                return;
            }
        }
    }

    // 플레이어의 범위를 반환하는 메소드
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // 너비를 반환하는 메서드 추가
    public int getWidth() {
        return width;
    }

    // 높이를 반환하는 메서드 추가
    public int getHeight() {
        return height;
    }

    // 그리기 메소드
    public void draw(Graphics g) {
        Color color = new Color(0x00A2E8);
        g.setColor(color);

        ImageIcon icon4;
        if (isFacingRight) {
            icon4 = new ImageIcon("./src/Image/player_Right.png");
        } else {
            icon4 = new ImageIcon("./src/Image/player_Left.png");
        }
        this.icon = icon4.getImage();

        g.drawImage(icon, x, y, width, height, null);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}
