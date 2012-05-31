package firstsubtext.subtext;

import java.io.File;
import data.*;

import android.os.Environment;
import android.util.Log;

public class Globals {
    static String timeStamp;
    static File mediaStorageDir;
    static Shape[] shapes;
    static Letter[] letters;
    static int stage;

    public Globals(String time){
    	timeStamp = time;
    	mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "SI_"+timeStamp);
    	
    	if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
            }
        }
    	
    	//Load all of the shapes into an array for referencing into an array
    	shapes = new Shape[5];
    	for(int i = 0; i < 5; i++){
    		shapes[i] = new Shape(i);
    	}
    	
    	letters = new Letter[26];
    	for(int i = 0; i < 26; i++){
    		letters[i] = new Letter(i);
    	}
    	
    	stage = 0;
    	
    }
    
    public static Shape getStageShape(){
    	return shapes[stage];
    }
    
    public static void nextStage(){
    	stage++;
    }
    
    public static String getTime(){
    	return timeStamp;
    }
    
    public static String getPath(){
    	return mediaStorageDir.getPath();
    }
    
    public static Shape getShape(int id){
    	return shapes[id];
    }
    
    public static Letter getLetter(int id){
    	return letters[id];
    }
    
    
}
