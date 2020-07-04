package com.example.week9;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ActivityResults extends AppCompatActivity {
    class MovieResult {
        Theatre theatre;
        String movie;
        Date date;
    }

    ArrayList<MovieResult> results = new ArrayList<MovieResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Hakutulokset");


        Intent intent = getIntent();
        final String timeBefore = intent.getStringExtra("TIME_BEFORE");
        final String timeAfter = intent.getStringExtra("TIME_AFTER");
        final String movieName = intent.getStringExtra("MOVIE_NAME");
        final String movieDate = intent.getStringExtra("SHOW_DATE");
        final int theatreId = intent.getIntExtra("THEATRE_ID", 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchShows(movieName, movieDate, theatreId, timeBefore, timeAfter);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateListContent();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void updateListContent() {
        ListView listView = (ListView) findViewById(R.id.results_list);

        ArrayList<String> strList = new ArrayList<String>();
        final ArrayAdapter < String > adapter = new ArrayAdapter < String >
                (this, android.R.layout.simple_list_item_1,
                        strList);

        for (MovieResult result : results) {
            adapter.add(result.movie + "\n" + result.theatre.getName() + "\nKello: " + parseTimeStrFromDate(result.date));
        }

        listView.setAdapter(adapter);
    }

    void searchShows(String movieName, String date, int theatreId, String timeBefore, String timeAfter) {
        //ArrayList<MovieResult> results = new ArrayList<MovieResult>();
        results.clear();
        String urlAddress = "https://www.finnkino.fi/xml/Schedule/";

        if (theatreId != 0) {
            urlAddress = "https://www.finnkino.fi/xml/Schedule/?area=" + theatreId + "&dt=" + date;
        }

        try {
            URL url = new URL(urlAddress);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Show");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                int theatreId_ = Integer.parseInt(element.getElementsByTagName("TheatreID").item(0).getTextContent());
                String theatreName = element.getElementsByTagName("Theatre").item(0).getTextContent();
                String showStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String movie = element.getElementsByTagName("Title").item(0).getTextContent();

                // Date does not match
                if (!date.isEmpty() && theatreId == 0) {
                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat parser2 = new SimpleDateFormat("dd.MM.yyyy");
                    Date showStartDate = parser.parse(showStart);
                    Date searchDate = parser2.parse(date);

                    if (showStartDate.compareTo(searchDate) != 0) {
                        continue;
                    }
                }

                SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date showStartDateTime = parser.parse(showStart);

                // Time range does not match
                if (!isTimeInRange(timeBefore, timeAfter, showStartDateTime)) {
                    continue;
                }

                // Movie name does not match
                if (!movieName.isEmpty() && !movie.toLowerCase().contains(movieName.toLowerCase())) {
                    continue;
                }

                MovieResult result = new MovieResult();
                result.movie = movie;
                result.theatre = new Theatre(theatreId_, theatreName);
                result.date = showStartDateTime;
                results.add(result);
            }
        } catch (Exception e) {
            Log.e("XMLException", e.toString());
        }
    }

    boolean isTimeInRange(String timeBefore, String timeAfter, Date showStart) {
        Date showStartTime = parseTimeFromDate(showStart);

        if (!timeBefore.isEmpty()) {
            Date dateBefore = parseTimeFromString(timeBefore);
            if (!(showStartTime.compareTo(dateBefore) < 0)) {
                return false;
            }
        }

        if (!timeAfter.isEmpty()) {
            Date dateAfter = parseTimeFromString(timeAfter);
            if (!(showStartTime.compareTo(dateAfter) > 0)) {
                return false;
            }
        }

        return true;
    }

    Date parseTimeFromDate(Date date) {
        Date returnDate = new Date();
        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            returnDate = parser.parse(time);
        } catch (Exception e) {

        } finally {
            return returnDate;
        }
    }

    String parseTimeStrFromDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    Date parseTimeFromString(String time) {
        Date returnDate = new Date();

        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            returnDate = parser.parse(time);
        } catch (Exception e) {

        } finally {
            return returnDate;
        }
    }
}
