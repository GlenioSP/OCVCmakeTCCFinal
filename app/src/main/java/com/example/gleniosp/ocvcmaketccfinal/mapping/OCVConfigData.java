package com.example.gleniosp.ocvcmaketccfinal.mapping;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;

import java.util.ArrayList;

public class OCVConfigData {

    private Context mContext;

    private final int minColorH = 0;
    private final int maxColorH = 179;

    private final int minColorS = 0;
    private final int maxColorS = 255;

    private final int minColorV = 0;
    private final int maxColorV = 255;

    // for field and goal filtering
    private int LowerLowHRed = 0;
    private int LowerHighHRed = 10;

    private int LowerLowSRed = 170;
    private int LowerHighSRed = 255;

    private int LowerLowVRed = 50;
    private int LowerHighVRed = 150;

    private int UpperLowHRed = 160;
    private int UpperHighHRed = 179;

    private int UpperLowSRed = 170;
    private int UpperHighSRed = 255;

    private int UpperLowVRed = 50;
    private int UpperHighVRed = 150;

    // for black obstacles filtering
    private int LowHBlack = 0;
    private int HighHBlack = 179;

    private int LowSBlack = 0;
    private int HighSBlack = 255;

    private int LowVBlack = 0;
    private int HighVBlack = 90;

    // for cell grid
    private final int MAX_CELL_NUMBER = 100;
    private final int MIN_CELL_NUMBER = 1;
    private int YCells = 5;
    private int XCells = 5;

    // for Hough Circle - E-Puck
    private final int minHoughCMinDistDivider = 1;
    private final int maxHoughCMinDistDivider = 50;
    private final int minHoughCParam1= 1;
    private final int maxHoughCParam1 = 500;
    private final int minHoughCParam2 = 1;
    private final int maxHoughCParam2 = 500;
    private final int minHoughCMinRadius = 0;
    private final int maxHoughCMinRadius = 200;
    private final int minHoughCMaxRadius = 0;
    private final int maxHoughCMaxRadius = 200;
    private int HoughCMinDistDivider = 8;
    private int HoughCParam1 = 100;
    private int HoughCParam2 = 30;
    private int HoughCMinRadius = 0;
    private int HoughCMaxRadius = 0;


    /*
     * CONSTRUCTORS
     * ***************************************/

    public OCVConfigData(Context context) {
        mContext = context;
    }

    /*
     * GETTERS
     * ***************************************/

    public int getMinColorH() {
        return minColorH;
    }

    public int getMaxColorH() {
        return maxColorH;
    }

    public int getMinColorS() {
        return minColorS;
    }

    public int getMaxColorS() {
        return maxColorS;
    }

    public int getMinColorV() {
        return minColorV;
    }

    public int getMaxColorV() {
        return maxColorV;
    }

    public int getLowerLowHRed() { return LowerLowHRed; }

    public int getLowerHighHRed() {
        return LowerHighHRed;
    }

    public int getLowerLowSRed() {
        return LowerLowSRed;
    }

    public int getLowerHighSRed() {
        return LowerHighSRed;
    }

    public int getLowerLowVRed() {
        return LowerLowVRed;
    }

    public int getLowerHighVRed() {
        return LowerHighVRed;
    }

    public int getUpperLowHRed() {
        return UpperLowHRed;
    }

    public int getUpperHighHRed() {
        return UpperHighHRed;
    }

    public int getUpperLowSRed() {
        return UpperLowSRed;
    }

    public int getUpperHighSRed() {
        return UpperHighSRed;
    }

    public int getUpperLowVRed() {
        return UpperLowVRed;
    }

    public int getUpperHighVRed() {
        return UpperHighVRed;
    }

    public int getLowHBlack() {
        return LowHBlack;
    }

    public int getHighHBlack() {
        return HighHBlack;
    }

    public int getLowSBlack() {
        return LowSBlack;
    }

    public int getHighSBlack() {
        return HighSBlack;
    }

    public int getLowVBlack() {
        return LowVBlack;
    }

    public int getHighVBlack() {
        return HighVBlack;
    }

    public int getMAX_CELL_NUMBER() {
        return MAX_CELL_NUMBER;
    }

    public int getMIN_CELL_NUMBER() {
        return MIN_CELL_NUMBER;
    }

    public int getYCells() {
        return YCells;
    }

    public int getXCells() {
        return XCells;
    }

    public int getHoughCMinDistDivider() {
        return HoughCMinDistDivider;
    }

    public int getHoughCParam1() {
        return HoughCParam1;
    }

    public int getHoughCParam2() {
        return HoughCParam2;
    }

