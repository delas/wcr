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

package net.sf.wcr;

import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.media.control.VideoControl;
import net.sf.wcr.bluetooth.ClientThread;
import net.sf.wcr.bluetooth.ServerThread;
import net.sf.wcr.core.GameMode;
import net.sf.wcr.forms.capture.ClientCaptureForm;
import net.sf.wcr.forms.capture.ServerCaptureForm;
import net.sf.wcr.forms.capture.SinglePlayerCaptureForm;
import net.sf.wcr.forms.misc.CreditsForm;
import net.sf.wcr.forms.menu.DeviceListForm;
import net.sf.wcr.forms.menu.FindDeviceForm;
import net.sf.wcr.forms.menu.MainMenuForm;
import net.sf.wcr.forms.menu.SelectColorForm;
import net.sf.wcr.forms.misc.SplashScreen;

/**
 * Base project class.
 * 
 * This is the main applet class, it not only extends a MIDlet Java class, but
 * implements a DiscoveryListener: this class does not contain only the graphic
 * related stuff, but is also the main Bluetooth core.
 *
 * @author Andrea Burattin
 */
public class WCR extends MIDlet implements DiscoveryListener
{

    /** The current display object */
    public Display display;
    /** The application identifier */
    public static final UUID WCR_UUID  = new UUID("1a310b97237f81937", false);
    public static final String WCR_SERVICE = "WCR-game";
    private boolean searchDone = false;

    /* all the various application form */
    SplashScreen ss;
    MainMenuForm mmf;
    CreditsForm cf;
    SinglePlayerCaptureForm spcf;

    private Vector devices;
    private LocalDevice local;
    private DiscoveryAgent agent;

    private static VideoControl video_control;
    private ClientThread ct;
    private ServerThread st;
    private GameMode gameMode;


    /**
     * Class constructor
     */
    public WCR()
    {}



