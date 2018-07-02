package com.delex.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * <h2>OkHttp3Connection</h2>
 * <p>
 * Class to handle all types of api calls
 * </p>
 *
 * @since 10/6/16.
 */
public class OkHttp3Connection {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * <h1>OkHttpRequestData</h1>
     * <p>
     * Class is use to hold three parameter i.e String object,RequestBody object and OkHttpRequestCallback
     * in a single place.
     * Because async Task takes only single parameter nad i have to send three parameter so.
     * Wrapping three things into a single object and sending one object to async task.
     * </p>
     *
     * @see RequestBody
     */
    private static class OkHttpRequestData {
        public String request_Url;
        public JSONObject requestBody;
        OkHttp3RequestCallback callbacks;
        Request_type request_type;
        String token;
        String lang_id;
    }

    /**
     * <h2>doOkhttpRequest</h2>
     * <p>
     * This method receive all the data and Store then into to single
     * array of class
     * Service Call using okHttp Request.
     * </p>
     * <p>
     * this Method Take a Request Body and a url,and OkHttpRequestCallback and does a Asyntask,
     * and does a request to the given Url
     * </p>
     *
     * @param token       , contains the session token.
     * @param request_Url contains the url of the given Service link to do performance.
     * @param requestBody contains the require data to send the given Url link.
     * @param callbacks   contains the reference to set the call back response to the calling class.
     */
    public static void doOkHttp3Connection(String token, String lang_id, String request_Url, Request_type request_type, JSONObject requestBody, OkHttp3RequestCallback callbacks) {
        OkHttpRequestData data = new OkHttpRequestData();
        data.request_Url = request_Url;
        data.requestBody = requestBody;
        data.callbacks = callbacks;
        data.request_type = request_type;
        data.token = token;
        data.lang_id = lang_id;

        /**
         * Calling the Async task to perform the Service call.*/
        new OkHttpRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
    }

