package data;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;

public class Letter{

	private int  	id;
	private int[]   x_points;
	private int[]   y_points;
	private int[]	shape_ids;
	private Bitmap  image;
	private Bitmap 	edited;
	


	public Letter(int id){
		this.id = id;
        Log.d("Letter", "Initialized "+id);
	}

	public void setInfo(int[] x, int[]y, int[] s){
		x_points = x;
		y_points = y;
		shape_ids = s;
	}
	
	
	
}