    /*
     * =========================================================================
     * MIDlet methods
     * =========================================================================
     */
    /**
     * Signals the MIDlet that it has entered the Active state.
     */
    public void startApp()
    {
	try
	{
	    display = Display.getDisplay(this);

            ss = new SplashScreen(this);

            display.setCurrent(ss);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    /**
     * Signals the MIDlet to enter the Paused state.
     */
    public void pauseApp()
    {
    }

    /**
     * Signals the MIDlet to terminate and enter the Destroyed state.
     * 
     * @param unconditional If true when this method is called, the MIDlet must
     * cleanup and release all resources. If false the MIDlet may throw
     * MIDletStateChangeException to indicate it does not want to be destroyed
     * at this time. 
     */
    public void destroyApp(boolean unconditional)
    {
    }




    /*
     * =========================================================================
     * DiscoveryListener methods
     * =========================================================================
     */
    /**
     * 
     * @param remoteDevice
     * @param deviceClass
     */
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass)
    {
        try
        {
            UUID[] searchList = new UUID[]{WCR_UUID};
            int[] attribSet = {0x0100, 0x0001, 0x0002, 0x0003, 0x0004};
            discoveryAgent().searchServices(attribSet, searchList, remoteDevice, this);
        }
        catch(Exception e)
        {
            do_alert(e.getMessage(), Alert.FOREVER);
        }
    }

    /**
     * 
     * @param param
     */
    public void inquiryCompleted(int param)
    {
	switch (param)
	{
	    case DiscoveryListener.INQUIRY_COMPLETED:
                /* Inquiry completed normally */
                try
                {
                    for (int i = 0, cnt = devices.size(); i < cnt; i++)
                    {
                        waitForSearchDone();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
		showDeviceList();
		break;
	    case DiscoveryListener.INQUIRY_ERROR:
                /* Error during inquiry */
		this.do_alert("Inqury error", 4000);
		break;
	    case DiscoveryListener.INQUIRY_TERMINATED:
                /* Inquiry terminated by agent.cancelInquiry() */
		mainMenu();
		break;
	}
    }

    /**
     * 
     * @param transID
     * @param serviceRecord
     */
    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord)
    {
        for (int i = 0; i < serviceRecord.length; i++)
        {
            DataElement serviceNameElement = serviceRecord[i].getAttributeValue(0x0100);
            String serviceName = (String)serviceNameElement.getValue();
            if (serviceName.equals(WCR_SERVICE))
            {
                do_alert("New device found!", 500);
                devices.addElement(serviceRecord[i].getHostDevice());
            }
        }
    }

    /**
     * 
     * @param transID
     * @param respCode
     */
    public void serviceSearchCompleted(int transID, int respCode)
    {
        searchDone = true;
        synchronized (this)
        {
            this.notifyAll();
        }
    }

    /**
     * 
     */
    public void cancelInquiry()
    {}



    /*
     * =========================================================================
     * Own methods
     * =========================================================================
     */
    
    private void waitForSearchDone()
    {
        searchDone = false;

        try {
            while (!searchDone)
            {
                synchronized (this) {
                    this.wait();
                }
            }
        } catch (Exception error) {
        }
    }

    
    /**
     * This is the method that shows the main application menu
     */
    public void mainMenu()
    {
        if (mmf == null)
        {
            mmf = new MainMenuForm(this);
        }
        display.setCurrent(mmf);
    }

    /**
     * This is the method that shows the application credits
     */
    public void creditsForm()
    {
        if (cf == null)
        {
            cf = new CreditsForm(this);
        }
        display.setCurrent(cf);
    }

    /**
     * This is the method that shows the form searching devices
     */
    public void findDevices()
    {
        FindDeviceForm fdf = new FindDeviceForm(this);
	try
	{
            display.setCurrent(fdf);
	    devices = new java.util.Vector();
            discoveryAgent().startInquiry(DiscoveryAgent.GIAC, this);
	}
	catch (Exception e)
	{
	    this.do_alert("Erron in initiating search:\n" + e.getMessage(), 4000);
            e.printStackTrace();
	}
    }

    /**
     * This is the method that shows the form with all the present devices
     */
    public void showDeviceList()
    {
        display.setCurrent(new DeviceListForm(this));
    }

    /**
     * This is the method invoked when a single player game session is starting
     *
     * @param gameColor the current game color
     */
    public void showSinglePlayerCamera(int gameColor)
    {
        display.setCurrent(new SinglePlayerCaptureForm(this, gameColor));
    }

    /**
     * This method builds a new ClientCaptureForm, with the gave color
     * 
     * @param gameColor the current game color
     */
    public void showClientCamera(int gameColor)
    {
        display.setCurrent(new ClientCaptureForm(this, gameColor));
    }

    /**
     * This method builds a new ServerCaptureForm, with the gave color
     * 
     * @param gameColor the current game color
     */
    public void showServerCamera(int gameColor)
    {
        display.setCurrent(new ServerCaptureForm(this, gameColor));
    }

    /**
     * Shows a color list menu
     */
    public void selectGameColor()
    {
        display.setCurrent(new SelectColorForm(this));
    }

    /**
     * Shows an alert message
     * 
     * @param msg the string message
     * @param time_out the string message duration
     */
    public void do_alert(String msg, int time_out)
    {
	if (display.getCurrent() instanceof Alert)
	{
	    ((Alert) display.getCurrent()).setString(msg);
	    ((Alert) display.getCurrent()).setTimeout(time_out);
	}
	else
	{
	    Alert alert = new Alert("WCR");
	    alert.setString(msg);
	    alert.setTimeout(time_out);
	    display.setCurrent(alert);
	}
    }

    /**
     * Gets the current discoveryAgent
     * 
     * @return a discovery agent
     */
    public DiscoveryAgent discoveryAgent()
    {
        if (agent == null)
        {
            agent = local().getDiscoveryAgent();
        }
        return agent;
    }

    /**
     * Gets the current localDevice
     * 
     * @return the local device
     */
    public LocalDevice local()
    {
        try
        {
            if (local == null)
            {
                local = LocalDevice.getLocalDevice();
            }
        }
        catch(BluetoothStateException e)
        {
            e.printStackTrace();
        }
        return local;
    }

    /**
     * Gets a player handler to the device camera
     * 
     * @return the camera player
     */
    public static final Player getCamera()
    {
//        if (player == null)
//        {
            try
            {
                return Manager.createPlayer("capture://image");
//                player = Manager.createPlayer("capture://image");
            }
            catch (Exception ex1)
            {
                try
                {
                    return Manager.createPlayer("capture://video");
//                    player = Manager.createPlayer("capture://video");
                }
                catch (Exception ex2)
                {
                    System.out.println("Get camera device failed!");
                    return null;
//                    player = null;
                }
            }
//        }
//        return player;
    }

    /**
     * Gets a video control associated with the device camera
     * 
     * @return the video control
     */
    public static final VideoControl getVideoControl()
    {
        Player p = getCamera();
        if (p != null)
        {
            try
            {
                if (video_control == null)
                {
                    p.prefetch();
                    p.realize();
                    video_control = (VideoControl)p.getControl("VideoControl");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return video_control;
    }

    /**
     * Sets the current game mode
     * 
     * @param gm a game mode
     */
    public void gameMode(GameMode gm)
    {
        gameMode = gm;
    }

    /**
     * Gets the current game mode
     * 
     * @return the current game mode
     */
    public GameMode gameMode()
    {
        return gameMode;
    }

    /**
     * Sets the applet server thread
     * 
     * @param st the server thread to be setted
     */
    public void ServerThread(ServerThread st)
    {
        this.st = st;
    }

    /**
     * Gets the current server thread
     * 
     * @return the current server thread
     */
    public ServerThread ServerThread()
    {
        return this.st;
    }

    /**
     * Sets the applet client thread
     * 
     * @param ct the client thread to be setted
     */
    public void ClientThread(ClientThread ct)
    {
        this.ct = ct;
    }

    /**
     * Gets the current client thread
     * 
     * @return the current client thread
     */
    public ClientThread ClientThread()
    {
        return this.ct;
    }

    /**
     * Gets the devices list
     * 
     * @return the found devices list
     */
    public Vector getAllDevices()
    {
        return this.devices;
    }
}
