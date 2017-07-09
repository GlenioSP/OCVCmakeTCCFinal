package com.example.gleniosp.ocvcmaketccfinal;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.communication.BluetoothService;
import com.example.gleniosp.ocvcmaketccfinal.communication.ChatService;
import com.example.gleniosp.ocvcmaketccfinal.communication.AllDevicesListActivity;
import com.example.gleniosp.ocvcmaketccfinal.communication.ConnectedDevicesListActivity;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVConfigData;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVCropAndGoalFragment;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVEPuckFragment;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVGridFragment;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVObstaclesFragment;
import com.example.gleniosp.ocvcmaketccfinal.utils.FileTools;
import com.example.gleniosp.ocvcmaketccfinal.utils.LoadFileListActivity;
import com.example.gleniosp.ocvcmaketccfinal.utils.SaveFileListActivity;
import com.example.gleniosp.ocvcmaketccfinal.utils.TimerConfig;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gleniosp on 05/06/17.
 */

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    /*
     * CLASS VARIABLES
     * *******************************************************/

    public static final String TAG = "MAINACTIVITY_LOG";

    private OCVConfigData ocvConfigData = null;

    private FileTools fileTools = null;
    private final String defaultConfigFile = "OCVConfigData_Default.txt";
    private final String configPathFile = "OCVConfigFileStorage";
    private final String prefixConfigFile = "OCVConfigData_";
    private final String prefixMapFile = "OCVMap_";
    private final String mapPathFile = "OCVMapFileStorage";
    private final int LOAD_CONFIG_REQUEST_CODE = 40;
    private final int LOAD_MAP_REQUEST_CODE = 41;
    private final int SAVE_REQUEST_CODE = 42;
    // myExternalFile can be a dir or a single file
    private File myExternalFile;
    private boolean enableMenuFileDefault = false;

    private Fragment currentFragment = null;
    private boolean setField = false;
    private final int OCV_CROP_AND_GOAL = 1;
    private final int OCV_OBSTACLES = 2;
    private final int OCV_GRID = 3;
    private final int OCV_EPUCK = 4;
    private final int OCV_ORIGINAL = 5;
    private final int OCV_MAP = 6;
    private final int OCV_DRAW_FIELD = 7;
    private int OCV_CURRENT = OCV_ORIGINAL;
    
    private boolean timerSave = false;
    private boolean timerSend = false;
    private ArrayList<String> mapData = null;
    private ArrayList<String> mapDataTimerCopy = null;
    private Bitmap mapBitmapTimerCopy = null;
    private Handler timerHandler;
    private long totalTimeInMilli = 0;
    private final int REQUEST_TIMER_CONFIG = 60;

    private BluetoothService bluetoothService = null;
    private final int REQUEST_ENABLE_BT = 50;
    private final int REQUEST_CLOSE_CONNECTION_DEVICE = 51;
    public static ChatService chatService = null;

    public static final int BLUETOOTH_MESSAGE_READ = 1;
    public static final int BLUETOOTH_MESSAGE_DEVICE_NAME = 2;
    public static final int BLUETOOTH_MESSAGE_TOAST = 3;
    public static final String BLUETOOTH_DEVICE_NAME = "device_name";
    public static final String BLUETOOTH_TOAST_RECEIVED = "toast";
    // StringBuffer is thread safe, StringBuilder not. However StringBuilder is faster...
    private StringBuffer outStringBuffer = new StringBuffer("");

    private ImageButton imgBtnFlashLight;
    private boolean isFlashOn = false;
    private boolean hasFlash = false;

    private CameraView _cameraBridgeViewBase;

    Mat mRgba;

    private BaseLoaderCallback _baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    // Load ndk built module, as specified in moduleName in build.gradle
                    // after opencv initialization
                    System.loadLibrary("native-lib");
                    _cameraBridgeViewBase.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    /*
     * METHODS
     * *******************************************************/

    // GETTERS AND SETTERS
    public OCVConfigData getOcvConfigData() {
        return ocvConfigData;
    }

    public void setSetField(boolean setField) {
        this.setField = setField;
    }

    // OTHER METHODS

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        _cameraBridgeViewBase = (CameraView) findViewById(R.id.activity_main_surface);
        _cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        _cameraBridgeViewBase.setCvCameraViewListener(this);

        imgBtnFlashLight = (ImageButton) findViewById(R.id.imgBtnFlashLight);

        initializeFlashLight();

        ocvConfigData = new OCVConfigData(this);

        fileTools = new FileTools(this);

        bluetoothService = new BluetoothService(this);
        if (bluetoothService.getBluetoothAdapter() == null) {
            Toast.makeText(this, "Bluetooth Adapter is not available in your device. Menus " +
                            "SERVER and SEND will be disabled.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

        if (bluetoothService.getBluetoothAdapter() != null) {
            if (chatService == null) {
                chatService = new ChatService(this, handler);
            }
        }
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, _baseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            _baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        if (bluetoothService.getBluetoothAdapter() != null) {
            if (bluetoothService.getBluetoothEnabled()) {
                if (chatService != null) {
                    if (chatService.getState() == ChatService.STATE_NONE) {
                        chatService.startServer();
                    }
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        disableCamera();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        disableCamera();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        disableCamera();

        if (bluetoothService.getBluetoothAdapter() != null) {
            unregisterReceiver(bluetoothService.getBluetoothReceiverService());

            if (chatService != null) {
                chatService.stopServer();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragManager;
        FragmentTransaction fragTransaction;
        if (currentFragment != null) {
            fragManager = getFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            fragTransaction.remove(currentFragment).commit();

            currentFragment = null;
        }

        switch (item.getItemId()) {
            case R.id.submenu_file_default:
                myExternalFile = new File(getExternalFilesDir(configPathFile), defaultConfigFile);
                if (myExternalFile.exists()) {
                    fileTools.readConfigFile(Uri.fromFile(myExternalFile));
                }
                else {
                    Toast.makeText(getApplicationContext(),
                    "Application could not find a Default Config Data File. " +
                            "Please check if the internal MEDIA is mounted or use the Config Menu and " +
                            "then the Redefine Default Menu to create a new Default Config Data File.",
                    Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.submenu_file_redefine_default:
                myExternalFile = new File(getExternalFilesDir(configPathFile), defaultConfigFile);
                fileTools.writeGeneralFile(Uri.fromFile(myExternalFile), fileTools.buildOCVDataArray());

                return true;
            case R.id.submenu_file_load:
                myExternalFile = getExternalFilesDir(configPathFile);

                // Fires an intent to spin up the "file chooser" UI and select a file to load.
                Intent intentLoad = new Intent(this, LoadFileListActivity.class);
                intentLoad.putExtra("DIRECTORY", myExternalFile.getAbsolutePath());
                startActivityForResult(intentLoad, LOAD_CONFIG_REQUEST_CODE);

                return true;
            case R.id.submenu_file_save:
                myExternalFile = getExternalFilesDir(configPathFile);
                String preGeneratedFileName = new SimpleDateFormat("dd-MM-yyyy---HH-mm-ss'.txt'").
                        format(new Date());
                String randomName = prefixConfigFile + preGeneratedFileName;

                // Fires an intent to spin up the "file chooser" UI and select a path to write a file.
                Intent intentSave = new Intent(this, SaveFileListActivity.class);
                intentSave.putExtra("DIRECTORY", myExternalFile.getAbsolutePath());
                intentSave.putExtra("FILE_NAME", randomName);
                startActivityForResult(intentSave, SAVE_REQUEST_CODE);

                return true;
            case R.id.submenu_opencv_config_crop_and_goal:
                OCVCropAndGoalFragment fragCropAndGoal = new OCVCropAndGoalFragment();
                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.add(R.id.activity_main, fragCropAndGoal, "FragCropAndGoal");
                fragTransaction.commit();
                currentFragment = fragCropAndGoal;

                OCV_CURRENT = OCV_CROP_AND_GOAL;
                invalidateOptionsMenu();
                return true;
            case R.id.submenu_opencv_config_obstacles:
                OCVObstaclesFragment fragObstacles = new OCVObstaclesFragment();
                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.add(R.id.activity_main, fragObstacles, "FragObstacles");
                fragTransaction.commit();
                currentFragment = fragObstacles;

                OCV_CURRENT = OCV_OBSTACLES;
                invalidateOptionsMenu();
                return true;
            case R.id.submenu_opencv_config_grid:
                OCVGridFragment fragGrid = new OCVGridFragment();
                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.add(R.id.activity_main, fragGrid, "FragGrid");
                fragTransaction.commit();
                currentFragment = fragGrid;

                OCV_CURRENT = OCV_GRID;
                invalidateOptionsMenu();
                return true;
            case R.id.submenu_opencv_config_epuck:
                OCVEPuckFragment fragEPuck = new OCVEPuckFragment();
                fragManager = getFragmentManager();
                fragTransaction = fragManager.beginTransaction();
                fragTransaction.add(R.id.activity_main, fragEPuck, "FragEPuck");
                fragTransaction.commit();
                currentFragment = fragEPuck;

                OCV_CURRENT = OCV_EPUCK;
                invalidateOptionsMenu();
                return true;
            case R.id.submenu_opencv_config_original:

                OCV_CURRENT = OCV_ORIGINAL;
                invalidateOptionsMenu();
                return true;
            case R.id.menu_set_unset_field:
                if (setField) {
                    resetField();
                    totalTimeInMilli = 0;
                    setField = false;
                }
                else {
                    setField = true;
                }
                invalidateOptionsMenu();

                return true;
            case R.id.submenu_map_field_start_stop:
                if (OCV_CURRENT == OCV_MAP) {
                    if (setField) {
                        OCV_CURRENT = OCV_DRAW_FIELD;
                    } else {
                        OCV_CURRENT = OCV_ORIGINAL;
                    }
                    totalTimeInMilli = 0;
                }
                else {
                    OCV_CURRENT = OCV_MAP;
                }
                invalidateOptionsMenu();

                return true;
            case R.id.submenu_map_field_timer_config:
                Intent timerConfig = new Intent(this, TimerConfig.class);
                startActivityForResult(timerConfig, REQUEST_TIMER_CONFIG);

                return true;
            case R.id.submenu_map_field_timer_save:
                if (!item.isChecked()) {
                    timerSave = true;
                } else {
                    timerSave = false;
                }
                invalidateOptionsMenu();

                return true;
            case R.id.submenu_map_field_timer_send:
                if (!item.isChecked()) {
                    timerSend = true;
                } else {
                    timerSend = false;
                }
                invalidateOptionsMenu();

                return true;
            case R.id.submenu_server_enable_disable_bluetooth:
                Intent enableIntent;
                enableIntent = bluetoothService.enableBluetooth();

                if (enableIntent != null) {
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                }
                else {
                    Toast.makeText(this, "Bluetooth is turning off. All connected devices will " +
                                    "be disconnected.",
                            Toast.LENGTH_LONG).show();
                    bluetoothService.getBluetoothAdapter().disable();
                    // broadcast receiver in BluetoothService class updates bluetoothEnabled
                    // variable when bluetooth is turned on or off
                }

                return true;
            case R.id.submenu_server_discoverable:
                Intent discoverableIntent;
                discoverableIntent = bluetoothService.ensureDiscoverable();

                if (discoverableIntent != null) {
                    startActivity(discoverableIntent);

                }
                else {
//                    Intent undiscoverableIntent;
//                    undiscoverableIntent = bluetoothService.cancelDiscoverable();
//
//                    startActivity(undiscoverableIntent);

                    Toast.makeText(this, "Bluetooth is already discoverable.",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.submenu_server_available_devices:
                // just for checking available devices that could possible connect to server
                // no action can be done here
                Intent serverAvailable = new Intent(this, AllDevicesListActivity.class);
                startActivity(serverAvailable);
                return true;
            case R.id.submenu_server_connected_devices:
                // touch any device to close it's connection with server
                Intent serverConnected = new Intent(this, ConnectedDevicesListActivity.class);
                startActivityForResult(serverConnected, REQUEST_CLOSE_CONNECTION_DEVICE);
                return true;
            case R.id.submenu_server_send_map:
                myExternalFile = getExternalFilesDir(mapPathFile);

                // Fires an intent to spin up the "file chooser" UI and select a file to load.
                Intent mapLoad = new Intent(this, LoadFileListActivity.class);
                mapLoad.putExtra("DIRECTORY", myExternalFile.getAbsolutePath());
                startActivityForResult(mapLoad, LOAD_MAP_REQUEST_CODE);

                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        // onPrepare is always called when any menu is pressed

        if (isExternalStorageWritable()) {
            enableMenuFileDefault = true;
        }
        else {
            enableMenuFileDefault = false;
        }

        if (!enableMenuFileDefault) {
            menu.findItem(R.id.submenu_file_default).setEnabled(false);
            menu.findItem(R.id.submenu_file_redefine_default).setEnabled(false);
            menu.findItem(R.id.submenu_file_load).setEnabled(false);
            menu.findItem(R.id.submenu_file_save).setEnabled(false);
        }
        else {
            menu.findItem(R.id.submenu_file_default).setEnabled(true);
            menu.findItem(R.id.submenu_file_redefine_default).setEnabled(true);
            menu.findItem(R.id.submenu_file_load).setEnabled(true);
            menu.findItem(R.id.submenu_file_save).setEnabled(true);
        }

        if (OCV_CURRENT == OCV_MAP) {
            menu.findItem(R.id.submenu_map_field_start_stop).
                    setTitle(R.string.title_map_field_stop);
            menu.findItem(R.id.submenu_map_field_timer_config).setEnabled(true);
            menu.findItem(R.id.submenu_map_field_timer_save).setEnabled(true);
            menu.findItem(R.id.submenu_map_field_timer_send).setEnabled(true);
        }
        else {
            menu.findItem(R.id.submenu_map_field_start_stop).
                    setTitle(R.string.title_map_field_start);
            menu.findItem(R.id.submenu_map_field_timer_config).setEnabled(false);
            menu.findItem(R.id.submenu_map_field_timer_save).setEnabled(false);
            menu.findItem(R.id.submenu_map_field_timer_send).setEnabled(false);
        }

        if (setField) {
            menu.findItem(R.id.menu_set_unset_field).
                    setTitle(R.string.title_menu_unset_field);

            menu.findItem(R.id.menu_map_field).setEnabled(true);
        }
        else {
            menu.findItem(R.id.menu_set_unset_field).
                    setTitle(R.string.title_menu_set_field);

            menu.findItem(R.id.menu_map_field).setEnabled(false);

            if (OCV_CURRENT == OCV_CROP_AND_GOAL) {
                menu.findItem(R.id.menu_set_unset_field).setEnabled(true);
            }
            else {
                menu.findItem(R.id.menu_set_unset_field).setEnabled(false);
            }
        }
        
        if (timerSave) {
            menu.findItem(R.id.submenu_map_field_timer_save).setChecked(true);
        } else {
            menu.findItem(R.id.submenu_map_field_timer_save).setChecked(false);
        }
        if (timerSend) {
            menu.findItem(R.id.submenu_map_field_timer_send).setChecked(true);
        } else {
            menu.findItem(R.id.submenu_map_field_timer_send).setChecked(false);
        }
        // at least one must be checked
        if ((!timerSend) && (!timerSave)) {
            menu.findItem(R.id.submenu_map_field_timer_save).setChecked(true);
            timerSave = true;
        }

        if (bluetoothService.getBluetoothAdapter() == null) {
            menu.findItem(R.id.menu_server).setEnabled(false);
        }
        else {
            if (bluetoothService.getBluetoothEnabled()) {
                menu.findItem(R.id.submenu_server_enable_disable_bluetooth).
                        setTitle(R.string.title_server_disable_bluetooth);
                menu.findItem(R.id.submenu_server_discoverable).setEnabled(true);
                menu.findItem(R.id.submenu_server_available_devices).setEnabled(true);
                menu.findItem(R.id.submenu_server_connected_devices).setEnabled(true);
                if (totalTimeInMilli > 0) {
                    menu.findItem(R.id.submenu_server_send_map).setEnabled(false);
                }
                else {
                    menu.findItem(R.id.submenu_server_send_map).setEnabled(true);
                }

                if (chatService != null) {
                    if (chatService.getState() == ChatService.STATE_NONE) {
                        chatService.startServer();
                    }
                }
            }
            else {
                menu.findItem(R.id.submenu_server_enable_disable_bluetooth).
                        setTitle(R.string.title_server_enable_bluetooth);
                menu.findItem(R.id.submenu_server_discoverable).setEnabled(false);
                menu.findItem(R.id.submenu_server_available_devices).setEnabled(false);
                menu.findItem(R.id.submenu_server_connected_devices).setEnabled(false);
                menu.findItem(R.id.submenu_server_send_map).setEnabled(false);

                if (chatService != null) {
                    chatService.stopServer();
                }
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }


    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent ( MotionEvent event )
    {

        // It only cares if the event is an UP action
        if ( event.getAction () == MotionEvent.ACTION_UP )
        {
            if (currentFragment != null && currentFragment.isVisible()) {
                // create a rect for storing the fragment window rect
                Rect r = new Rect( 0, 0, 0, 0 );
                // retrieve the fragment's windows rect
                currentFragment.getView().getHitRect(r);
                // check if the event position is inside the window rect
                boolean intersects = r.contains ( (int) event.getX (), (int) event.getY () );
                // if the event is not inside then we can close the fragment
                if ( !intersects ) {
                    FragmentManager fragManager = getFragmentManager();
                    FragmentTransaction fragTransaction = fragManager.beginTransaction();
                    fragTransaction.remove(currentFragment).commit();

                    currentFragment = null;
                    // notify that we consumed this event
                    return true;
                }
            }
        }
        // let the system handle the event
        return super.onTouchEvent ( event );
    }


    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        switch(requestCode) {
            case LOAD_CONFIG_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = null;
                    if (resultData != null) {
                        String filename = resultData.getStringExtra("FILE_NAME");
                        myExternalFile = new File(getExternalFilesDir(configPathFile), filename);
                        uri = Uri.fromFile(myExternalFile);
                    }
                    fileTools.readConfigFile(uri);
                }
                break;
            case LOAD_MAP_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = null;
                    if (resultData != null) {
                        String filename = resultData.getStringExtra("FILE_NAME");
                        myExternalFile = new File(getExternalFilesDir(mapPathFile), filename);
                        uri = Uri.fromFile(myExternalFile);
                    }
                    ArrayList<String> mapFile = fileTools.readMapFile(uri);

                    if (mapFile != null) {
                        sendBluetoothMessage(mapFile);
                    }
                }
                break;
            case SAVE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = null;
                    String filename;

                    if (resultData != null) {
                        filename = resultData.getStringExtra("FILE_NAME");
                        myExternalFile = new File(getExternalFilesDir(configPathFile), filename);
                        uri = Uri.fromFile(myExternalFile);
                        fileTools.writeGeneralFile(uri, fileTools.buildOCVDataArray());
                    }
                }
                break;
            case REQUEST_TIMER_CONFIG:
                if (resultCode == Activity.RESULT_OK) {
                    totalTimeInMilli = Long.parseLong(resultData.getStringExtra("TOTALTIME"));
                    final long timeIntervalInMilli = Long.parseLong(resultData.getStringExtra("INTERVAL"));

                    timerHandler = new Handler();

                    final Runnable runnableTimer = new Runnable() {
                        @Override
                        public void run() {

                            if (totalTimeInMilli > 0) {

                                if ((mapDataTimerCopy != null) && (mapBitmapTimerCopy != null)) {

                                    if (timerSave) {
                                        String preGeneratedFileName =
                                                new SimpleDateFormat("dd-MM-yyyy---HH-mm-ss'.txt'").format(new Date());
                                        String randomName = prefixMapFile + preGeneratedFileName;
                                        myExternalFile = new File(getExternalFilesDir(mapPathFile), randomName);

                                        Uri uri = Uri.fromFile(myExternalFile);
                                        fileTools.writeGeneralFile(uri, mapDataTimerCopy);

                                        // use the same filename to save mapImage just adding .png at the end
                                        if (randomName.contains(".")) {
                                            randomName = randomName.substring(0, randomName.lastIndexOf('.'));
                                        }
                                        myExternalFile = new File(getExternalFilesDir(mapPathFile), randomName + ".png");
                                        uri = Uri.fromFile(myExternalFile);
                                        fileTools.writeImageFile(uri, mapBitmapTimerCopy);
                                    }

                                    if (timerSend) {
                                        sendBluetoothMessage(mapDataTimerCopy);
                                    }
                                }

                                totalTimeInMilli = totalTimeInMilli - timeIntervalInMilli;

                                timerHandler.postDelayed(this, timeIntervalInMilli);
                            }
                        }
                    };

                    timerHandler.postDelayed(runnableTimer, timeIntervalInMilli);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth was successfully turned on.",
                            Toast.LENGTH_SHORT).show();
                    // broadcast receiver in BluetoothService class updates bluetoothEnabled
                    // variable when bluetooth is turned on or off
                }
                break;
            case REQUEST_CLOSE_CONNECTION_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String deviceAddress;
                    deviceAddress = resultData.getStringExtra("CONNECTED_DEVICE_ADDRESS");

                    chatService.disconnectDevice(deviceAddress);
                }
                break;
        }
    }

    /*
     * Bluetooth Communicaton specifics
     * *********************************/

    private synchronized void sendBluetoothMessage(ArrayList<String> data) {
        if (chatService.getState() != ChatService.STATE_CONNECTED) {
            // stops continuous mapping if there is no connected device

            if ((timerSend) && (!timerSave)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "There is no connected device. Timer will be set to zero...",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                totalTimeInMilli = 0;
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "There is no connected device.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            return;
        }

        for (String temp : data) {
            outStringBuffer.append(temp);
        }

        String message = outStringBuffer.toString();

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            chatService.writeToAll(send);
        }

        outStringBuffer.setLength(0);
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BLUETOOTH_MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    // TODO (Optional): if server can receive some answer, handle it here
                    break;
                case BLUETOOTH_MESSAGE_DEVICE_NAME:
                    String connectedDeviceName = msg.getData().getString(BLUETOOTH_DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Server connected to " + connectedDeviceName + ".",
                            Toast.LENGTH_LONG).show();
                    break;
                case BLUETOOTH_MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(BLUETOOTH_TOAST_RECEIVED), Toast.LENGTH_LONG)
                            .show();
                    break;
            }
            return false;
        }
    });


    /*
     * Flashlight specifics
     * *********************************/

    private void initializeFlashLight() {
        // check if device is supporting flashlight or not
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // Switch button click event to toggle flash on/off
        imgBtnFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
    }

    private void turnOnFlash() {
        if (hasFlash) {
            if (!isFlashOn) {
                if (_cameraBridgeViewBase == null) {
                    return;
                }

                _cameraBridgeViewBase.setFlashTorchOn();
                isFlashOn = true;

                toggleButtonImage();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, your device does not support flash.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void turnOffFlash() {
        if (hasFlash) {
            if (isFlashOn) {
                if (_cameraBridgeViewBase == null) {
                    return;
                }

                _cameraBridgeViewBase.setFlashTorchOff();
                isFlashOn = false;

                toggleButtonImage();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Sorry, your device does not support flash.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Toggle flashlight button images changing image states to on / off
    private void toggleButtonImage(){
        if(isFlashOn){
            imgBtnFlashLight.setImageResource(R.drawable.flashlight_on);
        }else{
            imgBtnFlashLight.setImageResource(R.drawable.flashlight_off);
        }
    }

    /*
     * OpenCV specifics
     * *********************************/

    public void disableCamera() {
        if (_cameraBridgeViewBase != null)
            _cameraBridgeViewBase.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC3);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)  {

        mRgba = inputFrame.rgba();

        switch(OCV_CURRENT) {
            case OCV_CROP_AND_GOAL:
                if (!setField) {
                    goalAndFieldExtraction(ocvConfigData, mRgba.getNativeObjAddr());
                }
                else {
                    drawField(mRgba.getNativeObjAddr());
                }
                break;
            case OCV_OBSTACLES:
                if (setField) {
                    obstaclesExtraction(ocvConfigData, mRgba.getNativeObjAddr());
                    drawField(mRgba.getNativeObjAddr());
                }
                break;
            case OCV_GRID:
                if (setField) {
                    divideFieldInCells(ocvConfigData, mRgba.getNativeObjAddr());
                    drawField(mRgba.getNativeObjAddr());
                }
                break;
            case OCV_EPUCK:
                if (setField) {
                    findEPuck(ocvConfigData, mRgba.getNativeObjAddr());
                    drawField(mRgba.getNativeObjAddr());
                }
                break;
            case OCV_ORIGINAL:
                break;
            case OCV_MAP:
                if (setField) {
                    obstaclesExtraction(ocvConfigData, mRgba.getNativeObjAddr());
                    findEPuck(ocvConfigData, mRgba.getNativeObjAddr());
                    divideFieldInCells(ocvConfigData, mRgba.getNativeObjAddr());
                    mapData = (ArrayList<String> ) mapField(ocvConfigData, mRgba.getNativeObjAddr());
                    drawField(mRgba.getNativeObjAddr());

                    if (mapData != null) {

                        mapDataTimerCopy = mapData;

                        // save Bitmap image
                        try {
                            mapBitmapTimerCopy = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.RGB_565);
                            Utils.matToBitmap(mRgba, mapBitmapTimerCopy);
                        } catch (Exception e) {
                            Log.d(TAG, "Error converting Mat to Bitmap.");
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case OCV_DRAW_FIELD:
                drawField(mRgba.getNativeObjAddr());
                break;
        }

        return mRgba;
    }

    /*
     * JNI (Native) Code
     * *********************/
    public native static void drawField(long mRgbaAddr);
    public native static void resetField();
    public native static void goalAndFieldExtraction(OCVConfigData callingObject, long mRgbaAddr);
    public native static void obstaclesExtraction(OCVConfigData callingObject, long mRgbaAddr);
    public native static void divideFieldInCells(OCVConfigData callingObject, long mRgbaAddr);
    public native static void findEPuck(OCVConfigData callingObject, long mRgbaAddr);
    public native static Object mapField(OCVConfigData callingObject, long mRgbaAddr);
}
