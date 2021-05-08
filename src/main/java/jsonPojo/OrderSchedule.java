package jsonPojo;

public class OrderSchedule
{
    private String tick;

    private String bookTitle;

    public String getTick ()
    {
        return tick;
    }

    public void setTick (String tick)
    {
        this.tick = tick;
    }

    public String getBookTitle ()
    {
        return bookTitle;
    }

    public void setBookTitle (String bookTitle)
    {
        this.bookTitle = bookTitle;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tick = "+tick+", bookTitle = "+bookTitle+"]";
    }
}