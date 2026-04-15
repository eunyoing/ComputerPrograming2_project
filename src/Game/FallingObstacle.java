package Game;

import javax.swing.*;
import java.awt.*;

public class FallingObstacle {
    private int x, y;  // 장애물의 위치
    private final int width = 50, height = 50;  // 장애물의 크기
    private final int speed = 7;  // 낙하 속도
    private Image icon;

    public FallingObstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;  // 장애물이 아래로 낙하
    }

    public void draw(Graphics g) {

        ImageIcon icon7 = new ImageIcon("./src/Image/drop.png");
        icon = icon7.getImage();
        g.drawImage(icon,x,y,width,height,null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);  // 충돌 감지를 위한 범위 반환
    }
    public int getY() {
        return y;
    }

}


