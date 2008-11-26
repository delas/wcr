package net.sf.wcr.forms.misc;

import java.io.IOException;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
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
        this.img_path = "/net/sf/wcr/media/files/splash/";

        try
        {
            ImageItem img = new ImageItem(
                    "",
                    Image.createImage(img_path + "wcr.png"),
                    ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_VCENTER,
                    "");
            append(img);

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
