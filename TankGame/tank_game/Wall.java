package tank_game;
import java.awt.Image;

public class Wall extends CollidableObj{
    boolean breakable;
    int health;

    public Wall(Image img, int x, int y, boolean destroyable){
        super(img, x, y);
        this.breakable = destroyable;
        health = 2;
    }

    public boolean isBreakable()
    {
        return breakable;
    }
    
    public void takeDamage(int damage)
    {
        health -= damage;
    }
    
    public boolean isDestroyed()
    {
        if(health <=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
