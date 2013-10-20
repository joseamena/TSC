package jose.antonio.mena.tonestackcalculator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BodeView extends View {
	
	public CircuitFragment viewController;		//am I an iOS programmer?
	
	private Paint paint;
	private double[] range;
	private double[] yRange;
	private int numPoints;
	private int actualNumPoints;
	private int decadeWidth;
	private int numDecades;
	private double[] points;
	private int pointsPerDecade;				//this is for plotting
	private boolean drawGrid = true;
	private boolean plotIsLog;
	private int plotTopMargin;
	private int plotBottomMargin;
	private int plotLeftMargin;
	private int plotRightMargin;
	private float plotWidth;
	private float plotHeight;
	private float[] plot;
	private double[] result;
	private int horizontalLines;
	
	
	private final static int DIVISIONS_PER_DECADE = 10; //this is what will be shown 
	private final static String TAG = "Bode View";
	
	//we will ask the main activity to be our controller
	//so need an instance
	private void init () {
		this.setDrawingCacheEnabled(true);
		paint = new Paint();
		range = new double[2];
		yRange = new double[2];
		this.plotTopMargin = 20;
		this.plotBottomMargin = 25;
		this.plotLeftMargin = 80;
		this.plotRightMargin = 10;
		range[0] = 20;
		range[1] = 20000;
		yRange[0] = -30;
		yRange[1] = 0;
		horizontalLines = 6;
		
 		this.numDecades = (int) Math.log10(range[1]/range[0]);
		this.numPoints = numDecades * 10;
		if (numPoints != 0 || numDecades != 0)
			this.pointsPerDecade = numPoints/numDecades;
		actualNumPoints = (pointsPerDecade*numDecades - numDecades + 1);
		plot = new float[actualNumPoints * 2];
		result = new double[actualNumPoints];
		points = new double[actualNumPoints];
		this.plotIsLog(true);
		
	}
	public double[] getyRange() {
		return yRange;
	}
	public void setyRange(double[] yRange) {
		this.yRange = yRange;
	}
	public BodeView(Context context) {
		super(context);
		setMinimumWidth(200);
		setMinimumHeight(200);
		//numPoints = 40;
		//numDecades = 4;
		this.init();
	}
	
	public BodeView (Context context, int numberOfDecades, int totalPoints) {
		super (context);
		setMinimumWidth(200);
		setMinimumHeight(200);
		//this.numDecades = numberOfDecades;
		//this.numPoints = totalPoints;
		this.init();
		
	}
	
	public BodeView (Context context, AttributeSet attrs) {
		super(context, attrs);
		setMinimumWidth(200);
		setMinimumHeight(200);
		//numPoints = 40;
		//numDecades = 4;
		this.init();
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	
		int desiredWidth = 0;
		int desiredHeight = 0;
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	    
	    Log.v(TAG, "width mode: ");
	    switch (widthMode) {
	    	case MeasureSpec.EXACTLY:
	    		Log.v (TAG, "EXACTLY");
	    		desiredWidth = widthSize;
	    		break;
	    	
	    	case MeasureSpec.AT_MOST:
	    		Log.v (TAG, "AT_MOST");
	    		desiredWidth = Math.min(widthSize, getSuggestedMinimumWidth());
	    		break;
	    	
	    	case MeasureSpec.UNSPECIFIED:
	    		Log.v (TAG,"UNSPECIFIED");
	    		break;
	    }
	    
	    //Log.v (TAG, "width size : " + widthSize);
	    
	    //Log.v(TAG, "height mode: ");
	    switch (heightMode) {
    	case MeasureSpec.EXACTLY:
    		Log.v (TAG, "EXACTLY");
    		desiredHeight = widthSize;
    		break;
    	
    	case MeasureSpec.AT_MOST:
    		Log.v (TAG, "AT_MOST");
    		desiredHeight = Math.min(heightSize, getSuggestedMinimumHeight());
    		break;
    	
    	case MeasureSpec.UNSPECIFIED:
    		Log.v (TAG,"UNSPECIFIED");
    		break;
    }
	    Log.v (TAG, "height size : " + heightSize);
	    
		setMeasuredDimension(widthSize, heightSize);
		this.plotWidth = getWidth() - this.plotLeftMargin - this.plotRightMargin;
		this.plotHeight = getHeight() - this.plotTopMargin - this.plotBottomMargin; 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		canvas.save();
		if (drawGrid) {
			
			canvas.drawColor(Color.WHITE);	//draw logarithmic grid
			
			//drawGrid = false;
			paint.setColor(Color.BLUE);			//draw a blue frame around it
			paint.setStrokeWidth(5);			//just so it looks better
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(this.plotLeftMargin, this.plotTopMargin, 
					getWidth() - this.plotRightMargin , 
					getHeight() - this.plotBottomMargin, paint);
			
			drawGrid(canvas);
		}
		
		
		drawCurve (canvas);
		canvas.restore();
		
	}
	
	private void obtainInputPoints () {
		
	}
	
	private void drawCurve (Canvas canvas) {
		for (int j = 0; j < numDecades; j++) {
			
			for (int i = 0; i < pointsPerDecade - 1; i++) {
				points[i + (j * (pointsPerDecade - 1))] = 
						(range[0] * (i + 1)) * Math.pow(10, j);
			}
		}
		
		points[points.length - 1] = range[1];
		
		result = this.viewController.getCurve(points, this.plotIsLog, this);
		
		//now we need to scale it
		decadeWidth = (int) (this.plotWidth/numDecades);
		
		float range = (float) (yRange[1] - yRange[0]);
		float offset = (float) ((yRange[1] / range) * plotHeight);
		
		for (int j = 0; j < numDecades; j++) {
			
			for (int i = 0; i <= pointsPerDecade - 2; i++) {
				
				plot[(i * 2) + (j * (pointsPerDecade-1) * 2)] =
                        (float) ((Math.log10(i + 1) + j) * decadeWidth + this.plotLeftMargin);
				
				
				if (this.plotIsLog) {
					//Log.v(TAG, "plot is log");
					//float range = (float) (yRange[1] - yRange[0]);
					plot[(i * 2) + 1 + (j * (pointsPerDecade-1) * 2)] = -(plotHeight / range) 
							* (float)result[i + j * (pointsPerDecade - 1) ] + plotTopMargin + offset;
							//(float) (this.plotHeight / (yRange[0] - yRange[1]) * 
							//(result[i + j *(pointsPerDecade - 1)] - yRange[1]));
												
				} else {
					plot[(i * 2) + 1 + (j * (pointsPerDecade-1) * 2)] =
	                        (float) (plotTopMargin + plotHeight - (result[i + j * (pointsPerDecade - 1) ] * plotHeight));
				}
				
			}
			
		}
		
		plot[plot.length - 2] = this.plotWidth + this.plotLeftMargin;
		//plot[plot.length - 1] = (float) (height - result[result.length - 1] * height);
		
		if (this.plotIsLog) {
			plot[plot.length - 1] = -(plotHeight / range) 
					* (float)result[result.length - 1 ] + plotTopMargin + offset;
					
					//(float) (this.plotHeight / (yRange[0] - yRange[1]) * 
					//(result[result.length - 1] - yRange[1]));
					//(float) (-(height/60) * result[result.length - 1]);
		} else {
			plot[plot.length - 1] = (float) (this.plotHeight - result[result.length - 1] 
					* this.plotHeight + plotTopMargin);
		}
		
		canvas.save();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4);
		//paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
		
		//canvas.drawLines(plot, paint);
		
		drawContinuosLines (plot, paint, canvas);
		canvas.restore();
	}
	
	public double[] getRange() {
		return range;
	}
	public void setRange(double[] range) {
		this.range = range;
	}
	
	private boolean pointIsInsideRect(float point) {
		//return true;
		if (point >= this.plotTopMargin && point <= this.plotTopMargin + this.plotHeight){
			return true;
		}
		return false;
		
	}
	private void drawContinuosLines (float[] pts , Paint paint, Canvas canvas) {
		//Log.v(TAG, pts.length + " to plot");
		for (int i = 0; i < pts.length - 3; i+=2) {
			if (this.pointIsInsideRect(pts[i+1]) && this.pointIsInsideRect(pts[i+3]))
				canvas.drawLine(pts[i], pts[i+1], pts[i+2], pts[i+3], paint );
			else if (this.pointIsInsideRect(pts[i+1]) && !this.pointIsInsideRect(pts[i+3])){
				
				float m = (pts[i+3] - pts[i+1]) / (pts[i+2] - pts[i]);
				float newY;
				float newX;
				
				if (m >= 0) {
					newY = this.plotHeight + this.plotTopMargin;
				} else {
					newY = this.plotTopMargin;
				}
				//line equation y = m * ( x - x1) + y1
				// x = ((y - y1)/m) + x1;   x1 => pts[i], y1 => pts[i+1]
				
				newX = ((newY - pts[i+1]) / m) + pts[i];
			
				Log.v(TAG, "slope = " + m);
				canvas.drawLine(pts[i], pts[i+1], 
								newX, newY, paint);
			}
		}
		
	}
	
	private void drawGrid (Canvas canvas) {
		
		
		int textSize = 20;
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(textSize);
		paint.setStrokeWidth(1);
		paint.setTextAlign(Align.CENTER);
		
		decadeWidth = (int) (this.plotWidth / numDecades);
		float start = this.plotLeftMargin; 
		float x;
		for (int j = 0; j < numDecades; j++) {
			
			paint.setColor(Color.GRAY);
			paint.setAlpha(100);
			
			
			for (int i = 1; i <= DIVISIONS_PER_DECADE; i++) {
				x =  (float) (start + Math.log10 (i) * decadeWidth);
				canvas.drawLine(x, this.plotTopMargin, 
						        x, getHeight() - this.plotBottomMargin, paint);
			}
			
			
			paint.setColor(Color.BLACK);
			int value = (int) (Math.pow(10, j) * range[0]);
			
			String sValue;
			
			if (value >= 1000) {
		 		sValue = String.valueOf(value/1000).concat("Khz");
			} else {
				sValue = String.valueOf(value).concat("hz");
			}
			
			canvas.drawText(sValue,
					start, getHeight() - 5, paint);
						
			start += decadeWidth;
		}
		
		float divisionWidth = this.plotHeight/(horizontalLines - 1);
		float vDivisionWidth = (float) ((yRange[1] - yRange[0]) / (horizontalLines - 1));
		
		paint.setTextAlign(Paint.Align.RIGHT);
		for (int i = 0; i < horizontalLines; i++ ) {
			paint.setColor(Color.GRAY);
			float y = this.plotHeight + this.plotTopMargin - (divisionWidth * i);
			canvas.drawLine(this.plotLeftMargin, y , 
							this.plotLeftMargin + this.plotWidth, 
							y, paint);
			
			paint.setColor(Color.BLACK);
			String s = String.valueOf(yRange[0] + i * vDivisionWidth).concat("dB");
			canvas.drawText(s, this.plotLeftMargin - 6, y, paint);
		}	
	}
	
	public boolean isPlotLog() {
		return plotIsLog;
	}
	public void plotIsLog(boolean plotIsLog) {
		this.plotIsLog = plotIsLog;
	}

}
