package module.view.svg;

import java.util.ArrayList;
import java.util.List;

import com.caverock.androidsvg.PreserveAspectRatio;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

/**
 * @author niuwei
 * @email nniuwei@163.com
 * @ClassName:SvgHelper.java
 * @Package:com.vgod.svganimation
 * @time:下午2:10:09 2015-1-10
 * @useage:TODO
 */
public class SvgHelper {
	private static final String LOG_TAG = "SvgHelper";
	private final List<SvgPath> mPaths = new ArrayList<SvgPath>();
	private final Paint mSourcePaint;
	
	private SVG mSvg;
	
	public SvgHelper(Paint sourcePaint){
		mSourcePaint = sourcePaint;
	}
	
	/**
	 * 加载资源
	 * */
	public void load(Context context, int svgResource){
		if(mSvg != null) return ;
		try {
			mSvg = SVG.getFromResource(context, svgResource);
            mSvg.setDocumentPreserveAspectRatio(PreserveAspectRatio.UNSCALED);
		} catch (SVGParseException e) {
			Log.e(LOG_TAG, "Could not load specified SVG resource", e);
		}
	}
	
	public static class SvgPath{
		private static final Region sRegion = new Region();
		private static final Region sMaxClip = new Region(Integer.MAX_VALUE,Integer.MAX_VALUE,
				Integer.MAX_VALUE,Integer.MAX_VALUE);
		
		public final Path path;
        public final Paint paint;
        public final float length;
        public final Rect bounds;
        
        public SvgPath(Path path, Paint paint){
        	this.path = path;
        	this.paint = paint;
        	
        	PathMeasure measure = new PathMeasure(path, false);
        	this.length = measure.getLength();
        	sRegion.setPath(path, sMaxClip);
        	bounds = sRegion.getBounds();
        }
	}
	
	public List<SvgPath> getPathsForViewport(final int width, final int height){
		mPaths.clear();
		Canvas canvas = new Canvas(){
			private final Matrix matrix = new Matrix();
			
			@Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }
			
            @SuppressWarnings("deprecation")
			@Override
            public void drawPath(Path path,Paint paint){
            	Path dstPath = new Path();
            	getMatrix(matrix);
            	path.transform(matrix,dstPath);
            	mPaths.add(new SvgPath(dstPath, new Paint(mSourcePaint)));
            }
		};
		
		RectF viewBoxF = mSvg.getDocumentViewBox();
		float scale = Math.min(width / viewBoxF.width(), height / viewBoxF.height());
		canvas.translate(
                (width - viewBoxF.width() * scale) / 2.0f,
                (height - viewBoxF.height() * scale) / 2.0f);
        canvas.scale(scale, scale);

        mSvg.renderToCanvas(canvas);
		return mPaths;
	}
}
