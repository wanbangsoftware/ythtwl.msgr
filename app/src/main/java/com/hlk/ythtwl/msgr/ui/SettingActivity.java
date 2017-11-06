package com.hlk.ythtwl.msgr.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hlk.hlklib.lib.inject.ViewId;
import com.hlk.hlklib.lib.inject.ViewUtility;
import com.hlk.hlklib.lib.view.ToggleButton;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.application.App;

/**
 * <b>功能描述：</b>设置<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/06 16:22 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/06 16:22 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class SettingActivity extends BaseActivity {

    public static void open(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @ViewId(R.id.toolbar)
    private Toolbar toolbar;
    @ViewId(R.id.ui_setting_state_text)
    private TextView settingStateView;
    @ViewId(R.id.ui_setting_sound_flag)
    private ToggleButton soundToggleButton;
    @ViewId(R.id.ui_setting_vibration_flag)
    private ToggleButton vibrationToggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ViewUtility.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (App.soundFlag) {
            if (!soundToggleButton.isToggleOn()) {
                soundToggleButton.setToggleOn(true);
            }
        } else {
            if (soundToggleButton.isToggleOn()) {
                soundToggleButton.setToggleOff(true);
            }
        }
        soundToggleButton.addOnToggleChangedListener(toggleChangedListener);
        if (App.vibrateFlab) {
            if (!vibrationToggleButton.isToggleOn()) {
                vibrationToggleButton.setToggleOn(true);
            }
        } else {
            if (vibrationToggleButton.isToggleOn()) {
                vibrationToggleButton.setToggleOff(true);
            }
        }
        vibrationToggleButton.addOnToggleChangedListener(toggleChangedListener);
        settingStateView.setText(App.soundFlag || App.vibrateFlab ? R.string.ui_text_setting_open : R.string.ui_text_setting_closed);
    }

    private ToggleButton.OnToggleChangedListener toggleChangedListener = new ToggleButton.OnToggleChangedListener() {
        @Override
        public void onToggle(ToggleButton button, boolean on) {
            if (button == soundToggleButton) {
                App.soundFlag = on;
            } else if (button == vibrationToggleButton) {
                App.vibrateFlab = on;
            }
            App.resetNimMessageNotify(App.soundFlag, App.vibrateFlab);
            settingStateView.setText(App.soundFlag || App.vibrateFlab ? R.string.ui_text_setting_open : R.string.ui_text_setting_closed);
        }
    };
}
