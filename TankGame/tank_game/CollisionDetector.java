package tank_game;

import java.awt.Rectangle;

public class CollisionDetector {
    //For Collision detector, calculate the collision of an object before it occurs;
    //return true if the objects will collide, otherwise return false.
    
    //for using collision detector, should call all collision checks before moving object,
    //then move object if no collisions, or perform action if needed if there are collisions,
    //then update objects speed/angle afterwards if keys are pressed for next time through the game loop.
    
    public CollisionDetector(){}
    
    public boolean TANKvWALL_FOR(Tank player, Wall wall)
    {
        Rectangle prec = new Rectangle(player.getX() + (int)Math.round(player.getSpeed()*Math.cos(Math.toRadians(player.getAngle()))), 
                player.getY() + (int)Math.round(player.getSpeed()*Math.sin(Math.toRadians(player.getAngle()))),
                player.getWidth(), player.getHeight());
        Rectangle wrec = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        
        if(prec.intersects(wrec))
            return true;
        else
            return false;
    }
    
    public boolean TANKvWALL_BACK(Tank player, Wall wall)
    {
        Rectangle prec = new Rectangle(player.getX() - (int)Math.round(player.getSpeed()*Math.cos(Math.toRadians(player.getAngle()))), 
                player.getY() - (int)Math.round(player.getSpeed()*Math.sin(Math.toRadians(player.getAngle()))),
                player.getWidth(), player.getHeight());
        Rectangle wrec = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        
        if(prec.intersects(wrec))
            return true;
        else
            return false;
    }
    
    public boolean TANKvTANK_FOR(Tank player1, Tank player2)
    {
        Rectangle p1rec = new Rectangle(player1.getX() + (int)Math.round(player1.getSpeed()*Math.cos(Math.toRadians(player1.getAngle()))), 
                player1.getY() + (int)Math.round(player1.getSpeed()*Math.sin(Math.toRadians(player1.getAngle()))),
                player1.getWidth(), player1.getHeight());
        Rectangle p2rec = new Rectangle(player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight());
        
        if(p1rec.intersects(p2rec))
            return true;
        else
            return false;
    }
    
    public boolean TANKvTANK_BACK(Tank player1, Tank player2)
    {
        Rectangle p1rec = new Rectangle(player1.getX() - (int)Math.round(player1.getSpeed()*Math.cos(Math.toRadians(player1.getAngle()))), 
                player1.getY() - (int)Math.round(player1.getSpeed()*Math.sin(Math.toRadians(player1.getAngle()))),
                player1.getWidth(), player1.getHeight());
        Rectangle p2rec = new Rectangle(player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight());
        
        if(p1rec.intersects(p2rec))
            return true;
        else
            return false;
    }
    
    public boolean TANKvPOWER(Tank player, PowerUp power)
    {
        Rectangle prec = new Rectangle(player.getX() + (int)Math.round(player.getSpeed()*Math.cos(Math.toRadians(player.getAngle()))), 
                player.getY() + (int)Math.round(player.getSpeed()*Math.sin(Math.toRadians(player.getAngle()))),
                player.getWidth(), player.getHeight());
        Rectangle powrec = new Rectangle(power.getX(), power.getY(), power.getWidth(), power.getHeight());
        
        if(prec.intersects(powrec))
            return true;
        else
            return false;
    }
    
    public boolean BULLETvWALL(Bullet bullet, Wall wall)
    {
        Rectangle brec = new Rectangle(bullet.getX() + (int)Math.round(bullet.getSpeed()*Math.cos(Math.toRadians(bullet.getAngle()))), 
                bullet.getY() + (int)Math.round(bullet.getSpeed()*Math.sin(Math.toRadians(bullet.getAngle()))),
                bullet.getWidth(), bullet.getHeight());
        Rectangle wrec = new Rectangle(wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
        
        if(brec.intersects(wrec))
            return true;
        else
            return false;
    }
    
    public boolean BULLETvPLAYER(Bullet bullet, Tank player)
    {
        Rectangle brec = new Rectangle(bullet.getX() + (int)Math.round(bullet.getSpeed()*Math.cos(Math.toRadians(bullet.getAngle()))), 
                bullet.getY() + (int)Math.round(bullet.getSpeed()*Math.sin(Math.toRadians(bullet.getAngle()))),
                bullet.getWidth(), bullet.getHeight());
        Rectangle prec = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        
        if(brec.intersects(prec))
            return true;
        else
            return false;
    }
}