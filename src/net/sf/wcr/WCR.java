package net.sf.wcr;

import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
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
import net.sf.wcr.media.capturing.ClientCapture;
import net.sf.wcr.media.Color;
import net.sf.wcr.media.capturing.ServerCapture;
import net.sf.wcr.media.capturing.Video;
import net.sf.wcr.media.VideoCanvas;
import net.sf.wcr.media.capturing.SinglePlayerCapture;

public class WCR extends MIDlet implements CommandListener, DiscoveryListener
{

    public Display display;
    public Command back, exit, singlePlayerCapture, clientCapture, serverCapture;
    public Player player;
    public VideoControl videoControl;
    public Video video;
    public UUID uuid;

    private List main_list;         /**< The main menu */
    private List dev_list;          /**< The devices list */
    private List color_choose_list; /**< The list of colors */

    private Vector devices;
    private LocalDevice local;
    private DiscoveryAgent agent;

    private ClientThread ct;
    private ServerThread st;
    private GameMode gameMode;


    public WCR()
    {
	back = new Command("Back", Command.BACK, 0);
	exit = new Command("Exit", Command.EXIT, 0);
	singlePlayerCapture = new Command("Capture", Command.SCREEN, 1);
        clientCapture = new Command("Capture", Command.SCREEN, 1);
        serverCapture = new Command("Capture", Command.SCREEN, 1);

        uuid = new UUID("1a310b97237f81937", false);
    }



