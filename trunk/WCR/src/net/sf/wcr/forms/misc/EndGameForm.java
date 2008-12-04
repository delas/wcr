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

import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import net.sf.wcr.WCR;

/**
 * This is the base form, shown when the game is finished, for some cause.
 * 
 * @author Andrea Burattin
 */
public class EndGameForm extends Form implements CommandListener
{
    private WCR wcr;
    private Command back;

    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     * @param title the form title
     * @param icon the icon file to show
     * @param text the text to append to the form
     */
    public EndGameForm(WCR wcr, String title, String icon, String text)
    {
        super(title);
        
        this.wcr = wcr;
        this.back = new Command("Back", Command.BACK, 0);
        
        try
        {
            ImageItem img = new ImageItem(
                    "", 
                    Image.createImage("/net/sf/wcr/media/files/splash/" + icon), 
                    ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_VCENTER,
                    "");
            append(img);
            if (!text.equals(""))
            {
                append("\n");
                append(text);
            }

            addCommand(back);
            setCommandListener(this);
        }
        catch(IOException e)
        {}
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
