package com.moviesbyreex.utils;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkUtil {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";
    private static final String MIN_VOTE_COUNT_VALUE = "200";
    private static final String API_KEY = "bbd20fd474245f975491851e299787be";
    private static final String LANGUAGE_VALUE = "en-US";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    private static URL buildURL(int sortBy, int page) {
        URL resultURL;
        String sortMethod;
        if (sortBy == POPULARITY) {
            sortMethod = SORT_BY_POPULARITY;
        } else {
            sortMethod = SORT_BY_TOP_RATED;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, sortMethod)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();

        try {
            resultURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return resultURL;
    }

    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        URL url = buildURL(sortBy, page);
        return new JSONLoadTask().executeTask(url);
    }

    private static class JSONLoadTask {

        public JSONObject executeTask(URL url) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            JSONObject result = null;

            Callable<JSONObject> callable = () -> {
                JSONObject jsonObject;
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder builder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = bufferedReader.readLine();
                    }
                    jsonObject = new JSONObject(builder.toString());
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return jsonObject;
            };

            Future<JSONObject> future = executor.submit(callable);
            try {
                result = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
            Log.i("Json Object Result: ", result.toString());
            return result;
        }
    }
}
