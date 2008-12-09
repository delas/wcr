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

package net.sf.wcr.bluetooth;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnectionNotifier;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import net.sf.wcr.WCR;
import net.sf.wcr.forms.misc.LoserForm;
import net.sf.wcr.media.Color;

/**
 * This class is an extension of the NetThread, it describes a server flow, for
 * this particular game. The run() method contains all the network protocol to
 * comunicate with the client.
 *
 * @author Andrea Burattin
 */
public class ServerThread extends NetThread
{
    StreamConnectionNotifier notifier;
    int gameColor;
    
    
    public ServerThread(WCR p, int gameColor) throws BluetoothStateException
    {
	super(p);
        this.gameColor = gameColor;
    }
    
    public int getGameColor()
    {
        return gameColor;
    }

    public void exec()
    {
        Packet p;
	try
	{
            /* create the connection */
	    if (!local().setDiscoverable(DiscoveryAgent.GIAC))
	    {
		System.out.println("Impossible set to discoverable");
	    }
            String connStr = "btspp://localhost:" + parent.uuid + ";name=" + parent.WCR_SERVICE;
//	    notifier = (StreamConnectionNotifier)Connector.open("btspp://localhost:" + parent.uuid);
            notifier = (StreamConnectionNotifier)Connector.open(connStr);
            
            Form f = new Form("Server activated");
	    f.append(new Gauge("Server activated, waiting for device...", false,
                Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING));
	    parent.display.setCurrent(f);
            
            /* accept the incoming connection */
	    conn(notifier.acceptAndOpen());

            /* create a welcome packet */
            p = new Packet("WELCOME", Color.getColorName(gameColor));
            
            /* send the welcome packet */
            p.submit(out());

            /* read the response packet */
            p = read();

            if (p.getCommand().equals("WANNAPLAY"))
            {
                /* ok, now we can start the game! */
                parent.do_alert("Game starting in 3 seconds...", 3000);
                Thread.sleep(3000);
                
                parent.showServerCamera(gameColor);
            }
            
            /* now, wait to lose... :/ */
            waitToLose();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
    
    private void waitToLose() throws IOException
    {
        Packet lost = read();
        if (lost.getCommand().equals("YOULOSE"))
        {
            parent.display.setCurrent(new LoserForm(parent));
        }

        close();
    }
}
