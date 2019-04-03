//scratch

import java.util.ArrayList;
import java.lang.StringBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import java.io.File;

import java.lang.Math;


abstract class Sprite extends Object //extends object utilizes a v-table so as to keep track of "isMario"
{
	// dimensions for sprites
	int x;
	int y;
	int w;
	int h;

	Model model;

	//int for sum
	int someCount;

	//boolean for triggers
	boolean trigger;

	//update method
	abstract void update();

	//draw method
	abstract void draw(Graphics g, Model m); //how do draw method

	//load/unload json method
	abstract Json marshal();

	boolean isMario()
	{
		return false;
	}

	//...
	abstract Sprite cloneMe(Model newModel);//....!!

	Sprite(){ } //default const

	Sprite(Sprite that, Model newModel)//....!!
	{
		x = that.x;
		y = that.y;
		w = that.w;
		h = that.h;
		someCount = that.someCount;
		trigger = that.trigger;

		model = newModel;
	}
}

class Block extends Sprite {

	ArrayList<Sprite> sprites;

	Image[] block_pic;

	Model model; //instantiate because deepcopy

	Block(int xCord, int yCord)
	{

		x = xCord;
		y = yCord;

		w = 60;
		h = 60;

		someCount = someCount; //counts num of coins spawned from individual coin blocks

		trigger = trigger;


	}

	Block(Block that) //for deepcopy
	{

		x = that.x;

		y = that.y;

		w = that.w;

		h = that.h;

		trigger = that.trigger;

		someCount = that.someCount;

	}

	//should be done with all objects except Mario
	Block(Model mod)
	{
		model = mod;
	}

	Block(Block copyme, Model mymodel)
	{
		model = mymodel;
	}

	Sprite cloneMe(Model newModel)
	{
		return new Block(this);
	}

	Json marshal()
	{
		//string to identify "Block
		Json ob = Json.newObject();

		ob.add("x", x);
		ob.add("y", y);
		ob.add("w", w);
		ob.add("h", h);
		ob.add("someCount", someCount);
		ob.add("trigger",trigger);
		ob.add("Type", "Block");

		return ob;
	}

	Block(Json ob)
	{
		x = (int)ob.getLong("x");
		y = (int)ob.getLong("y");
		w = (int)ob.getLong("w");
		h = (int)ob.getLong("h");

		someCount = (int)ob.getLong("someCount");

		trigger = (boolean) ob.getBool("trigger");


		}

	//update
	void update()
	{
		x = x;
		y = y;
	}

	//draw
	void draw(Graphics g,  Model m)
	{
		if(block_pic ==  null)
		{
			block_pic = new Image[2];
			//lazy loading
			block_pic[0] = loadImage("block.png"); // default pic of non-empty coin block
			block_pic[1] = loadImage("block_empty.png"); //replacement pic of empty coin block
		}

		if(trigger == false)//if empty trigger hasnt been tripped use default
			g.drawImage(block_pic[0], x - (m.mario.x - 200) , y , null);

		if(trigger == true)
			g.drawImage(block_pic[1], x - (m.mario.x - 200) , y , null);

	}

	//loadImage method
	static Image loadImage(String filename) //lazy-loading images
	{
		Image im = null; //set to null on start

		try
		{
			im = ImageIO.read(new File(filename));
		}

		catch (Exception e)
		{
			e.printStackTrace(System.err);
			System.exit(1);
		}

		return im;
	}
}


class Coin extends Sprite {

	ArrayList<Sprite> sprites;

	Image coin_pic;

	double  vert_vel, horiz_vel;

	double rand = Math.random();

	int prev_x, prev_y;

	int jumpCount;

	Model model;

	Coin(int xCord, int yCord)
	{

		x = xCord;

		y = yCord;

		vert_vel = -9.8;

		w = 40;

		h = 40;


	}

	Coin(Coin that) //for deepcopy
	{

		x = that.x;

		y = that.y;

		w = that.w;

		h = that.h;

		jumpCount = that.jumpCount;
	}

	Coin(Model mod)
	{
		model = mod;
	}

	Coin(Coin copyme, Model mymodel)
	{
		model = mymodel;
	}

	Sprite cloneMe(Model newModel)
	{
		return new Coin(this);
	}

	Json marshal()
	{
		//string to identify "Block
		Json ob = Json.newObject();

		ob.add("x", x);

		ob.add("y", y);

		ob.add("w", w);

		ob.add("h", h);

		ob.add("jumpCount",jumpCount);

		ob.add("Type", "Coin");

		return ob;
	}

