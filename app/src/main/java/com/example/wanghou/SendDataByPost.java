package com.example.wanghou;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//发送数据类
public class SendDataByPost {
    private String path;
    private HashMap<String, String> params;
    private String enc;
    private byte[] data;
    private String result = "";
    //初始化三个参数
    public SendDataByPost(String path, HashMap<String, String> params, String enc) {
        this.path = path;
        this.params = params;
        this.enc = enc;
    }
    //发送数据函数
    public String SendDataByPostStart() throws IOException {
        StringBuffer entryBuilder = new StringBuffer();
        if (params != null) {
            int i=0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String str1=entry.getKey();
                String str2=entry.getValue();
                entryBuilder.append(str1);
                entryBuilder.append("=");
                entryBuilder.append(URLEncoder.encode(str2, enc));
                entryBuilder.append("&");
//                Log.e("i=",String.valueOf(i++));
//                Log.e("result",URLDecoder.decode(entryBuilder.toString(), enc));
                 //有时候报错是因为灭有添加异常
            }
            entryBuilder.deleteCharAt(entryBuilder.length() - 1);
        }
        Log.e("发送函数打包的数据=", URLDecoder.decode(entryBuilder.toString(), enc));
        data = entryBuilder.toString().getBytes(enc);
        //报错是因为下函数头部没有设置MalformedURLException 异常处理
        URL url = new URL(path);
        //自动处理出错是将鼠标放在下方红色波浪线，然后先按住ALT，再按住Entry，出现修改提示
        HttpURLConnection coon = (HttpURLConnection) url.openConnection();
        coon.setRequestMethod("POST");
        coon.setConnectTimeout(10000);
        //设置post请求必要的请求头
        coon.setRequestProperty("Context-Type", "application/x-www-form-urlencoded");
        coon.setRequestProperty("Context-Length", String.valueOf(data) + "");
        //允许写出
        coon.setDoOutput(true);
        OutputStream outputStream = coon.getOutputStream();
        outputStream.write(data);
        Log.e("数据写出去了=", data.toString());
        outputStream.flush();
        outputStream.close();
        //要想传送成功下面这句话必须要写，判断了之后才可以传送成功
        if (coon.getResponseCode() == 200) {
            Log.e("请求成功=", "suncceed");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(coon.getInputStream()));
            String readline = null;
            while ((readline = bufferedReader.readLine()) != null) {
                result += readline;
            }
            bufferedReader.close();
            coon.disconnect();
            result = URLDecoder.decode(result, enc);
        }
        return result;
    }
}
