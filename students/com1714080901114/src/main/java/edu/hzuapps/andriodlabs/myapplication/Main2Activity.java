package edu.hzuapps.andriodlabs.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    protected static final int CHANGE_UI=1;
    protected static final int ERROR=2;
    private EditText firstupdates;
    private ImageView firstgames;
    private ImageView dcry;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){

        public void handleMessage(Message msg) {
            if(msg.what==CHANGE_UI){
                Bitmap bitmap=(Bitmap) msg.obj;
                firstgames.setImageBitmap(bitmap);
            }
            else if (msg.what==ERROR){
                Toast.makeText(Main2Activity.this,"12",Toast.LENGTH_SHORT).show();
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firstupdates=(EditText)findViewById(R.id.firstupdate);
        firstgames=(ImageView)findViewById(R.id.firstgame);
        //dcry=(ImageView)findViewById(R.id.view1);
        /*dcry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });*/
    }

    public void onClick(View v) {
        Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
        startActivity(intent);
    }

    public void ShowToast(View view){
        Toast.makeText(this,"未有版主编辑",Toast.LENGTH_SHORT).show();
    }


    public void click(View view){
        final String path=firstupdates.getText().toString().trim();
        if(TextUtils.isEmpty(path)){
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
        else{
            new Thread()
            {
                private HttpURLConnection conn;
                private Bitmap bitmap;
                public void run(){
                    try{
                        URL url=new URL(path);
                        conn=(HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        conn.setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;"+"SV1;.NET4.0C;.NET4.0E;.NET CLR 2.0.50727;"+".NET.CLR 3.0.4506.2152;.NET CLR 3.5.30729;Shuame)");
                        int code=conn.getResponseCode();
                        if(code==200){
                            InputStream is=conn.getInputStream();
                            bitmap= BitmapFactory.decodeStream(is);
                            Message msg=new Message();
                            msg.what=CHANGE_UI;
                            msg.obj=bitmap;
                            handler.sendMessage(msg);
                        }
                        else{
                            Message msg=new Message();
                            msg.what=ERROR;
                            handler.sendMessage(msg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Message msg=new Message();
                        msg.what=ERROR;
                        handler.sendMessage(msg);
                    }
                };
            }.start();
        }
    }

}
