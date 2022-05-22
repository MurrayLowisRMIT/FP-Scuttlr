package scuttlr.application.controllers;

import scuttlr.application.model.Board;
import scuttlr.application.model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Reader
{

    public User loadUser(String fileAddress) throws IOException, ClassNotFoundException
    {
        User user;
        FileInputStream fileIn = new FileInputStream(fileAddress);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        user = (User) in.readObject();
        in.close();
        fileIn.close();
        return user;
    }

    public String loadQuote()
    {
        List<String> quotes = null;
        try
        {
            quotes = Files.readAllLines(Path.of("src/main/resources/scuttlr/application/documents/Quotes.txt"), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return quotes.get(new Random().nextInt(quotes.size()));
    }

    public Board loadBoard(String fileAddress) throws IOException, ClassNotFoundException
    {
        Board board;
        FileInputStream fileIn = new FileInputStream(fileAddress);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        board = (Board) in.readObject();
        in.close();
        fileIn.close();
        return board;
    }
}