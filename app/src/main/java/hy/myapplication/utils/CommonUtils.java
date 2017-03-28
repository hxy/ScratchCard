package hy.myapplication.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author licg 常用的工具类
 */
public class CommonUtils {
    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return 返回长度为2的int数组，ini[0] 宽度 ，ini[1] 长度
     */
    public static int[] getDeviceSize(Context context) {
        int[] deviceSize = new int[2];
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        deviceSize[0] = display.getWidth();
        deviceSize[1] = display.getHeight();
        return deviceSize;
    }
}
