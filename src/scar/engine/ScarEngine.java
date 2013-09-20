package scar.engine;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import scar.frame.ScarFrame;
import scar.handlers.ScarInput;
import scar.object.ScarButton;
import scar.object.ScarImage;
import scar.object.ScarMessage;
import scar.object.ScarObject;
import scar.object.ScarOval;
import scar.object.ScarRectangle;
import scar.sound.ScarSound;

/**
 * @author Zachariah T. Dzielinski
 */
public abstract class ScarEngine implements ScarInterface, Runnable
{	
	private ArrayList<ScarObject> objs = new ArrayList<ScarObject>();
	private ArrayList<Object> draws = new ArrayList<Object>();
	private ArrayList<ScarButton> buttons = new ArrayList<ScarButton>();
	private boolean drawObjs;
	private boolean drawStrings;
	private boolean drawOvals;
	private boolean drawRects;
	private boolean drawImages;
	private ScarFrame frame;
	private ScarInput input;
	private ScarSound player;
	private String state;
	private long timer;
	private int fps;


	/**
	 * Constructor creating a frame with specified width and height
	 * @param width Frame width
	 * @param height Frame height
	 */
	public ScarEngine(int width, int height, int fps, boolean fullScreen)
	{
		System.err.print("Successful: ScarEngine Class Initiated.\n");
		input = new ScarInput();
		player = new ScarSound();
		frame = new ScarFrame(this, width, height, fullScreen);
		frame.panel.addKeyListener(input);
		frame.panel.addMouseListener(input);
		this.fps = fps;
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * Public access to the engines timer
	 * @return The engines timer
	 */
	public long getTimer()						{				return timer;			}

	/**
	 * Public access to the engines Frames Per Second
	 * @return Frames Per Second
	 */
	public int getFPS()							{				return fps;				}
	/**
	 * Public access to all of the objects in the game
	 * @return All of the objects in the game
	 */
	public ArrayList<ScarObject> getObjects()	{				return objs;			}

	/**
	 * Public access to all of the buttons in the game
	 * @return All of the buttons in the game
	 */
	public ArrayList<ScarButton> getButtons()	{				return buttons;			}

	/**
	 * Public access to the games frame
	 * @return The games frame
	 */
	public ScarFrame getFrame()					{				return frame;			}

	/**
	 * Method to set the background image of the frame
	 * @param image Location of the image to use
	 */
	public void setBGImage(String image)		{			frame.setBGImage(image);	}

	/**
	 * Sets the background color to the parameter
	 * @param color The color to set
	 */
	public void setBGColor(Color color)			{	frame.panel.setBackground(color);	}

	/**
	 * Void to set the games gameState
	 * @param state the gameState to call
	 */
	public void setGameState(String state)		{	this.state = "doFrame" + state;		}

	/**
	 * Method to check if this key has been pressed
	 * @param key Key to check
	 * @return True or false
	 */
	public boolean getKeyPressed(int key)		{	return input.getKeyPressed(key);	}

	/**
	 * Method to check if this key has been released
	 * @param key Key to check
	 * @return True or false
	 */
	public boolean getKeyReleased(int key)		{	return input.getKeyReleased(key);	}

	/**
	 * Returns the mouse point
	 * @return The mouse point
	 */
	public Point getMouse()		{	return MouseInfo.getPointerInfo().getLocation();	}

	/**
	 * Returns if the mouse was right clicked
	 * @return A boolean
	 */
	public boolean rightClick()
	{
		return input.rightClicked;
	}

	/**
	 * Returns if the mouse was left clicked
	 * @return A boolean
	 */
	public boolean leftClick()
	{
		return input.leftClicked;
	}

	/**
	 * Returns the mouse x position
	 * @return A double
	 */
	public double mouseX()
	{
		return getMouse().getX() - frame.getX();
	}

	/**
	 * Returns the mouse y position
	 * @return A double
	 */
	public double mouseY()
	{
		return getMouse().getY() - frame.getY();
	}

	/**
	 * Method to clear a key if it is being held down
	 * @param key
	 */
	public void clearKey(int key)				{			input.clearKey(key);		}

	/**
	 * All objects doFrames are called but they are not drawn
	 */
	public void hideObjects()					{			drawObjs= false;			}

	/**
	 * Hides all messages
	 */
	public void hideStrings()					{			drawStrings= false;			}

	/**
	 * Hides all ovals
	 */
	public void hideOvals()						{			drawOvals= false;			}

	/**
	 * Hides all rectangles
	 */
	public void hideRects()						{			drawRects= false;			}

	/**
	 * Hides all images
	 */
	public void hideImages()					{			drawImages= false;			}

	/**
	 * Shows all objects, images, etc
	 */
	public void showAll()
	{
		drawObjs = true;
		drawStrings = true;
		drawOvals = true;
		drawRects = true;
		drawImages = true;
	}

	/**
	 * Hides all objects, images, etc
	 */
	public void hideAll()
	{
		drawObjs = false;
		drawStrings = false;
		drawOvals = false;
		drawRects = false;
		drawImages = false;
	}

	/**
	 * Method to draw a message to the frame
	 * @param message Message to draw
	 */
	public synchronized void drawString(ScarMessage message)
	{
		draws.add(message);
	}

	/**
	 * Method to draw a rectangle to the frame
	 * @param message Rectangle to draw
	 */
	public synchronized void drawRect(ScarRectangle rectangle)
	{
		draws.add(rectangle);
	}

	/**
	 * Method to draw an oval to the frame
	 * @param message Oval to draw
	 */
	public synchronized void drawOval(ScarOval oval)
	{
		draws.add(oval);
	}

	/**
	 * Method to draw an oval to the frame
	 * @param message Oval to draw
	 */
	public synchronized void drawImage(ScarImage image)
	{
		draws.add(image);
	}

	/**
	 * Call each objects act method
	 */
	public synchronized void moveObjects()
	{
		for(ScarObject o: objs)
			o.doFrame();
	}

	public void playSound(String sound)
	{
		player.playSound(sound);
	}

	public void stopSound(String sound)
	{
		player.stop(sound);
	}

	/**
	 * This engines main game loop
	 */
	private void gameLoop()
	{
		if(getKeyPressed(KeyEvent.VK_ESCAPE))
			frame.dispose();
		timer++;
		showAll();
		try
		{
			getClass().getMethod("doFrame").invoke(this);
			if(state != null)
				getClass().getMethod(state).invoke(this);
		}
		catch (IllegalArgumentException e){}
		catch (SecurityException e){}
		catch (IllegalAccessException e){}
		catch (InvocationTargetException e){}
		catch (NoSuchMethodException e){}
		frame.repaint();
		checkCollisions();
		input.clearAll();
		player.clear();
	}

	/**
	 * Method to check collisions between all of the objects
	 */
	private synchronized void checkCollisions()
	{
		for(int x = objs.size()-1; x >= 0; x--)
		{
			if(objs.get(x).remove)
				objs.remove(x);
		}
		for(ScarObject a: objs)
		{
			for(ScarObject b: objs)
			{
				if(a != b)
				{
					if(a.getArea().intersects(b.getArea().getBounds2D()))
					{
						a.hitObj(b);
						b.hitObj(a);
					}
				}
			}
		}
	}

	/**
	 * Method for the frame to call so the engine can draw things
	 * @param g The graphics object to draw things
	 */
	public synchronized void draw(Graphics2D g)
	{
		Graphics2D g2d = g;
		if(drawObjs)
		{
			for(ScarObject o: objs)
			{
				o.draw(g2d);
			}
		}
		for(Object o: draws)
		{
			if(o instanceof ScarMessage && drawStrings)
			{
				ScarMessage m = (ScarMessage) o;
				g2d.setPaint(m.color);
				g2d.setFont(new Font(m.font,0,m.size));
				double width = g2d.getFontMetrics().getStringBounds(m.message,g2d).getWidth();
				double height = g2d.getFontMetrics().getStringBounds(m.message,g2d).getHeight();
				AffineTransform at = new AffineTransform();
				at.rotate(m.degree,(width/2) + m.x,(height/2) + m.y);
				g2d.setTransform(at);
				g2d.drawString(m.message,m.x,m.y);
			}
			else if(o instanceof ScarRectangle && drawRects)
			{
				ScarRectangle r = (ScarRectangle) o;
				g2d.setPaint(r.color);
				AffineTransform at = new AffineTransform();
				at.rotate(r.degree,(r.width/2) + r.x,(r.height/2) + r.y);
				g2d.setTransform(at);
				if(r.filled)
					g2d.fillRect(r.x,r.y,r.width,r.height);
				else
					g2d.drawRect(r.x,r.y,r.width,r.height);
			}
			else if(o instanceof ScarOval && drawOvals)
			{
				ScarOval v = (ScarOval) o;
				g2d.setPaint(v.color);
				AffineTransform at = new AffineTransform();
				at.rotate(v.degree,(v.width/2) + v.x,(v.height/2) + v.y);
				g2d.setTransform(at);
				if(v.filled)
					g2d.fillOval(v.x,v.y,v.width,v.height);
				else
					g2d.drawOval(v.x,v.y,v.width,v.height);
			}
			else if(o instanceof ScarImage && drawImages)
			{
				ScarImage i = (ScarImage) o;
				AffineTransform at = new AffineTransform();
				at.rotate(i.degree,(i.image.getWidth()/2) + i.x,(i.image.getHeight()/2) + i.y);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,i.alpha));
				g2d.setTransform(at);
				if(i.scaled)
					g2d.drawImage(i.image,i.x,i.y,frame.getWidth(),frame.getHeight(),Color.black,null);
				else
					g2d.drawImage(i.image,at,null);
			}
		}
		draws.clear();
		g2d.dispose();
	}

	/**
	 * Implementation unnecessary
	 */
	public void run()
	{
		while(frame.isVisible())
		{
			long time = System.currentTimeMillis();
			gameLoop();
			time = (1000/fps) - (System.currentTimeMillis() - time);
			if(time > 0)
			{
				try
				{
					Thread.sleep(time);
				}catch(Exception e){}
			}
		}
		exit();
	}

	/**
	 * Built in method to safely exit the game
	 */
	public void exit()
	{
		System.err.print("Successful: ScarEngine Class Terminated.");
		player.stopAll();
		frame.dispose();
	}

	/**
	 * Default doFrame method called every frame no
	 * matter what game state you are in
	 */
	public abstract void doFrame();
}