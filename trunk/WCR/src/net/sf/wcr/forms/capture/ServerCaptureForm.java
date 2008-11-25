package net.sf.wcr.forms.capture;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.media.VideoCanvas;
import net.sf.wcr.media.capturing.ServerCapture;

/**
 * This is the form for a server player game mode.
 *
 * @author Andrea Burattin
 */
public class ServerCaptureForm extends VideoCanvas implements CommandListener
{
    private WCR wcr;
    private int gameColor;
    private Command shoot;

    /**
     * Form constructor
     *
     * @param wcr the main WCR application
     * @param gameColor the current game color
     */
    public ServerCaptureForm(WCR wcr, int gameColor)
    {
        super(wcr, gameColor);

        this.wcr = wcr;
        this.gameColor = gameColor;
        this.shoot = new Command("Shoot!", Command.OK, 0);

//        player.realize();
//        player.start();

        addCommand(shoot);
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
        if (c == shoot)
        {
            new ServerCapture(wcr, player, wcr.ServerThread()).start();
        }
    }
}
