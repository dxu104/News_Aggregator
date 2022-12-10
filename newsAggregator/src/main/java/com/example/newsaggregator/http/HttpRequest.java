package com.example.newsaggregator.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HttpRequest tool to implement a Http Get or Http Post request.
 *
 */
public class HttpRequest {

    /**
     * Coding format
     */
    private final static String UTF8 = "utf-8";
    /**
     * length of byte array（1MB）
     */
    private final static int BYTE_ARRAY_LENGTH = 1024 * 1024;

    /**
     * Implement Get Http request for the response
     *
     * @param url
     * @return response
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * Implement Get Http request for the response
     *
     * @param url     target url
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> headers) {
        HttpGet get = new HttpGet(url);
        return getRespString(get, headers);
    }

    /**
     * Implement Post Http request for the response
     *
     * @param url
     * @return
     */
    public static String post(String url) {
        return post(url, null, null);
    }

    /**
     * Implement Get Http request for the response
     *
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, null, params);
    }

    /**
     * Implement Post Http request for the response
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String post(String url, Map<String, String> headers, Map<String, String> params) {
        HttpPost post = new HttpPost(url);
        post.setEntity(getHttpEntity(params));
        return getRespString(post, headers);
    }

    /**
     * Implement Post Http request with Json Parameter for the response
     *
     * @param url
     * @param json
     * @return
     */
    public static String postJson(String url, String json) {
        return postJson(url, null, json);
    }

    /**
     * Implement Post Http request with Json Parameter for the response
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static String postJson(String url, Map<String, String> headers, String json) {
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        post.setEntity(new StringEntity(json, UTF8));
        return getRespString(post, headers);
    }

    /**
     * Implement Post Http request with File Parameter for the response
     *
     * @param url
     * @param params
     * @return
     */
    public static String postFile(String url, Map<String, Object> params) {
        return postFile(url, null, params);
    }

    /**
     * Implement Post Http request with File Parameter for the response
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postFile(String url, Map<String, String> headers, Map<String, Object> params) {
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (Objects.nonNull(params) && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (Objects.isNull(value)) {
                    builder.addPart(key, new StringBody("", ContentType.TEXT_PLAIN));
                } else {
                    if (value instanceof File) {
                        builder.addPart(key, new FileBody((File) value));
                    } else {
                        builder.addPart(key, new StringBody(value.toString(), ContentType.TEXT_PLAIN));
                    }
                }
            }
        }
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        return getRespString(post, headers);
    }

    /**
     * Download file
     *
     * @param url      download url
     * @param path     saving path（such as：D:/images, there will be a default path if a user do not set a path）
     * @param fileName fileName（such as：hello.jpg）
     */
    public static void download(String url, String path, String fileName) {
        HttpGet get = new HttpGet(url);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = null;
        if (Objects.isNull(path) || path.isEmpty()) {
            filePath = fileName;
        } else {
            if (path.endsWith("/")) {
                filePath = path + fileName;
            } else {
                filePath += path + "/" + fileName;
            }
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = new FileOutputStream(file); InputStream in = getRespInputStream(get, null)) {
            if (Objects.isNull(in)) {
                return;
            }
            byte[] bytes = new byte[BYTE_ARRAY_LENGTH];
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * acquire entity of a Http request, such as, HttpEntity
     *
     * @param params
     * @return HttpEntity
     */
    private static HttpEntity getHttpEntity(Map<String, String> params) {
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(pairs, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Set up a header for a Http request
     *
     * @param request
     * @param headers
     */
    private static void setHeaders(HttpUriRequest request, Map<String, String> headers) {
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            // If the headers are not empty, it will set up
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        } else {
            // Otherwise, it is empty, there will be a default one for it.
            request.setHeader("Connection", "keep-alive");
            request.setHeader("Accept-Encoding", "gzip, deflate, br");
            request.setHeader("Accept", "*/*");
            request.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        }
    }

    /**
     * Implement Http request, and get an inputstream from a Http response
     *
     * @param request
     * @return
     */
    private static InputStream getRespInputStream(HttpUriRequest request, Map<String, String> headers) {
        // Set up a header
        setHeaders(request, headers);
        // acquire a response from a Http request
        HttpResponse response = null;
        try {
            response = HttpClients.createDefault().execute(request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // acquire an Entity
        HttpEntity entity = response.getEntity();
        // acquire an inputStream from a Http response
        InputStream in = null;
        if (Objects.nonNull(entity)) {
            try {
                in = entity.getContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return in;
    }

    /**
     * Implement Post Http request, and get the Http response
     *
     * @param request
     * @return
     */
    private static String getRespString(HttpUriRequest request, Map<String, String> headers) {
        byte[] bytes = new byte[BYTE_ARRAY_LENGTH];
        int len = 0;
        try (InputStream in = getRespInputStream(request, headers);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            if (Objects.isNull(in)) {
                return "";
            }
            while ((len = in.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            return bos.toString(UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
