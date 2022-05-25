package scuttlr.application.model;

import scuttlr.application.controllers.Writer;

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
        saveUpdates();
    }

    // get the names of all boards owned by user
    public LinkedList<String> getUserBoardNames()
    {
        return this.userBoardNames;
    }

    // adds name of board to list of boards owned by user
    public void addBoardToUser(String boardName)
    {
        this.userBoardNames.add(boardName);
        saveUpdates();
    }

    public void removeBoardFromUser(String boardName)
    {
        for (int i = 0; i < this.userBoardNames.size(); i++)
        {
            if (this.userBoardNames.get(i).matches(boardName))
            {
                this.userBoardNames.remove(i);
                saveUpdates();
            }
        }
    }

    public void setCurrentBoard(String boardName)
    {
        this.currentBoard = boardName;
    }

    // creating board here ensures password is added securely
    //    public Board createBoard(String boardName)
    //    {
    //        Board board = new Board(boardName, this.password);
    //        addToUserBoards(boardName);
    //        return board;
    //    }
    public Board createBoard(Board board)
    {
        for (int i = 0; i < this.userBoardNames.size(); i++)
        {
            System.out.println(this.userBoardNames.get(i));
        }

        // overwrite duplicate if applicable
        for (int i = 0; i < this.getUserBoardNames().size(); i++)
        {
            if (board.getBoardName().matches(this.getUserBoardNames().get(i)))
            {
                this.getUserBoardNames().remove(i);
            }
        }
        Board newBoard = new Board(board.getBoardName(), this.password);
        newBoard.setColumns(board.getColumns());
        addBoardToUser(board.getBoardName());
        saveUpdates();
        return board;
    }

    public void saveUpdates()
    {
        Writer writer = new Writer();
        writer.saveUser();
    }
}
