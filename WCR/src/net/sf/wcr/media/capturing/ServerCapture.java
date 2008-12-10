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

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.Packet;
import net.sf.wcr.bluetooth.ServerThread;
import net.sf.wcr.forms.misc.ConnectionLostForm;
import net.sf.wcr.forms.misc.WinnerForm;
import net.sf.wcr.media.Color;

/**
 * This is the implementation of a multiplayer - server game mode
 * 
 * @author Andrea Burattin
 */
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
        
        try
        {
            /* is the color of the photo the same as the required one? */
            if (color == st.getGameColor())
            {
                /* ooook, you're the winner! :) */
                st.gameFinished(true);
                /* said this to your oppenent */
                Packet p = new Packet("YOULOSE");
                st.write(p);
                /* we can now close the camera... */
                player.close();
//                /* we can close the connection */
//                st.close();
                /* inform yourself about this */
                midlet.display.setCurrent(new WinnerForm(midlet));
            }
            else
            {
                /* submit something, to check the connection */
                Packet p = new Packet("SHOOT");
                st.write(p);
                /* print a "do panic" message! :) */
                String msg = "Wrong color: required " + 
                        Color.getColorName(st.getGameColor()) + ", shooted " +
                        Color.getColorName(color) + "!";
                midlet.do_alert(msg, 2000);
            }
        }
        catch(Exception e)
        {
            System.out.println("=============> Exception on ServerCapture");
            e.printStackTrace();
            midlet.display.setCurrent(new ConnectionLostForm(midlet));
        }
    }
}
