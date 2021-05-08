package jsonPojo;

public class Services
{
    private String inventoryService;

    private Time time;

    private String selling;

    private Customers[] customers;

    private String logistics;

    private String resourcesService;

    public String getInventoryService ()
    {
        return inventoryService;
    }

    public void setInventoryService (String inventoryService)
    {
        this.inventoryService = inventoryService;
    }

    public Time getTime ()
    {
        return time;
    }

    public void setTime (Time time)
    {
        this.time = time;
    }

    public String getSelling ()
    {
        return selling;
    }

    public void setSelling (String selling)
    {
        this.selling = selling;
    }

    public Customers[] getCustomers ()
    {
        return customers;
    }

    public void setCustomers (Customers[] customers)
    {
        this.customers = customers;
    }

    public String getLogistics ()
    {
        return logistics;
    }

    public void setLogistics (String logistics)
    {
        this.logistics = logistics;
    }

    public String getResourcesService ()
    {
        return resourcesService;
    }

    public void setResourcesService (String resourcesService)
    {
        this.resourcesService = resourcesService;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [inventoryService = "+inventoryService+", time = "+time+", selling = "+selling+", customers = "+customers+", logistics = "+logistics+", resourcesService = "+resourcesService+"]";
    }
}