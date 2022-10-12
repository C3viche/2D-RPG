import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.graphics.Color;

public class Character
{
    private int size;
    private Color color;
    private int speed;
    private float direction;
    private float posX;
    private float posY;
    
    public Character(int size, Color color, int speed, float direction, float x, float y)
    {
        this.size = size;
        this.color = color;
        this.speed = speed;
        this.direction = direction;
        this.posX = x;
        this.posY = y;
    }
    
    public Character(int size, Color color, int speed, float direction)
    {
        this.size = size;
        this.color = color;
        this.speed = speed;
        this.direction = direction;
        this.posX = 90;
        this.posY = 90;
    }
    
    //Getters
    public int getSize()
    {
        return size;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public int getSpeed()
    {
        return speed;
    }
    
    public float getDirection()
    {
        return direction;
    }
    
    public float getX()
    {
        return posX;    
    }
    
    public float getY()
    {
        return posY;
    }
    
    //Setters
    public void setSize(int newSize)
    {
        size = newSize;
    }
    
    public void setColor(Color col)
    {
        color = col;
    }
    
    public void setSpeed(int newSpeed)
    {
        speed = newSpeed;
    }
    
    public void setDirection(float newDirection)
    {
        direction = newDirection;
    }
    
    public void setX(float x)
    {
        posX = x;
    }
    
    public void setY(float y)
    {
        posY = y;
    }
}
