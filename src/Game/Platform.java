package Game;

import javax.swing.*;
import java.awt.*;

class  Platform {
    private int x, y, width, height;
    private int speed = 0; // 기본적으로 정적 플랫폼은 속도가 0
    private int leftLimit, rightLimit; // 움직이는 플랫폼의 이동 범위
    private boolean isMoving = false; // 기본값은 정적 플랫폼
    private boolean movingRight = true; // 움직이는 방향
    private Image icon;

    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // 움직이는 플랫폼 생성자
    public Platform(int x, int y, int width, int height, int speed, int leftLimit, int rightLimit) {
        this(x, y, width, height);
        this.speed = speed;
        this.leftLimit = leftLimit;
        this.rightLimit = rightLimit;
        this.isMoving = true;
    }

    // 업데이트 메서드 (움직이는 플랫폼만 동작)
    public void update() {
        if (isMoving) {
            if (movingRight) {
                x += speed;
                if (x + width >= rightLimit) {
                    movingRight = false;
                }
            } else {
                x -= speed;
                if (x <= leftLimit) {
                    movingRight = true;
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawRect(x,y,width,height);
        String path= null;
        if(width == 2450){
            //1층
            path = "./src/Image/platform/under_1.png";
        }
        else if(width == 300){
            //1층 2층 사이
            path = "./src/Image/platform/under_1_2.png";
        }
        else if(width == 2600){
            //2층
            path = "./src/Image/platform/under_2.png";
        }
        else if(width == 300){
            //3층
            path = "./src/Image/platform/under_3.png";
        }
        else if(width == 800){
            //4층
            path = "./src/Image/platform/under_4.png";
        }
        else if(width ==50||width ==40){
            path = "./src/Image/platform/block (2).jpg";
        }
        else if(width ==100){
            path = "./src/Image/platform/block (3).png";
        }
        else if (width == 1500) {
            path = "./src/Image/platform/tutorial_under_1.png";
        }
        else if(width == 1120) {
            path = "./src/Image/platform/tutorial_under_2.png";
        }
        else if(width == 4300) {
            path = "./src/Image/platform/under_5.png";
        }
        else if(width == 3000) {
            path = "./src/Image/platform/under_6.png";
        }
        else if(width == 1250) {
            path = "./src/Image/platform/under_7.png";
        }
        ImageIcon icon6 = new ImageIcon(path);
        icon = icon6.getImage();
        g.drawImage(icon,x,y,width,height,null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
