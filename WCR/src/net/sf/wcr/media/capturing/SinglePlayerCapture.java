package net.sf.wcr.media.capturing;

import net.sf.wcr.media.Video;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.media.Color;

public class SinglePlayerCapture extends Video
{
    private VideoControl video_control;

    public SinglePlayerCapture(WCR midlet, Player player)
    {
        super(midlet, player);
        this.video_control = (VideoControl)player.getControl("VideoControl");
    }
    
    protected void exec() throws MediaException
    {
        byte[] raw = video_control.getSnapshot("encoding=jpeg");
        
        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);

        Form f = new Form("Capture screenshot");
//        f.setCommandListener(midlet);
//        f.addCommand(midlet.back);

        f.append("Color: \t" + Color.getColorName(color) + "\n");
        f.append(image);
        midlet.display.setCurrent(f);
    }
}
