package com.fireblend.uitest.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.fireblend.uitest.R;
import com.fireblend.uitest.utilities.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreferenceActivity extends AppCompatActivity {

    @BindView(R.id.edittext_size) EditText textSize;
    @BindView(R.id.edittext_color) EditText bgColor;
    @BindView(R.id.switch_enable_delete) Switch deleteEnabled;
    @BindView(R.id.switch_enable_grid) Switch gridEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        ButterKnife.bind(this);
        Integer textFontSize = PreferenceManager.getTextsizeFromPreferences(this);
        String backgroundColor = PreferenceManager.getBgColorFromPreferences(this);
        boolean deleteVisible = PreferenceManager.getDeleteVisibilityFromPreferences(this);
        deleteEnabled.setChecked(deleteVisible);
        boolean gridView = PreferenceManager.getEnableGridFromPreferences(this);
        textSize.setText(textFontSize.toString());
        bgColor.setText(backgroundColor);
        gridEnabled.setChecked(gridView);
    }


    @OnClick(R.id.button_save_preferences)
    public void savePreferences(View v){
        Integer textFontSize = Integer.parseInt(textSize.getText().toString());
        String backgroundColor = bgColor.getText().toString();
        boolean deleteVisible = deleteEnabled.isChecked();
        boolean enableGrid = gridEnabled.isChecked();
        PreferenceManager.savePreferences(this, textFontSize, backgroundColor, deleteVisible, enableGrid);
    }

    @OnClick(R.id.back)
    public void goBack(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
