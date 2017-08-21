package com.shuishou.digitalmenu.ui;

import android.content.Context;
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
    }

    public ChangeLanguageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangeLanguageTextView(Context context, String txtChinese, String txtEnglish){
        super(context);
        this.txtChinese = txtChinese;
        this.txtEnglish = txtEnglish;
    }

    public void show(byte language){
        if (language == MainActivity.LANGUAGE_CHINESE){
            setText(txtChinese);
        } else if (language == MainActivity.LANGUAGE_ENGLISH){
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
