package jsonPojo;

public class Vehicles
{
    private String speed;

    private String license;

    public String getSpeed ()
    {
        return speed;
    }

    public void setSpeed (String speed)
    {
        this.speed = speed;
    }

    public String getLicense ()
    {
        return license;
    }

    public void setLicense (String license)
    {
        this.license = license;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [speed = "+speed+", license = "+license+"]";
    }
}