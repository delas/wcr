package net.sf.wcr.media;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Form;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ClientThread;
import net.sf.wcr.bluetooth.Packet;

public class ClientCapture extends Video
{

    private ClientThread ct;
    
    
    public ClientCapture(WCR midlet, ClientThread ct)
    {
        super(midlet);
        this.ct = ct;
    }
    
    private void clientGUI()
    {
        Form f = new Form("");
        f.append("You win");
        f.setCommandListener(midlet);
        f.addCommand(midlet.clientCapture);
        midlet.display.setCurrent(f);
        
        Packet p = new Packet("YOULOSE");
        ct.write(p);
    }

    public void run()
    {
        try
        {
            clientGUI();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
