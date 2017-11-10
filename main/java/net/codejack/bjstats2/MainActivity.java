package net.codejack.bjstats2;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.codejack.bjstats2.settings.ExecutionSettings;
import net.codejack.bjstats2.settings.HouseStrategy;
import net.codejack.bjstats2.settings.PlayerStrategy;
import net.codejack.bjstats2.settings.SettingsContainer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Context context = this;

    public static final int INTENT_PLAYER = 1;
    public static final int INTENT_HOUSE = 2;
    public static final int INTENT_EXECUTION = 3;
    public static final int INTENT_LOAD_FILE = 4;

    private PlayerStrategy player_strat;
    private HouseStrategy house_strat;
    private ExecutionSettings execution_strat;
    private SettingsContainer settingsContainer;

    private Button save, load;
    private TextView player, house, execution, settings, file_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        processLoad();

        showProgressFragment();
    }

    private void init() {

        save = (Button) findViewById(R.id.button_file_save);
        load = (Button) findViewById(R.id.button_file_load);
        file_read = (TextView) findViewById(R.id.text_file_read);

        player = (TextView) findViewById(R.id.main_select_player);
        house = (TextView) findViewById(R.id.main_select_house);
        execution = (TextView) findViewById(R.id.main_select_execution);

        save.setOnClickListener(this);
        load.setOnClickListener(this);
        player.setOnClickListener(this);
        house.setOnClickListener(this);
        execution.setOnClickListener(this);

        player_strat = new PlayerStrategy();
        house_strat = new HouseStrategy();
        execution_strat = new ExecutionSettings();
        settingsContainer = new SettingsContainer();
        settingsContainer.setPlayer(player_strat);
        settingsContainer.setHouse(house_strat);
        settingsContainer.setExecution(execution_strat);
    }

    private void showProgressFragment() {
        ProgressFragment fragment = ProgressFragment.getInstance();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.add(R.id.fragment_progress, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_select_player: sendIntent(PlayerActivity.class, "player_strat");
                break;
            case R.id.main_select_house: sendIntent(HouseActivity.class, "house_strat");
                break;
            case R.id.main_select_execution: sendIntent(ExecutionActivity.class, "execution_strat");
                break;
            case R.id.button_file_save: openDialog();
                break;
            case R.id.button_file_load: sendIntent(LoadSettingsActivity.class, "settings_object");
        }
    }

    private void sendIntent(Class a, String s) {
        Intent intent = new Intent(getBaseContext(), a);
        int i = 0;
        switch (s) {
            case "player_strat": intent.putExtra("object", (Parcelable) player_strat);
                i = INTENT_PLAYER;
                break;
            case "house_strat": intent.putExtra("object", (Parcelable) house_strat);
                i =INTENT_HOUSE;
                break;
            case "execution_strat": intent.putExtra("object", (Parcelable) execution_strat);
                i = INTENT_EXECUTION;
                break;
            case "settings_object":
                intent.putExtra("player", (Parcelable) player_strat);
                intent.putExtra("house", (Parcelable) house_strat);
                intent.putExtra("execution", (Parcelable) execution_strat);
                i = INTENT_LOAD_FILE;
                break;
        }
        startActivityForResult(intent, i);
    }

    private void sendToast(String s) {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void openDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        if (!getText(file_read).equalsIgnoreCase("untitled")) {
            userInputDialogEditText.setText(getText(file_read));
            file_read.append("");
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        saveSettings(userInputDialogEditText);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void saveSettings(EditText e) {
        String s = getText(e);
        if (s.equalsIgnoreCase("untitled")) sendToast("Invalid name: " + s);
        else if (s.equalsIgnoreCase("")) sendToast("No name provided");
        else saveToFile(s);
    }

    private void saveToFile(String name) {
        if (processSave(name)) {
            file_read.setText(name);
        }
        else sendToast("Failed to save file");
    }

    public boolean processSave(String name) {
        settingsContainer.setPlayer(this.player_strat);
        settingsContainer.setExecution(this.execution_strat);
        settingsContainer.setHouse(this.house_strat);
        try {
            FileOutputStream fos = openFileOutput(name.toLowerCase() + ".srl", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(settingsContainer);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void processLoad() {
        FileInputStream fis;
        try {
            fis = openFileInput("untitled.srl");
            ObjectInputStream is = new ObjectInputStream(fis);
            settingsContainer = (SettingsContainer) is.readObject();
            is.close();
            fis.close();

            player_strat = settingsContainer.getPlayer();
            house_strat = settingsContainer.getHouse();
            execution_strat = settingsContainer.getExecution();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getText(EditText e) {
        return e.getText().toString().trim();
    }

    private String getText(TextView e) {
        return e.getText().toString().trim();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_PLAYER) {
            this.player_strat = data.getParcelableExtra("object");
            settingsContainer.setPlayer(player_strat);
            processSave("untitled");
        }
        else if (requestCode == INTENT_HOUSE) {
            this.house_strat = data.getParcelableExtra("object");
            settingsContainer.setHouse(house_strat);
            processSave("untitled");
        }
        else if (requestCode == INTENT_EXECUTION) {
            this.execution_strat = data.getParcelableExtra("object");
            settingsContainer.setExecution(execution_strat);
            processSave("untitled");
        }
        else if (requestCode == INTENT_LOAD_FILE) {
            player_strat = data.getParcelableExtra("player");
            house_strat = data.getParcelableExtra("house");
            execution_strat = data.getParcelableExtra("execution");
            settingsContainer.setPlayer(player_strat);
            settingsContainer.setHouse(house_strat);
            settingsContainer.setExecution(execution_strat);
            final String s = data.getStringExtra("settings_file");
            if (s != null) file_read.setText(s);
            processSave("untitled");
        }
    }

    public SettingsContainer getSimulation() {
        return this.settingsContainer;
    }
}