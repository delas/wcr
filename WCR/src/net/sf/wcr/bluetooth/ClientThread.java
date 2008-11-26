package net.sf.wcr.bluetooth;

import java.io.IOException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import net.sf.wcr.WCR;
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
    
    public int getGameColor()
    {
        return gameColor;
    }

    public void exec()
    {
        Packet p;
	try
        {
                parent.do_alert("preparing agent", 1000);
                Thread.sleep(1000);
            /* create the connection */
            DiscoveryAgent agent = local().getDiscoveryAgent();
            
                parent.do_alert("preparing connection string", 1000);
                Thread.sleep(1000);
                
            String connStr = "btspp://"+ remote.getBluetoothAddress() +":1;master=false;encrypt=false;authenticate=false";
//            String connStr = agent.selectService(
//                    parent.uuid,
//                    ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            System.out.println(connStr);
                parent.do_alert("preparing connection", 1000);
                Thread.sleep(1000);
            conn((StreamConnection) Connector.open(connStr));

                parent.do_alert("connected!", 1000);
                Thread.sleep(1000);
                
            /* read welcome packet */
            p = read();
            
                parent.do_alert("read welcome!", 1000);
                Thread.sleep(1000);
            
            if (p.getCommand().equals("WELCOME"))
            {
                /* get the current game color */
                gameColor = Color.getColorValue(p.getParameter());
                
                /* prepare the response packet */
                p = new Packet("WANNAPLAY");
                
                /* submit the response packet */
                p.submit(out());
                
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
	catch (Exception e)
	{}
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
