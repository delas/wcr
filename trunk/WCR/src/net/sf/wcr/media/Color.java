package net.sf.wcr.media;

import javax.microedition.lcdui.Image;

/**
 * This class can be used for all the color related operations. In particular,
 * this class contains the integer hexadecimal definitions of the color used in
 * the project (in ARGB format), and a method for the fetching of the dominand
 * color of an image.
 *
 * @author Andrea Burattin
 */
public class Color
{

    public static final int BLACK  = 0xFF000000; /**< Black representation */
    public static final int RED    = 0xFFFF0000; /**< Red representation */
    public static final int YELLOW = 0xFFFFFF00; /**< White representation */
    public static final int GREEN  = 0xFF00FF00; /**< Green representation */
    public static final int BLUE   = 0xFF0000FF; /**< Blue representation */
    public static final int WHITE  = 0xFFFFFFFF; /**< White representation */
    
    
    /**
     * This method converts a string with a color name to its integer RGB
     * representation
     * 
     * @param colorName a color name
     * @return the integer ARGB representation
     */
    public static int getColorValue(String colorName)
    {
        int color = -1;
        colorName = colorName.toUpperCase();
        if (colorName.equals("BLACK"))  color = BLACK;
        if (colorName.equals("RED"))    color = RED;
        if (colorName.equals("GREEN"))  color = GREEN;
        if (colorName.equals("BLUE"))   color = BLUE;
        if (colorName.equals("WHITE"))  color = WHITE;
        if (colorName.equals("YELLOW")) color = YELLOW;
        return color;
    }
    
    /**
     * This method removes the alpha channel, converting ARGB to RGB
     * 
     * @param ARGB the current color
     * @return the parameter color, without the alpha channel
     */
    public static int argbToRgb(int ARGB)
    {
        return ARGB & 0xffffff;
    }
    
    /**
     * This method is useful to get a color to be used in a text of a defined
     * background (to be readable)
     * 
     * @param backgroundColor the actual color of the background
     * @return a readable color in the background
     */
    public static int getTextColor(int backgroundColor)
    {
        int color = -1;
        if (backgroundColor == BLACK)  color = WHITE;
        if (backgroundColor == RED)    color = WHITE;
        if (backgroundColor == GREEN)  color = BLACK;
        if (backgroundColor == BLUE)   color = WHITE;
        if (backgroundColor == WHITE)  color = BLACK;
        if (backgroundColor == YELLOW) color = BLACK;
        return color;
    }
    
    /**
     * This method, starting from a color ARGB representation, gets its color
     * name
     *
     * @param color a ARGB color
     * @return the color english name
     */
    public static String getColorName(int color)
    {
        String colorName = "UNDEFINED";
        switch (color)
        {
            case (BLACK):
                colorName = "black";
                break;
            case (RED):
                colorName = "red";
                break;
            case (GREEN):
                colorName = "green";
                break;
            case (BLUE):
                colorName = "blue";
                break;
            case (WHITE):
                colorName = "white";
                break;
            case (YELLOW):
                colorName = "yellow";
                break;
        }
        ;
        return colorName;
    }
    
    /**
     * This method converts from RGB format to HSV format
     *
     * @param r the R component of a pixel
     * @param g the G component of a pixel
     * @param b the B component of a pixel
     * @return the HSV representation of a pixel from RGB format
     */
    private static int[] rgb2hsv(int r, int g, int b)
    {
        int hsv[] = {-1, -1, -1}; //representation of pixel on HSV format	
        int min;    //Min. value of RGB
        int max;    //Max. value of RGB
        int delMax; //Delta RGB value

        if (r > g)
        {
            min = g;
            max = r;
        }
        else
        {
            min = r;
            max = g;
        }
        if (b > max)
        {
            max = b;
        }
        if (b < min)
        {
            min = b;
        }
        delMax = max - min;

        float H = 0, S;
        float V = max;

        if (delMax == 0)
        {
            H = 0;
            S = 0;
        }
        else
        {
            S = delMax / 255f;
            if (r == max)
            {
                H = ((g - b) / (float) delMax) * 60;
            }
            else if (g == max)
            {
                H = (2 + (b - r) / (float) delMax) * 60;
            }
            else if (b == max)
            {
                H = (4 + (r - g) / (float) delMax) * 60;
            }
        }

        hsv[0] = (int) (H);
        hsv[1] = (int) (S * 100);
        hsv[2] = (int) (V * 100);
        return hsv;
    }

