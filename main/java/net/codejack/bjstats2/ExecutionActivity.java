package net.codejack.bjstats2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import net.codejack.bjstats2.settings.ExecutionSettings;

public class ExecutionActivity extends AppCompatActivity {

    private Intent intent;

    private ExecutionSettings execution;
    private Button save, cancel;
    private Spinner dealer, player1, player2, executions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execution);

        init();

        initValues();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) execution = (ExecutionSettings) bundle.get("object");
        else execution = new ExecutionSettings();

        dealer = (Spinner) findViewById(R.id.settings_spinner_dealer);
        player1 = (Spinner) findViewById(R.id.settings_spinner_player1);
        player2 = (Spinner) findViewById(R.id.settings_spinner_player2);
        executions = (Spinner) findViewById(R.id.settings_spinner_executions);

        save = (Button) findViewById(R.id.settings_button_save);
        cancel = (Button) findViewById(R.id.settings_button_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send back result
                finishClass();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data to object

                execution.setDealer(getCard(dealer));
                execution.setPlayer1(getCard(player1));
                execution.setPlayer2(getCard(player2));

                String s = executions.getSelectedItem().toString().replace(",", "");
                execution.setLoops(Integer.parseInt(s));

                intent.putExtra("object", (Parcelable) execution);

                // send back result
                finishClass();
            }
        });
    }

    private void initValues() {
        int loops = execution.getLoops();
        int i = 0;
        switch (loops) {
            case 10000: i = 1;
                break;
            case 100000: i = 2;
                break;
            case 1000000: i = 3;
                break;
            case 10000000: i = 4;
                break;
            case 50000000: i = 5;
                break;
            case 100000000: i = 6;
                break;
        }
        executions.setSelection(i);

        dealer.setSelection(selectCard(execution.getDealer()));
        player1.setSelection(selectCard(execution.getPlayer1()));
        player2.setSelection(selectCard(execution.getPlayer2()));
    }

    private void finishClass() {
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getCard(Spinner spinner) {
        String s = "";
        int i = spinner.getSelectedItemPosition();
        if (i == 10) s = "A";
        else if (i < 10 && i > 0) s = "" + ++i;
        return s;
    }

    private int selectCard(String card) {
        int i = 0;
        if (card.equals("A")) i = 10;
        else if (card.length() > 0) i = Integer.parseInt(card) - 1;
        return i;
    }
}