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
            String connStr = "btspp://localhost:1;master=false;encrypt=false;authenticate=false";
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
