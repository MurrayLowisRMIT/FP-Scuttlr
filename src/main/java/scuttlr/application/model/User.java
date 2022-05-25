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
            updateUserBoards(loadedBoards);
            success = true;

            // remove old user save file
            File file = new File("src/main/resources/scuttlr/application/accounts/" + oldUsername + ".ser");
            file.delete();

            // delete board save files associated with old username
            for (int i = 0; i < loadedBoards.size(); i++)
            {
                file = new File("src/main/resources/scuttlr/application/boards/" + oldUsername + "_" + loadedBoards.get(i).getBoardName() + ".ser");
                file.delete();
            }
        }
        return success;
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
        updateUser();
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

    public void setCurrentBoard(String boardName)
    {
        this.currentBoard = boardName;
    }

    // creating board here ensures password is added securely
    public Board createBoard(Board board)
    {
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
        updateUser();
        return board;
    }

    public void updateUser()
    {
        Writer writer = new Writer();
        writer.saveUser();
    }

    // create new board save files using new username
    public void updateUserBoards(LinkedList<Board> oldBoards)
    {
        LinkedList<Board> newBoards = new LinkedList<>();
        Writer writer = new Writer();
        // copy data from old boards and write new boards with new data
        for (int i = 0; i < oldBoards.size(); i++)
        {
            Board board = createBoard(new Board(oldBoards.get(i).getBoardName(), null));
            board.setColumns(oldBoards.get(i).getColumns());
            writer.saveBoard(board);
        }
    }
}
