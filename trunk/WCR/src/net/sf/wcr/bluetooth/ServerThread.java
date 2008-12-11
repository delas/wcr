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
import net.sf.wcr.Debug;
import net.sf.wcr.WCR;
import net.sf.wcr.forms.misc.ConnectionLostForm;
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
    
    protected void finalize()
    {
        super.finalize();
    }
    
    public void close() throws IOException
    {
        super.close();
        Debug.dbg("   Closing notifier...", 7, this);
        notifier.close();
        notifier = null;
        Debug.dbg("   Notifier closed", 7, this);
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
            Debug.dbg("Starting connection procedure", 3, this);
            /* create the connection */
	    if (!local().setDiscoverable(DiscoveryAgent.GIAC))
	    {
		System.out.println("Impossible to set to discoverable");
	    }
            String connStr = "btspp://localhost:" + WCR.WCR_UUID + ";name=" + WCR.WCR_SERVICE;
            Debug.dbg("   Connection string: "+ connStr, 3, this);
            
            notifier = (StreamConnectionNotifier)Connector.open(connStr);
            Debug.dbg("   Localy connected", 3, this);
            
            Form f = new Form("Server activated");
	    f.append(new Gauge("Server activated, waiting for device...", false,
                Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING));
	    parent.display.setCurrent(f);
            
            /* accept the incoming connection */
	    conn(notifier.acceptAndOpen());
            Debug.dbg("   Connected with someone", 3, this);

            /* create a welcome packet */
            p = new Packet("WELCOME", Color.getColorName(gameColor));
            
            /* send the welcome packet */
            p.submit(out());
            Debug.dbg("   Welcome message sent", 3, this);

            /* read the response packet */
            p = read();
            Debug.dbg("   Welcome-response red", 3, this);

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
        catch(IOException e)
        {
            Debug.dbg(e, 9, this);
        }
	catch (Exception e)
	{
            Debug.dbg(e, 9, this);
        }
        finally
        {
//            parent.ServerThread(null);
            Debug.dbg("ServerThread body finished", 3, this);
        }
    }
    
    private void waitToLose() throws IOException, InterruptedException
    {
        Debug.dbg("   Starting waitToLose()", 3, this);
        Packet lost = null;
        /* wait until we receive a YOULOST message */
        do
        {
            lost = read();
            if (lost == null || gameFinished())
            {
                break;
            }
        } while (!lost.getCommand().equals("YOULOSE"));

        /* we can be here for two causes:
         * - the connection with the client has been lost (if lost == null)
         * - packet red contains a "YOULOSE" message, so we have lost */
        if (lost != null && lost.getCommand().equals("YOULOSE"))
        {
            parent.display.setCurrent(new LoserForm(parent));
        }
        else if (!gameFinished())
        {
            parent.display.setCurrent(new ConnectionLostForm(parent));
        }
        
        close();
        Debug.dbg("   Finished waitToLose()", 3, this);
    }
}