    /**
     * This method is useful to get the dominant color for the image gave as
     * parameter
     *
     * @param img the image to be parsed
     * @return an integer representation of the current image dominant color
     */
    public static int getDominantColor(Image img)
    {
        /* variable declaration */
        int[] palette =
        {
            0, // black
            0, // red
            0, // green
            0, // blue
            0, // white
            0  // yellow
        };
        /* indexes */
        int H_INDEX = 0;
        int S_INDEX = 1;
        int V_INDEX = 2;
        int BLACK_INDEX = 0;
        int RED_INDEX = 1;
        int GREEN_INDEX = 2;
        int BLUE_INDEX = 3;
        int WHITE_INDEX = 4;
        int YELLOW_INDEX = 5;
        
        /*representation of image on RGB format*/
        int[] imgRgb = new int[img.getWidth() * img.getHeight()];
        
        int[][] imgHsv = new int[img.getWidth() * img.getHeight()][3];
        int maxVal = -1;
        int maxIndex = -1;
        int dominantColor = -1;
        int pixel;
        int r;
        int g;
        int b;

        /* get the ARGB value array, from the parameter image */
        img.getRGB(imgRgb, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        /* calculate the current pixel color */
        for (int i = 0; i < img.getHeight() * img.getWidth(); i++)
        {
            pixel = imgRgb[i];
            r = (pixel >> 16) & 0xff;
            g = (pixel >> 8) & 0xff;
            b = (pixel) & 0xff;

            /*conversion from rgb to hsv*/
            imgHsv[i] = rgb2hsv(r,g,b);

            /* it's black */
            if (imgHsv[i][V_INDEX] < 65)
            {
                palette[BLACK_INDEX]++;
                continue;
            }
            /* it's white */
            if (imgHsv[i][S_INDEX] < 15 && imgHsv[i][V_INDEX] > 60)
            {
                palette[WHITE_INDEX]++;
                continue;
            }
            /* it's red */
            if ((imgHsv[i][H_INDEX] >= 290 && imgHsv[i][H_INDEX] <= 360) ||
                (imgHsv[i][H_INDEX] >= 0 && imgHsv[i][H_INDEX] <= 45))
            {
                palette[RED_INDEX]++;
                continue;
            }
            /* it's yellow */
            if (imgHsv[i][H_INDEX] >= 46 && imgHsv[i][H_INDEX] <= 70)
            {
                palette[YELLOW_INDEX]++;
                continue;
            }
            /* it's green */
            if (imgHsv[i][H_INDEX] >= 71 && imgHsv[i][H_INDEX] <= 165)
            {
                palette[GREEN_INDEX]++;
                continue;
            }
            /* it's blue */
            if (imgHsv[i][H_INDEX] >= 166 && imgHsv[i][H_INDEX] <= 289)
            {
                palette[BLUE_INDEX]++;
                continue;
            }
        }

        /* calculate the dominant color */
        for (int i = 0; i < palette.length; i++)
        {
            if (palette[i] > maxVal)
            {
                maxVal = palette[i];
                maxIndex = i;
            }
        }

        switch (maxIndex)
        {
            case 0:
                dominantColor = BLACK;
                break;
            case 1:
                dominantColor = RED;
                break;
            case 2:
                dominantColor = GREEN;
                break;
            case 3:
                dominantColor = BLUE;
                break;
            case 4:
                dominantColor = WHITE;
                break;
            case 5:
                dominantColor = YELLOW;
                break;
        }
        return dominantColor;
    }
}
