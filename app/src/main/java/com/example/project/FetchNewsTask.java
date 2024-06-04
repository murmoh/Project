package com.example.project;
import com.example.project.NewsItems;


import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class FetchNewsTask extends AsyncTask<String, Void, List<NewsItems>> {
    public interface AsyncResponse {
        void processFinish(List<NewsItems> output);
    }

    private AsyncResponse delegate = null;

    public FetchNewsTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<NewsItems> doInBackground(String... urls) {
        return fetchNewsData(urls[0]);
    }

    @Override
    protected void onPostExecute(List<NewsItems> result) {
        delegate.processFinish(result);
    }

    private List<NewsItems> fetchNewsData(String urlString) {
        List<NewsItems> newsItems = new ArrayList<>();
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            inputStream = connection.getInputStream();
            newsItems = parseXML(inputStream);
        } catch (Exception e) {
            Log.e("FetchNewsTask", "Error fetching news data", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("FetchNewsTask", "Error closing input stream", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return newsItems;
    }

    private List<NewsItems> parseXML(InputStream inputStream) throws Exception {
        List<NewsItems> newsItems = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        NodeList nodeList = document.getElementsByTagName("item");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            NewsItems newsItem = new NewsItems(
                    element.getElementsByTagName("title").item(0).getTextContent(),
                    element.getElementsByTagName("description").item(0).getTextContent(),
                    element.getElementsByTagName("link").item(0).getTextContent()
            );
            newsItems.add(newsItem);
        }
        return newsItems;
    }
}
