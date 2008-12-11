/*
 * Author: Andrea Burattin
 * Copyright (C) 2008  Andrea Burattin, Riccardo Ferro
 *
 * $Id$
 */

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 *                   MA  02111-1307, USA.
 */

package net.sf.wcr.forms.misc;

import java.io.IOException;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.StringItem;
import net.sf.wcr.Debug;
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

        this.loading_time = 2500;
        this.wcr = wcr;
        this.img_path = "/net/sf/wcr/media/files/splash/";

        try
        {
            ImageItem img = new ImageItem(
                    "",
                    Image.createImage(img_path + "wcr.png"),
                    ImageItem.LAYOUT_CENTER | ImageItem.LAYOUT_VCENTER,
                    "");
            StringItem str = new StringItem("Welcome", "to WCR");
            append(img);
            append(str);

            Thread t = new Thread(this);
            t.start();
        }
        catch(IOException e)
        {
            Debug.dbg(e, 9, this);
        }
    }

    public void run()
    {
        try
        {
            Thread.sleep(this.loading_time);
            wcr.mainMenu();
        }
        catch(InterruptedException ie)
        {
            Debug.dbg(ie, 9, this);
        }
    }
}
