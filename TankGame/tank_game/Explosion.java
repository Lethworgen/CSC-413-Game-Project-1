package tank_game;

import java.awt.Image;
import java.util.ArrayList;

public class Explosion {
    private int x, y;
    private ArrayList<Image> images;
    private long expTime;
    private int frame;
    
    public Explosion(ArrayList<Image> imgs, int x, int y)
    {
        this.images = imgs;
        this.x = x;
        this.y = y;
        expTime  = 100000000;
        frame = 0;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public Image getImg()
    {
        return images.get(frame);
    }
    
    public void moveUpdate(long time)
    {
        expTime = expTime - time;
        if(expTime < 0)
        {
            expTime  = 50000000;
            frame++;
        }
    }
    
    public boolean hasFrame()
    {
        if(frame + 1 < images.size())
            return true;
        else
            return false;
    }
}
