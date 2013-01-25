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


package photo.anytype;

import java.io.File;
import java.util.ArrayList;

import data.Letter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class LetterPhotoAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private ArrayList bitmaps;
    private ArrayList ids;
    private boolean photo;
    private boolean all;

    public LetterPhotoAdapter(Context c, boolean photo, boolean all) {
    	this.all = all;
    	this.photo = photo;
    	
        mContext = c;   
        bitmaps = new ArrayList();
        ids = new ArrayList();
        File f;
        
        if(!all){
	        boolean[] included = {false, false, false, false, false};
	        
	        //add the shapes from this letter here
	        Letter l = Globals.getLetter(Globals.force_letter);
	        int[] shape_ids = l.getShapeIds();
	        for(int i = 0; i < shape_ids.length; i++){
	        	if(Globals.stageHasVideo(shape_ids[i]) && !included[shape_ids[i]]){
	        	f = new File(Globals.getTestPath() + File.separator + "IMG_"+shape_ids[i] + "_CROP.png");
	        	bitmaps.add(f);
	        	ids.add(shape_ids[i]);
	        	included[shape_ids[i]] = true;
	        	}
	        }
        }else{
        	for(int i = 0; i < Globals.shapes.length; i++){
	        	if(photo) f = new File(Globals.getTestPath() + File.separator + "IMG_"+i + ".png");
	        	else  f = new File(Globals.getTestPath() + File.separator + "IMG_"+i + "_CROP.png");
	        	bitmaps.add(f);
	        	ids.add(i);
	        }
        	
        }
    }

    public int getCount() {
        return bitmaps.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes

        	imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(Globals.letter_size/2, Globals.letter_size/2));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setId((Integer) ids.get(position));

        } else {
            imageView = (ImageView) convertView;
        }
        
       imageView.setImageBitmap(Globals.decodeSampledBitmapFromResource((File) (bitmaps.get(position)),Globals.letter_size/2, Globals.letter_size/2));

		    
        return imageView;
    }
    
    
    

    


  
}
