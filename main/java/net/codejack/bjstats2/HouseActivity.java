package net.codejack.bjstats2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import net.codejack.bjstats2.settings.HouseStrategy;

public class HouseActivity extends AppCompatActivity implements View.OnClickListener {

    private HouseStrategy house_strat;

    private Intent intent;

    private Button button_save, button_cancel;
    private Switch check_draw17, check_holecard, check_doubleonsoft, check_splitacesone, check_splitacesnobjs
            , check_resplitaces, check_surrender, check_surrendervsace, check_surrenderearly;
    private Spinner decks, max_splits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

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

        if (bundle != null) house_strat = (HouseStrategy) bundle.get("object");
        else house_strat = new HouseStrategy();

        button_save = (Button) findViewById(R.id.house_button_save);
        button_cancel = (Button) findViewById(R.id.house_button_cancel);

        button_cancel.setOnClickListener(this);
        button_save.setOnClickListener(this);

        check_draw17 = (Switch) findViewById(R.id.house_draw_17);
        check_holecard = (Switch) findViewById(R.id.house_hole_card);
        check_doubleonsoft = (Switch) findViewById(R.id.house_double_soft);
        check_splitacesone = (Switch) findViewById(R.id.house_split_aces_one);
        check_splitacesnobjs = (Switch) findViewById(R.id.house_split_aces_noblackjacks);
        check_resplitaces = (Switch) findViewById(R.id.house_aces_no_resplit);
        check_surrender = (Switch) findViewById(R.id.house_surrender);
        check_surrendervsace = (Switch) findViewById(R.id.house_surrender_ace);
        check_surrenderearly = (Switch) findViewById(R.id.house_surrender_early);

        check_draw17.setOnClickListener(this);
        check_holecard.setOnClickListener(this);
        check_doubleonsoft.setOnClickListener(this);
        check_splitacesone.setOnClickListener(this);
        check_splitacesnobjs.setOnClickListener(this);
        check_resplitaces.setOnClickListener(this);
        check_surrender.setOnClickListener(this);
        check_surrendervsace.setOnClickListener(this);
        check_surrenderearly.setOnClickListener(this);

        decks = (Spinner) findViewById(R.id.house_spinner_decks);
        max_splits = (Spinner) findViewById(R.id.house_spinner_max_splits);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.house_button_cancel: finishClass();
                break;
            case R.id.house_button_save: saveData();
                break;
            case R.id.house_draw_17: toggleCheck(check_draw17);
                break;
            case R.id.house_hole_card: toggleCheck(check_holecard);
                break;
            case R.id.house_double_soft: toggleCheck(check_doubleonsoft);
                break;
            case R.id.house_split_aces_one: toggleCheck(check_splitacesone);
                break;
            case R.id.house_split_aces_noblackjacks: toggleCheck(check_splitacesnobjs);
                break;
            case R.id.house_aces_no_resplit: toggleCheck(check_resplitaces);
                break;
            case R.id.house_surrender: toggleCheck(check_surrender);
                break;
            case R.id.house_surrender_ace: toggleCheck(check_surrendervsace);
                break;
            case R.id.house_surrender_early: toggleCheck(check_surrenderearly);
                break;
        }
    }

    private void toggleCheck(Switch c) {
        c.toggle();
    }

    private void saveData() {
        // add all variables to houseStrategy class
        if (check_holecard.isChecked()) house_strat.setHolecard(true);
        else house_strat.setHolecard(false);
        if (check_draw17.isChecked()) house_strat.setDraw17(true);
        else house_strat.setDraw17(false);
        if (check_doubleonsoft.isChecked()) house_strat.setDoubleonsoft(true);
        else house_strat.setDoubleonsoft(false);
        if (check_splitacesone.isChecked()) house_strat.setSplitacesone(true);
        else house_strat.setSplitacesone(false);
        if (check_splitacesnobjs.isChecked()) house_strat.setSplitacesnobjs(true);
        else house_strat.setSplitacesnobjs(false);
        if (check_resplitaces.isChecked()) house_strat.setResplitaces(true);
        else house_strat.setResplitaces(false);
        if (check_surrender.isChecked()) house_strat.setSurrender(true);
        else house_strat.setSurrender(false);
        if (check_surrendervsace.isChecked()) house_strat.setSurrendervsace(true);
        else house_strat.setSurrendervsace(false);
        if (check_surrenderearly.isChecked()) house_strat.setSurrenderearly(true);
        else house_strat.setSurrenderearly(false);

        int i = decks.getSelectedItemPosition();
        if (i == 5) i = 8;
        else if (i == 4) i = 6;
        else i++;
        house_strat.setDecks(i);

        i = max_splits.getSelectedItemPosition();
        if (i == 3) i = 0;
        else i = i + 1;
        house_strat.setMaxsplits(i);

        intent.putExtra("object", (Parcelable) house_strat);

        // send back result
        finishClass();
    }

    private void initValues() {
        if (house_strat.getHolecard()) toggleCheck(check_holecard);
        if (house_strat.getDraw17()) toggleCheck(check_draw17);
        if (house_strat.getDoubleonsoft()) toggleCheck(check_doubleonsoft);
        if (house_strat.getSplitacesone()) toggleCheck(check_splitacesone);
        if (house_strat.getSplitacesnobjs()) toggleCheck(check_splitacesnobjs);
        if (house_strat.getResplitaces()) toggleCheck(check_resplitaces);
        if (house_strat.getSurrender()) toggleCheck(check_surrender);
        if (house_strat.getSurrendervsace()) toggleCheck(check_surrendervsace);
        if (house_strat.getSurrenderearly()) toggleCheck(check_surrenderearly);

        int i = house_strat.getDecks();
        if (i == 6) i = 4;
        else if (i == 8) i = 5;
        else i--;
        decks.setSelection(i);

        i = house_strat.getMaxsplits();
        if (i == 0) i = 3;
        else i--;
        max_splits.setSelection(i);
    }

    private void finishClass() {
        setResult(RESULT_OK, intent);
        finish();
    }
}