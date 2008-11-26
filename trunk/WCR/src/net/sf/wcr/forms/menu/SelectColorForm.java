package net.sf.wcr.forms.menu;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ServerThread;
import net.sf.wcr.core.GameMode;
import net.sf.wcr.media.Color;

/**
 * This is the form to choose the color to shoot.
 * 
 * @author Andrea Burattin
 */
public class SelectColorForm extends List implements CommandListener
{
    private WCR wcr;
    private Command back;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public SelectColorForm(WCR wcr)
    {
        super("Select the color to shoot", Choice.IMPLICIT);
        
        this.wcr = wcr;
        this.back = new Command("Back", Command.BACK, 0);
        
        append("Black", null);
        append("Red", null);
        append("Green", null);
        append("Blue", null);
        append("Yellow", null);
        append("White", null);
        
        addCommand(back);
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
        try
        {
            if (c == back)
            {
                wcr.mainMenu();
            }
            else
            {
                if (getSelectedIndex() >= 0)
                {
                    int selected_element = getSelectedIndex();
                    int gameColor = Color.getColorValue(getString(selected_element));

                    if (wcr.gameMode().equals(GameMode.CREATE_GAME))
                    {
                        ServerThread st = new ServerThread(wcr, gameColor);
                        wcr.ServerThread(st);
                        wcr.ServerThread().start();
                    }
                    else if (wcr.gameMode().equals(GameMode.SINGLE_PLAYER))
                    {
                        wcr.showSinglePlayerCamera(gameColor);
                    }
                }
            }
        }
        catch(Exception e)
        {}
    }
}
