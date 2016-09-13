package com.iGap.helpers;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.os.AsyncTask;
import com.iGap.adapter.G;
import com.iGap.interfaces.OnComplet;


public class HelperFetchWebsiteData extends AsyncTask<String, String, Void> {

    OnComplet  complet;
    JSONObject jo;


    /**
     * this class get a website adress and return the atribuit websit contain title and descrioption and icon
     * 
     * @param complett
     */

    public HelperFetchWebsiteData(OnComplet complett) {
        this.complet = complett;
    }


    @Override
    protected Void doInBackground(String... params) {

        try {
            // Connect to website
            Document document = Jsoup.connect(params[0]).userAgent(G.UserAgent).get();
            // Get the html document title
            String websiteTitle = document.title();

            Elements description = document.select("meta[name=description]");
            // Locate the content attribute
            String websiteDescription = description.attr("content");
            String wicon = "";
            Elements metafav = document.select("link[rel=shortcut icon]");
            try {
                if (metafav != null) {
                    wicon = metafav.first().attr("href");
                }
            }
            catch (Exception e) {

            }

            Elements metaOgImage = document.select("meta[property=og:image]");

            try {
                if (metaOgImage != null) {
                    wicon = metaOgImage.first().attr("content");
                }
            }
            catch (Exception e) {

            }
            G.cmd.addwebsite(params[0], websiteTitle, websiteDescription, wicon);
            jo = new JSONObject();
            try {
                jo.put("title", websiteTitle);
                jo.put("description", websiteDescription);
                jo.put("icon", wicon);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {

        if (jo != null) {
            if (complet != null) {
                complet.complet(true, jo.toString());
            }
        }

        super.onPostExecute(result);
    }
}
