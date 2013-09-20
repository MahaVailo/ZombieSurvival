package scar.frame;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import scar.engine.ScarEngine;

public class ScarFrame extends JFrame
{
	public BlinkPanel panel;
	public BufferedImage bgImage;
	public BufferedImage tiles;

	public ScarFrame(ScarEngine e, int width, int height, boolean fullScreen)
	{
		setLocation(0,0);
		setTitle("Scar Game");
		setUndecorated(fullScreen);
		setResizable(false);
		if(!fullScreen)
			setSize(width,height);
		else
			setExtendedState(MAXIMIZED_BOTH);
		panel = new BlinkPanel(e);
		add(panel);
		setVisible(true);
	}

	public void paint(Graphics g)
	{
		if(panel != null)
			panel.repaint();
	}

	public void setBGImage(String image)
	{
		try
		{
			bgImage = ImageIO.read(new File(image));
		}catch (IOException e){e.printStackTrace();}
	}

	public class BlinkPanel extends JPanel
	{
		private ScarEngine e;

		public BlinkPanel(ScarEngine e)
		{
			this.e = e;
			setFocusable(true);
			setLayout(null);
			try
			{
				Toolkit k = Toolkit.getDefaultToolkit();
				Cursor c = k.createCustomCursor(k.getImage("res/cursor.png"), new Point(0,0), "Cursor");
				setCursor(c);
				
			}catch(Exception ex){ex.printStackTrace();}
		}

		public synchronized void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			if(bgImage != null)
				g2d.drawImage(bgImage,0,0,getWidth(),getHeight(), null);
			e.draw(g2d);
			g2d.setTransform(new AffineTransform());
		}
	}
}