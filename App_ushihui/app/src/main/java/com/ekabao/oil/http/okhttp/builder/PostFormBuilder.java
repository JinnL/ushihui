package com.ekabao.oil.http.okhttp.builder;

import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.request.PostFormRequest;
import com.ekabao.oil.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public class PostFormBuilder extends OkHttpRequestBuilder
{
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build()
    {
        return new PostFormRequest(url, tag, params, headers, files).build()
        		;
    }

    public PostFormBuilder addFile(String name, String filename, File file)
    {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public static class FileInput
    {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file)
        {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString()
        {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }

    //
    @Override
    public PostFormBuilder url(String url)
    {
        this.url = url;
        return this;
    }

    @Override
    public PostFormBuilder tag(Object tag)
    {
        this.tag = tag;
        return this;
    }

    @Override
    public PostFormBuilder params(Map<String, String> params)
    {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val)
    {
        if (this.params == null)
        {
            params = new LinkedHashMap<>();
            params.put("token", LocalApplication.getInstance().sharereferences.getString("token", ""));
        }
        params.put(key, val);

        // TODO: 2019/3/27
        if (!params.containsKey("token")) {
            params.put("token", LocalApplication.getInstance().sharereferences.getString("token", ""));
        }
        if (!params.containsKey("version")) {
            params.put("version", UrlConfig.version);
        }
        if (!params.containsKey("channel")) {
            params.put("channel", "2");
        }

        return this;
    }
    //不需要传公有字段
    @Override
	public OkHttpRequestBuilder addParam(String key, String val) {
    	if (this.params == null)
        {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);

        // TODO: 2019/3/27
        if (!params.containsKey("token")) {
            params.put("token", LocalApplication.getInstance().sharereferences.getString("token", ""));
        }
        if (!params.containsKey("version")) {
            params.put("version", UrlConfig.version);
        }
        if (!params.containsKey("channel")) {
            params.put("channel", "2");
        }
        return this;
	}
    
    @Override
    public PostFormBuilder headers(Map<String, String> headers)
    {
        this.headers = headers;
        return this;
    }


    @Override
    public PostFormBuilder addHeader(String key, String val)
    {
        if (this.headers == null)
        {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

}
