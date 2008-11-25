package net.sf.wcr.media;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import net.sf.wcr.WCR;

public abstract class Video extends Thread
{
    protected WCR midlet;
    protected Player player;
    
    public Video(WCR midlet, Player player)
    {
	this.midlet = midlet;
        this.player = player;
    }
    
    protected abstract void exec() throws MediaException;

    public void run()
    {
        try
        {
            exec();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
};
