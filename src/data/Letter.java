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

import firstsubtext.subtext.Globals;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

public class Letter{

	private int  	id;
	private int[]   x_points;
	private int[]   y_points;
	private float[] rotations;
	private int[]	shape_ids;
	private Bitmap  image;
	private Bitmap 	edited;
	private Rect bounds;
	


	public Letter(int id){
		this.id = id;
        Log.d("Letter", "Initialized "+id);
	}

	public void setInfo(int[] x, int[]y, float[] r, int[] s){
		x_points = x;
		y_points = y;
		rotations = r;
		shape_ids = s;
		makeBounds();
	}
	
	public int[] getXPoints(){
		return x_points;
	}
	
	public int[] getYPoints(){
		return y_points;
	}
	
	public float[] getRotations(){
		return rotations;
	}
	
	public int[] getShapeIds(){
		return shape_ids;
	}
	
	public int getId(){
		return id;
	}
	
	//sets the left point of the letter to 0, 0
	public void cleanUp(){
		//offset(-1*bounds.left, -1*bounds.top);
	}
	
	public void makeBounds(){
		bounds = new Rect(1000000, 1000000, -1000000, -1000000);
		
		for(int i = 0; i < x_points.length; i++){
			Rect s_bound = Globals.getShape(shape_ids[i]).getBounds();
			if(x_points[i] < bounds.left) bounds.left = x_points[i];
			if((x_points[i] + s_bound.width())  > bounds.right) bounds.right = (x_points[i] + s_bound.width());
			if(y_points[i] < bounds.top) bounds.top = y_points[i];
			if((y_points[i]+ s_bound.height()) > bounds.bottom) bounds.bottom = (y_points[i]+ s_bound.height());
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
	
	
	
}
