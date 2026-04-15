package Game;

import javax.swing.*;
import java.awt.*;

public class cloud {
    private int x, y, width,height;
    private Image icon;

    public cloud(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height=height;
    }

    public void draw(Graphics g) {
            Color color = new Color(0x00A2E8);
            g.setColor(color);
            ImageIcon icon8 = new ImageIcon("./src/Image/cloud_3.png");
            icon = icon8.getImage();
            g.drawImage(icon,x,y,width,height,null);

    }
}
