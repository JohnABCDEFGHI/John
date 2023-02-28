package com.gyh.Hamburger.common;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Base64;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 七牛云上传文件工具类
 */
public class QiniuCloudUtil {

    // 设置需要操作的账号的AK和SK                  （AK和SK均在七牛云中获得，以下会说明）
    private static final String ACCESS_KEY = "ACCESS_KEY";
    private static final String SECRET_KEY = "SECRET_KEY";

    // 要上传的空间                               （刚刚新建空间的名称）
    private static final String bucketname = "bucketname";

    // 密钥
    private static final Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    //新建空间时，七牛云分配出的域名 （自己可在万网购买域名解析后，绑定到加速域名）
    private static final String DOMAIN = "rknrnoljd.hn-bkt.clouddn.com";

    public static String getUpToken() {
        return auth.uploadToken(bucketname, null, 3600, new StringMap().put("insertOnly", 1));
    }

    //base64方式上传
    public static String put64image(byte[] base64, String key) throws Exception {
        String file64 = Base64.encodeToString(base64, 0);
        Integer len = base64.length;

        //华北空间使用 upload-z1.qiniu.com，华南空间使用 upload-z2.qiniu.com，北美空间使用 upload-na0.qiniu.com
        String url = "http://upload-z2.qiniu.com/putb64/" + len + "/key/" + UrlSafeBase64.encodeToString(key);

        RequestBody rb = RequestBody.create(null, file64);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/octet-stream")
                .addHeader("Authorization", "UpToken " + getUpToken())
                .post(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        System.out.println(response);
        //返回图片地址   https://q5jhgxz4q.bkt.clouddn.com/812bbd78-62d3-44bc-836c-9ee27ba4866a
        //用此地址可在网页中访问到上传的图片
        return DOMAIN + key;
    }

    /**
     * @param key 图片的文件名
     * @Explain 删除空间中的图片
     */
    public static void delete(String key) {
        BucketManager bucketManager = new BucketManager(auth);
        try {
            bucketManager.delete(bucketname, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
