package net.sf.wcr.forms.misc;

import net.sf.wcr.WCR;

/**
 * Form to be shown when a user wins a game
 * 
 * @author Andrea Burattin
 */
public class WinnerForm extends EndGameForm
{
    public WinnerForm(WCR wcr)
    {
        super(wcr, "You win!", "win.png", "");
    }
}
