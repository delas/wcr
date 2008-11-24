package net.sf.wcr.media.capturing;

import javax.microedition.media.MediaException;
import net.sf.wcr.WCR;

public abstract class Video extends Thread
{
    WCR midlet;
    
    public Video(WCR midlet)
    {
	this.midlet = midlet;
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
