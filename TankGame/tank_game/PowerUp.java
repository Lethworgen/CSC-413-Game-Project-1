package tank_game;

import java.awt.Image;

public class PowerUp extends CollidableObj{
    int type;
    boolean visible;
    long time;
    
    public PowerUp(Image img, int type, int x, int y){
        super(img, x, y);
        this.type = type;
        visible = true;
    }
    
    public int getType()
    {
        time = 30000000000L;
        visible = false;
        return type;
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public void update(long l)
    {
        time = time  - l;
        if(time < 0)
        {
            visible = true;
        }
    }
}
