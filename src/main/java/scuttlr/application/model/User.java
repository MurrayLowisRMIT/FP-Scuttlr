package scuttlr.application.model;

import scuttlr.application.controllers.Writer;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

import static scuttlr.application.Main.userController;

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

    // most recently active board
    private String currentBoardName;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.userBoardNames = new LinkedList<>();
    }

    public String getCurrentBoardName()
    {
        return this.currentBoardName;
    }

    public boolean checkPassword(String password)
    {
        return password.matches(this.password);
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
        updateUser();
    }

    public boolean updateUsername(String username, LinkedList<Board> loadedBoards)
    {
        boolean success = false;
        if (userController.checkUsernameAvailable(username))
        {
            // update username
            String oldUsername = this.username;
            this.username = username;
            updateUser();
            reconstructUserBoards(loadedBoards);
            success = true;

            // remove old user save file
            File file = new File("src/main/resources/scuttlr/application/accounts/" + oldUsername + ".ser");
            file.delete();

            // delete board save files associated with old username
            for (Board loadedBoard : loadedBoards)
            {
                file = new File("src/main/resources/scuttlr/application/boards/" + oldUsername + "_" + loadedBoard.getBoardName() + ".ser");
                file.delete();
            }
        }
        return success;
    }

    public boolean updateBoardName(Board oldBoard, String newBoardName)
    {
        // skip if an existing file already has the name
        boolean success = false;
        if (!this.userBoardNames.contains(newBoardName))
        {
            // delete old board save file
            for (int i = 0; i < this.userBoardNames.size(); i++)
            {
                if (this.userBoardNames.get(i).matches(oldBoard.getBoardName()))
                {
                    this.userBoardNames.remove(i);
                }
            }
            File file = new File("src/main/resources/scuttlr/application/boards/" + this.username + "_" + oldBoard.getBoardName() + ".ser");
            file.delete();

            // create new board with new name
            Board board = new Board(newBoardName, null);
            board.setColumns(oldBoard.getColumns());
            Writer writer = new Writer();
            writer.saveBoard(board);
            success = true;
        }
        return success;
    }

    // get the names of all boards owned by user
    public LinkedList<String> getUserBoardNames()
    {
        return this.userBoardNames;
    }

    public void removeBoardFromUser(String boardName)
    {
        for (int i = 0; i < this.userBoardNames.size(); i++)
        {
            if (this.userBoardNames.get(i).matches(boardName))
            {
                this.userBoardNames.remove(i);
                updateUser();
            }
        }
    }

    // creating board here ensures password is added securely
    public Board createBoard(Board board)
    {
        Board newBoard = new Board(board.getBoardName(), this.password);
        newBoard.setColumns(board.getColumns());
        // overwrite duplicate if applicable
        for (int i = 0; i < this.getUserBoardNames().size(); i++)
        {
            if (board.getBoardName().matches(this.getUserBoardNames().get(i)))
            {
                this.getUserBoardNames().remove(i);
            }
        }
        this.userBoardNames.add(newBoard.getBoardName());
        this.currentBoardName = newBoard.getBoardName();
        updateUser();
        return newBoard;
    }

    public void updateUser()
    {
        Writer writer = new Writer();
        writer.saveUser();
    }

    // create new board save files using new username
    public void reconstructUserBoards(LinkedList<Board> oldBoards)
    {
        Writer writer = new Writer();
        // copy data from old boards and write new boards with new data
        for (Board oldBoard : oldBoards)
        {
            Board board = new Board(oldBoard.getBoardName(), null);
            board.setColumns(oldBoard.getColumns());
            writer.saveBoard(board);
        }
    }
}
