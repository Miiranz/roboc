import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Engine extends JComponent {
    /**
     * Текущая координата
     */
    private int x = 0;
    /**
     * Скорость движения (положительная - вправо, отрицательная - влево)
     */
    private int HeroX, HeroY, EnemyY = 0;
    private int screenWidth = 0;
    private static int dx = 0;
    private int sitmodelcount = 0;
    private int y = 0;
    private int floor = 0;
    private double dy = 0;
    private boolean jumping = false;
    private boolean sitdown = false;
    private boolean situp = false;
    private static boolean isright = true;
    private boolean canfire = true;
    private boolean falling = false;
    private int reload = -1;
    private BufferedImage background;
    private Image modelStop, modelWalk, modelJump;
    private Image modelSittingDown1, modelSittingDown2, modelSittingDown3;
    private Image image;
    private Image protivnik;
    private Image PatronHero;
    private boolean objFound = false;
    private ArrayList<Bullet> Bullets = new ArrayList<Bullet>();
    private ArrayList<EnvObj> objcts = new ArrayList<EnvObj>();
    private ArrayList<Enemy> prtvnk = new ArrayList<Enemy>();
    private Random random = new Random();
    private int countUntilEnemy = 150 + random.nextInt(400 - 150);
    private int WaitUntil = 0;

    public Engine() throws IOException {
// Загружаем изображение из файла:
        background = ImageIO.read(getClass().getResource("game.png"));
        //   model = getToolkit().getImage(getClass().getResource("model3.gif"));
        modelStop = getToolkit().getImage(getClass().getResource("herostop.png"));
        modelWalk = getToolkit().getImage(getClass().getResource("model3.gif"));
        modelJump = getToolkit().getImage(getClass().getResource("herojump.png"));
        modelSittingDown1 = getToolkit().getImage(getClass().getResource("sittingdown1.png"));
        modelSittingDown2 = getToolkit().getImage(getClass().getResource("sittingdown2.png"));
        modelSittingDown3 = getToolkit().getImage(getClass().getResource("sittingdown3.png"));
        modelSittingDown1.getWidth(this);
        modelSittingDown2.getWidth(this);
        modelSittingDown3.getWidth(this);
        PatronHero = getToolkit().getImage(getClass().getResource("bullet.png"));
        protivnik = getToolkit().getImage(getClass().getResource("enemy.gif"));
        image = modelStop;
        EnvObj yashik1 = new EnvObj(2510, 0, 110, 70, "yashik");
        EnvObj yashik2 = new EnvObj(2610, 0, 180, 140, "yashik");
        EnvObj yashik3 = new EnvObj(3875, 0, 275, 70, "yashik");
        EnvObj yashik4 = new EnvObj(5070, 0, 200, 150, "yashik");
        EnvObj yashik5 = new EnvObj(5270, 0, 80, 70, "yashik");
        EnvObj yashik6 = new EnvObj(6430, 0, 200, 70, "yashik");
        EnvObj yashik7 = new EnvObj(8965, 0, 110, 70, "yashik");
        EnvObj yashik8 = new EnvObj(9070, 0, 185, 150, "yashik");
        objcts.add(yashik1);
        objcts.add(yashik2);
        objcts.add(yashik3);
        objcts.add(yashik4);
        objcts.add(yashik5);
        objcts.add(yashik6);
        objcts.add(yashik7);
        objcts.add(yashik8);
        EnvObj doroga1 = new EnvObj(1555, 380, 1090, 1, "doroga");
        EnvObj doroga2 = new EnvObj(4865, 453, 255, 1, "doroga");
        EnvObj doroga3 = new EnvObj(5225, 453, 1175, 1, "doroga");
        EnvObj doroga4 = new EnvObj(6495, 453, 575, 1, "doroga");
        EnvObj doroga5 = new EnvObj(7210, 453, 670, 1, "doroga");
        EnvObj doroga6 = new EnvObj(9295, 380, 825, 1, "doroga");

        objcts.add(doroga1);
        objcts.add(doroga2);
        objcts.add(doroga3);
        objcts.add(doroga4);
        objcts.add(doroga5);
        objcts.add(doroga6);

// Устанавливаем начальный размер компонента (высота - по высоте изображения)
        setPreferredSize(new Dimension(1100, background.getHeight()));
// Для того, чтобы обрабатывать нажатия клавиш, компонент должен иметь фокус ввода:
        setFocusable(true);
// Нажатия кнопок влево/вправо меняют скорость движения
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    dx = -5;
                    isright = false;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    dx = 5;
                    isright = true;
                } else if (e.getKeyCode() == KeyEvent.VK_UP && !jumping && !falling) {
                    dy = 18.0;
                    jumping = true;
                } else if (e.getKeyCode() == KeyEvent.VK_CONTROL && canfire) {
                    canfire = false;
                    Music.getMusic().PlayHeroShoot();
                    if (isright) {
                        if (sitdown) {
                            Bullet a = new Bullet(HeroX + 145, HeroY + 135, isright);
                            Bullets.add(a);
                        } else {
                            Bullet a = new Bullet(HeroX + 145, HeroY + 63, isright);
                            Bullets.add(a);
                        }
                    } else {
                        if (sitdown) {
                            Bullet a = new Bullet(HeroX - 152, HeroY + 135, isright);
                            Bullets.add(a);
                        } else {
                            Bullet a = new Bullet(HeroX - 152, HeroY + 63, isright);
                            Bullets.add(a);
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && !sitdown && !jumping && !jumping) {
                    sitdown = true;
                    situp = false;
                    sitmodelcount = 0;
                }
            }

            public void keyReleased(KeyEvent e) {
                //modelStop = getToolkit().getImage(getClass().getResource("herostop.png"));
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    dx = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && sitdown) {
                    sitdown = false;
                    situp = true;
                    sitmodelcount = 4;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL && !canfire) {
                    reload = 0;
                }
            }
        });
