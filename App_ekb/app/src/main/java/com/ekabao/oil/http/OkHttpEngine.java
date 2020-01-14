package com.ekabao.oil.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.ekabao.oil.global.LocalApplication;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by lj on 2016/11/22.
 */
public class OkHttpEngine {
    private String TAG = OkHttpEngine.class.getSimpleName();
    private OkHttpClient okhttp;
    private OkHttpClient.Builder builder;
    private Gson gson = new Gson();
    private HashMap<String,String> headerMap = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    private static OkHttpEngine mInstance = new OkHttpEngine();
    private OkHttpEngine(){
        builder = new OkHttpClient.Builder();
    }
    public static OkHttpEngine create(){
         return mInstance;
    }

    public OkHttpEngine connectTimeout(int timeout, TimeUnit unit){
        builder.connectTimeout(timeout,unit);
        return mInstance;
    }

    /**
     * 自定义HttpClient
     * @param client
     */
    public void setOkhttpClient(OkHttpClient client){
        okhttp = client;
    }

    /**
     * 创建okHttpClint
     */
    private void createOkhttpClient() {
        if(okhttp==null){
            //custom cache policy
//            builder.addInterceptor(new SmartCacheInterceptor());

            okhttp = builder.build();
        }
    }

    public OkHttpEngine readTimeout(int timeout, TimeUnit unit){
        builder.readTimeout(timeout,unit);
        return mInstance;
    }
    public OkHttpEngine writeTimeout(int timeout, TimeUnit unit){
        builder.writeTimeout(timeout,unit);
        return mInstance;
    }

    /**
     * 添加请求头参数
     * @param key
     * @param value
     * @return
     */
    public OkHttpEngine addHeader(String key, String value){
        headerMap.put(key,value);
        return mInstance;
    }
    public OkHttpEngine setHeaders(HashMap<String,String> headers){
        if(headers!=null){
            headerMap.putAll(headers);
        }
        return mInstance;
    }
    public OkHttpEngine setHeaders() {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("version", UrlConfig.version);
        hashMap.put("channel", "2");

        /*hashMap.put("ua", "1;1.0.0;1;API0;android;ANDROID1.0.0");

        SharePrefUtil sharePrefUtil = SharePrefUtil.create(AiyoucheApp.context);
        int tokenId = sharePrefUtil.getInt("tokenId", 0);
        String token = sharePrefUtil.getString("token", "");

        if (tokenId != 0 && !TextUtils.isEmpty(token)) {
            String base64 = getBase64(tokenId+":"+token);
            hashMap.put("Authorization", "Basic " + base64);                         //;Authorization37:3AC93091DA89285FC2FCFE4C68102AB6

        }*/
        headerMap.putAll(hashMap);

        return mInstance;
    }
    /**
     * 缓存配置
     * @param cacheDir
     * @param cacheSize
     * @return
     */
    public OkHttpEngine cache(File cacheDir, long cacheSize){
        builder.cache(new Cache(cacheDir,cacheSize));
        return mInstance;
    }


    /**
     * get请求
     * @param url
     * @param callback
     * @param 
     */
    public  Call get(String url,  OkHttpCallback callback){
        //LogUtils.e(TAG,"请求的url: "+url);
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        Request request = builder.build();
        //2.发送请求
        return executeCall( callback, request);
    }

    /**
     * post请求，不需要提交请求体参数
     * @param url
     * @param callback
     * @param 
     * @return
     */
    public  Call post(String url,  OkHttpCallback callback){
        return post(url,null,callback);
    }

    /**
     * post提交字符串内容，此时需要可以指定字符串的contentType,如果不指定。默认是text/plain
     * @param url
     * @param postContent
     * @param contentType
     * @param callback
     * @param 
     */
    public  Call post(String url,String postContent,String contentType,  OkHttpCallback callback){
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        builder.post(RequestBody.create(MediaType.parse(TextUtils.isEmpty(contentType)
            ?"text/plain":contentType),postContent));
        Request request = builder.build();
        //2.发送请求
        return executeCall( callback, request);
    }

    /**
     * 进行post请求，支持提交key/value,multipart,多文件上传
     * @param url
     * @param params    将要提交的参数封装到params中
     * @param callback
     * @param 
     */
    public  Call post(String url,PostParams params,  OkHttpCallback callback){
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        if(params!=null){
           // LogUtils.e("params"+params.toString());

            HashMap<String, Object> params1 = params.getParams();
            if (!params1.containsKey("token")) {
                params1.put("token", LocalApplication.getInstance().sharereferences.getString("token", ""));
            }
            if (!params1.containsKey("version")) {
                params1.put("version", UrlConfig.version);
            }
            if (!params1.containsKey("channel")) {
                params1.put("channel", "2");
            }


            RequestBody requestBody = createFormBody(params);
            builder.post(requestBody);
        }else if (params==null&&headerMap.size()>0){
            /**
             * params 当它为null 时,就走的是默认的get方法啦
             * */
            PostParams login = new PostParams();
           // HashMap<String, Object> logins = login.getParams();
          //  logins.put("version", UrlConfig.version);
           // logins.put("channel", "2");
            RequestBody requestBody = createFormBody(login);
            builder.post(requestBody);
            //LogUtils.e("当它为null 时,就走的是默认的get方法啦");
        }
        Request request = builder.build();
        //2.发送请求
        return executeCall( callback, request);
    }

