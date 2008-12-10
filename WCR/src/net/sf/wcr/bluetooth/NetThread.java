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
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.StreamConnection;
import net.sf.wcr.WCR;
import net.sf.wcr.forms.misc.ConnectionLostForm;

/**
 * This abstract class is a prototype for all the network operations. It is
 * intended as every network flow extends this class, and implements the only
 * abstract method: run. This is the run method of the network thread, and it
 * will contain all the flow operations.
 *
 * @author Andrea Burattin
 */
abstract public class NetThread extends Thread
{
    protected WCR parent;
    private StreamConnection conn;
    private OutputStream out;
    private InputStream in;
    private LocalDevice local;
    private boolean gameFinished;


    /**
     * Class constructor
     *
     * @param p the main WCR object
     * @throws BluetoothStateException
     */
    public NetThread(WCR p) throws BluetoothStateException
    {
	this.parent = p;
        this.gameFinished = false;
        local = LocalDevice.getLocalDevice();
    }
    
    public void run()
    {
        try
        {
            exec();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * The only abstract method, the thread main flow
     */
    abstract public void exec();
    
    /**
     * This method to get an output stream object for current connection
     * 
     * @return the OutputStream from current connection
     * @throws java.io.IOException
     */
    public OutputStream out() throws IOException
    {
        if (out == null)
        {
            out = conn.openDataOutputStream();
        }
        return out;
    }
    
    /**
     * This method to get an input stream object for current connection
     * 
     * @return the InputStream from current connection
     * @throws java.io.IOException
     */
    public InputStream in() throws IOException
    {
        if (in == null)
        {
            in = conn.openDataInputStream();
        }
        return in;
    }
    
    /**
     * This method sets the current connection object
     * 
     * @param conn the new connection
     */
    public void conn(StreamConnection conn)
    {
        this.conn = conn;
    }
    
    /**
     * This method to get the current stream connection object
     * 
     * @return the actual StreamConnection
     */
    public StreamConnection conn()
    {
        return conn;
    }
    
    /**
     * This method to get the current local device
     * 
     * @return the actual LocalDevice
     */
    public LocalDevice local()
    {
        return local;
    }

    /**
     * This method writes a packet through the network connection
     *
     * @param pkg the packet to be written
     * @throws Exception in some situation an exception can be throwned (e.g.
     * connection lost)
     */
    public void write(Packet pkg) throws Exception
    {
	pkg.submit(out());
    }

    /**
     * This method listen the network channel, and return the read packet
     *
     * @return the read packet
     * @throws java.io.IOException
     */
    public Packet read() throws IOException
    {
        Packet p = null;
        try
        {
            p = Packet.fetch(in());
        }
        catch(OutOfMemoryError e)
        {}
        catch(NullPointerException e)
        {}
        return p;
    }

    /**
     * This method close all the opened connection
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException
    {
        if (conn != null)
        {
            conn.close();
            conn = null;
        }
        if (out != null)
        {
            out.close();
            out = null;
        }
        if (in != null)
        {
            in.close();
            in = null;
        }
    }
    
    /**
     * Method to get the game status
     * 
     * @return the current game status
     */
    public boolean gameFinished()
    {
        return gameFinished;
    }
    
    /**
     * Method to set the game status
     * 
     * @param gameFinished the game status to be set
     */
    public void gameFinished(boolean gameFinished)
    {
        /* if there is a winner (or a loser), the game MUST be finished */
        this.gameFinished = gameFinished;
    }
}
