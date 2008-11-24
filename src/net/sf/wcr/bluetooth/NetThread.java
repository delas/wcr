package net.sf.wcr.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.StreamConnection;
import net.sf.wcr.WCR;

/**
 * This abstract class is a prototype for all the network operations. It is
 * intended as every network flow extends this class, and implements the only
 * abstract method: run. This is the run method of the network thread, and it
 * will contain all the flow operations.
 *
 * @author Andrea Burattin
 */
abstract public class NetThread implements Runnable
{
    protected WCR parent;
    private StreamConnection conn;
    private OutputStream out;
    private InputStream in;
    private LocalDevice local;


    /**
     * Class constructor
     *
     * @param p the main WCR object
     * @throws BluetoothStateException
     */
    public NetThread(WCR p) throws BluetoothStateException
    {
	this.parent = p;
        local = LocalDevice.getLocalDevice();
	Thread t = new Thread(this);
	t.start();
    }

    /**
     * The only abstract method, the thread main flow
     */
    abstract public void run();
    
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
     */
    public void write(Packet pkg)
    {
	try
	{
            pkg.submit(out());
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * This method listen the network channel, and return the read packet
     *
     * @return the read packet
     * @throws java.io.IOException
     */
    public Packet read() throws IOException
    {
        return Packet.fetch(in());
    }

    /**
     * This method close all the opened connection
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException
    {
	conn.close();
        if (out != null)
        {
            out.close();
        }
        if (in != null)
        {
            in.close();
        }
    }
}
