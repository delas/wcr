package net.sf.wcr.media;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Form;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.Packet;
import net.sf.wcr.bluetooth.ServerThread;

public class ServerCapture extends Video
{

    private ServerThread st;
    
    public ServerCapture(WCR midlet, ServerThread st)
    {
        super(midlet);
        this.st = st;
    }
    
    private void serverGUI()
    {
        Form f = new Form("");
        f.append("You win");
        f.setCommandListener(midlet);
        f.addCommand(midlet.clientCapture);
        midlet.display.setCurrent(f);
        
        Packet p = new Packet("YOULOSE");
        st.write(p);
    }
    
    public void run()
    {
        try
        {
            serverGUI();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
