package com.example.week9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TheatreList theatres = new TheatreList();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    theatres.fetchAllTheatres();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTheatres();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        final EditText inputTimeBefore = (EditText) findViewById(R.id.input_time_before);
        final EditText inputTimeAfter = (EditText) findViewById(R.id.input_time_after);
        final EditText inputDate = (EditText) findViewById(R.id.input_date);
        final EditText inputMovieName = (EditText) findViewById(R.id.input_movie_name);
        final Button searchButton = (Button) findViewById(R.id.button_search);
        final Spinner sp = (Spinner) findViewById(R.id.theatre_spinner);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityResults.class);
                intent.putExtra("MOVIE_NAME", inputMovieName.getText().toString());
                intent.putExtra("TIME_BEFORE", inputTimeBefore.getText().toString());
                intent.putExtra("TIME_AFTER", inputTimeAfter.getText().toString());
                intent.putExtra("SHOW_DATE", inputDate.getText().toString());
                intent.putExtra("THEATRE_ID", theatres.getTheatres().get(sp.getSelectedItemPosition()).getId());
                startActivity(intent);
            }
        });
    }

    void updateTheatres() {
        ArrayList<String> theatreList = new ArrayList<String>();

        for (Theatre theatre : theatres.getTheatres()) {
            theatreList.add(theatre.getName());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, theatreList);
        Spinner sp = (Spinner) findViewById(R.id.theatre_spinner);
        sp.setAdapter(adp);
        sp.setSelection(0);
    }
}
