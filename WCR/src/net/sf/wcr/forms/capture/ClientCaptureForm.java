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

package net.sf.wcr.forms.capture;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import net.sf.wcr.WCR;
import net.sf.wcr.media.VideoCanvas;
import net.sf.wcr.media.capturing.ClientCapture;

/**
 * This is the form for a client player game mode.
 *
 * @author Andrea Burattin
 */
public class ClientCaptureForm extends VideoCanvas implements CommandListener
{
    private WCR wcr;
    private int gameColor;
    private Command shoot;

    /**
     * Form constructor
     *
     * @param wcr the main WCR application
     * @param gameColor the current game color
     */
    public ClientCaptureForm(WCR wcr, int gameColor)
    {
        super(wcr, gameColor);

        this.wcr = wcr;
        this.gameColor = gameColor;
        this.shoot = new Command("Shoot!", Command.OK, 0);

//        player.realize();
//        player.start();

        addCommand(shoot);
        setCommandListener(this);
    }

    /**
     * The CommandListener required method
     *
     * @param c
     * @param s
     */
    public void commandAction(Command c, Displayable s)
    {
        if (c == shoot)
        {
            new ClientCapture(wcr, player, wcr.ClientThread()).start();
        }
    }
}
