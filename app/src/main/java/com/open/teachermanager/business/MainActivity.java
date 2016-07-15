package com.open.teachermanager.business;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.open.teachermanager.R;
import com.open.teachermanager.business.baseandcommon.BaseActivity;
import com.open.teachermanager.factory.RequiresPresenter;


@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> {
    SimpleDraweeView mAvatorImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPresenter().registJpush();
        Uri uri = Uri.parse("http://b.hiphotos.baidu.com/image/pic/item/7a899e510fb30f2493c8cbedcc95d143ac4b0389.jpg");
        mAvatorImg= (SimpleDraweeView) findViewById(R.id.mAvatorImg);
        mAvatorImg.setImageURI(uri);
    }
}
