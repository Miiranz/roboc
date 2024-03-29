import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by User on 24.11.2014.
 */
public class Bullet {
    int x;
    int y;
    boolean right;
    Timer timer = new Timer(20, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (Engine.isGameover() || Engine.isWin()) {
                timer.stop();
            } //если выйграл или проиграл то стоп
            if (right) {
                if (Engine.getIsRight()) {
                    x += 20 - Engine.getDX();
                } else x += 20 - Engine.getDX();
            } else {
                if (Engine.getIsRight()) {
                    x -= 20 + Engine.getDX();
                } else x -= 20 - Engine.getDX();
            }
        }
    });

    public Bullet(int x, int y, boolean right) {
        this.x = x;
        this.y = y;
        this.right = right;
        timer.start();
    }

    public int GetX() {
        return x;
    }

    public int GetY() {
        return y;
    }

}
