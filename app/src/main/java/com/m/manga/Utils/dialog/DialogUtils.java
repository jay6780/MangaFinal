package com.m.manga.Utils.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.m.manga.R;
import com.m.manga.Utils.Constants;
import com.m.manga.Utils.SPUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogUtils {
    private SharedPreferences appSettingsPrefs;
    private static final String PREF = "AppSettingsPrefs";
    private static final String NIGHT_MODE = "NightMode";
    private static final String GIF_FILENAME = "hint.gif";

    public void showHint(Context context) {
        appSettingsPrefs = context.getSharedPreferences(PREF, MODE_PRIVATE);
        boolean isNightModeOn = appSettingsPrefs.getBoolean(NIGHT_MODE, false);
        int bg = 0;
        if(isNightModeOn){
            bg = R.drawable.bg_dialog_white;
        }else{
            bg = R.drawable.bg_dialog_black;
        }
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.hint_layout))
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentBackgroundResource(bg)
                .setGravity(Gravity.CENTER)
                .setCancelable(true)
                .create();
        View dialogView = dialog.getHolderView();
        String gifUrl = "https://mjhzcevwpeoysfttzayo.supabase.co/storage/v1/object/public/asasas/hint.gif";
        ImageView iv_hint = dialogView.findViewById(R.id.iv_hint);
        TextView tv_changes = dialogView.findViewById(R.id.tv_changes);
        TextView tv_dismiss = dialogView.findViewById(R.id.tv_dismiss);
        tv_dismiss.setTextColor(isNightModeOn? Color.parseColor("#ffffff") : Color.parseColor("#000000"));
        tv_dismiss.setBackgroundResource(!isNightModeOn? R.drawable.bg_dialog_white : R.drawable.bg_dialog_black);
        tv_changes.setTextColor(!isNightModeOn? Color.parseColor("#ffffff") : Color.parseColor("#000000"));
        tv_changes.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_dismiss.setTextSize(SPUtils.getInstance().getFloat(Constants.fontSize,13f));
        tv_dismiss.setOnClickListener(view -> notShowAgain(dialog));

        File localGifFile = getLocalGifFile(context);
        if (localGifFile.exists()) {
            Glide.with(context)
                    .asGif()
                    .fitCenter()
                    .placeholder(R.mipmap.app_logo)
                    .load(localGifFile)
                    .into(iv_hint);
        } else {
            Glide.with(context)
                    .asGif()
                    .fitCenter()
                    .placeholder(R.mipmap.app_logo)
                    .load(gifUrl)
                    .into(iv_hint);
            downloadGif(context, gifUrl);
        }

        dialog.show();
    }

    private void notShowAgain(DialogPlus dialogPlus) {
        SPUtils.getInstance().put(Constants.SHOWDIALOG,true);
        dialogPlus.dismiss();
    }

    private File getLocalGifFile(Context context) {
        return new File(context.getFilesDir(), GIF_FILENAME);
    }

    private void downloadGif(Context context, String gifUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(gifUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    File outputFile = getLocalGifFile(context);

                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.close();
                    inputStream.close();

                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}