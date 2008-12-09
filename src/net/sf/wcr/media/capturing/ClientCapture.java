package net.sf.wcr.media.capturing;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ClientThread;
import net.sf.wcr.bluetooth.Packet;
import net.sf.wcr.media.Color;

public class ClientCapture extends Video
{

    private ClientThread ct;
    
    
    public ClientCapture(WCR midlet, ClientThread ct)
    {
        super(midlet);
        this.ct = ct;
    }
    
    protected void exec() throws MediaException
    {
        /* get the image */
        byte[] raw = midlet.videoControl.getSnapshot("encoding=jpeg");
        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);
        
        /* is the color of the photo the same as the required one? */
        if (color == ct.getGameColor())
        {
            /* ooook, you're the winner! :) */
            /* said this to your oppenent */
            Packet p = new Packet("YOULOSE");
            ct.write(p);
            /* inform yourself about this */
            Form f = new Form("");
            f.append("You win");
            f.setCommandListener(midlet);
            f.addCommand(midlet.clientCapture);
            midlet.display.setCurrent(f);
        }
        else
        {
            String msg = "Wrong color: required " + 
                    Color.getColorName(ct.getGameColor()) + ", shooted " +
                    Color.getColorName(color) + "!";
            midlet.do_alert(msg, 2000);
        }
    }
}