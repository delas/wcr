package net.sf.wcr.forms.menu;

import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import net.sf.wcr.WCR;
import net.sf.wcr.bluetooth.ClientThread;

/**
 * This is the form with all the devices found. This form is showed when a
 * player is trying to join an already created game. First of all, he will have
 * a gauge, while waiting to find all devices and, after that, this form will
 * show.
 * 
 * @author Andrea Burattin
 */
public class DeviceListForm extends List implements CommandListener
{
    private WCR wcr;
    private Command back;
    private Vector devices;
    private String img_path;
    private Image opponent_icon;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public DeviceListForm(WCR wcr)
    {
        super("Choose your opponent", Choice.IMPLICIT);
        
        try
        {
            this.wcr = wcr;
            this.devices = wcr.getAllDevices();
            this.back = new Command("Back", Command.BACK, 0);
            this.img_path = "/net/sf/wcr/media/files/";
            this.opponent_icon = Image.createImage(img_path + "icon/opponent.png");

            String device_name;

            /* let's add all the finded devices */
            for (int x = 0; x < devices.size(); x++)
            {
                device_name = ((RemoteDevice) devices.elementAt(x)).getFriendlyName(false) + " - " + ((RemoteDevice) devices.elementAt(x)).getBluetoothAddress();
                append(device_name, opponent_icon);
            }
            
            addCommand(back);
            setCommandListener(this);
        }
        catch (Exception e)
        {}
    }
    
    /**
     * The CommandListener required method
     * 
     * @param c
     * @param s
     */
    public void commandAction(Command c, Displayable s)
    {
        if (c == back)
        {
            /* the user clicked on the back button */
            wcr.mainMenu();
        }
        else
        {
            try
            {
                /* can we create a new client thread for the game? */
                if (getSelectedIndex() >= 0)
                {
                    ClientThread ct = new ClientThread(wcr, (RemoteDevice)devices.elementAt(getSelectedIndex()));
                    wcr.ClientThread(ct);
                }
            }
            catch(BluetoothStateException e)
            {}
        }
    }
}
