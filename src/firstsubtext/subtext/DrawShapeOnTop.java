package firstsubtext.subtext;

import data.Shape;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.View;

class DrawShapeOnTop extends View {
	
	private Shape shape;
	private boolean confirm;

    public DrawShapeOnTop(Context context, Shape s, boolean c) {
            super(context);
            shape = s;
            confirm = c;
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
    		float[] points = shape.getPoints();
    	
    	
            Paint paint = new Paint();
    		
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, 20, 520, 60, paint); 
            
   
            paint.setTextSize((float)24);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
           
            if(confirm) canvas.drawText("Does this look right to you? "+shape.getId(), 20, 50, paint);
            else canvas.drawText("Find something that fits into this shape..."+shape.getId()+" "+points.length, 20, 50, paint);
           
          
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.YELLOW);
            
            //gets the shape from the shape class and draws them
            canvas.drawLines(points, paint);
            
            super.onDraw(canvas);
    }

}
