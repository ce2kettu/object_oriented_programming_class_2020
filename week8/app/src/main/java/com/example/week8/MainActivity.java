package com.example.week8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private BottleDispenser bottleDispenser = BottleDispenser.getInstance();
    private TextView outputText;
    private final String receiptFileName = "receipt.txt";

    public void changeOutput(String message) {
        outputText.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
        ArrayAdapter<CharSequence> adapterSizes = ArrayAdapter.createFromResource(this, R.array.size_types, android.R.layout.simple_spinner_item);
        adapterSizes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSizes);

        final Spinner typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(this, R.array.soda_types, android.R.layout.simple_spinner_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapterTypes);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_money);
        final TextView textView = (TextView) findViewById(R.id.seekbar_text);
        outputText = (TextView) findViewById(R.id.output_text);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textView.setText("" + progress);
                textView.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.setProgress(0);

        final Button buttonBuy = findViewById(R.id.buy_button);
        final Button buttonReturnMoney = findViewById(R.id.return_button);
        final Button buttonAddMoney = findViewById(R.id.add_money_button);

        final Context context = this;

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    typeSpinner.getSelectedItem().toString();
                    String sizeStr = sizeSpinner.getSelectedItem().toString();
                    BottleSize size = BottleSize.NORMAL;

                    switch (sizeStr) {
                        case "Small":
                            size = BottleSize.SMALL;
                            break;
                        case "Normal":
                            size = BottleSize.NORMAL;
                            break;
                        case "Big":
                            size = BottleSize.BIG;
                            break;
                    }

                    BottleDispenser.BuyResult buyResult = bottleDispenser.buyBottle(typeSpinner.getSelectedItem().toString(), size);
                    changeOutput(buyResult.message);

                    if (buyResult.success) {
                        String receiptText = String.format("Receipt for purchase:\nItem name: %s\nItem price: %.2f", buyResult.bottle.getName(), buyResult.bottle.getPrice());
                        OutputStreamWriter ows = new OutputStreamWriter((context.openFileOutput(receiptFileName, Context.MODE_PRIVATE)));
                        ows.write(receiptText);
                        ows.close();
                    }
                } catch (Exception e) {
                    Log.e("WriteFileException", e.getMessage());
                }
            }
        });

        buttonReturnMoney.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = bottleDispenser.returnMoney();
                changeOutput(msg);
            }
        });

        buttonAddMoney.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = bottleDispenser.addMoney(seekBar.getProgress());
                changeOutput(msg);
                seekBar.setProgress(0);
            }
        });
    }

}
