package scar.object;
import java.awt.Color;

public class ScarMessage
{
	public String message;
	public String font;
	public Color color;
	public int size;
	public int x;
	public int y;
	public int degree;
	
	public ScarMessage(String font, String message, Color color, int size, int degree, int x, int y)
	{
		this.font = font;
		this.message = message;
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.degree = degree;
	}
}