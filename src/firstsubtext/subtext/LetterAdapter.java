package firstsubtext.subtext;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class LetterAdapter extends BaseAdapter {
    private Context mContext;

    public LetterAdapter(Context c) {
        mContext = c;
		Log.d("Canvas Call", "Letter Adapter Created");

    }

    public int getCount() {
        return 26;
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
            imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setId(position);
            
            imageView.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.d("Delete", "touched at "+event.getX()+", "+event.getY());
					addToCanvas((ImageView)v, event);
					return false;
				}});
            
            
        } else {
            imageView = (ImageView) convertView;
        }

		File f = new File(Globals.getTestPath() + File.separator +Globals.intToChar(position) + ".png");
		imageView.setImageBitmap(Globals.decodeSampledBitmapFromResource(f,60, 60));
		    
		
      

        return imageView;
    }
    
    public void addToCanvas(ImageView v, MotionEvent e){
		((CanvasActivity)mContext).addToCanvas(v, e);

    }

  
}