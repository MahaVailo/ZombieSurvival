package scar.object;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import scar.engine.ScarEngine;

public abstract class ScarButton
{
	private BufferedImage reg;
	private BufferedImage hover;
	private BufferedImage clicked;
	private ScarEngine e;
	public ScarButton(ScarEngine e, String reg, String hover, String clicked)
	{
		this.e = e;
		try
		{
			this.reg = ImageIO.read(new File(reg));
			this.hover = ImageIO.read(new File(hover));
			this.clicked = ImageIO.read(new File(clicked));
		}catch(Exception ex){ex.printStackTrace();}
		e.getButtons().add(this);
	}
	
	public abstract void clicked();
}