package com.example.gleniosp.ocvcmaketccfinal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.mapping.OCVConfigData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileTools {

    private Context mContext;

    private final String delimiter = ":";
    private final String paramDivider = "\n";

    public FileTools(Context mContext) {
        this.mContext = mContext;
    }

    public List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".txt")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public void readConfigFile(Uri uri)
    {
        try{
            InputStream inputStream = mContext.getApplicationContext().
                    getContentResolver().openInputStream(uri);

            if (inputStream != null) {

                OCVConfigData tempOcvConfigData = new OCVConfigData(mContext);

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String currentline;

                ArrayList<String> unformattedLine = new ArrayList<String>();
                ArrayList<String> incorrectParametersNumber = new ArrayList<String>();
                ArrayList<String> notNumberValue = new ArrayList<String>();
                ArrayList<String> incorrectParameterName = new ArrayList<String>();

                while ((currentline = reader.readLine()) != null) {

                    if (currentline.contains(delimiter))
                    {
                        String[] parts = currentline.split(delimiter);

                        if (parts.length == 2)
                        {
                            String param = parts[0].toLowerCase();
                            String value = parts[1];

                            if (isNumeric(value))
                            {
                                if (param.equals("lowerlowhred"))
                                {
                                    tempOcvConfigData.setLowerLowHRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowerhighhred"))
                                {
                                    tempOcvConfigData.setLowerHighHRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowerlowsred"))
                                {
                                    tempOcvConfigData.setLowerLowSRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowerhighsred"))
                                {
                                    tempOcvConfigData.setLowerHighSRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowerlowvred"))
                                {
                                    tempOcvConfigData.setLowerLowVRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowerhighvred"))
                                {
                                    tempOcvConfigData.setLowerHighVRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperlowhred"))
                                {
                                    tempOcvConfigData.setUpperLowHRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperhighhred"))
                                {
                                    tempOcvConfigData.setUpperHighHRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperlowsred"))
                                {
                                    tempOcvConfigData.setUpperLowSRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperhighsred"))
                                {
                                    tempOcvConfigData.setUpperHighSRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperlowvred"))
                                {
                                    tempOcvConfigData.setUpperLowVRed(Integer.parseInt(value));
                                }
                                else if (param.equals("upperhighvred"))
                                {
                                    tempOcvConfigData.setUpperHighVRed(Integer.parseInt(value));
                                }
                                else if (param.equals("lowhblack"))
                                {
                                    tempOcvConfigData.setLowHBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("highhblack"))
                                {
                                    tempOcvConfigData.setHighHBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("lowsblack"))
                                {
                                    tempOcvConfigData.setLowSBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("highsblack"))
                                {
                                    tempOcvConfigData.setHighSBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("lowvblack"))
                                {
                                    tempOcvConfigData.setLowVBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("highvblack"))
                                {
                                    tempOcvConfigData.setHighVBlack(Integer.parseInt(value));
                                }
                                else if (param.equals("ycells"))
                                {
                                    tempOcvConfigData.setYCells(Integer.parseInt(value));
                                }
                                else if (param.equals("xcells"))
                                {
                                    tempOcvConfigData.setXCells(Integer.parseInt(value));
                                }
                                else if (param.equals("houghcmindistdivider"))
                                {
                                    tempOcvConfigData.setHoughCMinDistDivider(Integer.parseInt(value));
                                }
                                else if (param.equals("houghcparam1"))
                                {
                                    tempOcvConfigData.setHoughCParam1(Integer.parseInt(value));
                                }
                                else if (param.equals("houghcparam2"))
                                {
                                    tempOcvConfigData.setHoughCParam2(Integer.parseInt(value));
                                }
                                else if (param.equals("houghcminradius"))
                                {
                                    tempOcvConfigData.setHoughCMinRadius(Integer.parseInt(value));
                                }
                                else if (param.equals("houghcmaxradius"))
                                {
                                    tempOcvConfigData.setHoughCMaxRadius(Integer.parseInt(value));
                                }
                                else
                                {
                                    incorrectParameterName.add(param + "\n");
                                }
                            }
                            else
                            {
                                notNumberValue.add(param + " " + value + "\n");
                            }
                        }
                        else
                        {
                            incorrectParametersNumber.add(currentline + "\n");
                        }
                    }
                    else
                    {
                        unformattedLine.add(currentline + "\n");
                    }
                }

                if (unformattedLine.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file are not formatted correctly:\n" + unformattedLine.toString(),
                            Toast.LENGTH_LONG).show();
                }
                else if (incorrectParametersNumber.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file do not have a correct number of parameters:\n" + incorrectParametersNumber.toString(),
                            Toast.LENGTH_LONG).show();
                }
                else if (notNumberValue.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file do not have numeric parameter values:\n" + notNumberValue.toString(),
                            Toast.LENGTH_LONG).show();
                }
                else if (incorrectParameterName.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following parameters provided by the file are not correct parameters:\n" + incorrectParameterName.toString(),
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    ((MainActivity ) mContext).getOcvConfigData().copyData(tempOcvConfigData);

                    inputStream.close();

                    Toast.makeText(mContext.getApplicationContext(), "File was successfully read.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext.getApplicationContext(), "The file was not found or lost.",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<String> readMapFile(Uri uri)
    {
        try{
            InputStream inputStream = mContext.getApplicationContext().
                    getContentResolver().openInputStream(uri);

            if (inputStream != null) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                ArrayList<String> tempArrayList = new ArrayList<String>();

                int largerGridNumber = 0;

                int gridSize = 1;

                String currentline;

                ArrayList<String> unformattedLine = new ArrayList<String>();
                ArrayList<String> incorrectParametersNumber = new ArrayList<String>();
                ArrayList<String> notNumberValue = new ArrayList<String>();
                ArrayList<String> incorrectParameterName = new ArrayList<String>();

                while ((currentline = reader.readLine()) != null) {

                    if (currentline.contains(delimiter))
                    {
                        String[] parts = currentline.split(delimiter);

                        if (parts.length == 2)
                        {
                            String param = parts[0].toLowerCase();
                            String value = parts[1];

                            if (isNumeric(value))
                            {
                                if (param.equals("gridsize"))
                                {
                                    tempArrayList.add(param + delimiter + value + paramDivider);
                                    gridSize = Integer.valueOf(value);
                                }
                                else if (param.equals("goal"))
                                {
                                    // get the largest cell/grid number of the objects to
                                    // compare with the gridsize value
                                    if (Integer.parseInt(value) >= largerGridNumber) {
                                        largerGridNumber = Integer.parseInt(value);
                                    }
                                    tempArrayList.add(param + delimiter + value + paramDivider);
                                }
                                else if (param.equals("obstacle"))
                                {
                                    if (Integer.parseInt(value) >= largerGridNumber) {
                                        largerGridNumber = Integer.parseInt(value);
                                    }
                                    tempArrayList.add(param + delimiter + value + paramDivider);
                                }
                                else if (param.equals("epuck"))
                                {
                                    if (Integer.parseInt(value) >= largerGridNumber) {
                                        largerGridNumber = Integer.parseInt(value);
                                    }
                                    tempArrayList.add(param + delimiter + value + paramDivider);
                                }
                                else
                                {
                                    incorrectParameterName.add(param + "\n");
                                }
                            }
                            else
                            {
                                notNumberValue.add(param + " " + value + "\n");
                            }
                        }
                        else
                        {
                            incorrectParametersNumber.add(currentline + "\n");
                        }
                    }
                    else
                    {
                        unformattedLine.add(currentline + "\n");
                    }
                }

                if (unformattedLine.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file are not formatted correctly:\n" + unformattedLine.toString(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
                else if (incorrectParametersNumber.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file do not have a correct number of parameters:\n" + incorrectParametersNumber.toString(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
                else if (notNumberValue.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following lines in the file do not have numeric parameter values:\n" + notNumberValue.toString(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
                else if (incorrectParameterName.size() > 0)
                {
                    Toast.makeText(mContext.getApplicationContext(),
                            "The following parameters provided by the file are not correct parameters:\n" + incorrectParameterName.toString(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
                else
                {
                    if (largerGridNumber <= (gridSize - 1)) {

                        inputStream.close();

                        return tempArrayList;
                    }
                    else {
                        Toast.makeText(mContext.getApplicationContext(), "There are some objects with " +
                                        "position outside the grid field.",
                                Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            }
            else {
                Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext.getApplicationContext(), "The file was not found or lost.",
                    Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    public void writeGeneralFile(Uri uri, ArrayList<String> tempData)
    {
        try{
            ParcelFileDescriptor pfd = mContext.getApplicationContext().
                    getContentResolver().openFileDescriptor(uri, "w");

            if (pfd != null) {

                FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());

                for (String temp : tempData) {
                    fileOutputStream.write(temp.getBytes());
                }

                fileOutputStream.close();
                pfd.close();

                Toast.makeText(mContext.getApplicationContext(), "File was successfully saved.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext.getApplicationContext(), "The file was not found or lost.",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext.getApplicationContext(), "There was an error accessing the file.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void writeImageFile(Uri uri, Bitmap image)
    {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(uri.getPath());
            // PNG is a lossless format, the compression factor (100) is ignored
            image.compress(Bitmap.CompressFormat.PNG, 100, out);

            Toast.makeText(mContext.getApplicationContext(), "Map was successfully saved.",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d(MainActivity.TAG, "Error accessing Image's FileOutputStream.");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    // TODO: TOAST HERE
                }
            } catch (IOException e) {
                Log.d(MainActivity.TAG, "Error closing Image's FileOutputStream.");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> buildOCVDataArray() {

        ArrayList<String> param = new ArrayList<String>();
        ArrayList<Integer> values = new ArrayList<Integer>();

        param.add("LowerLowHRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerLowHRed());

        param.add("LowerHighHRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerHighHRed());

        param.add("LowerLowSRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerLowSRed());

        param.add("LowerHighSRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerHighSRed());

        param.add("LowerLowVRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerLowVRed());

        param.add("LowerHighVRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowerHighVRed());


        param.add("UpperLowHRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperLowHRed());

        param.add("UpperHighHRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperHighHRed());

        param.add("UpperLowSRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperLowSRed());

        param.add("UpperHighSRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperHighSRed());

        param.add("UpperLowVRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperLowVRed());

        param.add("UpperHighVRed");
        values.add(((MainActivity ) mContext).getOcvConfigData().getUpperHighVRed());


        param.add("LowHBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowHBlack());

        param.add("HighHBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHighHBlack());

        param.add("LowSBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowSBlack());

        param.add("HighSBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHighSBlack());

        param.add("LowVBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getLowVBlack());

        param.add("HighVBlack");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHighVBlack());


        param.add("YCells");
        values.add(((MainActivity ) mContext).getOcvConfigData().getYCells());

        param.add("XCells");
        values.add(((MainActivity ) mContext).getOcvConfigData().getXCells());


        param.add("HoughCMinDistDivider");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHoughCMinDistDivider());

        param.add("HoughCParam1");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHoughCParam1());

        param.add("HoughCParam2");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHoughCParam2());

        param.add("HoughCMinRadius");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHoughCMinRadius());

        param.add("HoughCMaxRadius");
        values.add(((MainActivity ) mContext).getOcvConfigData().getHoughCMaxRadius());

        ArrayList<String> config = new ArrayList<String>();

        for ( int i = 0; i < param.size(); i++)
        {
            config.add(param.get(i) + delimiter + String.valueOf(values.get(i)) + paramDivider);
        }

        return config;
    }

    public boolean isNumeric(String str)
    {
        // inclusive negative numbers
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
