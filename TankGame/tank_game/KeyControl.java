package tank_game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//Handles key events for keyboard input
public class KeyControl extends KeyAdapter{
    
    private GameEvents events;
    
    public KeyControl() {}
    
    public KeyControl(GameEvents events){
        this.events = events;
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        events.setKey(e);
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        events.setKey(e);
    }
}
