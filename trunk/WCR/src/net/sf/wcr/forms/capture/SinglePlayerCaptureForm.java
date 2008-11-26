package net.sf.wcr.forms.capture;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import net.sf.wcr.WCR;
import net.sf.wcr.media.VideoCanvas;
import net.sf.wcr.media.capturing.SinglePlayerCapture;

/**
 * This is the form for a single player game mode.
 *
 * @author Andrea Burattin
 */
public class SinglePlayerCaptureForm extends VideoCanvas implements CommandListener
{
    private WCR wcr;
    private int gameColor;
    private Command shoot;
    private Command back;

    /**
     * Form constructor
     *
     * @param wcr the main WCR application
     * @param gameColor the current game color
     */
    public SinglePlayerCaptureForm(WCR wcr, int gameColor)
    {
        super(wcr, gameColor);

        this.wcr = wcr;
        this.gameColor = gameColor;
        this.shoot = new Command("Shoot!", Command.OK, 0);
        this.back = new Command("Back", Command.BACK, 1);
//
//        player.prefetch();
//        player.realize();
//        player.start();

        addCommand(shoot);
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
        if (c == back)
        {
            player.close();
            wcr.mainMenu();
        }
        else if (c == shoot)
        {
            new SinglePlayerCapture(wcr, player, gameColor).start();
        }
    }
}
