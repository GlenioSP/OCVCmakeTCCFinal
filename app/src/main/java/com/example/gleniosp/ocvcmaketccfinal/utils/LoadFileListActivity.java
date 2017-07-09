package com.example.gleniosp.ocvcmaketccfinal.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.R;

import java.io.File;
import java.util.List;

public class LoadFileListActivity extends Activity {

    private TextView tvDialogPathLoadFileListTitle;
    private ListView lvDialogLoadFileList;
    private Button btnDialogCancel;

    private ArrayAdapter<String> fileListArrayAdapter;

    private FileTools fileTools = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_load_file);

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
        tvDialogPathLoadFileListTitle = (TextView) findViewById(R.id.tvDialogPathLoadFileListTitle);
        lvDialogLoadFileList = (ListView) findViewById(R.id.lvDialogLoadFileList);
        btnDialogCancel = (Button) findViewById(R.id.btnDialogCancel);
    }

    private void bindEventHandler() {
        lvDialogLoadFileList.setOnItemClickListener(mFileClickListener);

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initializeValues() {
        fileListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        lvDialogLoadFileList.setAdapter(fileListArrayAdapter);


        Intent callingIntent = getIntent();

        String directory = callingIntent.getExtras().getString("DIRECTORY");

        tvDialogPathLoadFileListTitle.setText(directory);

        List<File> files = fileTools.getListFiles(new File(directory));

        if (files.size() > 0) {
            for (File file : files) {
                fileListArrayAdapter.add(file.getName());
            }
        } else {
            String noFiles = getResources().getText(R.string.activity_dialog_no_file).toString();
            fileListArrayAdapter.add(noFiles);
        }
    }


    private AdapterView.OnItemClickListener mFileClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String filename = ((TextView) v).getText().toString();

            Intent intent = new Intent();
            intent.putExtra("FILE_NAME", filename);

            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
