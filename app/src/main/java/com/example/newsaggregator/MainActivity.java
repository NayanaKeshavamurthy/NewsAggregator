package com.example.newsaggregator;

import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsaggregator.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    private DrawerLayout drawerLayoutObject;
    private ListView drawerListObject;
    ViewPager2 viewPager;

    private ActionBarDrawerToggle drawerToggleObject;

    NewsAdapter newsAdapter;

    ArrayList<NewsArticles> listNewsArticles = new ArrayList<NewsArticles>();

    private static final String sourcesURL = "https://newsapi.org/v2/sources?";

    private static final String urlKey = "bd200bb707a844d2b9b02a8a4ca8da21";

    private RequestQueue queueObject;

    private static final String TAG = "MainActivity";

    private final HashMap<String, HashSet<String>> channelCategoryMapping = new HashMap<>();
    private final HashMap<String, String> channelIdMapping = new HashMap<>();

    private final HashMap<String, Integer> categoryColorMapping = new HashMap<>();

    private ArrayAdapter<String> arrayAdapter;
    private final ArrayList<String> channelList = new ArrayList<>();

    private final HashMap<String, Integer> colChannel = new HashMap<>();

    private ActivityMainBinding binding;

    String chosenChannel = "";
    String chosenChannelId = "";

    private Menu drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialise the drawer layout
        drawerLayoutObject = binding.drawerLayout;

        // Initialise the pager view and set its orientation =
        viewPager = binding.viewPager;
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Initialise the drawer list
        drawerListObject = binding.drawerList;

        // initialise the drawer toggle with drawer layout created
        drawerToggleObject = new ActionBarDrawerToggle(
                this, drawerLayoutObject, R.string.drawer_open, R.string.drawer_close);


        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.drawer_item, channelList)
        {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                TextView textView=(TextView) view.findViewById(R.id.drawerText);
                int col = textView.getCurrentTextColor();

                String channelName = textView.getText().toString();

                for(String category : channelCategoryMapping.keySet())
                {
                    if(channelCategoryMapping.get(category).contains(channelName) && !category.equalsIgnoreCase("All"))
                        col = categoryColorMapping.get(category);
                }
                textView.setTextColor(col);
                return textView;
            }
        };

        drawerListObject.setAdapter(arrayAdapter);

        if (hasNetworkConnection())
            downloadData();

        else
            setTitle("No Network Connection");

        // Set up the drawer item click callback method
        drawerListObject.setOnItemClickListener(
                (parent, view, position, id) -> {
                    drawerLayoutObject.closeDrawer(drawerListObject);
                    String channel = channelList.get(position);
                    String channelId = (channelIdMapping.get(channel).toString());
                    chosenChannel = channel;
                    chosenChannelId = channelId;

                    setTitle(chosenChannel);
                    listNewsArticles.clear();
                    downloadNewsArticles(channelId);
                }
        );

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // create a news article content adapter  for the list created for content and changes
        newsAdapter = new NewsAdapter(this, listNewsArticles);
        viewPager.setAdapter(newsAdapter);


    }

    public void downloadData()
    {
        // build the url to pass to volley
        Uri.Builder urlBuilder = Uri.parse(sourcesURL).buildUpon();
        urlBuilder.appendQueryParameter("apiKey",urlKey);

        // initialise the volley queue
        queueObject = Volley.newRequestQueue(this);

        // fetch the url string
        String newsSourcesUrl = urlBuilder.build().toString();

        // handle volley error
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    // loop through news sources
                    JSONArray sourcesArray = response.getJSONArray("sources");

                    for(int i = 0; i < sourcesArray.length(); i++)
                    {
                        JSONObject jsonObject = sourcesArray.getJSONObject(i);

                        // get the category and channel related details from api data
                        String channelCategory = jsonObject.getString("category");
                        String channelId = jsonObject.getString("id");
                        String channelName = jsonObject.getString("name");

                        //mapping channels to their respective categories
                        if (!channelCategoryMapping.containsKey(channelCategory))
                            channelCategoryMapping.put(channelCategory, new HashSet<>());

                        Objects.requireNonNull(channelCategoryMapping.get(channelCategory)).add(channelName);

                        if (!channelCategoryMapping.containsKey("All"))
                            channelCategoryMapping.put("All", new HashSet<>());

                        Objects.requireNonNull(channelCategoryMapping.get("All").add(channelName));

                        if (!channelIdMapping.containsKey(channelName))
                            channelIdMapping.put(channelName, channelId);
                    }

                    ArrayList<String> sortedKeys = new ArrayList<String>(channelCategoryMapping.keySet());

                    Collections.sort(sortedKeys);

                    int colorValue = 0;

                    Random randomObj = new Random();

                    for(String categoryKey : sortedKeys)
                    {
                        SpannableString colorString = new SpannableString(categoryKey);
                        colorValue = Color.rgb(randomObj.nextInt(255),randomObj.nextInt(255),randomObj.nextInt(255));
                        colorString.setSpan(new ForegroundColorSpan(colorValue),0,categoryKey.length(),0);
                        if(!categoryKey.equalsIgnoreCase("All"))
                            categoryColorMapping.put(categoryKey,colorValue);
                        drawerMenu.add(colorString);
                    }

                    channelList.addAll(channelCategoryMapping.get("All"));
                    Collections.sort(channelList);
                    arrayAdapter.notifyDataSetChanged();
                    setTitle("News Gateway"+ "("+Integer.toString(channelList.size())+")");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        // handle volley error
        Response.ErrorListener responseError = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "API response error : ",error);
            }
        };

        // json volley request
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, newsSourcesUrl, null, responseListener, responseError){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };

        // adding the request to volley queue
        queueObject.add(jsonObjectRequest);
    }

    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggleObject.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggleObject.onConfigurationChanged(newConfig);
    }

    public void downloadNewsArticles(String channelId)
    {
        setTitle(chosenChannel);
        String urlLink="https://newsapi.org/v2/top-headlines?sources="+ channelId +"&apiKey=bd200bb707a844d2b9b02a8a4ca8da21";

        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articlesArray = response.getJSONArray("articles");
                            for (int j = 0;j < articlesArray.length();j++)
                            {
                                JSONObject jsonObject = articlesArray.getJSONObject(j);
                                String author = jsonObject.getString("author");

                                String title = jsonObject.getString("title");

                                String url = jsonObject.getString("url");

                                String urlToImage = jsonObject.getString("urlToImage");

                                String publishedAt = jsonObject.getString("publishedAt");

                                String desc = jsonObject.getString("description");

                                listNewsArticles.add(new NewsArticles(author,title,desc,url,urlToImage,publishedAt));

                            }
                            /*newsAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(0);*/
                            newsAdapter.notifyItemRangeChanged(0, listNewsArticles.size());


                        } catch (Exception e) {
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try
                {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlLink,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };
        // Add the request to the RequestQueue.
        queueObject.add(jsonObjectRequest);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("Channel", chosenChannel);
        outState.putString("Channel_ID", chosenChannelId);
        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        chosenChannel = savedInstanceState.getString("Channel");
        chosenChannelId = savedInstanceState.getString("Channel_ID");

        if(!TextUtils.isEmpty(chosenChannel))
            downloadNewsArticles(chosenChannelId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        drawerMenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        //chosenChannel = "";
        //chosenChannelId = "";

        if (drawerToggleObject.onOptionsItemSelected(item))
        {
            Log.d("mainActivity", "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        listNewsArticles.clear();
        newsAdapter.notifyDataSetChanged();
        channelList.clear();

        HashSet<String> channelListTemp = channelCategoryMapping.get(item.getTitle().toString());
        if (channelListTemp != null) {
            channelList.addAll(channelListTemp);
            Collections.sort(channelList);
        }

        arrayAdapter.notifyDataSetChanged();
        setTitle("News Gateway"+ "("+Integer.toString(channelList.size())+")");
        return super.onOptionsItemSelected(item);
    }

}