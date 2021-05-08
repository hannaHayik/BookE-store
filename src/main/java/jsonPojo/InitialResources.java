package jsonPojo;

public class InitialResources
{
    private Vehicles[] vehicles;

    public Vehicles[] getVehicles ()
    {
        return vehicles;
    }

    public void setVehicles (Vehicles[] vehicles)
    {
        this.vehicles = vehicles;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vehicles = "+vehicles+"]";
    }
}