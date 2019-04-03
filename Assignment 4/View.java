import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.io.File;
import java.awt.Color;

import java.awt.Rectangle;
import java.util.*;
import java.util.Arrays;


class View extends JPanel
{

	Model model; //

	static Image[] mario_images;
	static Image background;

	View(Controller c, Model m) //lazy loading mario images
	{

			model = m;
			background = loadImage( "background1.png"); //loading background1.png

	}

	static Image loadImage(String filename) //lazy-loading images
	{
		Image im = null; //set to null on start

		try
		{
			im = ImageIO.read(new File(filename));
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return im;
	}
	
		public void paintComponent(Graphics g)
		{

			g.drawImage(background, (int)(( - model.mario.x + 500) * .2), 100 , null); // background

			g.setColor(Color.gray);
			g.drawLine(0, 500 ,2000, 500);


			for(int i = 0; i < model.sprites.size(); i++)
			{
				model.sprites.get(i).draw(g, model);
			}

		}
		
		void update() {
			repaint();

		}
}