// Таймер будет срабатывать каждые 20 миллисекунд (50 раз в секунду)
        Timer timer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// Изменяем текущие координаты
                if (WaitUntil == countUntilEnemy) {
                    Enemy enemy = new Enemy(x+screenWidth+20, EnemyY);
                    prtvnk.add(enemy);
                    WaitUntil = 0;
                    Random random = new Random();
                    countUntilEnemy = 150 + random.nextInt(400 - 150);
                }
                WaitUntil++;
                if (reload >= 6) {
                    canfire = true;
                    reload = -1;
                }
                if (reload >= 0 && reload <= 7) {
                    reload++;
                }
                if ((dx < 0 && x < 5) || sitdown == true || (dx > 0 && x >= 10555)) {
                    dx = 0;
                } else {
                    objFound = false;
                    for (EnvObj d : objcts) {
                        if (d.tip.equals("yashik")) {
                            if (HeroX + dx >= d.x && HeroX + dx < d.x + d.width && y < d.y + d.height) {
                                dx = 0;
                                objFound = true;
                            }
                            if (HeroX + dx >= d.x && HeroX + dx < d.x + d.width && y >= d.y + d.height) {
                                objFound = true;
                                floor = d.height;
                            }
                        } else {
                            if (HeroX + dx >= d.x && HeroX + dx < d.x + d.width && y >= d.y + d.height) {
                                objFound = true;
                                floor = d.y + d.height;
                            }
                        }
                    }
                    x += dx;
                    if (!(objFound) && !(falling)) {
                        floor = 0;
                        if (y > floor && !jumping) {
                            falling = true;
                            dy = -0.5;
                        }
                    } else if (floor < y && !jumping && !falling) {
                        falling = true;
                        dy = -0.5;
                    }
                }
                if (sitdown) {
                    sitmodelcount++;
                    if (sitmodelcount > 3) {
                        sitmodelcount = 4;
                    }
                }
                if (situp) {
                    sitmodelcount--;
                    if (sitmodelcount < 0) {
                        sitmodelcount = -1;
                    }
                }
                if (jumping && !falling) {
                    dy -= 0.5;
                    y += dy;
                    if (y + dy <= floor) {
                        y = floor;
                        Music.getMusic().PlayFalling();
                        jumping = false;
                    }
                }
                if (falling) {
                    dy -= 0.5;
                    y += dy;
                    if (y + dy <= floor) {
                        y = floor;
                        Music.getMusic().PlayFalling();
                        falling = false;
                    }
                }

// Перерисовываем картинку
                repaint();
            }
        });