    /**
     * <h1>OkHttpRequest</h1>
     * OkHttpRequest extends async task to perform the function indecently .
     * Does a service call using OkHttp client.
     * <p>
     * This class extends async task and override the method of async task .
     * on doInBackground method of async task.
     * performing a service call to th given url and sending data given to the class.
     * By the help of the OkHttpClient and sending the call back method to the calling Activity by setting
     * data to the given reference of call-Back Interface object.
     * </P>
     * If Any thing Happened to the service call like Connection Failed or any thin else.
     * Telling to the User that connection is too slow when handling Exception.
     *
     * @see Response
     * @see OkHttpClient
     */
    private static class OkHttpRequest extends AsyncTask<OkHttpRequestData, Void, String> {
        OkHttp3RequestCallback callbacks;
        boolean error = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(OkHttpRequestData... params) {
            callbacks = params[0].callbacks;
            String result = "";
            try {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(30, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                builder.writeTimeout(30, TimeUnit.SECONDS);
                OkHttpClient httpClient = builder.build();
                Request request = null;
                if (params[0].request_type.equals(Request_type.URl)) {
                    String url = getUrl(params[0].request_Url, params[0].requestBody);
                    request = new Request.Builder()
                            .addHeader("authorization", params[0].token)
                            .addHeader("lang", params[0].lang_id)
                            .addHeader("userType", "" + Constants.USER_TYPE)
                            .url(url)
                            .build();
                    Log.e("Request Body", url);
                    Log.d("getShipmentFare", "URl: " + params[0].token);

                } else if (params[0].request_type.equals(Request_type.POST)) {
                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", params[0].token)
                            .addHeader("lang", params[0].lang_id)
                            .post(body)
                            .build();

                    Log.d("getShipmentFare", "doInBackground: " + params[0].token + " , " + params[0].lang_id);

                    final Buffer buffer = new Buffer();


                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("header Body: ", request.header("authorization"));


                } else if (params[0].request_type.equals(Request_type.PUT)) {


                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", params[0].token)
                            .addHeader("lang", params[0].lang_id)
                            .addHeader("userType", "" + Constants.USER_TYPE)
                            .put(body)
                            .build();
                    final Buffer buffer = new Buffer();
                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (params[0].request_type.equals(Request_type.DELETE)) {

                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", params[0].token)
                            .addHeader("lang", params[0].lang_id)
                            .addHeader("userType", "" + Constants.USER_TYPE)
                            .delete(body)
                            .build();
                    final Buffer buffer = new Buffer();
                    try {
                        request.body().writeTo(buffer);
                        buffer.close();
                        Log.e("Request Body", buffer.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    RequestBody body = RequestBody.create(JSON, params[0].requestBody.toString());
                    request = new Request.Builder()
                            .url(params[0].request_Url)
                            .addHeader("authorization", params[0].token)
                            .addHeader("lang", params[0].lang_id)
                            .put(body)
                            .get()
                            .build();

                    Log.e("Request Body", params[0].request_Url);
                    Log.e("header Body:else: ", request.header("authorization"));

                }

                Response response = httpClient.newCall(request).execute();
                int statusCode = response.code();
                System.out.println("statusCode=" + statusCode);
                switch (statusCode) {
                    case 503:

                        error = true;
                        result = "Server Error (503 Bad Gateway)";
                        break;

                    default:
                        result = response.body().string();
                }
            } catch (UnsupportedEncodingException e) {

                error = true;
                OkHttp3Connection.printLog("UnsupportedEncodingException" + e.toString());
                result = "Connection Failed..Retry !";
                e.printStackTrace();

            } catch (IOException e) {
                error = true;
                OkHttp3Connection.printLog("Read IO exception" + e.toString());
                result = "Connection is too slow...Retry!";
                e.printStackTrace();
            } catch (Exception e) {
                error = true;
                printLog("Read Exception" + e.toString());
                result = "Connection is too slow...Retry!";
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!error) {
                callbacks.onSuccess(result);
            } else {
                callbacks.onError(result);
            }
        }
    }


    /**
     * <h2>getUnsafeOkHttpClient</h2>
     * <p>
     * method to get getUnsafeOkHttpClient okHttpClient
     * i.e. without ssl certificate
     * </P>
     *
     * @return
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            //return true;
                            HostnameVerifier hv =
                                    HttpsURLConnection.getDefaultHostnameVerifier();
                            return hv.verify("dayrunnerapp.com", session);
//                            return true;
                        }
                    }).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>OkHttp3RequestCallback</h2>
     * <p>
     * interface for OkHttp api response call back
     * </P>
     */
    public interface OkHttp3RequestCallback {
        /**
         * Called When Success result of JSON request
         *
         * @param result
         */
        void onSuccess(String result);

        /**
         * Called When Error result of JSON request
         *
         * @param error
         */
        void onError(String error);

    }

    /**
     * <h2>getUrl</h2>
     * <p>
     * method to get the url
     * </P>
     */
    private static String getUrl(String url, JSONObject jsonObject) {
        String service_url = url + "?";
        String query = "";
        Iterator<String> object_keys = jsonObject.keys();
        try {
            while (object_keys.hasNext()) {
                String keys_value = object_keys.next();
                query = query + keys_value + "=" + jsonObject.getString(keys_value) + "&";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Vallue", service_url + query);

        return service_url + query;
    }

    /**
     * <H3>Request_type</H3>
     * <p>
     * <p>
     * </p>
     */
    public enum Request_type {
        GET("getRequest"),
        URl("urlRequest"),
        POST("postRequest"),
        DELETE("deleteRequest"),
        PUT("putRequest");
        public String value;

        Request_type(String value) {
            this.value = value;
        }
    }

    public static void printLog(String message) {
        Log.d("OKHTTPCONNECTION", message);
    }

    /**
     * <h2>callGeocodingRequest</h2>
     * This method is used to call google API for geocoder
     *
     * @param url geoCoder URL
     * @return returns the response from the geocoder
     */
    public static String callGeoCodingRequest(String url) {
        System.out.println("utility url..." + url);
        url = url.replaceAll(" ", "%20");
        String resp = null;
        HttpGet httpRequest;
        try {

            httpRequest = new HttpGet(url);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            final long contentLength = bufHttpEntity.getContentLength();
            if ((contentLength >= 0)) {
                InputStream is = bufHttpEntity.getContent();
                int tobeRead = is.available();
                System.out.println("Utility callhttpRequest tobeRead.." + tobeRead);
                ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
                int ch;

                while ((ch = is.read()) != -1) {
                    bytestream.write(ch);
                }

                resp = new String(bytestream.toByteArray());
                System.out.println("Utility callhttpRequest resp.." + resp);


            }
        } catch (MalformedURLException e) {

            System.out.println("Utility callhttpRequest.." + e);
            e.printStackTrace();

        } catch (ClientProtocolException e) {

            System.out.println("Utility callhttpRequest.." + e);
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("Utility callhttpRequest.." + e);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Utility Exception.." + e);
        }
        return resp;
    }
}