import java.awt.*;

public class Mob {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image icon;

    public Mob(int x, int y, int width, int height, Image icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.icon = icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
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

    public Image getIcon() {
        return icon;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