// Запускаем таймер:
        timer.start();
    }

    /**
     * Рисование фона
     *
     * @param g графический контекст
     * @param x текущие координаты
     */
    private void drawBackground(Graphics g, int x) {
// Определяем ширину экрана:
        screenWidth = getWidth();
// Определяем ширину изображения:
        int imageWidth = background.getWidth();
// x1 - координата, в которой нужно нарисовать самое левое изображение:
        int x1;
        if (x >= 0) {
            x1 = -x % imageWidth;
        } else {
            x1 = -x % imageWidth - imageWidth;
        }
// Продолжаем рисовать, пока не замостим весь экран:
   //     while (x1 < screenWidth) {
            g.drawImage(background, x1, 0, this);
         //   x1 += imageWidth;
      //  }
        if (sitdown && !jumping) {
            switch (sitmodelcount) {
                case 1:
                    image = modelSittingDown1;
                    break;
                case 2:
                    image = modelSittingDown2;
                    break;
                case 3:
                    image = modelSittingDown3;
                    break;
            }
        } else if (situp) {
            switch (sitmodelcount) {
                case 0:
                    image = modelStop;
                    break;
                case 1:
                    image = modelSittingDown1;
                    break;
                case 2:
                    image = modelSittingDown2;
                    break;
                case 3:
                    image = modelSittingDown3;
                    situp = false;
                    break;
            }

        } else if (jumping || falling) {
            image = modelJump;
        } else {
            image = dx != 0 ? modelWalk : modelStop;
        }
        HeroX = x + 50;
        HeroY = getHeight() - 265 - y;
        EnemyY = getHeight() - 220 - floor;
        if (image == modelSittingDown3) {
            g.drawImage(image, HeroX-x+(isright ? 0 : 140), HeroY + 71, isright ? 124 : -124, 146, this);
        } else {
            g.drawImage(image, HeroX-x+(isright ? 0 : 140), HeroY, isright ? 140 : -140, 225, this);
        }
        g.setColor(Color.white);
        for (Iterator<Bullet> i = Bullets.iterator(); i.hasNext(); ) {
            Bullet s = i.next();
            if (s.GetX()-x > screenWidth + 15) {

                //Bullets.remove(s);
                i.remove();
            }
        }
        for (Iterator<Enemy> i = prtvnk.iterator(); i.hasNext(); ) {
            Enemy s = i.next();
            g.drawString("Enemy x=" + String.valueOf(s.GetX()), 50, 150);
            if (s.GetX()-x +100< 0 || s.GetHP()<=0) {
                //Bullets.remove(s);
                i.remove();
            }
        }

        for (Enemy s : prtvnk) {
            g.drawImage(protivnik, s.GetX()-x, s.GetY(), 91, 178, this);
            for (Iterator<Bullet> i = Bullets.iterator(); i.hasNext(); ) {
                Bullet b = i.next();
                g.drawString("Bullet x" + String.valueOf(b.GetX()), 50, 125);
                g.drawString("Enemy x=" + String.valueOf(s.GetX()), 50, 150);
                if (b.GetX() > s.GetX() && b.GetX()<s.GetX()+91) {
                    //Bullets.remove(s);
                    s.decHP();
                    i.remove();

                }
            }

        }
        for (Bullet s : Bullets) {
            g.drawImage(PatronHero, s.GetX()-x, s.GetY(), 22, 21, this);


        }

        g.draw3DRect(screenWidth - 300, 20, 230, 45, true);
        g.setColor(Color.red);
        g.fillRect(screenWidth - 300, 20, 230, 45);
        g.setColor(Color.yellow);
        g.drawString("100%", screenWidth - 200, 45);
        g.drawString("X=" + String.valueOf(x), 50, 50);
        g.drawString("Y=" + String.valueOf(y), 50, 75);
        g.drawString("Hero X=" + String.valueOf(HeroX),50,100);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g, x);
    }

    public static void start() throws IOException {
// Создаем главное окно приложения с заголовком
        JFrame frame = new JFrame("RoboCop 3");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
// Создаем компонент...
        Engine scene = new Engine();
// ...и добавляем его в окно
        frame.add(scene, BorderLayout.CENTER);
// Авто-определение размера окна
        frame.pack();
        frame.setResizable(false);
// Перемещение окна в центр экрана
        frame.setLocationRelativeTo(null);
// Показываем окно на экране
        frame.setVisible(true);
    }

    public static int getDX() {
        return dx;
    }

    public static boolean getIsRight() {
        return isright;
    }

}


