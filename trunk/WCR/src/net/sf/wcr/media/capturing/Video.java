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

package net.sf.wcr.media.capturing;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import net.sf.wcr.WCR;

/**
 * This class is the raw game implementation. This class is a prototype (is an
 * abstract class) for the game mode. It contains all the must-be-implemented
 * method by the single game modes:
 * <ul>
 *   <li>single player</li>
 *   <li>multiplayer
 *   <ul>
 *      <li>client</li>
 *      <li>server</li>
 *    </ul></li>
 * </ul>
 * 
 * @author Andrea Burattin
 */
public abstract class Video extends Thread
{
    protected WCR midlet;
    protected Player player;
    
    /**
     * Base constructor
     * 
     * @param midlet the game owner
     * @param player the player camera handler
     */
    public Video(WCR midlet, Player player)
    {
	this.midlet = midlet;
        this.player = player;
    }
    
    /**
     * This is the only method that have to implement the game logic, and that
     * is different between all the subclasses
     * 
     * @throws javax.microedition.media.MediaException
     */
    protected abstract void exec() throws MediaException;

    /**
     * Let's start playing!
     */
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
