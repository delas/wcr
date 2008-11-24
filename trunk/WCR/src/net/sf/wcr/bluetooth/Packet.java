package net.sf.wcr.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class represents a symbolic packet. These are the main entities
 * that "navigates" through the Bluetooth channel.
 *
 * @author Andrea Burattin
 */
public class Packet
{

    private String cmd;
    private String params;

    /**
     * This constructor can build a new package starting from a command and its
     * parameters.
     *
     * @param cmd the main command
     * @param params command parameter
     */
    public Packet(String cmd, String params)
    {
        this.cmd = cmd;
        this.params = params;
    }

    /**
     * This constructor can build a new package starting from a command.
     *
     * @param cmd the main command
     */
    public Packet(String cmd)
    {
        this.cmd = cmd;
    }

    /**
     * This method to fetch the packet cmd
     *
     * @return the command
     */
    public String getCommand()
    {
        return cmd;
    }

    /**
     * This method to set the current command
     * 
     * @param cmd the command
     */
    public void setCommand(String cmd)
    {
        this.cmd = cmd;
    }

    /**
     * This method to fetch the string parameter
     *
     * @return a string representation of the parameter
     */
    public String getParameter()
    {
        return params;
    }

    /**
     * This method to set the current param
     * 
     * @param params the param
     */
    public void setParameter(String params)
    {
        this.params = params;
    }

    /**
     * This method submits the current package to the output stream as parameter
     *
     * @param os the OutputStream object, where the packet has to be written
     * @throws java.io.IOException
     */
    public void submit(OutputStream os) throws IOException
    {
        os.write(this.serialize());
    }

    /**
     * This method read a packet from an input stream
     *
     * @param in the InputStream where listen for packet
     * @return the read packet
     * @throws java.io.IOException
     */
    static public Packet fetch(InputStream in) throws IOException
    {
        int totDelimiter = 0;
        char data;
        String str;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        do
        {
            data = (char) in.read();
            out.write(data);
            if (data == '§')
            {
                totDelimiter++;
            }
        }
        while (totDelimiter < 2);
        str = out.toString();
        return Packet.deserialize(str);
    }

    /**
     * This method serialize a packet to a string, to be submitted to Bluetooth.
     *
     * @return the packet serialization
     */
    private byte[] serialize()
    {
        String ret = new String();
        if (cmd != null)
        {
            ret += cmd.replace('§', 'S');
        }
        ret += '§';
        if (params != null)
        {
            ret += params.replace('§', 'S');
        }
        ret += '§';
        return ret.getBytes();
    }

    /**
     * This static method allows the user to deserialize a string to build the
     * original packet.
     *
     * @param serialized the packet-serialization string
     * @return the corresponding packet
     */
    private static Packet deserialize(String serialized)
    {
        int sepPos = serialized.indexOf("§");
        String cmd = serialized.substring(0, sepPos);
        String para = serialized.substring(sepPos + 1, serialized.length() - 1);
        return new Packet(cmd, para);
    }
}
