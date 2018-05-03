package tank_game;


import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Observer;
import java.util.Observable;

public class Tank extends CollidableObj implements Observer{
    protected int health = 5, damage;
    private int up, down, left, right, shoot; //keys
    private double angle;
    private int speed;
    boolean turn_left, turn_right, move_up, move_down, fire;
    private long bulletTime;
    private long powerUpTime;
    private int powerUpType;
    
    Tank(Image img, int x, int y, int angle, int up, int down, int left, int right, int shoot){
        super(img, x, y);

        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        this.speed = 4;
        this.angle = angle;
        turn_left = false;
        turn_right = false;
        move_up = false;
        move_down = false;
        fire = false;
        this.powerUpType = 0;
        this.powerUpTime = 0;
        this.bulletTime = 0;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public double getAngle()
    {
        return this.angle;
    }
    
    public int getSpeed()
    {
        return this.speed;
    }
    
    public int getHealth()
    {
        return this.health;
    }
    
    public int getTankCenterX(){
        return x+(height/2);
    }
    
    public int getTankCenterY(){
        return y+(width/2);
    }
    
    public int getTankCamX(int screenW, int boundsX)
    {
        int camX;
        if(this.getTankCenterX() < screenW/4)
            camX = 0;
        else if(this.getTankCenterX() > boundsX - screenW/4)
            camX = 960;//camX = boundsX - screenW/4; HARDCODED INSTEAD
        else 
            camX = this.getTankCenterX() - screenW/4;
        return camX;
    }
    
    public int getTankCamY(int screenH, int boundsY)
    {
        int camY;
        if(this.getTankCenterY() < screenH/2)
            camY = 0;
        else if(this.getTankCenterY() > boundsY - screenH/2)
            camY = 360;//camY = boundsY - screenH/2;
        else 
            camY = this.getTankCenterY() - screenH/2;
        return camY;
    }
    
    public void setHealth(int hp){
        health = hp;
    }
    
    public void takeDamage(int dmg){
        health -= dmg;
    }
    
    public boolean isDestroyed(){
        if(health <=0){
            return true;
        }else{
            return false;
        }
    }
    public void gainPower(int i)
    {
        if(i == 1)//heatlth
            this.health = 5;
        else if (i == 2)//speed up
        {
            this.speed = 6;
            this.powerUpType = i;
            this.powerUpTime = 20000000000L;
        }
        else if(i == 3)
        {
            if(this.powerUpType == 2)
                this.speed = 4;
            this.powerUpType = i;
            this.powerUpTime = 20000000000L;
        }
        
    }

    public boolean isReadyToFire()
    {
        if(fire && (this.bulletTime <= 0))
            return true;
        else
            return false;
    }
    
    public int getBulType()
    {
        if(powerUpType == 3)
            return 2;
        else 
            return 1;
    }
    
    /*
    * @param player; differentiates what tank did the shooting.
    */
    public Bullet shoot(Image img){
        int bulX = this.getTankCenterX() + (int)Math.round((this.width/2)*Math.cos(Math.toRadians(this.angle)));
        int bulY =  this.getTankCenterY() - 8 + (int)Math.round((this.height/2)*Math.sin(Math.toRadians(this.angle)));       
        Bullet bul;
        if(powerUpType == 3)
            bul = new Bullet(img, bulX, bulY, this.angle, 2);
        else
            bul = new Bullet(img, bulX, bulY, this.angle, 1);
        this.bulletTime = 500000000;
        return bul;
    }
    
    //Moving the tank
    public void updateMove(long time, boolean colf, boolean colb){
        //bullet time spacing management
        this.bulletTime = this.bulletTime - time;
        //powerUp time management
        this.powerUpTime = this.powerUpTime - time;
        if(this.powerUpType != 0 && this.powerUpTime < 0)
        {
            if(this.powerUpType == 2)
                this.speed = 4;
            this.powerUpType = 0;
        }
        
        if(turn_left == true){
            angle -= 1.5;
        }
        
        if(turn_right == true){
            angle += 1.5;
        }
        
        if(!colf && move_up == true){
            x += (int)Math.round(speed * Math.cos(Math.toRadians(angle)));
            y += (int)Math.round(speed * Math.sin(Math.toRadians(angle)));
        }
        
        if(!colb && move_down == true){
            x -= (int)Math.round(speed * Math.cos(Math.toRadians(angle)));
            y -= (int)Math.round(speed * Math.sin(Math.toRadians(angle)));
        }
    }
    
    
    @Override
    public void update(Observable obj, Object arg){
        GameEvents ge = (GameEvents) arg;
            KeyEvent e = (KeyEvent) ge.event;
            //Left
            if(e.getKeyCode() == left){
                if(e.getID() == KeyEvent.KEY_RELEASED){
                    turn_left = false;
                } else if (e.getID() == KeyEvent.KEY_PRESSED){
                    turn_left = true;
                }
            }
            
            //Right
            if(e.getKeyCode() == right){
                if(e.getID() == KeyEvent.KEY_RELEASED){
                    turn_right = false;
                }else if (e.getID() == KeyEvent.KEY_PRESSED){
                    turn_right = true;
                }
            }
            
            //Up
            if(e.getKeyCode() == up){
                if(e.getID() == KeyEvent.KEY_RELEASED){
                    move_up = false;
                }else if (e.getID() == KeyEvent.KEY_PRESSED){
                    move_up = true;
                }
            }
            
            //Down
            if(e.getKeyCode() == down){
                if(e.getID() == KeyEvent.KEY_RELEASED){
                    move_down = false;
                }else if (e.getID() == KeyEvent.KEY_PRESSED){
                    move_down = true;
                }
            }
            
            //SHOOT TODO
            if(e.getKeyCode() == shoot){
                if(e.getID() == KeyEvent.KEY_RELEASED){
                    this.fire = false;
                
                }else if (e.getID() == KeyEvent.KEY_PRESSED){
                    this.fire = true;
                }
            }
        }
    }