    public int getHoughCMinRadius() {
        return HoughCMinRadius;
    }

    public int getHoughCMaxRadius() {
        return HoughCMaxRadius;
    }

    public int getMinHoughCMinDistDivider() { return minHoughCMinDistDivider; }

    public int getMaxHoughCMinDistDivider() {
        return maxHoughCMinDistDivider;
    }

    public int getMinHoughCParam1() {
        return minHoughCParam1;
    }

    public int getMaxHoughCParam1() {
        return maxHoughCParam1;
    }

    public int getMinHoughCParam2() {
        return minHoughCParam2;
    }

    public int getMaxHoughCParam2() {
        return maxHoughCParam2;
    }

    public int getMinHoughCMinRadius() {
        return minHoughCMinRadius;
    }

    public int getMaxHoughCMinRadius() {
        return maxHoughCMinRadius;
    }

    public int getMinHoughCMaxRadius() {
        return minHoughCMaxRadius;
    }

    public int getMaxHoughCMaxRadius() {
        return maxHoughCMaxRadius;
    }

    /*
     * SETTERS
     * ***************************************/

    public void setLowerLowHRed(int lowerLowHRed) {
        if (lowerLowHRed < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Hue Red can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerLowHRed > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Hue Red can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerLowHRed = lowerLowHRed;
    }

    public void setLowerHighHRed(int lowerHighHRed) {
        if (lowerHighHRed < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Hue Red can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerHighHRed > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Hue Red can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerHighHRed = lowerHighHRed;
    }

    public void setLowerLowSRed(int lowerLowSRed) {
        if (lowerLowSRed < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Saturation Red can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerLowSRed > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Saturation Red can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerLowSRed = lowerLowSRed;
    }

    public void setLowerHighSRed(int lowerHighSRed) {
        if (lowerHighSRed < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Saturation Red can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerHighSRed > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Saturation Red can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerHighSRed = lowerHighSRed;
    }

    public void setLowerLowVRed(int lowerLowVRed) {
        if (lowerLowVRed < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Value Red can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerLowVRed > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower Low Value Red can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerLowVRed = lowerLowVRed;
    }

    public void setLowerHighVRed(int lowerHighVRed) {
        if (lowerHighVRed < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Value Red can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowerHighVRed > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Lower High Value Red can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowerHighVRed = lowerHighVRed;
    }

    public void setUpperLowHRed(int upperLowHRed) {
        if (upperLowHRed < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Hue Red can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperLowHRed > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Hue Red can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperLowHRed = upperLowHRed;
    }

    public void setUpperHighHRed(int upperHighHRed) {
        if (upperHighHRed < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Hue Red can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperHighHRed > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Hue Red can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperHighHRed = upperHighHRed;
    }

    public void setUpperLowSRed(int upperLowSRed) {
        if (upperLowSRed < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Saturation Red can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperLowSRed > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Saturation Red can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperLowSRed = upperLowSRed;
    }

    public void setUpperHighSRed(int upperHighSRed) {
        if (upperHighSRed < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Saturation Red can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperHighSRed > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Saturation Red can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperHighSRed = upperHighSRed;
    }

    public void setUpperLowVRed(int upperLowVRed) {
        if (upperLowVRed < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Value Red can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperLowVRed > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper Low Value Red can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperLowVRed = upperLowVRed;
    }

    public void setUpperHighVRed(int upperHighVRed) {
        if (upperHighVRed < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Value Red can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (upperHighVRed > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Upper High Value Red can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        UpperHighVRed = upperHighVRed;
    }

    public void setLowHBlack(int lowHBlack) {
        if (lowHBlack < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Hue Black can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowHBlack > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Hue Black can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowHBlack = lowHBlack;
    }

    public void setHighHBlack(int highHBlack) {
        if (highHBlack < minColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Hue Black can't be lower than " + minColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (highHBlack > maxColorH) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Hue Black can't be higher than " + maxColorH + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        HighHBlack = highHBlack;
    }

    public void setLowSBlack(int lowSBlack) {
        if (lowSBlack < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Saturation Black can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowSBlack > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Saturation Black can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowSBlack = lowSBlack;
    }

    public void setHighSBlack(int highSBlack) {
        if (highSBlack < minColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Saturation Black can't be lower than " + minColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (highSBlack > maxColorS) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Saturation Black can't be higher than " + maxColorS + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        HighSBlack = highSBlack;
    }

    public void setLowVBlack(int lowVBlack) {
        if (lowVBlack < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Value Black can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (lowVBlack > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Low Value Black can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        LowVBlack = lowVBlack;
    }

    public void setHighVBlack(int highVBlack) {
        if (highVBlack < minColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Value Black can't be lower than " + minColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (highVBlack > maxColorV) {
            Toast.makeText(mContext.getApplicationContext(),
                    "High Value Black can't be higher than " + maxColorV + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        HighVBlack = highVBlack;
    }

    public void setYCells(int YCells) {
        if (YCells < MIN_CELL_NUMBER) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Number of vertical cells can't be lower than " + MIN_CELL_NUMBER + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (YCells > MAX_CELL_NUMBER) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Number of vertical cells can't be higher than " + MAX_CELL_NUMBER + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.YCells = YCells;
    }

    public void setXCells(int XCells) {
        if (XCells < MIN_CELL_NUMBER) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Number of horizontal cells can't be lower than " + MIN_CELL_NUMBER + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (XCells > MAX_CELL_NUMBER) {
            Toast.makeText(mContext.getApplicationContext(),
                    "Number of horizontal cells can't be higher than " + MAX_CELL_NUMBER + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.XCells = XCells;
    }

    public void setHoughCMinDistDivider(int houghCMinDistDivider) {
        if (houghCMinDistDivider < minHoughCMinDistDivider) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, minDist divider parameter can't be lower than " + minHoughCMinDistDivider + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (houghCMinDistDivider > maxHoughCMinDistDivider) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, minDist divider parameter can't be higher than " + maxHoughCMinDistDivider + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.HoughCMinDistDivider = houghCMinDistDivider;
    }

    public void setHoughCParam1(int houghCParam1) {
        if (houghCParam1 < minHoughCParam1) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, param1 parameter can't be lower than " + minHoughCParam1 + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (houghCParam1 > maxHoughCParam1) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, param1 parameter can't be higher than " + maxHoughCParam1 + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.HoughCParam1 = houghCParam1;
    }

    public void setHoughCParam2(int houghCParam2) {
        if (houghCParam2 < minHoughCParam2) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, param2 parameter can't be lower than " + minHoughCParam2 + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (houghCParam2 > maxHoughCParam2) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, param2 parameter can't be higher than " + maxHoughCParam2 + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.HoughCParam2 = houghCParam2;
    }

    public void setHoughCMinRadius(int houghCMinRadius) {
        if (houghCMinRadius < minHoughCMinRadius) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, minimum circle radius parameter can't be lower than " + minHoughCMinRadius + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (houghCMinRadius > maxHoughCMinRadius) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, minimum circle radius parameter can't be higher than " + maxHoughCMinRadius + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.HoughCMinRadius = houghCMinRadius;
    }

    public void setHoughCMaxRadius(int houghCMaxRadius) {
        if (houghCMaxRadius < minHoughCMaxRadius) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, maximum circle radius parameter can't be lower than " + minHoughCMaxRadius + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (houghCMaxRadius > maxHoughCMaxRadius) {
            Toast.makeText(mContext.getApplicationContext(),
                    "In Hough Circle, maximum circle radius parameter can't be higher than " + maxHoughCMaxRadius + ".",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        this.HoughCMaxRadius = houghCMaxRadius;
    }

    /*
     * OTHER METHODS
     * *************************/

    public void copyData(OCVConfigData copy) {

        setLowerLowHRed(copy.getLowerLowHRed());
        setLowerHighHRed(copy.getLowerHighHRed());

        setLowerLowSRed(copy.getLowerLowSRed());
        setLowerHighSRed(copy.getLowerHighSRed());

        setLowerLowVRed(copy.getLowerLowVRed());
        setLowerHighVRed(copy.getLowerHighVRed());


        setUpperLowHRed(copy.getUpperLowHRed());
        setUpperHighHRed(copy.getUpperHighHRed());

        setUpperLowSRed(copy.getUpperLowSRed());
        setUpperHighSRed(copy.getUpperHighSRed());

        setUpperLowVRed(copy.getUpperLowVRed());
        setUpperHighVRed(copy.getUpperHighVRed());


        setLowHBlack(copy.getLowHBlack());
        setHighHBlack(copy.getHighHBlack());

        setLowSBlack(copy.getLowSBlack());
        setHighSBlack(copy.getHighSBlack());

        setLowVBlack(copy.getLowVBlack());
        setHighVBlack(copy.getHighVBlack());


        setYCells(copy.getYCells());
        setXCells(copy.getXCells());


        setHoughCMinDistDivider(copy.getHoughCMinDistDivider());
        setHoughCParam1(copy.getHoughCParam1());
        setHoughCParam2(copy.getHoughCParam2());
        setHoughCMinRadius(copy.getHoughCMinRadius());
        setHoughCMaxRadius(copy.getHoughCMaxRadius());
    }
}
