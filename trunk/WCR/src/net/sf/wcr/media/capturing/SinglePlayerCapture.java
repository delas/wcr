package net.sf.wcr.media.capturing;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import net.sf.wcr.WCR;
import net.sf.wcr.media.Color;

public class SinglePlayerCapture extends Video
{
    
    
    public SinglePlayerCapture(WCR midlet)
    {
        super(midlet);
    }
    
    protected void exec() throws MediaException
    {
        byte[] raw = midlet.videoControl.getSnapshot("encoding=jpeg");

        Image image = Image.createImage(raw, 0, raw.length);
        int color = Color.getDominantColor(image);

        Form f = new Form("Capture screenshot");
        f.setCommandListener(midlet);
        f.addCommand(midlet.back);

        f.append("Color: \t" + Color.getColorName(color) + "\n");
        f.append(image);
        midlet.display.setCurrent(f);

        midlet.player.close();
        midlet.player = null;
        midlet.videoControl = null;
    }
}