	Coin(Json ob)
	{
		x = (int)ob.getLong("x");

		y = (int)ob.getLong("y");

		w = (int)ob.getLong("w");

		h = (int)ob.getLong("h");

		jumpCount = (int)ob.getLong("jumpCount");


	}

	//update
	void update()
	{
		jumpCount++;

		vert_vel += 9.8;

		//utilize rand for direction with a number being generated and if even: positive direction, if odd: negative direction
		//init rand here

		if(rand < 0.5) //if rand() greater than 0.5: direction is positive
			horiz_vel += 0.2;

		if(rand < 0.6)
			horiz_vel += 0.5;

		if(rand < 0.7)
			horiz_vel += 0.7;

		if(rand > 0.5 ) //else rand() less that 0.5: direction is negative
			horiz_vel -= 0.4;

		if(rand > 0.6)
			horiz_vel -= 0.10;

		if(rand > 0.7)
			horiz_vel -= 0.14;

		y += vert_vel; //gravity

		if(jumpCount == 1)//initial jump once initially spawned from coin block
			vert_vel = -38;

		if(y != 500) //if coin has not landed, use rand() to decid horizontal direction
			x += horiz_vel;// positive direction

	}

	//draw
	void draw(Graphics g, Model m)
	{
		if(coin_pic == null)
			coin_pic = loadImage("coin.png"); //lazy loading
		g.drawImage(coin_pic, x - (m.mario.x - 200) + w / 2, y - h, null);
	}

	//loadImage method
	static Image loadImage(String filename) //lazy-loading images
	{
		Image im = null; //set to null on start

		try {
			im = ImageIO.read(new File(filename));
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}

		return im;

	}
}

class Brick extends Sprite
{
	ArrayList<Sprite> sprites;

	Model model;

	//constructor
	Brick(int xCord, int yCord, int wValue, int hValue)
	{
		x = xCord;
		y = yCord;
		w = wValue;
		h = hValue;
	}

	Brick(Brick that) //for deepcopy
	{
		x = that.x;

		y = that.y;

		w = that.w;

		h = that.h;
	}

	Brick(Model mod)
	{
		model = mod;
	}

	Brick(Brick copyme, Model mymodel)
	{
		model = mymodel;
	}

	Sprite cloneMe(Model newModel)
	{
		return new Brick(this);
	}


	Json marshal()
	{
		//string to identify "Block
		Json ob = Json.newObject();

		ob.add("x", x);
		ob.add("y", y);
		ob.add("w", w);
		ob.add("h", h);
		ob.add("Type", "Brick");
		//ob.add("someCount", someCount);
		return ob;
	}

	Brick(Json ob)
	{
		x = (int)ob.getLong("x");
		y = (int)ob.getLong("y");
		w = (int)ob.getLong("w");
		h = (int)ob.getLong("h");
	}

	void update() //b/c abstracted to Sprite class needs to implament same func
	{
		x = x;
		y = y;
	}

	void draw(Graphics g,  Model m) //draw method
	{
		g.setColor(Color.red);

		g.drawRect(x - (m.mario.x - 200), y, w, h); // drawing rectangle and compensating for marios init position //should become draw method for Brick Sprite

		g.fillRect(x - (m.mario.x - 200), y, w, h);
	}
}

class Mario extends Sprite
{
	Model model;//model instantiated within mario class

	int prev_x, prev_y; //keeps last location inbetween frames

	int groundCount; // integer for how long since touched ground

	int frame; //to count frames

	int jumpCount;

	double vert_vel; // gravity

	double dist; //distance traveled

	boolean mario_still; // mario not moving = still frame

	boolean mario_collide; // mario collision deactivates//assists mario to not fall through error

	boolean mario_bump; //mario bump into bottom dimension of coin block

	boolean top_of_object;

	Image[] mario_images;

	ArrayList<Sprite> sprites;

	Mario()//constructor
	{

		x = 200; // starting coords
		y = 500;
		w = 60;
		h = 95;
		//jumpCount = 0;

		vert_vel = -9.8; // gravity

		 //intended num of pics to store
	}

