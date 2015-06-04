package utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-01-06
 * Time: 18:27
 * 常见的帮助类
 */
public class ViewUtils {
    private static ViewUtils instance;
    private ViewUtils(){}

    public static ViewUtils getInstance(){
        if (instance == null){
            synchronized (ViewUtils.class){
                if (instance == null)
                    instance = new ViewUtils();
            }
        }
        return instance;
    }


    /**
     * 得到自定义的progressDialog
     * @param context Context
     * @return
     */
    public Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    /**
     * 创建一个PopupWindow
     * @param context
     * @return
     */
    public PopupWindow getBottomPopupWindow(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_show_menu, null);
        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        TextView setting = (TextView)view.findViewById(R.id.pop_menu_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("设置被点击");
                popupWindow.dismiss();
            }
        });

        TextView about = (TextView)view.findViewById(R.id.pop_menu_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("关于被点击");
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    /**
     * 显示通知栏的信息
     * @param context
     *              Context 上下文
     * @param title
     *              String 显示的标题
     * @param content
     *              String 显示的内容
     * @param largeIcon
     *              Bitmap 显示的大图
     * @param notificationIntent
     *              PendingIntent 点击之后传递的Intent
     * @param id
     *              int Notification的ID
     */
    @SuppressWarnings("NewApi")
    public void showNotification(Context context, String title, String content, Bitmap largeIcon, Intent notificationIntent, int id){
        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 定义Notification的各种属性
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.login_default_avatar)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(largeIcon)
                .build();

        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
        //FLAG_ONGOING_EVENT 通知放置在正在运行
        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
        //notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
        //DEFAULT_LIGHTS  使用默认闪光提示
        //DEFAULT_SOUNDS  使用默认提示声音
        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
        notification.defaults = Notification.DEFAULT_LIGHTS;
        //叠加效果常量
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS =5000; //闪光时间，毫秒

        // 设置通知的事件消息
        PendingIntent contentItent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.contentIntent = contentItent;
        // 把Notification传递给NotificationManager
        notificationManager.notify(0, notification);
    }

    /**
     * 取消Notification的显示
     * @param context
     *          Context 上下文
     * @param id
     *          int id
     */
    public void clearNotification(Context context, int id){
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(id);
    }


    /**
     * 获取屏幕截图
     * @param context
     */
    public Bitmap getScreenCapture(Activity context){
        Bitmap bmp;
        //获取屏幕
        View decorView = context.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        bmp = decorView.getDrawingCache();
        return bmp;
    }

    /**
     * 根据view来生成bitmap图片，可用于截图功能
     */
    public Bitmap getScreenCapture(View v) {
        v.clearFocus(); //
        v.setPressed(false); //
        // 能画缓存就返回false
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    /**
     * 模糊处理
     * @param context
     * @param bitmap
     * @return
     */
    @SuppressWarnings("NewApi")
    public Bitmap blurBitmap(Context context, Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        blurScript.setRadius(25.f);
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        allOut.copyTo(outBitmap);
        bitmap.recycle();
        rs.destroy();
        return outBitmap;
    }

    /**
     * 将彩色图转换为纯黑白二色
     *
     * @param bmp
     * @return 返回转换好的位图
     */
    public Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return resizeBmp;
    }

    /**
     * 转换透明度
     * @return
     */
    public Bitmap createAlphaBitmap(Context context, int alpha){
        Drawable drawable = context.getResources().getDrawable(R.drawable.black);
        Bitmap bmp = FormatUtils.getInstance().drawable2Bitmap(drawable);
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                //转化成灰度像素
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }

        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    public Bitmap combimeBitmap(Context context, Bitmap resource, int alpha){
        Bitmap backBitmap = createAlphaBitmap(context, alpha);
        if (backBitmap == null) {
            return null;
        }
        int bgWidth = backBitmap.getWidth();
        int bgHeight = backBitmap.getHeight();
        int fgWidth = resource.getWidth();
        int fgHeight = resource.getHeight();
        Bitmap newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(backBitmap, 0, 0, null);
        canvas.drawBitmap(resource, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }
}
