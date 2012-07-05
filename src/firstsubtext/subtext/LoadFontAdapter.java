package firstsubtext.subtext;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class LoadFontAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList fonts;

    public LoadFontAdapter(Context c) {
        mContext = c;
        fonts = new ArrayList();
        
        File dir = new File(Globals.getBasePath());
        String[] children = dir.list();
        
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i=0; i<children.length; i++) {
                String filename = children[i];
                fonts.add(filename);
            }
        }

        
		Log.d("Canvas Call", "Letter Adapter Created");

    }
    

    public int getCount() {
        return fonts.size();
    }

    public Object getItem(int position) {
        return fonts.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	textView = new TextView(mContext);
        	textView.setWidth(1280);
        	textView.setHeight(30);
        	textView.setTextSize(16);
        	
           
        } else {
            textView = (TextView) convertView;
        }

		File f = new File(Globals.getTestPath() + File.separator +Globals.intToChar(position) + ".png");
		textView.setText((String)fonts.get(position));
		     

        return textView;
    }

  
}