/*
 * Author: Andrea Burattin
 * Copyright (C) 2008  Andrea Burattin, Riccardo Ferro
 *
 * $Id$
 */

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 *                   MA  02111-1307, USA.
 */

package net.sf.wcr.media;

import javax.microedition.lcdui.*;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;

/**
 * This is the VideoCanvas class, responsible of the representation of the
 * camera output
 * 
 * @author Andrea Burattin
 */
public class VideoCanvas extends Canvas
{
    private WCR midlet;
    protected Player player;
    private VideoControl video_control;
    private int gameColor;

    /**
     * Video canvas constructor
     * 
     * @param midlet the video canvas owner
     * @param gameColor the current game color
     */
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

    /**
     * Renders the Canvas. The application must implement this method in order
     * to paint any graphics. 
     * 
     * @param g the Graphics object to be used for rendering the Canvas
     */
    public void paint(Graphics g)
    {
        /* draw the background */
        g.setColor(gameColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        /* insert the title string */
        g.setColor(Color.getTextColor(gameColor));
    }
}
