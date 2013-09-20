package scar.sound;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import javazoom.jl.player.Player;

public class ScarSound
{
	private ArrayList<Player> sounds = new ArrayList<Player>();
	private ArrayList<String> names	= new ArrayList<String>();

	public void playSound(final String sound)
	{
		try
		{
			FileInputStream fis = new FileInputStream(sound);
			BufferedInputStream bis = new BufferedInputStream(fis);
			sounds.add(new Player(bis));
			names.add(sound);
			new Thread()
			{
				public void run()
				{
					try
					{
						sounds.get(sounds.size()-1).play();
					}catch(Exception ex){ex.printStackTrace();}
				}
			}.start();
		}catch(Exception e){e.printStackTrace();}
	}

	public synchronized void stop(String audio)
	{
		for(int x = 0; x < names.size(); x++)
		{
			if(names.get(x).equalsIgnoreCase(audio))
			{
				names.remove(x);
				sounds.remove(x);
			}
		}
	}

	public synchronized void clear()
	{
		for(int x = sounds.size()-1; x >= 0; x--)
		{
			if(sounds.get(x).isComplete())
			{
				sounds.remove(x);
				names.remove(x);
			}
		}
	}
	
	public void stopAll()
	{
		for(Player p: sounds)
		{
			p.close();
		}
	}
}