package scar.object;
import java.awt.Color;

public class ScarOval
{
	public Color color;
	public int degree;
	public int width;
	public int height;
	public int x;
	public int y;
	public boolean filled;
	
	public ScarOval(Color color, int x, int y, int width, int height, int degree, boolean filled)
	{
		this.color = color;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.degree = degree;
		this.filled = filled;
	}
}
