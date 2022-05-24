package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable
{
    private String username;
    // TODO should probably hash this or something so it's not readable in the serialized save file?
    // password is never returned, only checked
    private String password;
    // avatar stored as a byte array in the serialized save file
    private byte[] avatarData;
    // list of names of boards owned by the user
    private LinkedList<String> userBoardNames;
    // current board - this is the board loaded when logging in
    private String currentBoard;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.userBoardNames = new LinkedList<String>();
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean checkPassword(String password)
    {
        boolean confirmPassword = false;
        if (password.matches(this.password))
        {
            confirmPassword = true;
        }
        return confirmPassword;
    }

    public String getUsername()
    {
        return this.username;
    }

    public byte[] getAvatar()
    {
        return this.avatarData;
    }

    public void setAvatar(byte[] avatarData)
    {
        this.avatarData = avatarData;
    }

    // get the names of all boards owned by user
    public LinkedList<String> getUserBoardNames()
    {
        return this.userBoardNames;
    }

    // adds name of board to list of boards owned by user
    public void addToUserBoards(String boardName)
    {
        this.userBoardNames.add(boardName);
    }

    public void setCurrentBoard(String boardName)
    {
        this.currentBoard = boardName;
    }

    // creating board here ensures password is added securely
    // kinda pointless since the serialized object is still partially human-readable anyway
    public Board createBoard(String boardName)
    {
        Board board = new Board(boardName, this.password);
        addToUserBoards(boardName);
        return board;
    }
}
