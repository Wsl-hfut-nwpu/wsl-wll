package com.example.wanghou;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.docoor.BiaoZhi;
import com.example.docoor.DoctorMainActivity;
import com.example.docoor.DoctorMainFragment_3_submit_baogao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import shezhi_information_doctor_patient.Patient_information;

import static cn.bmob.newim.core.BmobIMClient.getContext;

public class MainFragment_3_Submit_AnLi extends AppCompatActivity {
    private TextView fabu_anli,yingxiang_1_shangchuan,yingxiang_2_shangchuan,yingxiang_3_shangchuan;
    private EditText question_describe;
    private EditText jieda_yingxiang;
    private ImageView yingxiang_1_huoqu,yingxiang_2_huoqu,yingxiang_3_huoqu;
    private String photoPath;
    private Uri imageUri;
    private Bitmap bitmap;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private String patient_phone;
    private Bitmap [] bitmaps;
    private String [] strings;
    private ImageButton fanhui_3;
    private final Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                        if("shangchun_succeed".equals((String)msg.obj)){
                        alert = null;
                        builder = new AlertDialog.Builder(MainFragment_3_Submit_AnLi.this);
                        alert = builder.setIcon(R.mipmap.quickly)
                                .setTitle("提示：")
                                .setMessage("   上传案例成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.dismiss();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                        Intent intent = new Intent(MainFragment_3_Submit_AnLi.this, MainActivity.class);
                        intent.putExtra("id",3);
                        BiaoZhi biaoZhi=new BiaoZhi();
                        biaoZhi.Just=true;
                        startActivity(intent);
                        //该finish没有问题
                        finish();
                        break;
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_3__submit__an_li);
        bitmaps=new Bitmap[3];
        strings=new String[3];
        patient_phone=getIntent().getStringExtra("patient_phone");
        //事件控件
        yingxiang_1_shangchuan=(TextView)findViewById(R.id.yingxiang_1_shangchuan);
        yingxiang_2_shangchuan=(TextView)findViewById(R.id.yingxiang_2_shangchuan);
        yingxiang_3_shangchuan=(TextView)findViewById(R.id.yingxiang_3_shangchuan);
        yingxiang_1_shangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(Intent.ACTION_PICK);
                intent_gallery.setType("image/*");
                startActivityForResult(intent_gallery, 200);
            }
        });
        yingxiang_2_shangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(Intent.ACTION_PICK);
                intent_gallery.setType("image/*");
                startActivityForResult(intent_gallery, 300);
            }
        });
        yingxiang_3_shangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下面函数是启动相册并且可以选择一张照片
                Intent intent_gallery = new Intent(Intent.ACTION_PICK);
                intent_gallery.setType("image/*");
                startActivityForResult(intent_gallery, 400);
            }
        });
        fabu_anli=(TextView)findViewById(R.id.fabu_anli);
        fabu_anli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(question_describe.getText().toString()) || "".equals(jieda_yingxiang.getText().toString())) {
                    alert = null;
                    //Acticity的话用自己的Activity名字代替就行，这是个新提法，要注意
                    builder = new AlertDialog.Builder(MainFragment_3_Submit_AnLi.this);
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   问题和解答不可为空")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    return;
                }else if (yingxiang_1_huoqu.getDrawable()==null||yingxiang_2_huoqu.getDrawable()==null||yingxiang_3_huoqu.getDrawable()==null) {
                    builder = new AlertDialog.Builder(MainFragment_3_Submit_AnLi.this);
                    alert = builder.setIcon(R.mipmap.quickly)
                            .setTitle("提示：")
                            .setMessage("   图片您还未上传完整")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.dismiss();
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                    return;
                }else{
                    //执行将全部数据上传服务器插入数据库，并且返回成功消息
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = Message.obtain();
                            try {
                                //图片解析为字符串太长，三张图片无法一起发送过来，准备更换方式，或者一张一张发送过来
                                HashMap<String, String> params = new HashMap<String, String>(7);
                                //Editext设置监听获取的字符串和直接获取字符串都是在用下面toString的方法
                                params.put("patient_phone", Patient_information.user_phone);
                                params.put("state", "patient_shangchuan_anli");
                                params.put("question",question_describe.getText().toString());
                                params.put("answer",jieda_yingxiang.getText().toString());
                                msg.obj = (String) new SendDataByPost("http://47.104.218.123:8080/wangluo/bingren_mainfragmnt_3", params, "utf-8").SendDataByPostStart();
                            } catch (Exception e) {
                            }
                            //数据封装到message
                            mHandler.obtainMessage(1, (String) msg.obj).sendToTarget();
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }//else结束
            }
        });
        //数据控件
        question_describe=(EditText)findViewById(R.id.question_describe);
        jieda_yingxiang=(EditText)findViewById(R.id.jieda_yingxiang);
        yingxiang_1_huoqu=(ImageView)findViewById(R.id.yingxiang_1_huoqu);
        yingxiang_2_huoqu=(ImageView)findViewById(R.id.yingxiang_2_huoqu);
        yingxiang_3_huoqu=(ImageView)findViewById(R.id.yingxiang_3_huoqu);
        fanhui_3=(ImageButton)findViewById(R.id.fanhui_3);
        fanhui_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {//相册    yingxiang_1_huoqu
            imageUri = data.getData();
            //获取照片路径
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            crop(imageUri);
            yingxiang_1_huoqu.setImageBitmap(setImageBitmap());
            UploadFileTask uploadFileTask1 = new UploadFileTask(MainFragment_3_Submit_AnLi.this,"http://47.104.218.123:8080/wangluo/Accept_Image_1");
            uploadFileTask1.execute(photoPath);
        }else if (requestCode == 300) {//相册    yingxiang_2_huoqu
            imageUri = data.getData();
            //获取照片路径
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            crop(imageUri);
            yingxiang_2_huoqu.setImageBitmap(setImageBitmap());
            UploadFileTask uploadFileTask2 = new UploadFileTask(MainFragment_3_Submit_AnLi.this,"http://47.104.218.123:8080/wangluo/Accept_Image_2");
            uploadFileTask2.execute(photoPath);
        }else if (requestCode == 400) {//相册    yingxiang_3_huoqu
            imageUri= data.getData();
            //获取照片路径
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            photoPath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            crop(imageUri);
            yingxiang_3_huoqu.setImageBitmap(setImageBitmap());
            UploadFileTask uploadFileTask3 = new UploadFileTask(MainFragment_3_Submit_AnLi.this,"http://47.104.218.123:8080/wangluo/Accept_Image_3");
            uploadFileTask3.execute(photoPath);
        } else if (requestCode == 500) {//相机，此处不用了
            photoPath = Environment.getExternalStorageDirectory() + "/image.jpg";
            crop(imageUri);
        } else if (requestCode == 600) {
            String bitmapToString = bitmapToString(bitmap);
            //请求服务器的接口，上传到服务器
        }
    }
    /**
     * 压缩图片，
     */
    private Bitmap setImageBitmap() {
        //获取imageview的宽和高
        int targetWidth = yingxiang_1_huoqu.getWidth();
        int targetHeight = yingxiang_1_huoqu.getHeight();

        //根据图片路径，获取bitmap的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        int photoWidth = options.outWidth;
        int photoHeight = options.outHeight;

        //获取缩放比例
        int inSampleSize = 1;
        if (photoWidth > targetWidth || photoHeight > targetHeight) {
            int widthRatio = Math.round((float) photoWidth / targetWidth);
            int heightRatio = Math.round((float) photoHeight / targetHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }

        //使用现在的options获取Bitmap
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }
    //把bitmap转换成字符串
    public String bitmapToString(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream btString = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btString);
        byte[] bytes = btString.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
    /**
     * 剪切图片,此处不设置了
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        //startActivityForResult(intent, 200);//同样的在onActivityResult中处理剪裁好的图片
    }
}
