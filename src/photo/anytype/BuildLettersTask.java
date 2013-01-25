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

package firstsubtext.subtext;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


	//use this to build each letter in the background so it doesn't hang the UI
	class BuildLettersTask extends AsyncTask<Void, Void, Void> {
	    /** The system calls this to perform work in a worker thread and
	      * delivers it the parameters given to AsyncTask.execute() */
		protected Void doInBackground(Void... params) {
	    	Log.d("Thread", "Starting Process");
	    	Globals.buildLetters();
	    	Log.d("Thread", "Finished Process");

			return null;
		}

	    /** The system calls this to perform work in the UI thread and delivers
	      * the result from doInBackground() */
	    protected void onPostExecute() {
	    	Log.d("Thread", "Execute");
	    }

	}	
	

