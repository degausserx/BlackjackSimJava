package net.codejack.bjstats2;

import android.os.AsyncTask;
import android.os.Bundle;

import net.codejack.bjstats2.settings.SettingsContainer;
import net.codejack.bjstats2.simulation.Simulation;

/**
 * Created by Degausser on 7/2/2017.
 */

class SimulateAsync extends AsyncTask<Integer, Integer, Void> {

    private SettingsContainer settingsContainer;
    private Simulation simulation;

    //region Callback
    interface IProgressAsyncTask {
        void setProgressBar(int progress);
        void resultTask(Bundle data);
    }

    private IProgressAsyncTask callback;
    void setCallback(IProgressAsyncTask callback) {
        this.callback = callback;
    }
    //endregion

    SimulateAsync(SettingsContainer simulation) {
        settingsContainer = simulation;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        int loops = settingsContainer.getExecution().getLoops();
        int i = 1, current = 0, num;;
        simulation = new Simulation(settingsContainer.getPlayer(), settingsContainer.getHouse(), settingsContainer.getExecution());
        double j;

        while (i <= loops && !isCancelled()) {
            j = (((i + 0.0) / loops) * 100);
            num = (int) Math.floor(j);
            if (num > current) {
                current = num;
                publishProgress(current);
            }
            simulation.deal();
            i++;
        }
        publishProgress(100);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if(callback != null) {
            callback.setProgressBar(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        postData(1);
        super.onPostExecute(aVoid);
    }

    protected void onCancelled() {
        postData(0);
        super.onCancelled();
    }

    private void postData(int success) {
        Bundle data = new Bundle();
        data.putInt("success", success);
        data.putInt("games", simulation.getGames());
        data.putInt("hands", simulation.getHands());
        data.putInt("wins", simulation.getDealer_lose());
        data.putInt("losses", simulation.getDealer_win());
        data.putDouble("units_won", simulation.getUnits_won());
        data.putDouble("units_lost", simulation.getUnits_lost());
        data.putInt("blackjacks", simulation.getBlackjacks());
        data.putInt("dealer_bust", simulation.getDealer_bust());
        data.putInt("dealer_draw", simulation.getDealer_draw());
        data.putBoolean("blackjack_on_split", simulation.getBlackjackOnSplit());

        if(callback != null) {
            callback.resultTask(data);
        }
    }

}
