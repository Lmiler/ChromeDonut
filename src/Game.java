import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private ChromeDonut chromeDonut;
    private Image icon;

    public Game() {
        chromeDonut = new ChromeDonut();
        setTitle("Chrome Donut");
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pics/donut.png"));
        setIconImage(icon);
        setVisible(true);
        add(chromeDonut);
    }
}
