package net.sf.wcr.media.capturing;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ClientThread;
import net.sf.wcr.bluetooth.Packet;
import net.sf.wcr.forms.misc.ConnectionLostForm;
import net.sf.wcr.forms.misc.WinnerForm;
import net.sf.wcr.media.Color;

/**
 * This is the implementation of a multiplayer - client game mode
 * 
 * @author Andrea Burattin
 */
public class ClientCapture extends Video
{
    private VideoControl video_control;
    private ClientThread ct;
    
    public ClientCapture(WCR midlet, Player player, ClientThread ct)
    {
        super(midlet, player);
        
        this.ct = ct;
        this.video_control = (VideoControl)player.getControl("VideoControl");
    }
    
    protected void exec() throws MediaException
    {
        /* get the image */
        byte[] raw = video_control.getSnapshot("encoding=jpeg");
        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);
        
        try
        {
            /* is the color of the photo the same as the required one? */
            if (color == ct.getGameColor())
            {
                /* ooook, you're the winner! :) */
                /* we can now close the camera... */
                player.close();
                /* said this to your oppenent */
                Packet p = new Packet("YOULOSE");
                ct.write(p);
                /* we can close the connection */
                ct.close();
                /* inform yourself about this */
                midlet.display.setCurrent(new WinnerForm(midlet));
            }
            else
            {
                String msg = "Wrong color: required " + 
                        Color.getColorName(ct.getGameColor()) + ", shooted " +
                        Color.getColorName(color) + "!";
                midlet.do_alert(msg, 2000);
            }
        }
        catch(IOException e)
        {
            midlet.display.setCurrent(new ConnectionLostForm(midlet));
        }
    }
}
