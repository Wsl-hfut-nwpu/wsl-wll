package com.example.wanghou;
import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;
public class UploadFileTask extends AsyncTask<String, Void, String> {
    public  String requestURL ;
    private ProgressDialog pdialog;
    private Activity context = null;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    //这种旋转的必须通过线程来实现，以后线程如果复杂我将采用此类型，不采用new Thread处理。
    public UploadFileTask(Activity ctx,String as) {
        requestURL=as;
        this.context = ctx;
        pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求\n\n\n");
    }
    @Override    //开始提示UI/或者其他操作
    protected void onPreExecute() {
        //该函数用于在开始任务前去做一些Ui操作，就比如最上面那个就不用自己写函数实现提示框
    }
    @Override   //正在执行提示Ui如进度条/或者其他操作
    protected void onProgressUpdate(Void... values) {
        //onProgressUpdate方法用于更新进度信息，正在执行的进度展示
    }
    @Override   //结束UI如对话框/或者其他操作   用String result，调用时就用result表示唯一值，如果参数里面写成String ...result,调用时用result[0]才可以
    protected void onPostExecute(String result) {
        //当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
        pdialog.dismiss();
        if (SendPictureByPost.SUCCESS.equalsIgnoreCase(result)) {
            builder = new AlertDialog.Builder(context);
            alert = builder.setIcon(R.mipmap.quickly)
                    .setTitle("提示：")
                    .setMessage("   上传成功!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                        }
                    }).create();             //创建AlertDialog对象
            alert.show();
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "上传失败!", Toast.LENGTH_SHORT).show();
        }
    }
    //onCancelled方法用于在取消执行中的任务时更改UI
    @Override
    protected void onCancelled() {
        super.onCancelled();
        //   progressBar.setProgress(0);  直接撤销进度条
    }
    @Override
    protected String doInBackground(String... params) {
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        //换句话说，该方法是核心的执行复杂操作的函数
        File file = new File(params[0]);
        return SendPictureByPost.uploadFile(file, requestURL);
    }
}
/*
    AsyncTask里面有3个参数，分别是泛型<Params, Progress, Result>，也就是说三个参数就表示<任务，进度，结果>
    ，只不过你要用什么具体的形式来传输参数，如AsyncTask<String, Integer, String>就是一种形式，就是说任务是传入了字符串，进度
    是传入了整形，结果也是传入了字符串。如果哪一项我们不用，用Void填入就行。
    那么具体我们用什么就要看促发这些函数的函数所用到的参数用什么传递了，比如excute传入字符串，那么protected String doInBackground(String... params) 里面就是
    String的Params，如果 publishProgress((int) ((count / (float) total) * 100))这样，那么 protected void onProgressUpdate(Integer... values) 函书就要用Integer描述了
    如果
    该类的实现流程如下：
        1：由使用该类的地方实例化该类的对象，注意，一个是对象里面携带一个任务，一个任务只能执行一会，否则报出异常
           由函数继承该类的子类调用执行函数.execute（）来执行，该函数的参数就是我要处理时用到的参数，只不过，这里有一个特殊的
           名字叫做任务。
        2：紧接着系统回调函数  protected void onPreExecute() 做任务开始前准备，所以自己可以在里面实现一些开始时的UI提醒操作,
        该函数没有参数
        3：紧接着系统回调函数protected String doInBackground(String... params) 执行任务，可以看到它的参数是params，就是params的数组
        ，在execute里面的参数就会放到params里面作为一个任务，换句话说，execute（A）等价于params[0]=A，此时就将参数A传到了params[0]里面，
        后台操作函数是就可以调用params[0]处理了，这就是一个所谓的任务传递，其实就是传入了一个参数。
        在该函数里面可以调用进度条，实现显示界面，但是显示不会再这里，会在 protected void onProgressUpdate(Void... values) 函数里处理进度条显示，
        就是说该函数只有在第3条执行且代码里面使用进度条时才会使用该函数
         int count = 0;
                    int length = -1;
                    while ((length = is.read(buf)) != -1) {
                        baos.write(buf, 0, length);
                        count += length;
                        //调用publishProgress产生进度条,此时进度条这个任务将会被放到  progresses[0]里面，最后onProgressUpdate调用progressBar.setProgress(progresses[0])方法显示
                        publishProgress((int) ((count / (float) total) * 100));
                        //为了演示进度,休眠500毫秒
                        Thread.sleep(500);
                    }
           4：可以设置撤销函数， mTask.cancel(true);  此时会调用protected void onCancelled() 撤销进度条
           5:当执行完protected String doInBackground(String... params) 会返回一个串，那么这个串就是protected void onPostExecute(String result)
           函数的参数，依靠这个串实现最后UI提示
           6:该类代替了New Thread 和 Handler的强大的功能
 */
