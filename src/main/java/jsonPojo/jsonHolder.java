package jsonPojo;

public class jsonHolder {
	private Services services;

    private InitialResources[] initialResources;

    private InitialInventory[] initialInventory;

    public Services getServices ()
    {
        return services;
    }

    public void setServices (Services services)
    {
        this.services = services;
    }

    public InitialResources[] getInitialResources ()
    {
        return initialResources;
    }

    public void setInitialResources (InitialResources[] initialResources)
    {
        this.initialResources = initialResources;
    }

    public InitialInventory[] getInitialInventory ()
    {
        return initialInventory;
    }

    public void setInitialInventory (InitialInventory[] initialInventory)
    {
        this.initialInventory = initialInventory;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [services = "+services+", initialResources = "+initialResources+", initialInventory = "+initialInventory+"]";
    }
}
