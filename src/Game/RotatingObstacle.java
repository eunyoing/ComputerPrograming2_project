package Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class RotatingObstacle {
    private int centerX, centerY; // 회전 중심
    private int radius; // 원의 반지름
    private int numCircles = 4; // 원의 개수
    private double angle = 0; // 회전 각도
    private ArrayList<Point> circlePositions; // 각 원의 위치들
    private int moveSpeedX = 2; // X축 이동 속도
    private int moveSpeedY = 1; // Y축 이동 속도

    private Image icon;

    public RotatingObstacle(int x, int y, int radius) {
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
        this.circlePositions = new ArrayList<>();
        updatePositions(); // 원들의 위치 계산
    }

    // 원들의 위치 업데이트
    private void updatePositions() {
        circlePositions.clear();
        double angleStep = 2 * Math.PI / numCircles; // 각 원 사이의 각도 차
        for (int i = 0; i < numCircles; i++) {
            double currentAngle = angle + i * angleStep;
            int x = (int) (centerX + Math.cos(currentAngle) * radius);
            int y = (int) (centerY + Math.sin(currentAngle) * radius);
            circlePositions.add(new Point(x, y));
        }
    }

    // 회전하는 메소드
    public void rotate() {
        angle += Math.PI / 180;  // 회전 각도 (1도씩 회전)
        updatePositions();
    }

    // 회전 구조물의 위치 이동
    public void move() {
        centerX += moveSpeedX; // X축으로 이동
        centerY += moveSpeedY; // Y축으로 이동

        // 이동한 후 화면을 벗어나지 않도록 제한 (화면 크기에 따라 조정 가능)
        if (centerX < 0 || centerX > Toolkit.getDefaultToolkit().getScreenSize().width) {
            moveSpeedX = -moveSpeedX; // X축 이동 방향 반전
        }
        if (centerY < 0 || centerY > Toolkit.getDefaultToolkit().getScreenSize().height) {
            moveSpeedY = -moveSpeedY; // Y축 이동 방향 반전
        }

        updatePositions(); // 원들의 위치 재계산
    }

    // 원 그리기
    public void draw(Graphics g) {
        ImageIcon icon3= new ImageIcon("./src/Image/fire.png");
        icon = icon3.getImage();
        for (Point p : circlePositions) {
            // 이미지를 각 원의 중심에 맞춰 그리기
            g.drawImage(icon, p.x - 10, p.y - 10, 25, 25, null);
        }
    }

    // 원들과 충돌 검사 (플레이어와의 충돌 체크)
    public boolean checkCollision(Rectangle playerBounds) {
        for (Point p : circlePositions) {
            // 각 원의 위치에 대해서 플레이어와 충돌이 나는지 체크
            if (playerBounds.intersects(new Rectangle(p.x - 10, p.y - 10, 25, 25 ))) {
                return true; // 충돌이 발생하면 true 반환
            }
        }
        return false; // 충돌이 없으면 false 반환
    }
}
