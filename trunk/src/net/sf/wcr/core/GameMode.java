package net.sf.wcr.core;

/**
 * This class describes all the possible game modes for this game.
 *
 * @author Andrea Burattin
 */
public class GameMode
{
    private int irep;

    /** Single player game mode */
    public static GameMode SINGLE_PLAYER = new GameMode(0);
    /** Game mode for a game creator */
    public static GameMode CREATE_GAME = new GameMode(1);
    /** Game mode of one who wants to join an already created game */
    public static GameMode JOIN_GAME = new GameMode(2);


    /**
     * Private constructor: the only game modes available are the predefined
     * ones
     *
     * @param irep a game mode identifier
     */
    private GameMode(int irep)
    {
        this.irep = irep;
    }

    /**
     * Indicates whether some game mode object is "equal to" this one
     *
     * @param gm the reference game mode with which to compare
     * @return true if this object is the same as the obj argument; false
     *         otherwise
     */
    public boolean equals(GameMode gm)
    {
        return this.irep == gm.irep;
    }
}
