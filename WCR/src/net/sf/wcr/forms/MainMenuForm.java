package net.sf.wcr.forms;

import java.io.IOException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import net.sf.wcr.WCR;
import net.sf.wcr.core.GameMode;

/**
 * This is the form with the main application menu. In truth, this is not a
 * Form, this is a List, that contains all the elements.
 * 
 * @author Andrea Burattin
 */
public class MainMenuForm extends List implements CommandListener
{
    private WCR wcr;
    private String img_path;
    private Command exit;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public MainMenuForm(WCR wcr)
    {
        super("Welcome to WCR", Choice.IMPLICIT);
        
        this.wcr = wcr;
        this.exit = new Command("Exit", Command.EXIT, 0);
        this.img_path = "/net/sf/wcr/media/files/";

        try
        {
            append("New Game", Image.createImage(img_path + "icon/newgame.png"));
            append("Join Game", Image.createImage(img_path + "icon/joingame.png"));
            append("Single Player", Image.createImage(img_path + "icon/singleplayer.png"));
            append("Credits", Image.createImage(img_path + "icon/credits.png"));
        }
        catch(IOException e)
        {}
        
        addCommand(exit);
        setCommandListener(this);
    }
    
    /**
     * The CommandListener required method
     * 
     * @param c
     * @param s
     */
    public void commandAction(Command c, Displayable s)
    {
        if (c == exit)
        {
            wcr.destroyApp(false);
            wcr.notifyDestroyed();
        }
        else
        {
            /* create game */
            if (getSelectedIndex() == 0)
            {
                wcr.gameMode(GameMode.CREATE_GAME);
                wcr.selectGameColor();
            }
            /* search game */
            else if (getSelectedIndex() == 1)
            {
                wcr.gameMode(GameMode.JOIN_GAME);
                wcr.findDevices();
            }
            /* single player */
            else if (getSelectedIndex() == 2)
            {
                wcr.gameMode(GameMode.SINGLE_PLAYER);
                wcr.selectGameColor();
            }
            /* credits */
            else if (getSelectedIndex() == 3)
            {
                wcr.creditsForm();
            }
        }
    }
}
