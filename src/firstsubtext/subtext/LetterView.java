package firstsubtext.subtext;

import java.io.File;

import data.Letter;
import data.Shape;
import firstsubtext.subtext.R.id;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

class LetterView extends View {

	private Letter letter;
	private boolean confirm;
	private Matrix image_scale;
	int[] x_points;
	int[] y_points;
	Bitmap bmap;

	public LetterView(Context context, Letter l) {
		super(context);
		letter = l;
	}

	@Override
	protected void onDraw(Canvas canvas) {
	
	Bitmap bmap = BitmapFactory.decodeFile(Globals.getPath() + File.separator +
	Globals.intToChar(letter.getId())+".png");
	canvas.translate(this.getWidth()/2, this.getHeight()/2);
	canvas.drawBitmap(bmap, new Matrix(), null);		
	super.onDraw(canvas);
	
	
	
	
		
	}
	
	

}
