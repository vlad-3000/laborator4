package com.example.lab_4;

import android.os.AsyncTask;

import com.example.lab_4.service.CommonService;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.impl.client.LaxRedirectStrategy;
import cz.msebera.android.httpclient.util.EntityUtils;

public class SongAsyncTask extends AsyncTask<String, String, String> {
    public interface ResultListener {
        void result(String author, String song);
    }

    private final String url;
    private String answerJson;
    private ResultListener mListener;

    public SongAsyncTask(String url, ResultListener listener) {
        this.url = url;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            CloseableHttpClient client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
            HttpPost request = new HttpPost(url);

            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                answerJson = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CommonService.getInstance().showToast(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String[] split = answerJson.split(" - ");
        mListener.result(split[0], split[1]);
    }
}
