package com.shuishou.digitalmenu.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/1/24.
 */

public class ChangeLanguageTextView extends android.support.v7.widget.AppCompatTextView {
    private String txtChinese;
    private String txtEnglish;

    public ChangeLanguageTextView(Context context){
        super(context);
        setTypeface(null, Typeface.BOLD);
    }

    public ChangeLanguageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(null, Typeface.BOLD);
    }

    public ChangeLanguageTextView(Context context, String txtChinese, String txtEnglish){
        super(context);
        setTypeface(null, Typeface.BOLD);
        this.txtChinese = txtChinese;
        this.txtEnglish = txtEnglish;
    }

    public void show(byte language){
        if (language == MainActivity.LANGUAGE_CHINESE){
            if (txtChinese.length() > 10)
                setText(txtChinese.substring(0, 10) + "...");
            else
                setText(txtChinese);
        } else if (language == MainActivity.LANGUAGE_ENGLISH){
            if (txtEnglish.length() > 15)
                setText(txtEnglish.substring(0, 15) + "...");
            else
                setText(txtEnglish);
        }
    }

    public String getTxtChinese() {
        return txtChinese;
    }

    public void setTxtChinese(String txtChinese) {
        this.txtChinese = txtChinese;
    }

    public String getTxtEnglish() {
        return txtEnglish;
    }

    public void setTxtEnglish(String txtEnglish) {
        this.txtEnglish = txtEnglish;
    }
}
