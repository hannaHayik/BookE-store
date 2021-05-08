package jsonPojo;

public class InitialInventory
{
    private String amount;

    private String bookTitle;

    private String price;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getBookTitle ()
    {
        return bookTitle;
    }

    public void setBookTitle (String bookTitle)
    {
        this.bookTitle = bookTitle;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", bookTitle = "+bookTitle+", price = "+price+"]";
    }
}
