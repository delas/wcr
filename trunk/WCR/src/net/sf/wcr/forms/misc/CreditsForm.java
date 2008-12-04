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

package net.sf.wcr.forms.misc;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import net.sf.wcr.WCR;

/**
 * This is the form with some credits notes.
 * 
 * @author Andrea Burattin
 */
public class CreditsForm extends Form implements CommandListener
{
    private WCR wcr;
    private Command back;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public CreditsForm(WCR wcr)
    {
        super("Info WCR");
        
        this.wcr = wcr;
        this.back = new Command("Back", Command.BACK, 0);
        
        append("This game has been developed for a Wireless Network course " +
               "at the University of Padua, Italy.");
        append("\n");
        append(new StringItem("Author", "Burattin Andrea, Ferro Riccardo"));
        append(new StringItem("Current version", "1.0"));
        append(new StringItem("Release date", "19 dec 2008"));
        append("\n");
        append("This code is licenced under the term of GNU GPL license.");
        
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
        if (c == back)
        {
            wcr.mainMenu();
        }
    }
}
