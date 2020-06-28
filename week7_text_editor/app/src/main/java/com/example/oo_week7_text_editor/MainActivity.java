package com.example.oo_week7_text_editor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Context context = null;
    EditText fileNameInput;
    EditText textInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonLoad = findViewById(R.id.button_load);
        final Button buttonSave = findViewById(R.id.button_save);
        fileNameInput = findViewById(R.id.input_filename);
        textInput = findViewById(R.id.input_text);
        context = MainActivity.this;

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                readFile(v);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeFile(v);
            }
        });
    }

    public void readFile(View v) {
        try {
            InputStream ins = context.openFileInput(fileNameInput.getText().toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(ins));
            String s = "";
            String content = "";

            while ((s = br.readLine()) != null) {
                content += s + "\n";
            }

            textInput.setText(content);
            ins.close();
        } catch (Exception e) {
            Log.e("ReadFileException", e.getMessage());
        }
    }

    public void writeFile(View v) {
        try {
            OutputStreamWriter ows = new OutputStreamWriter((context.openFileOutput(fileNameInput.getText().toString(), Context.MODE_PRIVATE)));
            ows.write(textInput.getText().toString());
            textInput.setText("");
            ows.close();
        } catch (Exception e) {
            Log.e("WriteFileException", e.getMessage());
        }
    }
}
