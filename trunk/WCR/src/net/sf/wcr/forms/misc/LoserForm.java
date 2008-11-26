package net.sf.wcr.forms.misc;

import net.sf.wcr.WCR;

/**
 * Form to be shown when a user loses a game
 * 
 * @author Andrea Burattin
 */
public class LoserForm extends EndGameForm
{
    public LoserForm(WCR wcr)
    {
        super(wcr, "You lost!", "lose.png", "");
    }
}
