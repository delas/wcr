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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import net.sf.wcr.WCR;

/**
 * This is the form that allow a player to show a nice gauge while waiting that
 * all devices are found.
 * 
 * @author Andrea Burattin
 */
public class FindDeviceForm extends Form implements CommandListener
{
    private WCR wcr;
    private Gauge gauge;
    private Command abort;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public FindDeviceForm(WCR wcr)
    {
        super("Device searching...");
        
        this.wcr = wcr;
        this.abort = new Command("Abort", Command.STOP, 0);
        this.gauge = new Gauge("Devices searching...", false, Gauge.INDEFINITE,
                          Gauge.CONTINUOUS_RUNNING);
        
        append(gauge);
        addCommand(abort);
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
        if (c == abort)
        {
            wcr.discoveryAgent().cancelInquiry(wcr);
        }
    }
}
