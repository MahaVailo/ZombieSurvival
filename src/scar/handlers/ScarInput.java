package scar.handlers;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import scar.engine.ScarEngine;

public class ScarInput implements KeyListener, MouseListener
{
	public ArrayList<Character> pressed = new ArrayList<Character>();
	public ArrayList<Character> released = new ArrayList<Character>();
	public ArrayList<Character> cleared = new ArrayList<Character>();
	public boolean leftClicked = false;
	public boolean rightClicked = false;

	public void keyPressed(KeyEvent e)
	{
		char keyCode = e.getKeyChar();
		for(Character c: pressed)
		{
			if(keyCode == c)
				return;
		}
		for(Character c: cleared)
		{
			if(keyCode == c)
				return;
		}
		pressed.add(keyCode);
	}

	public void keyReleased(KeyEvent e)
	{
		char keyCode = e.getKeyChar();
		released.clear();
		released.add(keyCode);
		for(Character c: pressed)
		{
			if(keyCode == c)
			{
				pressed.remove(c);
				break;
			}
		}
		for(Character c: cleared)
		{
			if(keyCode == c)
			{
				cleared.remove(c);
				break;
			}
		}
	}

	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

	public boolean getKeyPressed(int key)
	{
		for(Character c: pressed)
		{
			if(key == c)
				return true;
		}
		return false;
	}

	public boolean getKeyReleased(int key)
	{
		for(Character c: released)
		{
			if(key == c)
				return true;
		}
		return false;
	}

	public void clearKey(int key)
	{
		for(Character c: pressed)
		{
			if(key == c)
			{
				pressed.remove(c);
				cleared.add((char) key);
				break;
			}
		}
	}
	
	public void clearAll()
	{
		leftClicked = false;
		rightClicked = false;
		released.clear();
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			leftClicked = true;
		if(e.getButton() == MouseEvent.BUTTON3)
			rightClicked = true;
	}

	public void mouseReleased(MouseEvent e)
	{

	}
}