package net.sf.wcr.bluetooth;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import net.sf.wcr.WCR;
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
    
    public int getGameColor()
    {
        return gameColor;
    }

    public void run()
    {
        Packet p;
	try
        {
            /* create the connection */
            DiscoveryAgent agent = local().getDiscoveryAgent();
//            String connStr = "btspp://" + rd.getBluetoothAddress() + ":1a310b97237f81937";
            String connStr = agent.selectService(
                    parent.uuid,
                    ServiceRecord.AUTHENTICATE_NOENCRYPT, false);
            conn((StreamConnection) Connector.open(connStr));

            /* read welcome packet */
            p = read();
            
            if (p.getCommand().equals("WELCOME"))
            {
                /* get the current game color */
                gameColor = Color.getColorValue(p.getParameter());
                
                /* prepare the response packet */
                p = new Packet("WANNAPLAY");
                
                /* submit the response packet */
                p.submit(out());
                
                /* ok, now we can start the game! */
                parent.do_alert("Game starting in 3 seconds...", 3000);
                Thread.sleep(3000);
                
                parent.showClientCamera(gameColor);
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
        System.out.println(lost.getCommand());
        if (lost.getCommand().equals("YOULOSE"))
        {
            parent.do_alert(lost.getCommand(), Alert.FOREVER);
        }

        close();
    }
}
