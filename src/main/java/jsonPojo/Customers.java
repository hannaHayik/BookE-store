package jsonPojo;

public class Customers
{
    private CreditCard creditCard;

    private String id;

    private String distance;

    private String address;

    private String name;

    private OrderSchedule[] orderSchedule;

    public CreditCard getCreditCard ()
    {
        return creditCard;
    }

    public void setCreditCard (CreditCard creditCard)
    {
        this.creditCard = creditCard;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public OrderSchedule[] getOrderSchedule ()
    {
        return orderSchedule;
    }

    public void setOrderSchedule (OrderSchedule[] orderSchedule)
    {
        this.orderSchedule = orderSchedule;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [creditCard = "+creditCard+", id = "+id+", distance = "+distance+", address = "+address+", name = "+name+", orderSchedule = "+orderSchedule+"]";
    }
}