package tank_game;
import java.awt.Image;

public class Bullet extends CollidableObj{
    private int speed;
    private int bullet_damage;
    private double angle;
    int sizeX, sizeY;
    
    public Bullet(Image img, int x, int y, double angle, int dmg)
    {
        super(img, x, y);
        this.angle = angle;
        this.speed = 8;
        bullet_damage = dmg;
    }
    
    public int getDmg(){
        return this.bullet_damage;
    }
    
    public double getAngle()
    {
        return this.angle;
    }
    
    public void dealDamage(Tank t)
    {
        t.takeDamage(this.bullet_damage);
    }
    
    public int getDamage()
    {
        return bullet_damage;
    }
    
    public int getSpeed()
    {
        return this.speed;
    }
    
    public void moveUpdate(){
        this.x = this.x + (int)Math.round(this.speed*Math.cos(Math.toRadians(this.angle))); 
        this.y = this.y + (int)Math.round(this.speed*Math.sin(Math.toRadians(this.angle)));
    }
    
    
    
}
