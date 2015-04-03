package module.activity.common;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.kymjs.aframe.ui.BindView;

import module.core.BaseActivity;
import vgod.smarthome.R;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-30
 * Time: 01:41
 * FIXME
 */
public class ContactUsActivity extends BaseActivity{

    @BindView(id = R.id.activity_normal_contact_us)
    private LinearLayout contentLayout;

    @BindView(id = R.id.contact_email)
    private EditText emailText;
    @BindView(id = R.id.contact_phone)
    private EditText phoneText;
    @BindView(id = R.id.contact_content)
    private EditText contentText;
    @BindView(id = R.id.contact_submit, click = true)
    private Button submitContent;

    @Override
    protected void initData() {
        super.initData();
        contentLayout.setOnTouchListener(this);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()){
            case R.id.contact_submit:
                String email = emailText.getText().toString().trim();
                String phone = phoneText.getText().toString().trim();
                String content = contentText.getText().toString().trim();
                submit(email,phone,content);
                super.widgetClick(v);
                break;
        }
    }

    @Override
    public void setRootView() {
        super.setRootView();
        setActionBarView(true);
        setContentView(R.layout.activity_normal_contact_us);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 提交按钮
     * */
    private void submit(String email,String phone,String content){
        if (email.equals("") || email.isEmpty()) {
            Toast.makeText(this, "请输入email", Toast.LENGTH_SHORT).show();
            return;
        }else if (content.equals("") || content.isEmpty()) {
            Toast.makeText(this, "请输入您宝贵的意见", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMailByIntent(email, phone, content);
    }


    /**
     * 发送邮件
     * */
    private int sendMailByIntent(String email,String phone,String content){
        String[] reciver = new String[] { "buaaguahao2014@.com" };
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
        myIntent.putExtra(android.content.Intent.EXTRA_CC, email);
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, content + " -- by " + phone);
        startActivity(Intent.createChooser(myIntent, "意见反馈"));
        return 1;
    }


}
