import com.badlogic.gdx.ApplicationAdapter; 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle; 
import com.badlogic.gdx.math.Circle; 
import com.badlogic.gdx.Input.Keys; 
import com.badlogic.gdx.math.Vector2; 
import com.badlogic.gdx.math.MathUtils; 
import com.badlogic.gdx.math.Intersector; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.InputProcessor; 
import com.badlogic.gdx.*; 
import com.badlogic.gdx.utils.Array;  
import java.util.*; 
import com.badlogic.gdx.math.Polygon;

public class RPG extends ApplicationAdapter 
{
    private OrthographicCamera camera; //the camera to our world
    private Viewport viewport; //maintains the ratios of your world

    //These are all needed to draw text on the screeeeeeen!!!!!
    private SpriteBatch batch; 
    private BitmapFont font; 
    private GlyphLayout layout; 
    private ShapeRenderer renderer;
    private Character stubby;

    private List<Polygon> bullets;
    private List<Float> bulletDirections;

    @Override//this is called once when you first run your program
    public void create(){       
        camera = new OrthographicCamera(); 
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera); 

        batch = new SpriteBatch(); 
        layout = new GlyphLayout(); 
        font = new BitmapFont();
        renderer = new ShapeRenderer();

        stubby = new Character(10, new Color(0, 0, 255f, 0), 5, 0);
        bullets = new ArrayList<Polygon>();
        bulletDirections = new ArrayList<Float>();
    }

    @Override//this is called 60 times a second
    public void render(){

        Vector2 mouseLoc = viewport.unproject(new Vector2(Gdx.input.getX(),Gdx.input.getY()));
        float mouseX = mouseLoc.x;
        float mouseY = mouseLoc.y;
        //these two lines wipe and reset the screen so when something action had happened
        //the screen won't have overlapping images
        Gdx.gl.glClearColor(255f, 255f, 255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        faceObject(stubby, mouseX, mouseY);

        //Movement of stubby
        if(Gdx.input.isKeyPressed(Keys.W))
        {
            stubby.setY(stubby.getY() + stubby.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Keys.A))
        {
            stubby.setX(stubby.getX() - stubby.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Keys.S))
        {
            stubby.setY(stubby.getY() - stubby.getSpeed());
        }
        if(Gdx.input.isKeyPressed(Keys.D))
        {
            stubby.setX(stubby.getX() + stubby.getSpeed());
        }

        renderer.setAutoShapeType(true);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        //Stubby shoots so bullet is added to array
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
        {
            shoot(stubby);
            System.out.println(bullets.size());
        }

        //bullet rendering
        for(int i = bullets.size() - 1; i >= 0; i--)
        {
            System.out.println(bullets.get(i).getX() + " " + bullets.get(i).getY());
            if(bullets.get(i).getX() < Constants.WORLD_WIDTH && bullets.get(i).getX() > 0 && 
                bullets.get(i).getY() < Constants.WORLD_HEIGHT && bullets.get(i).getY() > 0)
            {
                //Renders rectangle with at angle of shot
                renderer.rect(bullets.get(i).getX(), bullets.get(i).getY(), 0, 0, 
                    Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT, 
                    1, 1, (180 * bulletDirections.get(i)) / (float)Math.PI);
                
                //Changes position of rectangle
                bullets.get(i).setPosition(
                    bullets.get(i).getX() + Constants.BULLET_SPEED * MathUtils.cos(bulletDirections.get(i)),
                    bullets.get(i).getY() + Constants.BULLET_SPEED * MathUtils.sin(bulletDirections.get(i))
                );
            }
            else
            {
                bullets.remove(i);
                bulletDirections.remove(i);
            }
        }

        renderer.setColor(stubby.getColor());
        renderer.line(stubby.getX(), stubby.getY(), mouseX, mouseY);
        renderer.circle(stubby.getX(), stubby.getY(), stubby.getSize());

        renderer.end();
        
        //Shows angle of direction in radians
        // batch.begin();
        // font.setColor(1f, 0f, 0f, 1f);
        // layout.setText(font, stubby.getDirection() + "");
        // font.draw(batch,
            // layout, 
            // Constants.WORLD_WIDTH / 2 - layout.width / 2, 
            // Constants.WORLD_HEIGHT / 2 + layout.height / 2);
        // batch.end();
    }

    public void faceObject(Character sprite, float objectX, float objectY)
    {
        float x = objectX - stubby.getX();
        float y = objectY - stubby.getY();
        float angle = MathUtils.atan2(y, x);

        sprite.setDirection(angle);
    }
    
    public void shoot(Character sprite)
    {
        //Dimensions of bullet
        float[] dim = {
            sprite.getX(), sprite.getY(),
            sprite.getX(), sprite.getY() + Constants.BULLET_HEIGHT,
            sprite.getX() + Constants.BULLET_WIDTH, sprite.getY() + Constants.BULLET_HEIGHT,
            sprite.getX() + Constants.BULLET_WIDTH, sprite.getY()
        };
        
        //Adjust bullet
        Polygon bullet = new Polygon(dim);
        bullet.setRotation((180 * stubby.getDirection()) / (float)Math.PI);
        //Adjust to center of line
        bullet.setPosition(
            sprite.getX() + MathUtils.sin(sprite.getDirection()) * (0.5f * Constants.BULLET_HEIGHT), 
            sprite.getY() - MathUtils.cos(sprite.getDirection()) * (0.5f * Constants.BULLET_HEIGHT)
        );
        
        bullets.add(bullet);
        bulletDirections.add(sprite.getDirection());
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose()
    {
        batch.dispose();
    }
}
