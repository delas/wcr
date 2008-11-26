package net.sf.wcr.forms.misc;

import net.sf.wcr.WCR;

/**
 * Form to be shown when the connection between two players is lost
 * 
 * @author Andrea Burattin
 */
public class ConnectionLostForm extends EndGameForm
{
    public ConnectionLostForm(WCR wcr)
    {
        super(wcr, "Connection lost...", "connection_lost.png", "Connection lost...");
    }
}
