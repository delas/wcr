package net.sf.wcr.media;


import javax.microedition.media.*;
import javax.microedition.lcdui.*;
import net.sf.wcr.WCR;

public class Video extends Thread
{

    WCR midlet;
    String[] colorNames =
    {
	"black",
	"red",
	"green",
	"blue",
	"white"
    };

    public Video(WCR midlet)
    {
	this.midlet = midlet;
    }

    public void run()
    {
//	captureVideo();
    }
    
    
    public void captureVideo()
    {
	try
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
	catch (MediaException me)
	{
	    midlet.do_alert(me.getMessage(), Alert.FOREVER);
	}
    }

    
};
