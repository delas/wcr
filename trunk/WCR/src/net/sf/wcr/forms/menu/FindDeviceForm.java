package net.sf.wcr.forms.menu;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import net.sf.wcr.WCR;

/**
 *
 * @author Andrea Burattin
 */
public class FindDeviceForm extends Form implements CommandListener
{
    private WCR wcr;
    private Gauge gauge;
    private Command abort;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public FindDeviceForm(WCR wcr)
    {
        super("Device searching...");
        
        this.wcr = wcr;
        this.abort = new Command("Abort", Command.STOP, 0);
        this.gauge = new Gauge("Devices searching...", false, Gauge.INDEFINITE,
                          Gauge.CONTINUOUS_RUNNING);
        
        append(gauge);
        addCommand(abort);
        setCommandListener(this);
    }
    
    /**
     * The CommandListener required method
     * 
     * @param c
     * @param s
     */
    public void commandAction(Command c, Displayable s)
    {
        if (c == abort)
        {
            wcr.discoveryAgent().cancelInquiry(wcr);
        }
    }
}
