package com.tauruslab.app.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private GridView mGridview;
    private MovieAdapter mMovieAdapter;

    private Map<String, String> mMoviesOrder = new TreeMap<>();

    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] keys = getResources().getStringArray(R.array.order_keys_array);
        String[] values = getResources().getStringArray(R.array.order_values_array);
        int i = 0;
        for(String key: keys) {
            mMoviesOrder.put(key, values[i++]);
        }

        mMovieAdapter = new MovieAdapter(this, 0, new ArrayList<Movie>());

        mGridview = findViewById(R.id.gridview_movies);
        mGridview.setAdapter(mMovieAdapter);

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
    }

    private void updateMoviesGrid(String moviesOrder) {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this, mMovieAdapter);
        moviesTask.execute(moviesOrder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.spinner_order);
        Spinner spinner = (Spinner) item.getActionView();

        List<String> keys = new ArrayList<>(mMoviesOrder.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, keys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String order = (String)adapterView.getItemAtPosition(position);
                if (order != null) {
                    updateMoviesGrid(mMoviesOrder.get(order));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.spinner_order || super.onOptionsItemSelected(item);
    }
}
