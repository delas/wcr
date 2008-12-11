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

import java.io.PrintStream;

/**
 * This class is used for printing debug informations
 * 
 * @author Andrea Burattin
 */
public class Debug
{
    /**
     * This variable can have values in the range 0 - 10, where 0 means no debug
     * printing, 10 maximum debug level
     */
    public static int debugLevel = 10;
    
    /**
     * This is the output stream for the logger
     */
    public static PrintStream output = System.out;
    
    /**
     * This method prints debug informations
     * FIXME: it would be nice to automagically get the obj parameter... but
     *        jme seems not to able to use technics as found in:
     *        http://www.javaspecialists.co.za/archive/newsletter.do?issue=137
     * 
     * @param msg the message to be printed
     * @param level the debug message level
     * @param obj the actual caller class
     */
    synchronized public static void dbg(String msg, int level, Object obj)
    {
        if (debugLevel >= level)
        {
            output.print("DBG [" + System.currentTimeMillis());
            if (debugLevel > 5)
            {
                String className = obj.getClass().getName();
                output.print(":" + 
                        className.substring(className.lastIndexOf('.') + 1));
            }
            output.print("] ");
            output.print(msg);
            output.print("\n");
        }
    }
    
    /**
     * This method prints exception debug informations
     * FIXME: it would be nice to automagically get the obj parameter... but
     *        jme seems not to able to use technics as found in:
     *        http://www.javaspecialists.co.za/archive/newsletter.do?issue=137
     * 
     * @param exc exception to be printed
     * @param level the debug message level
     * @param obj the actual caller class
     */
    synchronized public static void dbg(Throwable exc, int level, Object obj)
    {
        if (debugLevel >= level)
        {
            output.print("DBG [" + System.currentTimeMillis());
            if (debugLevel > 5)
            {
                String className = obj.getClass().getName();
                output.print(":" + 
                        className.substring(className.lastIndexOf('.') + 1));
            }
            String excName = exc.getClass().getName();
            
            output.print(":EXCEPTION] throw new ");
            output.print(excName.substring(excName.lastIndexOf('.') + 1) + 
                    "("+ exc.getMessage() +")");
            output.print("\n");
        }
    }
}
