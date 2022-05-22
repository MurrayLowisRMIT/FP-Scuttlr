package scuttlr.application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Quotes implements Serializable
{
    private ArrayList<String> quotes;

    public void addQuote(String quote)
    {
        this.quotes.add(quote);
    }

    public String getRandomQuote()
    {
        // TODO randomise
        return this.quotes.get(0);
    }

    // TODO getters/setters
}