    /**
     * 多文件上传
     * */

    public  Call post2(String url,RequestBody requestBody,  OkHttpCallback callback){
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);

        builder.post(requestBody);

        Request request = builder.build();
        //2.发送请求
        return executeCall( callback, request);
    }

    @NonNull
    private Request.Builder createRequestBuilder(String url) {
        Request.Builder builder = new Request.Builder();
        //1.addHeader
        Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            builder.addHeader(entry.getKey(),entry.getValue());
        }

        builder.url(url);
        return builder;
    }

    /**
     * 执行请求
     * @param callback
     * @param request
     * @param 
     */
    private Call executeCall( final OkHttpCallback callback, Request request) {
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if(callback!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(callback!=null){
                    final String content = response.body().string();

                    if(!TextUtils.isEmpty(content)){
                        //post data to UI Thread
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(content);

                            }
                        });

                    }
                }
            }
        });
        return call;
    }

    /**
     * 创建PostBody
     * @param params
     * @return
     */
    private RequestBody createFormBody(PostParams params) {
        RequestBody requestBody = null;
        Iterator<Map.Entry<String, Object>> iterator = params.getParams().entrySet().iterator();
        if(params.isMultipart()){
            MultipartBody.Builder builder = new MultipartBody.Builder();
            while (iterator.hasNext()){
                Map.Entry<String, Object> entry = iterator.next();
                if(entry.getValue() instanceof String){
                    builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                }else if (entry.getValue() instanceof File){
                    File file = (File) entry.getValue();
                   /* builder.addFormDataPart(entry.getKey(),file.getName(),
                            RequestBody.create(getMediaType(file),file));*/

                    //  MultipartBody.FORM .setType(MultipartBody.FORM)
                    builder.
                            addFormDataPart(entry.getKey(),file.getName(),
                            RequestBody.create(getMediaType(file),file));
                }
            }
            requestBody = builder.build();
        }else{
            FormBody.Builder builder = new FormBody.Builder();
            while (iterator.hasNext()){
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), (String) entry.getValue());
            }
            requestBody = builder.build();
        }
        return requestBody;
    }

    /**
     * 根据文件的后缀识别MediaType,只识别常见的类型
     * @param file
     * @return
     */
    private MediaType getMediaType(File file){
        MediaType mediaType = null;
        String name = file.getName();
        //这地方太坑爹了,加个1就好了,丫的逗我呢
        String subffix = name.substring(name.lastIndexOf(".")+1);
        subffix = subffix.toLowerCase();
        //LogUtil.e("subffix"+subffix);
        switch (subffix){
            case "png":
                mediaType = MediaType.parse("image/png");
                //mediaType = MediaType.parse("multipart/form-data");
                break;
            case "jpg":
           // case "JPEG":
            case "jpeg":
                mediaType = MediaType.parse("image/jpeg");
                //mediaType = MediaType.parse("multipart/form-data");
                break;
            case "webp":
                mediaType = MediaType.parse("image/webp");
                break;
            case "xml":
                mediaType = MediaType.parse("application/xml");
                break;
            case "md":
                mediaType = MediaType.parse("text/x-markdown");
                break;
            case "3gp":
                mediaType = MediaType.parse("video/3gp");
                break;
            case "mp4":
                mediaType = MediaType.parse("video/mp4");
                break;
            case "mp3":
                mediaType = MediaType.parse("audio/mpeg");
                break;
            case "avi":
                mediaType = MediaType.parse("video/x-msvideo");
                break;
            case "rmvb":
                mediaType = MediaType.parse("application/vnd.rn-realmedia-vbr");
                break;
            case "apk":
                mediaType = MediaType.parse("application/vnd.android.package-archive");
                break;
            case "exe":
                mediaType = MediaType.parse("application/octet-stream");
                break;
            case "js":
                mediaType = MediaType.parse("application/javascript");
                break;
            case "html":
            case "htm":
                mediaType = MediaType.parse("text/html");
                break;
            case "text":
            case "txt":
                mediaType = MediaType.parse("text/plain");
                break;
            case "json":
                mediaType = MediaType.parse("application/json");
                break;

        }
        return mediaType;
    }



    public interface OkHttpCallback{
        void onSuccess(String result);
        void onFail(IOException e);
    }



    
    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener){

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }




    /**
     * 响应体进度回调接口，比如用于文件下载中
     */
    public interface ProgressResponseListener {
        void onResponseProgress(long bytesRead, long contentLength, boolean done);
    }

    interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }


}
