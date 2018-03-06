package com.superspeed.common.kit;

import com.superspeed.common.WxPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by yanweiwen on 2017/12/6.
 */
public class HttpKit {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpKit.class);

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static String DEFAULT_CHARSET = "UTF-8";
    /** http请求链接超时和读取超时时长 */
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 15000;
    /** http ssl请求链接超时和读取超时时长 */
    private static final int SSL_CONNECT_TIMEOUT = 25000;
    private static final int SSL_READ_TIMEOUT = 25000;
    private static final boolean IS_DO_OUTPUT = true;
    private static final boolean IS_DO_INPUT = true;
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, " +
            "like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private HttpKit() {
    }

    /**
     * https 域名校验
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * https 证书管理
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }



    private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
    private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpKit.TrustAnyHostnameVerifier();

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = {new HttpKit.TrustAnyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS");    // ("TLS", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCharSet(String charSet) {
        if (StrKit.isBlank(charSet)) {
            throw new IllegalArgumentException("charSet can not be blank.");
        }
        HttpKit.DEFAULT_CHARSET = charSet;
    }

    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers,
            boolean isProxyModel) throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
            KeyManagementException {
        URL _url = new URL(url);

        HttpURLConnection conn = null;

        if (isProxyModel) {
            // http 代理
            String proxyIp = WxPayConfig.getInstance().getProxyIp();
            int proxyPort = WxPayConfig.getInstance().getProxyPort();
            InetSocketAddress socketAddress = new InetSocketAddress(proxyIp, proxyPort);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
            //以下三行是在需要验证时，输入帐号密码信息
            //String headerkey = "Proxy-Authorization";
            //帐号密码用:隔开，base64加密方式
            //String headerValue = "Basic "+ Base64.encode("atco:atco".getBytes());
            //conn.setRequestProperty(headerkey, headerValue);
            conn = (HttpURLConnection) _url.openConnection(proxy);
        } else {
            conn = (HttpURLConnection) _url.openConnection();
        }

        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection) conn).setHostnameVerifier(trustAnyHostnameVerifier);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(IS_DO_OUTPUT);
        conn.setDoInput(IS_DO_INPUT);

        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        conn.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return conn;
    }

    /**
     * Send GET request
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param headers 请求头
     * @param isProxyModel 是否启用代理模式
     * @return {@link String}
     */
    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers, boolean
            isProxyModel) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers, isProxyModel);
            conn.connect();
            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Send GET request with proxy
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param headers 请求头
     * @return {@link String}
     */
    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
        return get(url, queryParas, null, true);
    }

    public static String get(String url, Map<String, String> queryParas) {
        return get(url, queryParas, null);
    }

    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * Send GET request without proxy
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param headers 请求头
     * @return {@link String}
     */
    public static String getWithoutProxy(String url, Map<String, String> queryParas, Map<String, String> headers) {
        return get(url, queryParas, null, false);
    }

    public static String getWithoutProxy(String url, Map<String, String> queryParas) {
        return getWithoutProxy(url, queryParas, null);
    }

    public static String getWithoutProxy(String url) {
        return getWithoutProxy(url, null, null);
    }

    /**
     * Send POST request
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param headers 请求头
     * @param isProxyModel 是否启用代理模式
     * @return {@link String}
     */
    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers,
                              boolean isProxyModel) {
        HttpURLConnection conn = null;
        OutputStream out;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), POST, headers, isProxyModel);
            conn.connect();

            if (data != null) {
                out = conn.getOutputStream();
                out.write(data.getBytes(DEFAULT_CHARSET));
                out.flush();
                out.close();
            }

            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Send POST request with proxy
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param data 携带数据返回给服务端
     * @return {@link String}
     */
    public static String post(String url, Map<String, String> queryParas, String data) {
        return post(url, queryParas, data, null, true);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return post(url, null, data, headers, true);
    }

    public static String post(String url, String data) {
        return post(url, null, data, null, true);
    }

    /**
     * Send POST request without proxy
     * @param url 请求路径
     * @param queryParas 请求参数
     * @param data 携带数据返回给服务端
     * @return {@link String}
     */
    public static String postWithoutProxy(String url, Map<String, String> queryParas, String data) {
        return post(url, queryParas, data, null, false);
    }

    public static String postWithoutProxy(String url, String data, Map<String, String> headers) {
        return post(url, null, data, headers, false);
    }

    public static String postWithoutProxy(String url, String data) {
        return post(url, null, data, null, false);
    }

    /**
     * read data from response
     * @param conn
     * @return
     */
    private static String readResponseString(HttpURLConnection conn) {
        BufferedReader reader = null;
        try {
            StringBuilder ret;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_CHARSET));
            String line = reader.readLine();
            if (line != null) {
                ret = new StringBuilder();
                ret.append(line);
            } else {
                return "";
            }

            while ((line = reader.readLine()) != null) {
                ret.append('\n').append(line);
            }
            return ret.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Build queryString of the url
     * @param url
     * @param queryParas
     * @return
     */
    private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas == null || queryParas.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        boolean isFirst;
        if (url.indexOf('?') == -1) {
            isFirst = true;
            sb.append('?');
        } else {
            isFirst = false;
        }

        for (Entry<String, String> entry : queryParas.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append('&');
            }

            String key = entry.getKey();
            String value = entry.getValue();
            if (StrKit.notBlank(value)) {
                try {
                    value = URLEncoder.encode(value, DEFAULT_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            sb.append(key).append('=').append(value);
        }
        return sb.toString();
    }

    /**
     * 从请求域中读取数据
     * @param request
     * @return
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder ret;
            br = request.getReader();

            String line = br.readLine();
            if (line != null) {
                ret = new StringBuilder();
                ret.append(line);
            } else {
                return "";
            }

            while ((line = br.readLine()) != null) {
                ret.append('\n').append(line);
            }

            return ret.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Send SSL POST request with proxy
     * @param url      请求的地址
     * @param data     xml数据
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return String 回调的xml信息
     */
    public static String postSSL(String url, String data, String certPath, String certPass) {
        return postSSL(url, data, certPath, certPass, true);
    }

    /**
     * Send SSL POST request without proxy
     * @param url      请求的地址
     * @param data     xml数据
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return String 回调的xml信息
     */
    public static String postSSLWithoutProxy(String url, String data, String certPath, String certPass) {
        return postSSL(url, data, certPath, certPass, false);
    }

    private static HttpURLConnection getHttpSSLConnection(String url, String data, String certPath, String certPass,
                boolean isProxyModel) throws IOException, NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, CertificateException, UnrecoverableKeyException {
        HttpsURLConnection conn = null;
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, certPass.toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();
        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        sslContext.init(kms, null, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        URL _url = new URL(url);
        if (isProxyModel) {
            // https 代理
            String proxyIp = WxPayConfig.getInstance().getProxyIp();
            int proxyPort = WxPayConfig.getInstance().getProxyPort();
            InetSocketAddress socketAddress = new InetSocketAddress(proxyIp, proxyPort);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
            conn = (HttpsURLConnection) _url.openConnection(proxy);
        } else {
            conn = (HttpsURLConnection) _url.openConnection();
        }

        conn.setConnectTimeout(SSL_CONNECT_TIMEOUT);
        conn.setReadTimeout(SSL_READ_TIMEOUT);
        conn.setRequestMethod(POST);
        conn.setDoOutput(IS_DO_OUTPUT);
        conn.setDoInput(IS_DO_INPUT);

        conn.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        return conn;
    }

    /**
     * 涉及资金回滚的接口会使用到商户证书，包括退款、撤销接口的请求
     * @param url      请求的地址
     * @param data     xml数据
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return String 回调的xml信息
     */
    public static String postSSL(String url, String data, String certPath, String certPass, boolean isProxyModel) {
        HttpURLConnection conn = null;
        OutputStream out;
        try {
            conn = getHttpSSLConnection(url, data, certPath, certPass, isProxyModel);
            conn.connect();

            if (data != null) {
                out = conn.getOutputStream();
                out.write(data.getBytes(DEFAULT_CHARSET));
                out.flush();
                out.close();
            }

            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * closeQuietly
     * @param closeable 自动关闭
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}


