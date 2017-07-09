package com.example.gleniosp.ocvcmaketccfinal.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.R;

import java.io.File;
import java.util.List;

public class SaveFileListActivity extends Activity {

    private TextView tvDialogPathSaveFileListTitle;
    private ListView lvDialogSaveFileList;
    private EditText edtDialogSaveFileInputName;
    private Button btnDialogCancel;
    private Button btnDialogSave;

    private ArrayAdapter<String> fileListArrayAdapter;

    private FileTools fileTools = null;
    private List<File> files = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_save_file);

        setResult(Activity.RESULT_CANCELED);

        fileTools = new FileTools(this);

        getWidgetReferences();
        bindEventHandler();
        initializeValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getWidgetReferences() {
        tvDialogPathSaveFileListTitle = (TextView) findViewById(R.id.tvDialogPathSaveFileListTitle);
        lvDialogSaveFileList = (ListView) findViewById(R.id.lvDialogSaveFileList);
        edtDialogSaveFileInputName = (EditText) findViewById(R.id.edtDialogSaveFileInputName);
        btnDialogCancel = (Button) findViewById(R.id.btnDialogCancel);
        btnDialogSave = (Button) findViewById(R.id.btnDialogSave);
    }

    private void bindEventHandler() {

        lvDialogSaveFileList.setOnItemClickListener(mFileClickListener);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnDialogSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String filename = edtDialogSaveFileInputName.getText().toString();
                boolean fileAlreadyExists = false;

                if (files.size() > 0) {
                    for (File file : files) {
                        if (file.getName().toLowerCase().equals(filename.toLowerCase())) {
                            fileAlreadyExists = true;
                            break;
                        }
                    }
                } else {
                    // no file, so simple save it
                    fileAlreadyExists = false;
                }

                if (fileAlreadyExists) {

                    showDialog(filename);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("FILE_NAME", filename);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void initializeValues() {
        fileListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        lvDialogSaveFileList.setAdapter(fileListArrayAdapter);

        Intent callingIntent = getIntent();

        String directory = callingIntent.getExtras().getString("DIRECTORY");
        String filename = callingIntent.getExtras().getString("FILE_NAME");

        tvDialogPathSaveFileListTitle.setText(directory);

        files = fileTools.getListFiles(new File(directory));

        if (files.size() > 0) {
            for (File file : files) {
                fileListArrayAdapter.add(file.getName());
            }
        } else {
            String noFiles = getResources().getText(R.string.activity_dialog_no_file)
                    .toString();
            fileListArrayAdapter.add(noFiles);
        }

        edtDialogSaveFileInputName.setText(filename);
    }

    private AdapterView.OnItemClickListener mFileClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String filename = ((TextView) v).getText().toString();

            edtDialogSaveFileInputName.setText(filename);
        }
    };

    void showDialog(final String filename) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Warning!");

        final TextView input = new TextView(this);
        input.setText("A file named " + filename + " already exists. \nDo you want to replace it?");

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_tv_margin);
        input.setLayoutParams(params);
        container.addView(input);

        b.setView(container);

        b.setPositiveButton("REPLACE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                Intent intent = new Intent();
                intent.putExtra("FILE_NAME", filename);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        b.setNegativeButton("CANCEL", null);
        b.show();
    }
}
