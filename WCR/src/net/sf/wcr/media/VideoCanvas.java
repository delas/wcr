package net.sf.wcr.media;

import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;

public class VideoCanvas extends Canvas
{

    private WCR midlet;
    private int gameColor;
    private Font font;
    private String title;
    private int titleXPos;

    public VideoCanvas(WCR midlet, VideoControl videoControl, int gameColor)
    {
        int width = getWidth();
        int height = getHeight();
        this.midlet = midlet;
        this.gameColor = gameColor;
        font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        title = new String("SHOOT " + Color.getColorName(gameColor).toUpperCase() + "!");
        titleXPos = 5; //(width - font.stringWidth(title));

        videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
        try
        {
            videoControl.setDisplayLocation(2, font.getHeight());
            videoControl.setDisplaySize(width - 4, height - 4 - font.getHeight());
        }
        catch (MediaException me)
        {
            me.printStackTrace();
        }
        videoControl.setVisible(true);
    }

    public void paint(Graphics g)
    {
        /* draw the background */
        g.setColor(gameColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        /* insert the title string */
        g.setColor(Color.getTextColor(gameColor));
        g.drawString(title, titleXPos, 1, g.TOP | g.LEFT);
    }
}
