package Game;
import javax.swing.*;
import java.awt.*;

public class Item {
    private int x, y, diameter;
    private boolean collected = false;
    private Image icon;

    public Item(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }

    public void draw(Graphics g) {

        if (!collected) {
            g.fillOval(x, y, diameter, diameter);

            Color color = new Color(0x00A2E8);
            g.setColor(color);
            ImageIcon icon1 = new ImageIcon("./src/Image/item.png");
            icon = icon1.getImage();
            g.drawImage(icon,x,y,diameter,diameter,null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }
}