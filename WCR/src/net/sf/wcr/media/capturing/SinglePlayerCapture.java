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
