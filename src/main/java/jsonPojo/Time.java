package jsonPojo;

public class Time
{
    private String duration;

    private String speed;

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public String getSpeed ()
    {
        return speed;
    }

    public void setSpeed (String speed)
    {
        this.speed = speed;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [duration = "+duration+", speed = "+speed+"]";
    }
}