	//copy
	Mario(Mario that)
	{
		model = that.model;

		x = that.x;

		y = that.y;

		w = that.w;

		h = that.h;

		groundCount = that.groundCount;

		jumpCount = that.jumpCount;

		prev_x = that.prev_x;

		prev_y = that.prev_y;

		frame = that.frame;

		vert_vel = that.vert_vel;

		mario_still = that.mario_still;


	}

	boolean isMario() //overrides the sprite.isMario() method
	{
		return true;
	}

	Sprite cloneMe(Model newModel)
	{
		return new Mario(this);
	}

	Json marshal()
	{
		Json ob = Json.newObject();

		ob.add("x", x);

		ob.add("y", y);

		ob.add("w", w);

		ob.add("h", h);

		ob.add("prev_x",prev_x);

		ob.add("prev_y",prev_y);

		ob.add("groundCount",groundCount);

		ob.add("frame", frame);

		ob.add("mario_still", mario_still);

		ob.add("Type", "Mario");

		return ob;
	}

	//constructor for loading
	Mario(Json ob , Model m)
	{
		model = m;

		x = (int)ob.getLong("x");
		y = (int)ob.getLong("y");
		w = (int)ob.getLong("w");
		h = (int)ob.getLong("h");

		prev_x  = (int)ob.getLong("prev_x");
		prev_y = (int)ob.getLong("prev_y");

		groundCount = (int)ob.getLong("groundCount");

		frame = (int)ob.getLong("frame");

		mario_still = (boolean) ob.getBool("mario_still");



	}

	//lazy-loading images
	static Image loadImage(String filename)
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

	//collision function used by mario on other sprites
	void collisionDetect(Sprite b)
	{
		top_of_object = false;

		//LEFT
		if((x  >= b.x)  && (prev_x < b.x) ) // if hit left side //do not need this/ causes weird phasing error
		{
			x  = b.x - 1;	//adjust out

		//	groundCount = 0;

			mario_bump = false;
		}

		//RIGHT on left side
		if(( x - w ) <= (b.x + b.w) && (prev_x - w ) > (b.x + b.w)  )
		{
			x  = b.x + b.w + w + 1;	//adjust out

		//	groundCount = 0;

			mario_bump = false;

		}

		//TOP
		if((y  >= b.y) && (prev_y < b.y))
		{
			y = b.y - 1;	//adjust out
			vert_vel = 0; //to not fall through by gravity

			mario_bump = false;

			top_of_object = true;

		}

		//BOTTOM
		if((y - h <= b.y + b.h) && (prev_y - h > b.y + b.h))
		{
			y = b.y + b.h + h + 1;	//adjust out

			vert_vel += 48; // to bounce off of brick

			mario_bump = true; // set bool to true for coin spawning

		}

		//ELSE
		else // this func traps user to not be able to get out of collision //helps fix issue of genrally touching block on sides empties the block and triggers pic swap
		{
			//groundCount = 0;
			//mario_bump = false; // only when mario hits bottom panel does the bool become true

		}

		//System.out.println(top_of_object); //debugg
	}

	// jumping func
	void jump()
	{
		if(groundCount == 0) {
			vert_vel = -49;

//		System.out.println("incramenting jC"); //debugg
		}

	}

	void setJumpCount() //works
	{
		jumpCount++;
		//System.out.println("Mario's jump count is:" + " " + jumpCount);
	}

	void curr_state() // func helps track last location / for getting out of bricks
	{
		prev_x = x;
		prev_y = y;
	}

	void update()//give marios gravity to coin class
	{

// System.out.println(frame); // print out
//		System.out.println(prev_x);
//		System.out.println(x);


		//prev_y = y; //save last y

		vert_vel += 9.8; // incrament gravity to Vertical_velocity

		y += vert_vel; // incrament vert_vel to marios y cordinate

		if(x == prev_x) // if mario isnt moving on screen don't change frames
		{
			mario_still = true;
			groundCount=0;
		}

		
		// keep frame at start = 0, if mario is not moving
		if(mario_still == true)
			frame = 0;

		else// else incrament frame with each update
			frame++;

		if(frame > 4) //loop frames to make walk animation
			frame = 0;


		if(y > 500) // ground level
		{
			y = 500;
			//tracks time off ground
		}



		if(top_of_object == true )
			groundCount = 0;

		else if( y != 500 && prev_y != 500 ) {//if mario isnt touching the ground incrament: groundCount
			groundCount++;

		}

		if(y == 500 && prev_y == 500)
			top_of_object = false;

		if(groundCount >= 1 && (y != 500 ) )
			setJumpCount();




	}

