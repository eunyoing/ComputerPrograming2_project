package Game;

import javax.swing.*;
        import java.awt.*;



class TriangleObstacle {
    private int x, y, width, height;
    private boolean onFirstFloor;  // 1층 장애물인지 여부
    private Image icon;

    public TriangleObstacle(int x, int y, boolean onFirstFloor) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 56;
        this.onFirstFloor = onFirstFloor;
    }

    public void draw(Graphics g) {
        Color color = new Color(0x00A2E8);
        g.setColor(color);

        ImageIcon icon2 = new ImageIcon("./src/Image/block.png");
        icon = icon2.getImage();
        g.drawImage(icon,x,y,width,height ,null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // 1층 장애물만 왼쪽으로 이동
    public void moveLeft() {
        if (onFirstFloor) {
            this.x -= 70;  // 이동 속도 증가
        }
    }

    public int getY() {
        return y;
    }

    public boolean isOnFirstFloor() {
        return onFirstFloor;
    }
}


