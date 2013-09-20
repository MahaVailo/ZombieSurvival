package scar.object;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScarImage
{
	public BufferedImage image;
	public boolean scaled;
	public int x;
	public int y;
	public int degree;
	public float alpha;
	
	public ScarImage(String image, int x, int y, int degree, float alpha, boolean scaled)
	{
		try
		{
			this.image = ImageIO.read(new File(image));
		}catch (IOException e){e.printStackTrace();}
		this.scaled = scaled;
		this.x = x;
		this.y = y;
		this.degree = degree;
		this.alpha = alpha;
	}
}
