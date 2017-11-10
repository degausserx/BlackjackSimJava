package net.codejack.bjstats2;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.codejack.bjstats2.settings.SettingsContainer;

import java.text.NumberFormat;
import java.util.Locale;

public class ProgressFragment extends Fragment implements SimulateAsync.IProgressAsyncTask, View.OnClickListener {

    private static ProgressFragment instance;
    private SimulateAsync task;

    private Button button_start;
    private ProgressBar progress;
    private boolean simulation_status;
    private RelativeLayout results_layout;

    private TextView text_result_games;
    private TextView text_result_hands;
    private TextView text_result_wins;
    private TextView text_result_losses;
    private TextView text_result_units_won;
    private TextView text_result_units_lost;
    private TextView text_result_house_edge;
    private TextView text_result_draws;
    private TextView text_result_dealer_bust;
    private TextView text_result_blackjacks;

    public static ProgressFragment getInstance() {
        if(instance == null) {
            instance = new ProgressFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        v = initView(v);
        return v;
    }

    public ProgressFragment() {

    }

    private View initView(View v) {
        progress = (ProgressBar) v.findViewById(R.id.progress_loading_frag);
        button_start = (Button) v.findViewById(R.id.button_loading_start);
        results_layout = (RelativeLayout) v.findViewById(R.id.layout_relative_results);

        text_result_games = (TextView) v.findViewById(R.id.result_box_games);
        text_result_hands = (TextView) v.findViewById(R.id.result_box_hands);
        text_result_wins = (TextView) v.findViewById(R.id.result_box_wins);
        text_result_losses = (TextView) v.findViewById(R.id.result_box_losses);
        text_result_units_won = (TextView) v.findViewById(R.id.result_box_wins_units);
        text_result_units_lost = (TextView) v.findViewById(R.id.result_box_losses_units);
        text_result_house_edge = (TextView) v.findViewById(R.id.result_box_house_edge);
        text_result_draws = (TextView) v.findViewById(R.id.result_box_draws);
        text_result_dealer_bust = (TextView) v.findViewById(R.id.result_box_dealer_bust);
        text_result_blackjacks = (TextView) v.findViewById(R.id.result_box_blackjacks);

        button_start.setOnClickListener(this);

        switchEnableButton(true);

        return v;
    }


    @Override
    public void setProgressBar(int progress) {
        this.progress.setProgress(progress);
    }

    @Override
    public void resultTask(Bundle data) {
        applyStatistics(data);
        switchEnableButton(true);
        progress.setProgress(0);
        if (results_layout.getVisibility() == View.GONE) results_layout.setVisibility(View.VISIBLE);
    }

    private void switchEnableButton(boolean p) {
        if (p) {
            button_start.setText("Start");
            button_start.setBackgroundResource(R.color.colorGreen);
            simulation_status = false;
        }
        else {
            button_start.setText("Stop");
            button_start.setBackgroundResource(R.color.colorRed);
            simulation_status = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_loading_start) {
            if (!simulation_status) {
                SettingsContainer settingsContainer = ((MainActivity) this.getActivity()).getSimulation();
                task = new SimulateAsync(settingsContainer);
                task.setCallback(this);
                switchEnableButton(false);
                task.execute(0, 100);
            }
            else {
                task.cancel(true);
            }
        }
    }

    private void applyStatistics(Bundle data) {

        // final int success = data.getInt("success");
        final int games = data.getInt("games");
        final int hands = data.getInt("hands");
        final int wins = data.getInt("wins");
        final int losses = data.getInt("losses");
        final double units_won = data.getDouble("units_won");
        final double units_lost = data.getDouble("units_lost");
        final int blackjacks = data.getInt("blackjacks");
        final int dealer_bust = data.getInt("dealer_bust");
        final int draws = data.getInt("dealer_draw");
        final boolean blackjackonsplit = data.getBoolean("blackjack_on_split");
        final double total_units = units_lost + units_won;

        double edge = ((units_lost / (total_units)) - (units_won / (total_units)));
        double black;
        if (blackjackonsplit) black = (blackjacks / (hands + 0.0));
        else black = (blackjacks / (games + 0.0));
        double bust = (dealer_bust / (games + 0.0));

        edge = (double)Math.round((edge * 100) * 1000d) / 1000d;
        black = (double)Math.round((black * 100) * 1000d) / 1000d;
        bust = (double)Math.round((bust * 100) * 1000d) / 1000d;

        String edgeString = "" + edge + "%";
        String blackjackString = "" + black + "%";
        String bustString = "" + bust + "%";

        text_result_games.setText(NumberFormat.getIntegerInstance().format(games));
        text_result_hands.setText(NumberFormat.getIntegerInstance().format(hands));
        text_result_wins.setText(NumberFormat.getIntegerInstance().format(wins));
        text_result_draws.setText(NumberFormat.getIntegerInstance().format(draws));
        text_result_losses.setText(NumberFormat.getIntegerInstance().format(losses));
        text_result_units_won.setText(NumberFormat.getNumberInstance(Locale.US).format(units_won));
        text_result_units_lost.setText(NumberFormat.getNumberInstance(Locale.US).format(units_lost));
        text_result_blackjacks.setText(blackjackString);
        text_result_dealer_bust.setText(bustString);
        text_result_house_edge.setText(edgeString);
    }
}
