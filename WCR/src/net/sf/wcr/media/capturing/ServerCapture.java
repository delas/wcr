package net.sf.wcr.media.capturing;

import javax.microedition.lcdui.Form;
import net.sf.wcr.media.Video;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.Packet;
import net.sf.wcr.bluetooth.ServerThread;
import net.sf.wcr.media.Color;

public class ServerCapture extends Video
{
    private VideoControl video_control;
    private ServerThread st;
    
    public ServerCapture(WCR midlet, Player player, ServerThread st)
    {
        super(midlet, player);
        
        this.st = st;
        this.video_control = (VideoControl)player.getControl("VideoControl");
    }
    
    protected void exec() throws MediaException
    {
        /* get the image */
        byte[] raw = video_control.getSnapshot("encoding=jpeg");
        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);
        
        /* is the color of the photo the same as the required one? */
        if (color == st.getGameColor())
        {
            /* ooook, you're the winner! :) */
            /* said this to your oppenent */
            Packet p = new Packet("YOULOSE");
            st.write(p);
            /* inform yourself about this */
            Form f = new Form("");
            f.append("You win");
//            f.setCommandListener(midlet);
//            f.addCommand(midlet.serverCapture);
            midlet.display.setCurrent(f);
        }
        else
        {
            String msg = "Wrong color: required " + 
                    Color.getColorName(st.getGameColor()) + ", shooted " +
                    Color.getColorName(color) + "!";
            midlet.do_alert(msg, 2000);
        }
    }
}
