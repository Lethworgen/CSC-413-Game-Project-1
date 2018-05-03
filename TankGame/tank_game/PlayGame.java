package tank_game;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

public class PlayGame extends JPanel {

    private ArrayList<Bullet> p1bullets;
    private ArrayList<Bullet> p2bullets;
    private ArrayList<Wall> walls;
    private ArrayList<PowerUp> powerups;
    private ArrayList<Explosion> explosions;
    private Tank p1;
    private Tank p2;
    private int p1Lives = 3;
    private int p2Lives = 3;
    //aiming for this fps; can be tweeked, but changes the speed of EVERYTHING
    private final int fps = 60;
    boolean running = true;
    private Image p1_img, p2_img, lives_img, bullet_img, background_img, strong_wall_img,
            weak_wall_img, health_img, speed_img, missile_img, rocket_pickup_img;
    private ArrayList<Image> small_explo_img, big_explo_img;
    //boundaries for game; 5x5 of background img
    private final static int lowerBounds = 1200, upperBounds = 0, leftBounds = 0, rightBounds = 1600;
    private final int screenHeight = 840, screenWidth = 1280;
    JFrame window;
    Graphics2D g2d;
    BufferedImage buf;
    GameEvents p1event, p2event;
    Media gameMusic;

    private void init() {
        //initialize arrays
        p1bullets = new ArrayList<Bullet>();
        p2bullets = new ArrayList<Bullet>();
        walls = new ArrayList<Wall>();
        powerups = new ArrayList<PowerUp>();
        explosions = new ArrayList<Explosion>();
        
        small_explo_img = new ArrayList<Image>();
        big_explo_img = new ArrayList<Image>();

        //initialize images
        try {
            p1_img = ImageIO.read(new File("resources/Tank1.png"));
            p2_img = ImageIO.read(new File("resources/Tank2.png"));
            lives_img = ImageIO.read(new File("resources/icon.png"));
            bullet_img = ImageIO.read(new File("resources/Shell.png"));
            background_img = ImageIO.read(new File("resources/Background.png"));
            strong_wall_img = ImageIO.read(new File("resources/Wall1.png"));
            weak_wall_img = ImageIO.read(new File("resources/Wall2.png"));
            health_img = ImageIO.read(new File("resources/healthpack_Small.png"));
            speed_img = ImageIO.read(new File("resources/speedup.png"));
            missile_img = ImageIO.read(new File("resources/missile.png"));
            rocket_pickup_img = ImageIO.read(new File("resources/rocket_pickup.png"));
            
            Image exploTemp;
            exploTemp = ImageIO.read(new File("resources/small_explosion_frame_0.png"));
            small_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/small_explosion_frame_1.png"));
            small_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/small_explosion_frame_2.png"));
            small_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/small_explosion_frame_3.png"));
            small_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/small_explosion_frame_4.png"));
            small_explo_img.add(exploTemp);
            
            exploTemp = ImageIO.read(new File("resources/BigEx_0.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_1.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_2.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_3.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_4.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_5.png"));
            big_explo_img.add(exploTemp);
            exploTemp = ImageIO.read(new File("resources/BigEx_6.png"));
            big_explo_img.add(exploTemp);
            
            //initialize music
            final JFXPanel fxPanel = new JFXPanel();
            gameMusic = new Media(new File("resources/Music.mp3").toURI().toString());

        } catch (Exception e) {
            System.out.print(e.getStackTrace() + " Error loading resources");
        }

        //initialize tanks
        p1 = new Tank(p1_img, 140, 140, 0, KeyEvent.VK_W, KeyEvent.VK_S,
                KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        p2 = new Tank(p2_img, rightBounds - 180, lowerBounds - 180, 180, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        p1event = new GameEvents();
        p2event = new GameEvents();
        p1event.addObserver(p1);
        p1event.addObserver(p2);
        KeyControl p1keys = new KeyControl(p1event);
        KeyControl p2keys = new KeyControl(p2event);


        //initialize walls        
        wallsInit();
        //initiliaze poerups
        powerups.add(new PowerUp(health_img, 1, rightBounds/2, lowerBounds - 564));
        powerups.add(new PowerUp(speed_img, 2, 96, lowerBounds - 132));
        powerups.add(new PowerUp(speed_img, 2, rightBounds - 192, 192));
        powerups.add(new PowerUp(rocket_pickup_img, 3, 500, 500));
        powerups.add(new PowerUp(rocket_pickup_img, 3, rightBounds - 520, lowerBounds -430));
        
        
        //initialize the window
        window = new JFrame();
        window.addWindowListener(new WindowAdapter() {
        });
        window.add(this);
        window.pack();
        window.setVisible(true);
        window.setTitle("Tank Wars!");
        window.setSize(screenWidth, screenHeight);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //enable keylistening
        window.getContentPane().setFocusable(true);
        window.getContentPane().requestFocusInWindow();
        window.getContentPane().addKeyListener(p1keys);
        window.getContentPane().addKeyListener(p2keys);
    }

    public void wallsInit()
    {
        for(int i =0; i < 5; i++)
        {
            walls.add(new Wall(strong_wall_img, 240 + 48 * i, 288, false));
        }
        for(int i =0; i < 3; i++)
        {
            walls.add(new Wall(strong_wall_img, 240, 336 + 48 * i, false));
        }
        
        for(int i =0; i < 5; i++)
        {
            walls.add(new Wall(strong_wall_img, rightBounds - 288 -48*i, lowerBounds - 276, false));
        }
        for(int i =0; i < 3; i++)
        {
            walls.add(new Wall(strong_wall_img, rightBounds-288, lowerBounds - 324 - 48 * i, false));
        }
        
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(strong_wall_img, rightBounds-288, lowerBounds - 564 - 48 * i, false));
        }
        
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds-516 + 48 * i, lowerBounds - 660, true));
        }
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds-516 + 48 * i, lowerBounds - 708, true));
        }
        
        for(int i =0; i < 5; i++)
        {
            walls.add(new Wall(strong_wall_img, rightBounds/2 - 96 + 48 * i, 288, false));
        }
        
        for(int i =0; i < 5; i++)
        {
            walls.add(new Wall(strong_wall_img, rightBounds/2 -96 + 48 * i, lowerBounds - 276, false));
        }
        
        for(int i =0; i < 3; i++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds/2 -48 + 48 * i, lowerBounds - 612, true));
        }
        for(int i =0; i < 3; i++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds/2 -48 + 48 * i, lowerBounds - 516, true));
        }
        
        walls.add(new Wall(weak_wall_img, rightBounds/2 -48, lowerBounds - 564, true));
        walls.add(new Wall(weak_wall_img, rightBounds/2 +48, lowerBounds - 564, true));
        
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(weak_wall_img, 480 + 48 * i, lowerBounds - 468, true));
        }
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(weak_wall_img, 480 + 48 * i, lowerBounds - 420, true));
        }
        
        for(int i =0; i < 2; i++)
        {
            walls.add(new Wall(strong_wall_img, 240, 624 + 48 * i, false));
        }
        
        for(int i = 0; i < 5; i ++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds - 48 - 48*i, 288, true));
        }
        
        for(int i = 0; i < 7; i ++)
        {
            walls.add(new Wall(weak_wall_img, rightBounds - 288, 48 * i, true));
        }
        
        for(int i = 0; i < 5; i ++)
        {
            walls.add(new Wall(weak_wall_img, 240 - 48 - 48*i, lowerBounds - 276, true));
        }
        for(int i = 0; i < 5; i ++)
        {
            walls.add(new Wall(weak_wall_img, 240, lowerBounds - 84 - 48 * i, true));
        }
        
    }
    
    //seperate thread for music
    private void musicThreadLoop() {
        Thread loop = new Thread() {
            @Override
            public void run()
            {
                MediaPlayer mp = new MediaPlayer(gameMusic);
                mp.play();
            }
        };
        loop.start();
    }
    
    //used for timing
    private void timerLoop() {
        //previous time previous loop started
        long prevTime = System.nanoTime();
        //target time hoping to aim for each loop
        long targetTime = 1000000000 / fps;

        long lastFpsTime = 0;

        while (running) {
            long currTime = System.nanoTime();
            long timeDiff = currTime - prevTime;
            prevTime = currTime;
            //delta is the fraction of the time between the last frame window, 
            //and our target window. it is used to acurately move items the correct
            //distance, accounting for lost time
            double delta = timeDiff / targetTime;//need to cast as double??

            lastFpsTime += timeDiff;
            
            moveFrame(timeDiff);
            repaint();

            //sleep for any remaining time
            //if statement for potential negative time, then don't sleep
            if ((prevTime - System.nanoTime() + targetTime) > 0) {
                try {
                    Thread.sleep((prevTime - System.nanoTime() + targetTime) / 1000000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    //for inital painting/repainting
    @Override
    public void paintComponent(Graphics g) {
        if (buf == null) {
            buf = (BufferedImage) createImage(rightBounds, lowerBounds);
        }
        Graphics2D gtemp = (Graphics2D) g;
        g2d = buf.createGraphics();
        super.paintComponent(gtemp);
        //background tiles
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                g2d.drawImage(background_img, i * 320, j * 240, this);//swap i/j?
            }
        }
        //draw walls
        for (int i = 0; i < walls.size(); i++) {
            g2d.drawImage(walls.get(i).getImg(), walls.get(i).getX(), walls.get(i).getY(), this);
        }

        for (int i = 0; i < powerups.size(); i++) {
            if(powerups.get(i).isVisible())
                g2d.drawImage(powerups.get(i).getImg(), powerups.get(i).getX(), powerups.get(i).getY(), this);
        }
        
        //draw tanks
        paintRotatedImg(p1.getImg(), p1.getAngle(), p1.getX(), p1.getY());
        paintRotatedImg(p2.getImg(), p2.getAngle(), p2.getX(), p2.getY());

        //draw bullets
        for (int i = 0; i < p1bullets.size(); i++) {
            paintRotatedImg(p1bullets.get(i).getImg(), p1bullets.get(i).getAngle(), p1bullets.get(i).getX(), p1bullets.get(i).getY());
        }
        for (int i = 0; i < p2bullets.size(); i++) {
            paintRotatedImg(p2bullets.get(i).getImg(), p2bullets.get(i).getAngle(), p2bullets.get(i).getX(), p2bullets.get(i).getY());
        }
        //also explosions
        for (int i = 0; i < explosions.size(); i++) {
            g2d.drawImage(explosions.get(i).getImg(), explosions.get(i).getX(), explosions.get(i).getY(), this);
        }

        //draw splitscreen
        BufferedImage p1screen = buf.getSubimage(p1.getTankCamX(screenWidth, rightBounds), p1.getTankCamY(screenHeight, lowerBounds), screenWidth * 1 / 2, screenHeight);
        BufferedImage p2screen = buf.getSubimage(p2.getTankCamX(screenWidth, rightBounds), p2.getTankCamY(screenHeight, lowerBounds), screenWidth * 1 / 2, screenHeight);

        gtemp.drawImage(p1screen, 0, 0, this);
        gtemp.drawImage(p2screen, screenWidth / 2 + 2, 0, this);

        //draw minimap
        BufferedImage minimap = buf.getSubimage(0, 0, rightBounds, lowerBounds);
        gtemp.scale(.2, .2);

        gtemp.drawRect(2400, 0, minimap.getWidth() + 4, minimap.getHeight() + 2);//x y width height
        gtemp.drawImage(minimap, 2404, 0, null);//find x,y for minimap

        gtemp.setColor(Color.LIGHT_GRAY);
        gtemp.fillRect(165, 165, 1200, 125);
        gtemp.fillRect(4960, 165, 1200, 125);
        //healthbars
        if (p1.getHealth() > 3) {
            gtemp.setColor(Color.GREEN);
            gtemp.fillRect(175, 175, (1180 / 5) * p1.getHealth(), 105);
        } else if (p1.getHealth() > 1) {
            gtemp.setColor(Color.YELLOW);
            gtemp.fillRect(175, 175, (1180 / 5) * p1.getHealth(), 105);
        } else {
            gtemp.setColor(Color.RED);
            gtemp.fillRect(175, 175, (1180 / 5) * p1.getHealth(), 105);
        }

        if (p2.getHealth() > 3) {
            gtemp.setColor(Color.GREEN);
            gtemp.fillRect(4970, 175, (1180 / 5) * p2.getHealth(), 105);
        } else if (p2.getHealth() > 1) {
            gtemp.setColor(Color.YELLOW);
            gtemp.fillRect(4970, 175, (1180 / 5) * p2.getHealth(), 105);
        } else {
            gtemp.setColor(Color.RED);
            gtemp.fillRect(4970, 175, (1180 / 5) * p2.getHealth(), 105);
        }

        gtemp.scale(5, 5);

        //draw lives
        if (p1Lives == 3) {
            gtemp.drawImage(lives_img, 300, 30, null);
            gtemp.drawImage(lives_img, 340, 30, null);
            gtemp.drawImage(lives_img, 380, 30, null);
        } else if (p1Lives == 2) {
            gtemp.drawImage(lives_img, 300, 30, null);
            gtemp.drawImage(lives_img, 340, 30, null);
        } else if (p1Lives == 1) {
            gtemp.drawImage(lives_img, 300, 30, null);
        }

        if (p2Lives == 3) {
            gtemp.drawImage(lives_img, 940, 30, null);
            gtemp.drawImage(lives_img, 900, 30, null);
            gtemp.drawImage(lives_img, 860, 30, null);
        } else if (p2Lives == 2) {
            gtemp.drawImage(lives_img, 940, 30, null);
            gtemp.drawImage(lives_img, 900, 30, null);
        } else if (p2Lives == 1) {
            gtemp.drawImage(lives_img, 940, 30, null);
        }
        
        if(!running)
        {
            String s;
            if(p1Lives == 0){
                    s = "Player 2 wins!";
            }
            else{
                    s = "Player 1 wins!";
            }
            gtemp.setColor(Color.RED);
            gtemp.setFont((new Font("Arial Black", Font.BOLD, 36)));
            gtemp.drawString(s, 480, 400);
        }

        gtemp.dispose();
    }

    public BufferedImage bufferedImageConverter(Image img) {
        BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bimg;
    }

    public void paintRotatedImg(Image img, double angle, int x, int y) {
        double rAngle = Math.toRadians(angle);
        int h = (int) (img.getWidth(null) * Math.abs(Math.sin(rAngle)) + img.getHeight(null) * Math.abs(Math.cos(rAngle)));
        int w = (int) (img.getHeight(null) * Math.abs(Math.sin(rAngle)) + img.getWidth(null) * Math.abs(Math.cos(rAngle)));

        BufferedImage bimgTemp = bufferedImageConverter(img);

        AffineTransform old = g2d.getTransform();

        AffineTransform at = AffineTransform.getRotateInstance(rAngle, x + img.getWidth(this) / 2, y + img.getHeight(this) / 2);
        g2d.setTransform(at);
        g2d.drawImage(bimgTemp, x, y, null);
        g2d.setTransform(old);
    }

    //loop for collision detection/movement/creation
    private void moveFrame(long time) {
        //update explosions in progress
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).moveUpdate(time);
            if (!explosions.get(i).hasFrame()) {
                explosions.remove(i);
                i--;
            }
        }

        //updte powerup timers
        for(int i =0; i < powerups.size(); i ++)
        {
            powerups.get(i).update(time);
        }
        
        //movement collision check for tank 1
        CollisionDetector col = new CollisionDetector();

        boolean collision = false, colf = false, colb = false;
        for (int i = 0; i < walls.size(); i++) {
            collision = col.TANKvWALL_FOR(p1, walls.get(i));
            if (collision) {
                colf = true;
            }
            collision = col.TANKvWALL_BACK(p1, walls.get(i));
            if (collision) {
                colb = true;
            }
        }
        collision = col.TANKvTANK_FOR(p1, p2);
        if (collision) {
            colf = true;
        }
        collision = col.TANKvTANK_BACK(p1, p2);
        if (collision) {
            colb = true;
        }
        for (int i = 0; i < powerups.size(); i++) {
            collision = col.TANKvPOWER(p1, powerups.get(i));
            if (collision) {
                if(powerups.get(i).isVisible())
                    p1.gainPower(powerups.get(i).getType());
                break;
            }
        }
        p1.updateMove(time, colf, colb);
        if (p1.getX() < leftBounds) {
            p1.setX(leftBounds);
        }
        if (p1.getX() + p1.getWidth() > rightBounds - 6) {
            p1.setX(rightBounds - p1.getWidth() - 6);
        }
        if (p1.getY() < upperBounds) {
            p1.setY(upperBounds);
        }
        if (p1.getY() + p1.getHeight() > lowerBounds - 36) {
            p1.setY(lowerBounds - p1.getHeight() - 36);
        }

        if (p1.isReadyToFire()) {
            int type = p1.getBulType();
            if(type == 1)
                p1bullets.add(p1.shoot(bullet_img));
            else
                p1bullets.add(p1.shoot(missile_img));
        }

        //Collision detection for tank 2
        collision = false;
        colf = false;
        colb = false;
        for (int i = 0; i < walls.size(); i++) {
            collision = col.TANKvWALL_FOR(p2, walls.get(i));
            if (collision) {
                colf = true;
            }
            collision = col.TANKvWALL_BACK(p2, walls.get(i));
            if (collision) {
                colb = true;
            }
        }
        collision = col.TANKvTANK_FOR(p1, p2);
        if (collision) {
            colf = true;
        }
        collision = col.TANKvTANK_BACK(p1, p2);
        if (collision) {
            colb = true;
        }
        for (int i = 0; i < powerups.size(); i++) {
            collision = col.TANKvPOWER(p2, powerups.get(i));
            if (collision) {
                if(powerups.get(i).isVisible())
                    p2.gainPower(powerups.get(i).getType());
                break;
            }
        }
        p2.updateMove(time, colf, colb);
        if (p2.getX() < leftBounds) {
            p2.setX(leftBounds);
        }
        if (p2.getX() + p2.getWidth() > rightBounds - 6) {
            p2.setX(rightBounds - p2.getWidth() - 6);
        }
        if (p2.getY() < upperBounds) {
            p2.setY(upperBounds);
        }
        if (p2.getY() + p2.getHeight() > lowerBounds - 36) {
            p2.setY(lowerBounds - p2.getHeight() - 36);
        }
        if (p2.isReadyToFire()) {
            int type = p2.getBulType();
            if(type == 1)
                p2bullets.add(p2.shoot(bullet_img));
            else
                p2bullets.add(p2.shoot(missile_img));
        }

        //collision for bullets with p1
        for (int i = 0; i < p2bullets.size(); i++) {
            collision = false;
            collision = col.BULLETvPLAYER(p2bullets.get(i), p1);
            if (collision) {
                p2bullets.get(i).dealDamage(p1);
                explosions.add(new Explosion(small_explo_img, p2bullets.get(i).getX(), p2bullets.get(i).getY()));
                if (p1.isDestroyed()) {
                    explosions.add(new Explosion(big_explo_img, p1.getX(), p1.getY()));
                    p1 = new Tank(p1_img, 140, 140, 0, KeyEvent.VK_W, KeyEvent.VK_S,
                            KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
                    p1event.addObserver(p1);
                    p1Lives--;
                    if (p1Lives == 0) {
                        running = false;
                    }
                }
                p2bullets.remove(i);
                i--;
            }
        }

        //collision for bullets with p2
        for (int i = 0; i < p1bullets.size(); i++) {
            collision = false;
            collision = col.BULLETvPLAYER(p1bullets.get(i), p2);
            if (collision) {
                p1bullets.get(i).dealDamage(p2);
                explosions.add(new Explosion(small_explo_img, p1bullets.get(i).getX(), p1bullets.get(i).getY()));
                if (p2.isDestroyed()) {
                    explosions.add(new Explosion(big_explo_img, p2.getX(), p2.getY()));
                    p2 = new Tank(p2_img, rightBounds - 180, lowerBounds - 180, 180, KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                        KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
                    p1event.addObserver(p2);
                    p2Lives--;
                    if (p2Lives == 0) {
                        running = false;
                    }
                }
                p1bullets.remove(i);
                i--;
            }
        }

        //collision for bullets with walls
        for (int i = 0; i < p1bullets.size(); i++) {
            collision = false;
            for (int j = 0; j < walls.size(); j++) {
                collision = col.BULLETvWALL(p1bullets.get(i), walls.get(j));
                if (collision) {
                    if (walls.get(j).isBreakable()) {
                        walls.get(j).takeDamage(p1bullets.get(i).getDamage());
                        if (walls.get(j).isDestroyed()) {
                            walls.remove(j);
                        }
                    }
                    explosions.add(new Explosion(small_explo_img, p1bullets.get(i).getX(), p1bullets.get(i).getY()));
                    p1bullets.remove(i);
                    i--;
                    break;
                }
            }
            if (!collision) {
                p1bullets.get(i).moveUpdate();
                if (p1bullets.get(i).getX() + p1bullets.get(i).getWidth() < leftBounds) {
                    //create explosion
                    p1bullets.remove(i);
                    i--;
                } else if (p1bullets.get(i).getX() > rightBounds) {
                    //create explosion
                    p1bullets.remove(i);
                    i--;
                } else if (p1bullets.get(i).getY() + p1bullets.get(i).getHeight() < upperBounds) {
                    //create explosion
                    p1bullets.remove(i);
                    i--;
                } else if (p1bullets.get(i).getY() > lowerBounds) {
                    //create explosion
                    p1bullets.remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < p2bullets.size(); i++) {
            collision = false;
            for (int j = 0; j < walls.size(); j++) {
                collision = col.BULLETvWALL(p2bullets.get(i), walls.get(j));
                if (collision) {
                    if (walls.get(j).isBreakable()) {
                        walls.get(j).takeDamage(p2bullets.get(i).getDamage());
                        if (walls.get(j).isDestroyed()) {
                            walls.remove(j);
                        }
                    }
                    explosions.add(new Explosion(small_explo_img, p2bullets.get(i).getX(), p2bullets.get(i).getY()));
                    p2bullets.remove(i);
                    i--;
                    break;
                }
            }
            if (!collision) {
                p2bullets.get(i).moveUpdate();
                if (p2bullets.get(i).getX() + p2bullets.get(i).getWidth() < leftBounds) {
                    //create explosion
                    p2bullets.remove(i);
                    i--;
                } else if (p2bullets.get(i).getX() > rightBounds) {
                    //create explosion
                    p2bullets.remove(i);
                    i--;
                } else if (p2bullets.get(i).getY() + p2bullets.get(i).getHeight() < upperBounds) {
                    //create explosion
                    p2bullets.remove(i);
                    i--;
                } else if (p2bullets.get(i).getY() > lowerBounds) {
                    //create explosion
                    p2bullets.remove(i);
                    i--;
                }
            }
        }
    }

    public static void main(String[] args) {
        PlayGame game = new PlayGame();
        game.init();
        game.musicThreadLoop();
        game.timerLoop();
    }
}