	void draw(Graphics g, Model m) //draw method for mario
	{
		if(mario_images==null) {
			mario_images = new Image[5]; //intended num of pics to store


				mario_images[0] = loadImage("mario1.png");

			mario_images[0] = loadImage("mario1.png");
			mario_images[1] = loadImage("mario2.png");
			mario_images[2] = loadImage("mario3.png");
			mario_images[3] = loadImage("mario4.png");
			mario_images[4] = loadImage("mario5.png");
		}
		g.drawImage(mario_images[frame], 200 - w, y - 95 , null);
	}
}

class Model //model class
{

	int x, y ,w ,h;

	int horiz_vel, vert_vel;

	int tempX;

	int blockHitCount;

	int coinCount;

	boolean is_still; //is mario standing still i.e. not animated

	boolean	det_block; //detect if sprite bumped is a Block.

	boolean det_brick; //detects if sprite bumped is a brick.

	boolean topDetect;

	boolean isCopy;

	Mario mario;

	ArrayList<Sprite> sprites;  //initializes array

	enum Action //enumerates possible actions for mario
	{
		runRight, 		//works good//...
//		softJump,		//...
		jump,			// tune down power
		runJump,//same^
		wait;
	};

	Model() //default constructor
	{
		mario = new Mario();

		coinCount = 0;

		sprites = new ArrayList<Sprite>();

		sprites.add(mario);

		isCopy = false;

		//load("map.Json"); //loads map on intitial startup.
	}

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

	//	deep-copy constructor for Model
	Model(Model that)
	{

		isCopy = true;

		//copy sprites from linked list
		this.sprites = new ArrayList<Sprite>();

		for(int i =0; i < that.sprites.size(); i++) //set up for loop to iterate through arrayList
		{
			Sprite other = that.sprites.get(i); //get specific sprite

			Sprite clone = other.cloneMe(this); //call cloneMe method on object in sprite

			sprites.add(clone); //add sprite to arraylist

			if(clone.isMario()) //	if mario boolean returns true then this is mario
			{
				mario = (Mario)clone;		//sprite to conv to mario
			}
		}


		x = that.x;
		y = that.y;
		w = that.w;
		h = that.h;

		horiz_vel = that.horiz_vel;
		vert_vel = that.vert_vel;

		tempX =that.tempX;

		blockHitCount = that.blockHitCount;

		coinCount = that.blockHitCount;

		is_still = that.is_still; //is mario standing still i.e. not animated

		det_block = that.det_block; //detect if sprite bumped is a Block.

		det_brick = that.det_brick; //detects if sprite bumped is a brick.

		topDetect = that.topDetect;
	}

	//action method
	void doAction(Action a) // have enums like:  runRight, runLeft, runJumpRight, runJumpLeft, wait....
	{

		if(a == Action.jump )
		{
			mario.jump();
		}

		else if(a == Action.runRight)
		{
			mario.x += 10; //move right
			mario.mario_still = false;
		}


		else if(a == Action.runJump )
		{
			mario.x += 8;
			mario.jump();

			mario.mario_still = false;
		}

	}

	//AI variable constants
	int k = 7;
	int d = 39;

	//GET WORKING!!	//evaluate action method for AI: determining the best combinations of actions to result in highest depth value
	double evaluateAction(Action action, int depth)
	{

		// Evaluate the state
		if(depth >= d)
		{
			return mario.x + 5000 * coinCount - 2 * mario.jumpCount;
		}

		// Simulate the action
		Model copy = new Model(this); // uses the copy constructor


		copy.doAction(action);

		copy.update(); // advance simulated time

		// Recurse
		if(depth % k != 0)
			return copy.evaluateAction(action, depth + 1);

		else
		{
					double best = copy.evaluateAction(Action.runRight, depth + 1);

					best = Math.max(best, copy.evaluateAction(Action.jump, depth + 1));

					best = Math.max(best, copy.evaluateAction(Action.runJump, depth + 1));

					best = Math.max(best, copy.evaluateAction(Action.wait, depth + 1));

			return best;
		}
 }

	static boolean doesCollide(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) //collide method // called by collisionDetect
	{
		if(x1 + w1 < x2) return false;
		if(x1 > x2 + w2) return false;
		if(y1 + h1 < y2) return false;
		if(y1 > y2 + h2) return false;

		else
			return true;
	}

	void curr_state()
	{
		mario.curr_state();//saves marios previous x & y locations
	}

