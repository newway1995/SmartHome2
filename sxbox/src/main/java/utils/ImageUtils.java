package utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;

/**
 * Package: utils
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 16:30
 * FIXME
 */
public class ImageUtils {
    /**
     * 将Bitmap旋转
     * @param b
     * @param rotateDegree
     * @return 旋转 rotateDegree 之后的照片
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
    }

    /**
     *
     * @param matrix
     * @param mirror
     * @param displayOrientation
     * @param viewWidth
     * @param viewHeight
     */
    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }


    /**
     * 裁剪图片方法实现
     * @param uri
     * @return Intent
     */
    public static Intent startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        return intent;
    }
}