    /*
     * =========================================================================
     * MIDlet methods
     * =========================================================================
     */
    public void startApp()
    {
	try
	{
	    display = Display.getDisplay(this);

	    main_list = new List("Select Operation", Choice.IMPLICIT);
	    dev_list = new List("Select Device", Choice.IMPLICIT);

            main_list.append("New game", null);
	    main_list.append("Join game", null);
	    main_list.append("Single player", null);
	    main_list.addCommand(exit);
	    main_list.setCommandListener(this);

	    dev_list.addCommand(back);
	    dev_list.setCommandListener(this);

	    display.setCurrent(main_list);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void pauseApp()
    {
    }

    public void destroyApp(boolean unconditional)
    {
    }



    /*
     * =========================================================================
     * CommandListener methods
     * =========================================================================
     */
    public void commandAction(Command c, Displayable s)
    {
	try
	{
	    if (c == exit)
	    {
		destroyApp(false);
		notifyDestroyed();
	    }
	    else if (c == back)
	    {
		display.setCurrent(main_list);
	    }
	    else if (c == singlePlayerCapture)
	    {
		new SinglePlayerCapture(this).start();
	    }
            else if (c == clientCapture)
            {
                new ClientCapture(this, ct).start();
            }
            else if (c == serverCapture)
            {
                new ServerCapture(this, st).start();
            }
	    else if (c == List.SELECT_COMMAND)
	    {
		/* main menu */
		if (s == main_list)
		{
		    /* create game */
		    if (main_list.getSelectedIndex() == 0)
		    {
                        gameMode = GameMode.CREATE_GAME;
                        selectGameColor();
		    }
		    /* search game */
		    else if (main_list.getSelectedIndex() == 1)
		    {
                        gameMode = GameMode.JOIN_GAME;
			findDevices();
		    }
		    /* single player */
		    else if (main_list.getSelectedIndex() == 2)
		    {
                        gameMode = GameMode.SINGLE_PLAYER;
                        selectGameColor();
		    }
		}
		if (s == dev_list)
		{
		    //select triggered from the device list
		    if (dev_list.getSelectedIndex() >= 0)
		    {
			ct = new ClientThread(this, (RemoteDevice)devices.elementAt(dev_list.getSelectedIndex()));
		    }
		}
                else if (s == color_choose_list)
		{
                    if (color_choose_list.getSelectedIndex() >= 0)
                    {
                        int selected_element = color_choose_list.getSelectedIndex();
                        int gameColor = Color.getColorValue(color_choose_list.getString(selected_element));
                        if (gameMode.equals(GameMode.CREATE_GAME))
                        {
                            st = new ServerThread(this, gameColor);
                        }
                        else if (gameMode.equals(GameMode.SINGLE_PLAYER))
                        {
                           showSinglePlayerCamera(gameColor);
                        }
                    }
                }
	    }
	}
	catch(BluetoothStateException e)
	{
	    e.printStackTrace();
	}
    }



    /*
     * =========================================================================
     * DiscoveryListener methods
     * =========================================================================
     */
    public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass)
    {
        try
        {
            //int transID = agent.searchServices(null, new UUID[]{uuid}, remoteDevice, this);
            if(true)
            {
                devices.addElement(remoteDevice);
                do_alert("New device found!", 1000);
            }
        }
        //catch(BluetoothStateException e)
        catch(Exception e)
        {
            do_alert(e.getMessage(), Alert.FOREVER);
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] serviceRecord)
    {}

    public void inquiryCompleted(int param)
    {
	switch (param)
	{
	    case DiscoveryListener.INQUIRY_COMPLETED:    //Inquiry completed normally
		for (int x = 0; x < devices.size(); x++)
		{
		    try
		    {
			String device_name = ((RemoteDevice) devices.elementAt(x)).getFriendlyName(false) + " - " + ((RemoteDevice) devices.elementAt(x)).getBluetoothAddress();
			this.dev_list.append(device_name, null);
			display.setCurrent(dev_list);
		    }
		    catch (Exception e)
		    {
			do_alert("Error in adding devices", 4000);
		    }
		}
		break;
	    case DiscoveryListener.INQUIRY_ERROR:       // Error during inquiry
		this.do_alert("Inqury error", 4000);
		break;
	    case DiscoveryListener.INQUIRY_TERMINATED:  // Inquiry terminated by agent.cancelInquiry()
		this.do_alert("Inqury Canceled", 4000);
		break;
	}
    }

    public void serviceSearchCompleted(int transID, int respCode)
    {
	/*switch (respCode)
	{
	    case DiscoveryListener.SERVICE_SEARCH_COMPLETED:
		for (int x = 0; x < services.size(); x++)
		{
		    try
		    {
			DataElement ser_de = ((ServiceRecord) services.elementAt(x)).getAttributeValue(0x100);
			String service_name = (String) ser_de.getValue();
			serv_list.append(service_name, null);
			display.setCurrent(serv_list);
		    }
		    catch (Exception e)
		    {
			do_alert("Error in adding services ", 1000);
		    }
		}
		break;
	    case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
		this.do_alert("Device not Reachable", 4000);
		break;
	    case DiscoveryListener.SERVICE_SEARCH_ERROR:
		this.do_alert("Service serch error", 4000);
		break;
	    case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS:
		this.do_alert("No records returned", 4000);
		break;
	    case DiscoveryListener.SERVICE_SEARCH_TERMINATED:
		this.do_alert("Inqury Cancled", 4000);
		break;
	}*/
    }



    /*
     * =========================================================================
     * Own methods
     * =========================================================================
     */
    public void findDevices()
    {
	try
	{
	    devices = new java.util.Vector();

	    Form f = new Form("Searching devices...");
	    f.append(new Gauge("Searching devices...", false, Gauge.INDEFINITE,
                    Gauge.CONTINUOUS_RUNNING));
	    display.setCurrent(f);

            discoveryAgent().startInquiry(DiscoveryAgent.GIAC, this);
	}
	catch (Exception e)
	{
	    this.do_alert("Erron in initiating search:\n" + e.getMessage(), 4000);
            e.printStackTrace();
	}
    }

    public void showSinglePlayerCamera(int gameColor)
    {
	try
	{
	    player = getCamera();
            if (player == null)
            {
                throw new Exception("Can't start up camera!");
            }
	    player.realize();

	    videoControl = (VideoControl) player.getControl("VideoControl");
	    Canvas canvas = new VideoCanvas(this, videoControl, gameColor);
	    canvas.addCommand(singlePlayerCapture);
	    canvas.setCommandListener(this);
	    display.setCurrent(canvas);
	    player.start();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void showClientCamera(int gameColor)
    {
	try
	{
	    player = getCamera();
            if (player == null)
            {
                throw new Exception("Can't start up camera!");
            }
	    player.realize();

	    videoControl = (VideoControl) player.getControl("VideoControl");
	    Canvas canvas = new VideoCanvas(this, videoControl, gameColor);
	    canvas.addCommand(clientCapture);
            canvas.addCommand(back);
	    canvas.setCommandListener(this);
	    display.setCurrent(canvas);
	    player.start();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void showServerCamera(int gameColor)
    {
	try
	{
	    player = getCamera();
            if (player == null)
            {
                throw new Exception("Can't start up camera!");
            }
	    player.realize();

	    videoControl = (VideoControl) player.getControl("VideoControl");
	    Canvas canvas = new VideoCanvas(this, videoControl, gameColor);
	    canvas.addCommand(serverCapture);
	    canvas.setCommandListener(this);
	    display.setCurrent(canvas);
	    player.start();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void selectGameColor()
    {
        if (color_choose_list == null)
        {
            /* empty color list, let's create it */
            color_choose_list = new List("Select the color to shoot:", Choice.IMPLICIT);
            color_choose_list.append("Black", null);
            color_choose_list.append("Red", null);
            color_choose_list.append("Green", null);
            color_choose_list.append("Blue", null);
            color_choose_list.append("White", null);
            color_choose_list.addCommand(back);
            color_choose_list.setCommandListener(this);
        }
        display.setCurrent(color_choose_list);
    }

    public void do_alert(String msg, int time_out)
    {
	if (display.getCurrent() instanceof Alert)
	{
	    ((Alert) display.getCurrent()).setString(msg);
	    ((Alert) display.getCurrent()).setTimeout(time_out);
	}
	else
	{
	    Alert alert = new Alert("Bluetooth");
	    alert.setString(msg);
	    alert.setTimeout(time_out);
	    display.setCurrent(alert);
	}
    }
    
    public DiscoveryAgent discoveryAgent()
    {
        if (agent == null)
        {
            agent = local().getDiscoveryAgent();
        }
        return agent;
    }
    
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

    private static final Player getCamera()
    {
        try
        {
            return Manager.createPlayer("capture://image");
        }
        catch (Exception ex1)
        {
            try
            {
                return Manager.createPlayer("capture://video");
            }
            catch (Exception ex2)
            {
                System.out.println("Get camera device failed!");
                return null;
            }
        }
    }
}
