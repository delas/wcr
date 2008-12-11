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
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import net.sf.wcr.Debug;
import net.sf.wcr.WCR;
import net.sf.wcr.forms.misc.ConnectionLostForm;
import net.sf.wcr.forms.misc.LoserForm;
import net.sf.wcr.media.Color;

/**
 * This class is an extension of the NetThread, and contains the flow of a
 * client that want to play.
 *
 * @author Andrea Burattin
 */
public class ClientThread extends NetThread
{
    protected RemoteDevice remote;
    int gameColor;

    
    public ClientThread(WCR p, RemoteDevice rd) throws BluetoothStateException
    {
	super(p);
        this.remote = rd;
    }
    
    protected void finalize()
    {
        super.finalize();
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
            String connStr = parent.discoveryAgent().selectService(
                    WCR.WCR_UUID,
                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            Debug.dbg("   Connection string: "+ connStr, 3, this);

            conn((StreamConnection) Connector.open(connStr));
            Debug.dbg("   Connected with the server", 3, this);

            /* read welcome packet */
            p = read();
            Debug.dbg("   Welcome message received", 3, this);

            if (p.getCommand().equals("WELCOME"))
            {
                /* get the current game color */
                gameColor = Color.getColorValue(p.getParameter());
                
                /* prepare the response packet */
                p = new Packet("WANNAPLAY");
                
                /* submit the response packet */
                p.submit(out());
                Debug.dbg("   Wannaplay message sent", 3, this);
                
                /* ok, now we can start the game! */
                parent.do_alert("Game starting in 3 seconds...\n" +
                        "You have to shoot " + Color.getColorName(gameColor) + "!",
                        3000);
                Thread.sleep(3000);
                
                parent.showClientCamera(gameColor);
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
            Debug.dbg("ClientThread body finished", 3, this);
        }
    }
    
    private void waitToLose() throws IOException, InterruptedException
    {
        Debug.dbg("   Starting waitToLose()", 3, this);
        Packet lost;
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
