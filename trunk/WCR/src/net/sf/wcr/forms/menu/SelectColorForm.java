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

package net.sf.wcr.forms.menu;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import net.sf.wcr.Debug;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ServerThread;
import net.sf.wcr.core.GameMode;
import net.sf.wcr.media.Color;

/**
 * This is the form to choose the color to shoot.
 * 
 * @author Andrea Burattin
 */
public class SelectColorForm extends List implements CommandListener
{
    private WCR wcr;
    private Command back;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public SelectColorForm(WCR wcr)
    {
        super("Select the color to shoot", Choice.IMPLICIT);
        
        this.wcr = wcr;
        this.back = new Command("Back", Command.BACK, 0);
        
        append("Black", null);
        append("Red", null);
        append("Green", null);
        append("Blue", null);
        append("Yellow", null);
        append("White", null);
        
        addCommand(back);
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
        try
        {
            if (c == back)
            {
                wcr.mainMenu();
            }
            else
            {
                if (getSelectedIndex() >= 0)
                {
                    int selected_element = getSelectedIndex();
                    int gameColor = Color.getColorValue(getString(selected_element));

                    if (wcr.gameMode().equals(GameMode.CREATE_GAME))
                    {
                        wcr.ServerThread(null);
                        wcr.ServerThread(new ServerThread(wcr, gameColor));
                        wcr.ServerThread().start();
                    }
                    else if (wcr.gameMode().equals(GameMode.SINGLE_PLAYER))
                    {
                        wcr.showSinglePlayerCamera(gameColor);
                    }
                }
            }
        }
        catch(Exception e)
        {
            Debug.dbg(e, 9, this);
        }
    }
}
