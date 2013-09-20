package scar.object;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import scar.engine.ScarEngine;

public abstract class ScarObject
{
	public ScarEngine eng;
	private BufferedImage[] images;
	private BufferedImage image;
	private Area area;
	private int index = 0;
	private double xSpeed = 0;
	private double ySpeed = 0;
	private float alpha = 1f;
	private double degree = 0;
	private int rows = 0;
	private int cols = 0;
	private int skip = 0;
	private int x = 0;
	private int y = 0;
	private boolean drawSelf = true;
	public boolean remove = false;

	/**
	 * Public access to a real time reference of the engine
	 * @return
	 */
	public ScarEngine getEngine()		{	return eng;		}

	/**
	 * Public access to this objects x location
	 * @return This objects x location
	 */
	public int getX()				{	return x;			}

	/**
	 * Public access to this objects y location
	 * @return This objects y location
	 */
	public int getY()				{	return y;			}

	/**
	 * Public access to this objects x velocity
	 * @return This objects x velocity
	 */
	public double getXSpeed()			{	return xSpeed;		}

	/**
	 * Public access to this objects t velocity
	 * @return This objects t velocity
	 */
	public double getYSpeed()			{	return ySpeed;		}

	/**
	 * Public access to this objects alpha value
	 * @return This objects alpha value
	 */
	public float getAlpha()				{	return alpha;		}

	/**
	 * Public access to this objects rotational degree (0 - 359)
	 * @return This objects rotational degree
	 */
	public int getDegree()				{	return (int) degree;	}

	/**
	 * Public access to this objects bounding area
	 * @return This objects bounding area
	 */
	public Area getArea()			{	return area;	}

	/**
	 * Sets your degree to specified value. Negative values
	 * act the same as positive, any value exceeding 360 inclusive
	 * will round over to new degree
	 * @param degree Degree to be set
	 */
	public void setDegree(int degree)
	{
		if(degree > 360)
			this.degree = degree - 360;
		else if(degree < 0)
			this.degree = 360 - Math.abs(degree);
		else
			this.degree = Math.abs(degree);
	}

	/**
	 * Sets your alpha value (Essentially opaqueness) IE: 1f is regular image opaqueness
	 * @param alpha Alpha value to be set
	 */
	public void setAlpha(float alpha)
	{
		this.alpha = Math.abs(alpha);
		if(this.alpha > 1f)
			this.alpha = 1f;
	}

	/**
	 * Sets current x to given parameter
	 * @param x X location to be set
	 */
	public void setX(int x)
	{
		this.x = Math.abs(x);
	}

	/**
	 * Sets current y to given parameter
	 * @param y Y location to be set
	 */
	public void setY(int y)
	{
		this.y = Math.abs(y);
	}

	/**
	 * Rotates the object to a certain point
	 * @param xLoc X point
	 * @param yLoc Y point
	 */
	public void rotateTo(double xLoc, double yLoc)
	{
		int deg = (int) Math.toDegrees(Math.atan2(yLoc - (y + images[index].getHeight()/2), xLoc- (x + images[index].getWidth()/2)));
		degree = deg;
	}

	public void hideSelf()
	{
		drawSelf = false;
	}

	public void dbgPrint(String message)
	{
		eng.drawString(new ScarMessage("Arial", message, Color.WHITE, 15, 0, x, y));
	}

	public void setXSpeed(double xVel)
	{
		this.xSpeed = xVel;
	}

	public void setYSpeed(double yVel)
	{
		this.ySpeed = yVel;
	}

	/**
	 * Default object constructor
	 * @param engine Client engine reference
	 * @param x X location
	 * @param y Y location
	 */
	public ScarObject(ScarEngine engine, int x, int y)
	{
		this.eng = engine;
		this.x = x;
		this.y = y;
		area = new Area();
		engine.getObjects().add(this);
	}

	/**
	 * Set this objects image to the specified single row sprite
	 * @param image Sprite sheet to use
	 * @param width Width of a single image in sprite sheet
	 * @param wait Time to wait in between animations, -1 for single images
	 * @param rows Number of rows (images) in the sprite sheet
	 */
	public void setSprite(String image, int rows, int cols, int wait)	
	{
		try
		{
			this.image = ImageIO.read(new File(image));
		}catch(IOException e){e.printStackTrace();}
		this.rows = rows;
		this.cols = cols;
		this.skip = wait;
	}

	/**
	 * Method for frame to call, adds in other implementations
	 * besides the act() method
	 */
	public void doFrame()
	{
		if(!(""+xSpeed).equalsIgnoreCase("NaN"))
			x += xSpeed;
		if(!(""+ySpeed).equalsIgnoreCase("NaN"))
			y += ySpeed;
		act();
	}

	public void remove()
	{
		remove = true;
	}

	/**
	 * Public method for the frame to call so this object can draw itself
	 * @param g Graphics2D object to draw this object
	 */
	public void draw(Graphics2D g)
	{
		if(image != null)
		{
			int width = image.getWidth()/cols;
			int height = image.getHeight()/rows;
			AffineTransform at = new AffineTransform();
			at.setToTranslation(x,y);
			at.rotate(Math.toRadians(degree), width/2, height/2);
			area = new Area(at.createTransformedShape(new Rectangle(0,0,width,height)).getBounds2D());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
			images = new BufferedImage[rows * cols];
			for (int i = 0; i < cols; i++)
			{
				for (int j = 0; j < rows; j++)
				{
					images[(i * rows) + j] = image.getSubimage(i*width,j*height,width,height);
				}
			}
			if(drawSelf)
				g.drawImage(images[index],at,null);
			if(skip != -1)
			{
				if(eng.getTimer() % skip == 0)
					index += 1;
				if(index > images.length - 1)
					index = 0;
			}
		}
		drawSelf = true;
	}

	/**
	 * Default object acting method called every frame
	 */
	public abstract void act();

	/**
	 * Default method called when this object hits another,
	 * the other objects hit method is also called
	 * @param obj The object that hit you
	 */
	public abstract void hitObj(ScarObject obj);
}