	void makeBrick(int x, int y, int w, int h) //makes bricks
	{
		Sprite b = new Brick(x, y, w, h);

		sprites.add(b);
	}

	void makeBlock(int x, int y) //makes blocks
	{
		Sprite b = new Block(x,y); //added countint integer attached to object_sprite

		sprites.add(b);
	}

	void makeCoin(int x, int y) //makes coins
	{
		Sprite b = new Coin(x,y);

		sprites.add(b);

		coinCount++;
	}

	//marshaling constructor
	Json marshal()
	{
		Json ob = Json.newObject();

		Json json_bricks = Json.newList();

		ob.add("sprites", json_bricks);

		for(int i =0; i < sprites.size(); i++)
		{
			Sprite b = sprites.get(i);

			Json j = b.marshal();

			json_bricks.add(j);
		}
		System.out.println("Marshaling Finished.");
		return ob;

	}

	//save
	void save(String filename)
	{
		System.out.println("Saving.");

		Json ob = marshal(); // creates Json object "ob" and sends it to marshal()

		ob.save(filename);
	}

	//unmarshaling constructor
	void unmarshal(Json ob)
	{

		sprites.clear(); //clears array of bricks

		System.out.println("Bricks have been cleared.");

		Json json_bricks = ob.get("sprites");	//trades info to temp List

		if(json_bricks == null) // if not bricks are found
			System.out.println("no bricks/unmashalling failed.");

		else if(json_bricks != null) //if bricks are found
			System.out.println("Bricks detected/unmarshaling now.");


		for(int i =0; i < json_bricks.size(); i++) // builds bricks from Json.load()"map.json") file
		{
			Json j = json_bricks.get(i);
			String s = j.getString("Type");
			Sprite sp = null;

			if(s.equals("Mario"))
			{
				sp = new Mario(j, this);
				sprites.add(sp);
				mario = (Mario)sp;
			}

			else if(s.equals("Block"))
			{
				sp = new Block(j);
				sprites.add(sp);
			}

			else if(s.equals("Brick"))
			{
				sp = new Brick(j);
				sprites.add(sp);
			}

			else if(s.equals("Coin"))
			{
				sp = new Coin(j);
				sprites.add(sp);
			}

		}

		System.out.println( "Unmarshaling Finished.");
	}

	void load(String filename)
	{
		System.out.println("Loading."); //print out

		Json ob = Json.load(filename); // Creates Json object and sends to load file "filename"

		if(ob != null) //checks if the file is empty
		{
			unmarshal(ob); //unmarshals object "ob"

			System.out.println("File loaded sucessfully.");
		}

		ob.load(filename);
		if(ob == null)
			System.out.println("Error: File is empty.");
	}

	public void update() //updates model
	{
		mario.update();//updates marios position x,y

		if (!isCopy)
		{
			System.out.println("GroundCount:" + mario.groundCount);
			System.out.println("Top of object:" + mario.top_of_object);
			System.out.println("Coin count:" + coinCount);
			System.out.println("Jump count:" + mario.jumpCount);
		}


		for (int i = 0; i < sprites.size(); i++)
		{
			Sprite b = sprites.get(i);


			if (b.getClass().getName() == "Mario") {
				//mario.update();

			}

			if (b.getClass().getName() == "Brick") {
				if (doesCollide(mario.x - mario.w, mario.y - mario.h, mario.w, mario.h, b.x, b.y, b.w, b.h))
					mario.collisionDetect(b);
			}

			if (b.getClass().getName() == "Block") {
				if (doesCollide(mario.x - mario.w, mario.y - mario.h, mario.w, mario.h, b.x, b.y, b.w, b.h)) {
					mario.collisionDetect(b);

					if (b.someCount <= 6 && mario.mario_bump == true )
					{

						makeCoin(b.x, 250);

						mario.mario_bump = false; //reset perameters

						b.someCount++;

						//System.out.println(coinCount);
					}

					if (b.someCount == 5) {
						//System.out.println("Limit reached");
						//boolean to indicate the block to change images
						b.trigger = true;
					}

				}
			}

			for(Iterator<Sprite> id = sprites.iterator(); id.hasNext(); )
			{
				Sprite d = id.next();

				if (d.getClass().getName() == "Coin")
				{
					d.update();

					if (d.y >= 700) //deletes coin when touches ground
					id.remove();
				}
			}
		}
	}
}
