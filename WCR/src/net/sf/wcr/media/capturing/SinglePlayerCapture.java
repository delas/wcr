package net.sf.wcr.media.capturing;

import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.forms.misc.WinnerForm;
import net.sf.wcr.media.Color;

/**
 * This is the implementation of a singleplayer game mode
 * 
 * @author Andrea Burattin
 */
public class SinglePlayerCapture extends Video
{
    private VideoControl video_control;
    private int gameColor;

    public SinglePlayerCapture(WCR midlet, Player player, int gameColor)
    {
        super(midlet, player);
        this.video_control = (VideoControl)player.getControl("VideoControl");
        this.gameColor = gameColor;
    }
    
    protected void exec() throws MediaException
    {
        byte[] raw = video_control.getSnapshot("encoding=jpeg");
        
        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);

        /* is the color of the photo the same as the required one? */
        if (color == gameColor)
        {
            /* ooook, you hit the! :) */
            /* we can now close the camera... */
            player.close();
            /* inform yourself about this */
            midlet.display.setCurrent(new WinnerForm(midlet));
        }
        else
        {
            String msg = "Wrong color: required " + 
                    Color.getColorName(gameColor) + ", shooted " +
                    Color.getColorName(color) + "!";
            midlet.do_alert(msg, 2000);
        }
    }
}
