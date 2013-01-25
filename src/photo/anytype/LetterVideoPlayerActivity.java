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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import data.Letter;
import data.Shape;
import firstsubtext.subtext.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;

//each activity is a state. 
//this is the photo capture activity. It takes a picture 
public class LetterVideoPlayerActivity extends Activity {

	protected static final String TAG = null;
	private GridView mGrid;
	private LetterView letter_view;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lettervideo);

		Log.d("Canvas Call", "Find Grid View");
		mGrid = (GridView) findViewById(R.id.letter_grid);
		mGrid.setAdapter(new LetterPhotoAdapter(this, false, false));

		mGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				launchVideo(v.getId());
			}
		});
		
		letter_view = new LetterView(this);
		letter_view.addSingleLetter(Globals.force_letter);
		//letter_view.deselect(Globals.force_letter);
		
		FrameLayout letter_frame = (FrameLayout) findViewById(R.id.letter_frame);
		letter_frame.addView(letter_view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		

		Button back_button = (Button) findViewById(id.button_back);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Globals.playback_mode = false;
				finish();
			}
		});
	    
	}

	
	//launch the video player when clicked
	public void launchVideo(int stage){
		
		Globals.playback_mode = true;
		Globals.force_stage = stage;
		
		Intent intent = new Intent(this, VideoPlayerActivity.class);
		startActivity(intent);
	}

	@Override
	public void onRestart() {

	}

	@Override
	protected void onPause() {
		super.onPause();
	}



}
