package com.example.project;
import com.example.project.NewsItems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listViewNews;
    ArrayAdapter<NewsItems> adapter;
    List<NewsItems> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNews = findViewById(R.id.listViewNews);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newsList); // Note the change here
        listViewNews.setAdapter(adapter);

        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItems selectedNews = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("details", selectedNews.getDescription()); // Use getter to access description
                startActivity(intent);
            }
        });

        new FetchNewsTask(new FetchNewsTask.AsyncResponse() {
            @Override
            public void processFinish(List<NewsItems> output) {
                newsList = output;
                adapter.clear();
                adapter.addAll(newsList);
                adapter.notifyDataSetChanged();
            }
        }).execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");
    }
}
