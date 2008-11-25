package net.sf.wcr.forms.misc;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import net.sf.wcr.WCR;

/**
 * This is the form with some credits notes.
 * 
 * @author Andrea Burattin
 */
public class CreditsForm extends Form implements CommandListener
{
    private WCR wcr;
    private Command back;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public CreditsForm(WCR wcr)
    {
        super("Info WCR");
        
        this.wcr = wcr;
        this.back = new Command("Back", Command.BACK, 0);
        
        append("This game has been developed for a Wireless Network course " +
               "at the University of Padua, Italy.");
        append("\n");
        append(new StringItem("Author", "Burattin Andrea, Ferro Riccardo"));
        append(new StringItem("Current version", "1.0"));
        append(new StringItem("Release date", "19 dec 2008"));
        append("\n");
        append("This code is licenced under the term of GNU GPL licence.");
        
        addCommand(back);
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
        if (c == back)
        {
            wcr.mainMenu();
        }
    }
}
