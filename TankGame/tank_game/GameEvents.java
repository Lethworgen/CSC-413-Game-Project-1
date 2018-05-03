package tank_game;
import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author LethW
 */
public class GameEvents extends Observable{
    public Object event;
    
    public void setKey(KeyEvent e){
        event = e;
        setChanged();
        notifyObservers(this);
    }
    public Object getEvent() {
        return this.event;
    }
    
}