package net.codejack.bjstats2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import net.codejack.bjstats2.settings.SettingsContainer;
import net.codejack.bjstats2.settings.ExecutionSettings;
import net.codejack.bjstats2.settings.HouseStrategy;
import net.codejack.bjstats2.settings.PlayerStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadSettingsActivity extends AppCompatActivity {

    private SettingsContainer settingsContainer;

    private Intent intent;

    private Button button_load, button_delete, button_cancel;
    private ListView file_list;
    private ArrayAdapter adapter;
    private ArrayList<String> item_list;
    private String selected_text;
    private int selected_position;
    private View view_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_settings);

        init();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void init() {
        intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            PlayerStrategy player = (PlayerStrategy) bundle.get("player");
            HouseStrategy house = (HouseStrategy) bundle.get("house");
            ExecutionSettings execution = (ExecutionSettings) bundle.get("execution");
            settingsContainer = new SettingsContainer();
            settingsContainer.setExecution(execution);
            settingsContainer.setHouse(house);
            settingsContainer.setPlayer(player);
        }
        else {
            settingsContainer = new SettingsContainer();
            settingsContainer.setExecution(new ExecutionSettings());
            settingsContainer.setHouse(new HouseStrategy());
            settingsContainer.setPlayer(new PlayerStrategy());
        };

        view_object = new View(this);

        button_load = (Button) findViewById(R.id.load_button_load);
        button_cancel = (Button) findViewById(R.id.load_button_cancel);
        button_delete = (Button) findViewById(R.id.load_button_delete);
        file_list = (ListView) findViewById(R.id.load_list_files);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishClass();
                finish();
            }
        });

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data from file to object
                if (selected_text != null) {
                    FileInputStream fis;
                    try {

                        fis = openFileInput(selected_text + ".srl");
                        ObjectInputStream is = new ObjectInputStream(fis);
                        settingsContainer = (SettingsContainer) is.readObject();
                        is.close();
                        fis.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("player", (Parcelable) settingsContainer.getPlayer());
                    intent.putExtra("house", (Parcelable) settingsContainer.getHouse());
                    intent.putExtra("execution", (Parcelable) settingsContainer.getExecution());
                    intent.putExtra("settings_file", selected_text);

                    finishClass();
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_text != null) {
                    deleteDialog();
                }
            }
        });

        final File dir = new File(getFilesDir(), "");
        item_list = new ArrayList<>();

        for (String file : dir.list()) {
            File f = new File(file);
            String name = f.getName().substring(0, f.getName().length() - 4);
            if (!name.equals("untitled")) item_list.add(name);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, item_list);
        file_list.setClickable(true);
        file_list.setAdapter(adapter);

        file_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_text = file_list.getItemAtPosition(position).toString();
                selected_position = position;
                setHighlight(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_text = file_list.getItemAtPosition(position).toString();
                selected_position = position;
                setHighlight(view);
            }
        });
    }

    private void deleteDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_confirm_delete, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        alertDialogBuilderUserInput
                .setTitle(getResources().getString(R.string.delete_dialog_title) + " " + selected_text)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        File file = new File(getFilesDir(), selected_text + ".srl");
                        if (file.delete()) {
                            adapter.remove(selected_text);
                            if (selected_position >= adapter.getCount() || adapter.getCount() < 1) {
                                selected_text = null;
                            } else {
                                selected_text = file_list.getItemAtPosition(selected_position).toString();
                            }
                            adapter.notifyDataSetChanged();
                        }
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

    private void setHighlight(View view) {
        view_object.setBackgroundResource(0);
        view.setBackgroundResource(R.color.colorBlue);
        view_object = view;
    }

    private void finishClass() {
        setResult(RESULT_OK, intent);
        finish();
    }
}
