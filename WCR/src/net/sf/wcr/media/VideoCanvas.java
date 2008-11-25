package net.sf.wcr.media;

import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;

public class VideoCanvas extends Canvas
{
    private WCR midlet;
    protected Player player;
    private VideoControl video_control;
    private int gameColor;

    public VideoCanvas(WCR midlet, int gameColor)
    {
        int width = getWidth();
        int height = getHeight();
        this.midlet = midlet;
        this.player = WCR.getCamera();
        
        try
        {
            player.realize();

            this.video_control = (VideoControl)player.getControl("VideoControl");
            this.gameColor = gameColor;

            video_control.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
            video_control.setDisplayLocation(2, 2);
            video_control.setDisplaySize(width - 4, height - 4);
            video_control.setVisible(true);

            player.start();
        }
        catch (MediaException me)
        {
            me.printStackTrace();
        }
    }

    public void paint(Graphics g)
    {
        /* draw the background */
        g.setColor(gameColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        /* insert the title string */
        g.setColor(Color.getTextColor(gameColor));
    }
}
