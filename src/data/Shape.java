/**************************************************************                                                                       
AnyType                                    
Copyright (C) 2012-2013 by Laura Devendorf     
www.ischool.berkeley.edu/~ldevendorf/anytype                  
---------------------------------------------------------------             
                                                                           
This file is part of AnyType.

AnyType is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

AnyType is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with AnyTypePhoto. If not, see <http://www.gnu.org/licenses/>.

*****************************************************************/

package data;

import java.io.*;
import java.util.*;

import firstsubtext.subtext.Globals;
import firstsubtext.subtext.R;
import android.app.Activity;
import android.graphics.*;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Shape extends Activity{

	private int  	id;
	private int[]   x_points;
	private int[]   y_points;
	private Bitmap  image;
	private Bitmap 	edited;
	private Path 	path;
	private Rect 	bounds;
	private int[]   offset; //the amount this shape was shifted from zero
	


	public Shape(int id){
		this.id = id;
        Log.d("Shape", "Initialized "+id);
	}

	public void setPoints(int[] x, int[]y){
		x_points = x;
		y_points = y;
		makeBounds();
		offset = new int[2];
		offset[0] = bounds.left;
		offset[1] = bounds.top;
	}
	
	public int[] getOffset(){
		return offset;
	}
	
	public void makeBounds(){
		bounds = new Rect(1000000, 1000000, -1000000, -1000000);
		
		for(int i = 0; i < x_points.length; i++){
			if(x_points[i] < bounds.left) bounds.left = x_points[i];
			else if(x_points[i] > bounds.right) bounds.right = x_points[i];
			if(y_points[i] < bounds.top) bounds.top = y_points[i];
			else if(y_points[i] > bounds.bottom) bounds.bottom = y_points[i];
		}
	}
	
	public void offset(int x, int y){
		for(int i = 0; i < x_points.length; i++){
			x_points[i] += x;
			y_points[i] += y;
		}
		makeBounds();		
	}
	
	public Rect getBounds(){
		return bounds;
	}
	
	
	public Path getPath(){
		return path;
	}
	


	
	/**
	 * If this shape exists, load the edited shape
	 * @return whether or not the call was successful
	 */
	public boolean loadEditedShape(){
		return true;
	}


	/**
	 * Each time the user draws into the shape, use this function to update 
	 * the x and y coordinates
	 */
	public void updateShape(){

	}

	/**
	 * Return the image for this shape
	 */
	public Bitmap getImage(){
		return image;
	}

	/**
	 * This takes the captured image and converts it into the shape.  Must be called after capture image
	 * @return true if successful, false if not
	 */
	public boolean createShapeImage(){
		Log.d("Create Shape Image", "Entered");
		
		Paint 	p = new Paint();
		Bitmap  base = Bitmap.createBitmap(1024, 800, Bitmap.Config.ARGB_8888);
		Canvas 	c = new Canvas(base);
		
		//initialize the path for the clipping
		Path path = new Path();
		path.moveTo(x_points[0], y_points[0]);
		for(int i = 1; i < x_points.length; i++) path.lineTo(x_points[i], y_points[i]);
		path.lineTo(x_points[0], y_points[0]);
			
		RectF bounds = new RectF();
		boolean  exact = true;
		path.computeBounds(bounds, exact);
		edited = Bitmap.createBitmap((int)Math.ceil(bounds.width()), (int)Math.ceil(bounds.height()), Bitmap.Config.ARGB_8888);
		Log.d("Create Shape Image", "Bounds of Edited "+(int)Math.ceil(bounds.width())+", "+(int)Math.ceil(bounds.height()));

		
		


		image = BitmapFactory.decodeFile(Globals.getTestPath() + File.separator +
				"IMG_"+ Integer.toString(id) + ".jpg");

		Log.d("Create Shape Image", "Image Loaded");

		p.setARGB(255, 0, 0, 0);
		p.setStyle(Paint.Style.FILL);

		//draw this image to the bitmap with background white and foreground shape black
		c.drawARGB(255, 255, 255, 255);
        c.drawPath(path, p);

		Log.d("Create Shape Image", "Canvas Drawn");

//		//go through and update the pixels 
//		for(int i = 0; i < image.getWidth(); i++){
//			for(int j = 0; j < image.getHeight(); j++){
//				if(base.getPixel(i, j) == Color.WHITE) image.setPixel(i, j, Color.TRANSPARENT);	
//			}
//		}
		
		Log.d("Create Shape Image", "Image Created");


		return true;
	}
	
	public int[] getXPoints(){
		return x_points;
	}
	
	public int[] getYPoints(){
		return y_points;
	}
	
	public int getId(){
		return id;
	}

}
