package com.shuishou.digitalmenu.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuishou.digitalmenu.InstantValue;
import com.shuishou.digitalmenu.R;
import com.shuishou.digitalmenu.io.IOOperator;
import com.shuishou.digitalmenu.uibean.ChoosedFood;

import java.util.List;

/**
 * Created by Administrator on 2016/12/25.
 */

public class ChoosedFoodAdapter extends ArrayAdapter<ChoosedFood> {
    private int resourceId;
    private List<ChoosedFood> choosedFoods;
    private OperateChoosedFoodIFC operator;
    private ChangeLanguageTextView tvFoodName;

    public ChoosedFoodAdapter(Context context, int resource, List<ChoosedFood> objects){
        super(context, resource, objects);
        operator = (OperateChoosedFoodIFC) context;
        resourceId = resource;
        choosedFoods = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ChoosedFood cf = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView foodImage = (ImageView) view.findViewById(R.id.choosedfood_image);
        tvFoodName = (ChangeLanguageTextView) view.findViewById(R.id.choosedfood_name);
        TextView tvFoodPrice = (TextView) view.findViewById(R.id.choosedfood_price);
        TextView tvAmount = (TextView) view.findViewById(R.id.choosedfood_amount);
        TextView tvAddtionalRequirements = (TextView) view.findViewById(R.id.choosedfood_addtionrequirements);
        ImageView plusImage = (ImageView) view.findViewById(R.id.choosedfood_add_icon);
        ImageView minusImage = (ImageView) view.findViewById(R.id.choosedfood_minus_icon);
//        ImageView addRequirementsImage = (ImageView) view.findViewById(R.id.choosedfood_info_icon);
//        ImageView deleteImage = (ImageView) view.findViewById(R.id.choosedfood_delete_icon);

        tvAmount.setText(cf.getAmount()+"");
        tvAddtionalRequirements.setText(cf.getAdditionalRequirements());
//        foodImage.setImageResource(cf.getImageId());
        foodImage.setImageDrawable(IOOperator.getDishImageDrawable(view.getResources(), InstantValue.LOCAL_CATALOG_DISH_PICTURE + cf.getDish().getPictureName()));
        tvFoodName.setTxtEnglish(cf.getName_en());
        tvFoodName.setTxtChinese(cf.getName_cn());
        tvFoodName.show(MainActivity.getInstance().getLanguage());
        tvFoodPrice.setText("$" + String.format("%.2f", cf.getPrice()));

        plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator.plusDish(position);
            }
        });

        minusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operator.minusDish(position);
            }
        });

//        addRequirementsImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                operator.addRequirements(position);
//            }
//        });
//
//        deleteImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                operator.deleteDish(position);
//            }
//        });
        return view;
    }

}
