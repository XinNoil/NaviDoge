package cn.edu.tju.cs.navidoge.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.tju.cs.navidoge.MyApp;

/**
 * Created by doom on 15/4/2.
 */
public class AssetsHelper
{
    public static String getContent(String fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(MyApp.getContext().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            Log.v("assets",Result);
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap getBitmap(String fileName)
    {
        Bitmap bitmap=null;
        try {
            InputStream is = MyApp.getContext().getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
