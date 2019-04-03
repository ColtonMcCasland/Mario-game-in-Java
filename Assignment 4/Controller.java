import java.awt.event.MouseListener;

import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import java.awt.Robot;

class Controller implements ActionListener, MouseListener, KeyListener
{
	//objects
	View view; 
	Model model;
	
	//bools
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keyS; // marshal call
	boolean keyL; //unmarshal call
	boolean keySpace;
	boolean keyC;


	//ints
	int x1, y1, x2, y2, w, h ;
	int left, right, top, bottom;

	int blockHitCount;

	int jumpCount;


	//loads model
	Controller(Model m)
	{
		model = m;
	}


	void setView(View v)
		{
			view = v;
		}

		
	public void actionPerformed(ActionEvent e)
	{
		view.update();
	}

		//need to implement way to detect "click" , "drag", and "release", to get start -> end coordinates for "shape"
		public void mouseEntered(MouseEvent e) {    }
		public void mouseExited(MouseEvent e) {    }

		public void mouseClicked(MouseEvent e)
		{
			if(e.getY() < 100)
			{
				System.out.println("break here");
			}
		}

	public void mousePressed(MouseEvent e)
	{

//		System.out.println("mouse pressed");
//
		x1 = e.getX();
		y1 = e.getY();
//
//		System.out.println(x1);	// see int values
//		System.out.println(y1);

	}

	public void mouseReleased(MouseEvent e)
	{

//		System.out.println("mouse released");

		x2 = e.getX();	// other set of points
		y2 = e.getY();
//
//		System.out.println(x2);	// see int values
//		System.out.println(y2);


		w = Math.abs(x2 - x1);// helps make consistant sized rectangles each time
		h = Math.abs(y2 - y1);

		left = Math.min(x1,x2); // calculation for Left, Right, Top ,Bottom
		right = Math.max(x1,x2);
		top = Math.min(y1,y2);
		bottom = Math.max(y1,y2);

		model.makeBrick(left + (model.mario.x - 200), top, right - left, bottom - top);

	}

	
		public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case KeyEvent.VK_RIGHT: keyRight = true; break;
					case KeyEvent.VK_LEFT: keyLeft = true; break;
					case KeyEvent.VK_UP: keyUp = true; break;
					case KeyEvent.VK_DOWN: keyDown = true; break;
					case KeyEvent.VK_SPACE: keySpace = true; break;
					case KeyEvent.VK_S: keyS = true;  model.save("map.json"); System.out.println("Saving Current objects on screen to file.");	break; // press "s" to save .Json
					case KeyEvent.VK_L: keyL = true;  model.load("map.json");  System.out.println("Loading objects from file.");  break; // presses "l" to load .Json
					case KeyEvent.VK_C: keyC = true; break;
				}
			}

			public void keyReleased(KeyEvent e)
			{
				switch(e.getKeyCode())
				{
					case KeyEvent.VK_RIGHT: keyRight = false; break;
					case KeyEvent.VK_LEFT: keyLeft = false; break;
					case KeyEvent.VK_UP: keyUp = false; break;
					case KeyEvent.VK_DOWN: keyDown = false; break;
					case KeyEvent.VK_SPACE: keySpace = false; break;
				}
			}

			public void keyTyped(KeyEvent e) {}

			void update()
			{
//				model.update();

				model.curr_state();

				//model.mario.jumpCount = 0; //DO NOT TURN ON OTHERWISE JUMP NEVER INCRAMENTS PROPERLY

				double score_runRight = model.evaluateAction(Model.Action.runRight, 0);

				double score_runJump = model.evaluateAction(Model.Action.runJump, 0);

				double score_jump = model.evaluateAction(Model.Action.jump, 0);

			//	double score_wait = model.evaluateAction(Model.Action.wait, 0);

//
//				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
////
				System.out.println("runRight" + score_runRight);

				System.out.println("runJump" + score_runJump);

				System.out.println("jump:" + score_jump);

				System.out.println("coinCount:" + model.coinCount);

// 				System.out.println(" model.mario.groundCount:" +  model.mario.groundCount);
//				System.out.println(" model.mario.jumpCount:" +  model.mario.jumpCount);

//				System.exit(1); //stops game immediatly

//			~~~~~~~~~~~~~~~~~~~~~~~~~~
//			AI CONTROLS
				if(score_runJump > score_jump && score_runJump > score_runRight ) {
					model.doAction(Model.Action.runJump);
				}

				else if( score_jump > score_runRight && score_jump > score_runJump)
				{
					model.doAction(Model.Action.jump);

				}

				else
					model.doAction(Model.Action.runRight);

//				~~~~~~~~~~~~~~~~~~~~~~~~~~
//			PLAYER CONTROLS

//				if(keyRight || keyLeft == false) // tracks if mario is moving for animation
//					model.mario.mario_still = true;
//
//				if(keyRight || keyLeft == true)
//					model.mario.mario_still = false;
//
//				if(keyRight )
//					model.mario.x += 10;
//
//				if(keyLeft )
//					model.mario.x -= 10;
////
//				if(keySpace == true && model.mario.groundCount == 0)
//				{
//
//					model.mario.jump();
//
////					if(model.mario.groundCount == 2)
////						model.mario.setJumpCount();
//
//
//					keySpace = false;
//				}


				if(model.mario.mario_bump == true && model.det_block == true ) //block bump spawn coin
				{

					model.makeCoin(model.tempX, 250);

					model.mario.mario_bump = false; //reset perameters

					model.det_block = false;

				}

				if(keyC == true) //spawn bricks at a pre-determined location
				{
					model.makeBlock(400 + (model.mario.x - 200), 270);
					keyC = false;
				}


			}
}