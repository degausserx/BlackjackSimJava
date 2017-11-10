package net.codejack.bjstats2;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import net.codejack.bjstats2.settings.PlayerStrategy;

public class PlayerActivity extends AppCompatActivity {

    private Intent intent;

    private GridLayout grid;
    private TextView activeView;
    private PlayerStrategy playerstrat;
    private Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        init();

        loadGrid();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) playerstrat = (PlayerStrategy) bundle.get("object");
        else playerstrat = new PlayerStrategy();

        grid = (GridLayout) findViewById(R.id.grid_game);
        save = (Button) findViewById(R.id.player_button_save);
        cancel = (Button) findViewById(R.id.player_button_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishClass();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // load base
                intent.putExtra("object", (Parcelable) playerstrat);

                // finish
                finishClass();
            }
        });

        ViewTreeObserver vto = grid.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = grid.getChildAt(13).getMeasuredWidth();
                int height = grid.getChildAt(13).getMeasuredHeight();
                sizeGrid(width, height);

            }
        });

        int childCount = grid.getChildCount();
        for (int i = 1; i < childCount; i++) {
            TextView container = (TextView) grid.getChildAt(i);
            container.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    if (view.getId() > 0) {
                        activeView = (TextView) view;

                        PopupMenu popup = new PopupMenu(PlayerActivity.this, view);
                        String id_name = view.getResources().getResourceName(view.getId());
                        id_name = id_name.split("grid_")[1];
                        String player = id_name.split("_")[0];

                        if (player.length() > 1 && player.charAt(0) != '1' && player.substring(0, 1).equals(player.substring(1, 2))) {
                            popup.getMenuInflater().inflate(R.menu.playergridpopupsplit, popup.getMenu());
                        }
                        else {
                            popup.getMenuInflater().inflate(R.menu.playergridpopup, popup.getMenu());
                        }
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {
                                    case R.id.popup_card:
                                        handleChange(activeView, "H");
                                        break;
                                    case R.id.popup_double:
                                        handleChange(activeView, "D");
                                        break;
                                    case R.id.popup_double_stand:
                                        handleChange(activeView, "E");
                                        break;
                                    case R.id.popup_split:
                                        handleChange(activeView, "S");
                                        break;
                                    case R.id.popup_stand:
                                        handleChange(activeView, "X");
                                        break;
                                    case R.id.popup_surrender:
                                        handleChange(activeView, "A");
                                        break;
                                    case R.id.popup_surrender_stand:
                                        handleChange(activeView, "B");
                                        break;
                                }

                                return true;
                            }
                        });

                        popup.show();

                    }

                }
            });
        }
    }

    private void loadGrid() {
        TextView container;
        int dealer = 0;
        String player = "";
        String text = "";

        // load base
        for (int i = 12; i < 431; i++) {
            if ((i > 209 && i < 221) || (i > 308 && i < 320)) continue;
            container = (TextView) grid.getChildAt(i);
            if ((i % 11) == 1) player = container.getText().toString();
            else {
                dealer = (i % 11);
                if (dealer == 0) dealer = 11;
                text = playerstrat.getBaseStrat(player, dealer);
                if (text != null) {
                    setBG(container, text);
                    container.setText(text);
                }
            }
        }

    }

    private void setBG(TextView c, String text) {
        if (text.equals("X")) c.setBackgroundResource(R.drawable.stand);
        else if (text.equals("H")) c.setBackgroundResource(R.drawable.card);
        else if (text.equals("D")) c.setBackgroundResource(R.drawable.doub);
        else if (text.equals("E")) c.setBackgroundResource(R.drawable.doub);
        else if (text.equals("S")) c.setBackgroundResource(R.drawable.split);
        else c.setBackgroundResource(R.drawable.surrender);
    }

    private void handleChange(TextView v, String s) {
        String id_name = v.getResources().getResourceName(v.getId()).split("grid_")[1];
        String player = id_name.split("_")[0];
        String dealerString = id_name.split("_")[1];
        if (dealerString.equals("A")) dealerString = "11";
        setBG(v, s);
        int dealer = Integer.parseInt(dealerString);
        playerstrat.putBaseStrat(player, dealer, s);
        v.setText(s);
    }

    private void sizeGrid(int width, int height) {
        // set height

        if (width > height) {
            for (int i = 1; i < 441; i++) {
                grid.getChildAt(i).setMinimumHeight(width);
            }
        }
    }

    private void finishClass() {
        setResult(RESULT_OK, intent);
        finish();
    }
}