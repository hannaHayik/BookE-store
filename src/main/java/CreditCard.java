package jsonPojo;

public class CreditCard
{
    private String amount;

    private String number;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getNumber ()
    {
        return number;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", number = "+number+"]";
    }
}