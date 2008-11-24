package net.sf.wcr.forms;

import java.io.IOException;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import net.sf.wcr.WCR;

/**
 * The application splash screen. It's aim is to entertain the user while the
 * application is loading.
 * 
 * @author Andrea Burattin
 */
public class SplashScreen extends Form implements Runnable
{
    private WCR wcr;
    private String img_path;
    private long loading_time;
    
    /**
     * Form constructor
     * 
     * @param wcr the main WCR application
     */
    public SplashScreen(WCR wcr)
    {
        super("Welcome to WCR");
        
        this.loading_time = 2000;
        this.wcr = wcr;
        this.img_path = "/net/sf/wcr/media/files/";
        
        try
        {
            append(Image.createImage(img_path + "splash/wcr.png"));
            
            Thread t = new Thread(this);
            t.start();
        }
        catch(IOException e)
        {}
    }
    
    public void run()
    {
        try
        {
            Thread.sleep(this.loading_time);
            wcr.mainMenu();
        }
        catch(InterruptedException ie)
        {}
    }
}
