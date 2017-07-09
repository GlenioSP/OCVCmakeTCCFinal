#include <jni.h>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <android/log.h>
#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;


/**
 * DEFINE GLOBAL VARIABLES
 * ******************************************/

char const *TAG = "MAINACTIVITY_NATIVE_LOG";

Rect2f fieldRect;

// reference to localize new objects relative to the original image (before crop)
Point offSetParent(0,0);
Size parentOriginalSize(0,0);

Scalar contourColorBlue = Scalar(0, 0, 255);
Scalar contourColorRed = Scalar(255, 0, 0);
Scalar contourColorGreen = Scalar(0, 255, 0);
Scalar contourColorBlack = Scalar(0, 0, 0);
Scalar contourColorWhite = Scalar(255, 255, 255);

struct objectsInCell
{
    size_t gridSize;
    vector<string> objectName;
    vector<int> cellPosition;
};

const struct objectsInCell objectsInCellEmptyStruct = { 0 };

struct EPucksData
{
    vector<int> radius;
    vector<Point> center;
};

struct MappingObjects
{

    Rect2f foundGoal;
    vector<Rect2f> foundObstacles;
    vector<Rect2f> createdGrid;
    vector<Rect2f> foundEPucks;
    objectsInCell stObjectsInCell;

} stMappingObjects;

/* ******************************************/


/*
 * METHODS IMPLEMENTATION
 * ******************************************/

extern "C"
{
    EPucksData houghCircleTransform(Mat mGray,
                                    int iHoughCMinDistDivider, int iHoughCParam1, int iHoughCParam2,
                                    int iHoughCMinRadius, int iHoughCMaxRadius)
    {
        struct EPucksData stEPucksData;

        if (mGray.rows/iHoughCMinDistDivider > 0)
        {
            int radius;

            // Reduce the noise so we avoid false circle detection
            GaussianBlur(mGray, mGray, Size(9, 9), 2, 2 );

            vector<Vec3f> circles;

            /// Apply the Hough Transform to find the circles
            HoughCircles(mGray, circles, CV_HOUGH_GRADIENT, 1, mGray.rows/iHoughCMinDistDivider,
                         iHoughCParam1, iHoughCParam2, iHoughCMinRadius, iHoughCMaxRadius);

            /// Draw the circles detected
            for(size_t i = 0; i < circles.size(); i++)
            {
                Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
                radius = cvRound(circles[i][2]);

                stEPucksData.center.push_back(center);
                stEPucksData.radius.push_back(radius);
            }
        }

        return stEPucksData;
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    drawField
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_drawField
            (JNIEnv *jniEnv, jclass jClass, jlong mRgbaAddr)
    {
        if (fieldRect.width > 0)
        {
            Mat &temp = *(Mat *) mRgbaAddr;

            // draw field
            rectangle(temp, fieldRect, contourColorRed, 2, 8);
        }
    }

    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    resetField
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_resetField
            (JNIEnv *jniEnv, jclass jClass)
    {
        fieldRect = Rect2f();
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    goalAndFieldExtraction
     * Signature: (Lcom/example/gleniosp/ocvcmaketccfinal/mapping/OCVConfigData;J)V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_goalAndFieldExtraction
            (JNIEnv *jniEnv, jclass jClass,  jobject dataObject, jlong mRgbaAddr)
    {
        // empty fieldRect every time. So, if goalAndFieldExtraction doesn't find a field, other
        // methods can't use previous value of fieldRect.
        fieldRect = Rect2f();

        Mat &temp = *(Mat* ) mRgbaAddr;

        int iLowerLowHRed;
        int iLowerHighHRed;
        int iLowerLowSRed;
        int iLowerHighSRed;
        int iLowerLowVRed;
        int iLowerHighVRed;

        int iUpperLowHRed;
        int iUpperHighHRed;
        int iUpperLowSRed;
        int iUpperHighSRed;
        int iUpperLowVRed;
        int iUpperHighVRed;

        jclass cls = jniEnv->GetObjectClass(dataObject);

        jmethodID methodId;

        methodId = jniEnv->GetMethodID(cls, "getLowerLowHRed", "()I");
        iLowerLowHRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getLowerHighHRed", "()I");
        iLowerHighHRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getLowerLowSRed", "()I");
        iLowerLowSRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getLowerHighSRed", "()I");
        iLowerHighSRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getLowerLowVRed", "()I");
        iLowerLowVRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getLowerHighVRed", "()I");
        iLowerHighVRed = jniEnv->CallIntMethod(dataObject, methodId);


        methodId = jniEnv->GetMethodID(cls, "getUpperLowHRed", "()I");
        iUpperLowHRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getUpperHighHRed", "()I");
        iUpperHighHRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getUpperLowSRed", "()I");
        iUpperLowSRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getUpperHighSRed", "()I");
        iUpperHighSRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getUpperLowVRed", "()I");
        iUpperLowVRed = jniEnv->CallIntMethod(dataObject, methodId);

        methodId = jniEnv->GetMethodID(cls, "getUpperHighVRed", "()I");
        iUpperHighVRed = jniEnv->CallIntMethod(dataObject, methodId);


        /**
         * COLOR RED FILTERING
         * ******************************************/

        // Convert input image to HSV
        Mat mHsvRed;
        cv::cvtColor(temp, mHsvRed, cv::COLOR_BGR2HSV);

        // Threshold the HSV image, keep only the red pixels
        cv::Mat mLowerRedHueRange;
        cv::Mat mUpperRedHueRange;
        cv::inRange(mHsvRed, cv::Scalar(iLowerLowHRed, iLowerLowSRed, iLowerLowVRed),
                    cv::Scalar(iLowerHighHRed, iLowerHighSRed, iLowerHighVRed), mLowerRedHueRange);
        cv::inRange(mHsvRed, cv::Scalar(iUpperLowHRed, iUpperLowSRed, iUpperLowVRed),
                    cv::Scalar(iUpperHighHRed, iUpperHighSRed, iUpperHighVRed), mUpperRedHueRange);

        // Combine the extracted red images in one image
        cv::Mat mRedHueImage;
        cv::addWeighted(mLowerRedHueRange, 1.0, mUpperRedHueRange, 1.0, 0.0, mRedHueImage);

        //morphological opening (remove small objects from the foreground)
        erode(mRedHueImage, mRedHueImage, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
        dilate(mRedHueImage, mRedHueImage, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));

        GaussianBlur(mRedHueImage, mRedHueImage, Size(5,5), 2, 2);

        /* ******************************************/

        /**
         * FIELD/TABLE CROP AND GOAL EXTRACTION
         * ******************************************/

        // Find contours
        vector<vector<Point> > contours;
        vector<Vec4i> hierarchy;
        findContours(mRedHueImage, contours, hierarchy, cv::RETR_EXTERNAL, cv::CHAIN_APPROX_SIMPLE, Point2f(0, 0));

        if (contours.size() > 0)
        {
            vector<vector<Point> > contours_poly(contours.size());
            vector<Rect> boundRect(contours.size());

            for(size_t i = 0; i < contours.size(); i++)
            {
                approxPolyDP(contours[i], contours_poly[i], 3, true );
                boundRect[i] = boundingRect(Mat(contours_poly[i]));
            }

            if (boundRect.size() > 0)
            {
                // save the contour's position with the largest area. This contour is the goal.
                size_t largestRedArea = 0;

                for (size_t i = 0; i < boundRect.size(); i++)
                {
                    if (boundRect[i].area() >= boundRect[largestRedArea].area())
                    {
                        largestRedArea = i;
                    }
                }

                stMappingObjects.foundGoal = boundRect[largestRedArea];

                if (boundRect.size() >= 5) {

                    vector<Rect> boundRectSortedByArea;
                    vector<Rect> boundRectFourLargerArea(4);

                    boundRect.erase(boundRect.begin() + largestRedArea);

                    size_t largestArea = 0;
                    while (boundRect.size() > 0) {
                        for (size_t i = 0; i < boundRect.size(); i++) {
                            if (boundRect[i].area() >= boundRect[largestArea].area()) {
                                largestArea = i;
                            }
                        }

                        boundRectSortedByArea.push_back(boundRect[largestArea]);

                        boundRect.erase(boundRect.begin() + largestArea);

                        largestArea = 0;
                    }

                    boundRectFourLargerArea[0] = boundRectSortedByArea[0];
                    boundRectFourLargerArea[1] = boundRectSortedByArea[1];
                    boundRectFourLargerArea[2] = boundRectSortedByArea[2];
                    boundRectFourLargerArea[3] = boundRectSortedByArea[3];

                    // draw field rectangle
                    for (size_t i = 0; i < boundRectFourLargerArea.size(); i++)
                    {
                        rectangle(temp, boundRectFourLargerArea[i], contourColorRed, 2, 8);
                    }

                    // draw goal rectangle
                    rectangle(temp, stMappingObjects.foundGoal, contourColorRed, 2, 8);

                    // after finding the four rectangles with the larger area, find the most
                    // bottom-right point and the top-left point that maximizes the diagonal
                    // of the rectangle formed by these two points

                    vector<Point2f> fieldRectPoints(4);
                    for (size_t i = 0; i < boundRectFourLargerArea.size(); i++)
                    {
                        fieldRectPoints[i] = ((boundRectFourLargerArea[i].br() +
                                                   boundRectFourLargerArea[i].tl()) * 0.5);
                    }

                    // top-left Point
                    vector<int> topLeftIndexDesc;
                    vector<Point2f> copyFieldRectPoints = fieldRectPoints;

                    size_t mostTopLeft = 0;
                    // find the lower y
                    while (copyFieldRectPoints.size() > 0) {
                        for (size_t i = 0; i < copyFieldRectPoints.size(); i++) {
                            if (copyFieldRectPoints[i].y <= copyFieldRectPoints[mostTopLeft].y) {
                                mostTopLeft = i;
                            }
                        }

                        topLeftIndexDesc.push_back(mostTopLeft);

                        copyFieldRectPoints.erase(copyFieldRectPoints.begin() + mostTopLeft);

                        mostTopLeft = 0;
                    }

                    Point2f topLeft;
                    int topLeftIndex;
                    int distance;

                    // after finding the lower y, find the lower x
                    // depending on orientation, there could be a lower y with a larger x, so
                    // the app looks the two lower y for a lower x 
                    if (fieldRectPoints[topLeftIndexDesc[0]].x < fieldRectPoints[topLeftIndexDesc[1]].x)
                    {
                        topLeftIndex = topLeftIndexDesc[0];
                        topLeft = fieldRectPoints[topLeftIndexDesc[0]];
                        distance = norm(fieldRectPoints[topLeftIndex] - fieldRectPoints[topLeftIndexDesc[1]]);
                    }
                    else
                    {
                        topLeftIndex = topLeftIndexDesc[1];
                        topLeft = fieldRectPoints[topLeftIndexDesc[1]];
                        distance = norm(fieldRectPoints[topLeftIndex] - fieldRectPoints[topLeftIndexDesc[0]]);
                    }

                    // bottom-Right Point
                    Point2f bottomRight;
                    int bottomRightIndex;

                    for (size_t i = 0; i < fieldRectPoints.size(); i++) {
                        if (norm(fieldRectPoints[i] - fieldRectPoints[topLeftIndex]) >= distance)
                        {
                            bottomRightIndex = i;
                            distance = norm(fieldRectPoints[i] - fieldRectPoints[topLeftIndex]);
                        }
                    }

                    bottomRight = fieldRectPoints[bottomRightIndex];

                    fieldRect = Rect2f(topLeft, bottomRight);

                    // draw field
                    rectangle(temp, fieldRect, contourColorRed, 2, 8);

                    // save location relative to original image because the Goal's coordinates are relative to original image's coordinates
                    temp(fieldRect).locateROI(parentOriginalSize, offSetParent);

                    // then shift Goal Rectangle position after drawing for later use
                    stMappingObjects.foundGoal = stMappingObjects.foundGoal - (Point2f) offSetParent;

                }
                else
                {
                    __android_log_print(ANDROID_LOG_DEBUG, TAG, "Not all field's 4 edges and goal were found.");
                }
            }
            else
            {
                __android_log_print(ANDROID_LOG_DEBUG, TAG,
                                    "Crop and Goal contours could not be approximated by a rectangle.");
            }
        }
        else
        {
            __android_log_print(ANDROID_LOG_DEBUG, TAG, "No crop and goal contour was found.");
        }

        /* ******************************************/
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    obstaclesExtraction
     * Signature: (Lcom/example/gleniosp/ocvcmaketccfinal/mapping/OCVConfigData;J)V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_obstaclesExtraction
            (JNIEnv *jniEnv, jclass jClass, jobject dataObject, jlong mRgbaAddr)
    {
        if (fieldRect.width > 0)
        {

            Mat &temp = *(Mat *) mRgbaAddr;
            Mat mHsvBlack;

            int iLowHBlack;
            int iLowSBlack;
            int iLowVBlack;

            int iHighHBlack;
            int iHighSBlack;
            int iHighVBlack;

            jclass cls = jniEnv->GetObjectClass(dataObject);

            jmethodID methodId;

            methodId = jniEnv->GetMethodID(cls, "getLowHBlack", "()I");
            iLowHBlack = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getLowSBlack", "()I");
            iLowSBlack = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getLowVBlack", "()I");
            iLowVBlack = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHighHBlack", "()I");
            iHighHBlack = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHighSBlack", "()I");
            iHighSBlack = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHighVBlack", "()I");
            iHighVBlack = jniEnv->CallIntMethod(dataObject, methodId);


            Mat tempCrop = temp(fieldRect);

            vector<vector<Point> > contours;
            vector<Vec4i> hierarchy;

            cv::cvtColor(tempCrop, mHsvBlack, cv::COLOR_BGR2HSV);

            // Threshold the HSV image, keep only the black pixels
            cv::Mat mBlackHueImage;
            cv::inRange(mHsvBlack, cv::Scalar(iLowHBlack, iLowSBlack, iLowVBlack),
                        cv::Scalar(iHighHBlack, iHighSBlack, iHighVBlack), mBlackHueImage);


            findContours(mBlackHueImage, contours, hierarchy, cv::RETR_EXTERNAL,
                         cv::CHAIN_APPROX_SIMPLE, Point(0, 0));


            if (contours.size() > 0) {
                vector<vector<Point> > contours_poly_total(contours.size());

                for (size_t i = 0; i < contours.size(); i++) {
                    approxPolyDP(contours[i], contours_poly_total[i], 3, true);
                }

                vector<vector<Point> > contours_poly_rect;

                for (size_t i = 0; i < contours_poly_total.size(); i++) {
                    // if the number of vertices equals 4, it's a rectangle (or square)
                    if (contours_poly_total[i].size() == 4) {
                        contours_poly_rect.push_back(contours_poly_total[i]);
                    }
                }

                vector<Rect2f> boundRect(contours_poly_rect.size());

                for (size_t i = 0; i < contours_poly_rect.size(); i++) {

                    boundRect[i] = boundingRect(contours_poly_rect[i]);
                }

                if (boundRect.size() > 0) {
                    stMappingObjects.foundObstacles = boundRect;

                    string obstacleNumber;
                    std::stringstream ss;

                    for (size_t i = 0; i < stMappingObjects.foundObstacles.size(); i++) {
                        rectangle(tempCrop, stMappingObjects.foundObstacles[i], contourColorBlue, 1,
                                  8);
                        ss << i;
                        obstacleNumber.append(ss.str());
                        putText(tempCrop, obstacleNumber,
                                Point2f((stMappingObjects.foundObstacles[i].tl() +
                                         stMappingObjects.foundObstacles[i].br()) * 0.5),
                                cv::FONT_HERSHEY_COMPLEX_SMALL, 0.5, contourColorWhite, 1, CV_AA);
                        obstacleNumber.clear();
                        ss.str("");
                    }

                    __android_log_print(ANDROID_LOG_DEBUG, TAG,
                                        "Found obstacles size: %u",
                                        stMappingObjects.foundObstacles.size());
                } else {
                    __android_log_print(ANDROID_LOG_DEBUG, TAG,
                                        "Obstacles contours could not be approximated by a rectangle.");
                }
            } else {
                __android_log_print(ANDROID_LOG_DEBUG, TAG, "No obstacle contour was found.");
            }
        }
        else
        {
            __android_log_print(ANDROID_LOG_DEBUG, TAG, "Field was not extracted.");
        }
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    divideFieldInCells
     * Signature: (Lcom/example/gleniosp/ocvcmaketccfinal/mapping/OCVConfigData;J)V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_divideFieldInCells
            (JNIEnv *jniEnv, jclass jClass, jobject dataObject, jlong mRgbaAddr)
    {
        if (fieldRect.width > 0)
        {
            Mat &temp = *(Mat *) mRgbaAddr;

            int iYCells;
            int iXCells;

            jclass cls = jniEnv->GetObjectClass(dataObject);

            jmethodID methodId;

            methodId = jniEnv->GetMethodID(cls, "getYCells", "()I");
            iYCells = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getXCells", "()I");
            iXCells = jniEnv->CallIntMethod(dataObject, methodId);


            Mat tempCrop = temp(fieldRect);

            Mat mCellGrid = Mat::zeros(temp(fieldRect).size(), CV_8UC1);

            // Draw field cells
            //
            // Define the cell size in cellSize.
            //
            // Standard is divide the field in cells without leaving cell in half. That is, the cell number
            // must be a integer number. It is also possible to modify this cellSize value in runtime.

            // cell length in y (vertical) and x (horizontal) directions
            float cellSizeY = (float) (mCellGrid.rows - 1) / iYCells;
            float cellSizeX = (float) (mCellGrid.cols - 1) / iXCells;

            Point2f upperLeft, lowerRight;
            Rect2f gridRect;
            vector<Rect2f> cellContour;

            for (int y = 0; y < iYCells; y++) {
                for (int x = 0; x < iXCells; x++) {
                    upperLeft = Point2f(x * cellSizeX, y * cellSizeY);
                    lowerRight = Point2f(x * cellSizeX + cellSizeX, y * cellSizeY + cellSizeY);
                    gridRect = Rect2f(upperLeft, lowerRight);
                    cellContour.push_back(gridRect);
                }
            }

            string cellNumber;
            std::stringstream ss;

            for (size_t i = 0; i < cellContour.size(); i++) {
                // The cellContour's points have the right value..but it seems like the rectangle method
                // doesn't draw them correctly on screen, there is some gaps...

                rectangle(mCellGrid, cellContour[i], contourColorWhite, 1, 8);
                rectangle(tempCrop, cellContour[i], contourColorWhite, 1, 8);
                ss << i;
                cellNumber.append(ss.str());
                putText(tempCrop, cellNumber,
                        Point2f(cellContour[i].tl().x, cellContour[i].tl().y + 12),
                        cv::FONT_HERSHEY_COMPLEX_SMALL, 0.5, contourColorWhite, 1, CV_AA);
                cellNumber.clear();
                ss.str("");
            }

            stMappingObjects.createdGrid = cellContour;
        }
        else
        {
            __android_log_print(ANDROID_LOG_DEBUG, TAG, "Field was not extracted.");
        }
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    findEPuck
     * Signature: (Lcom/example/gleniosp/ocvcmaketccfinal/mapping/OCVConfigData;J)V
     */
    JNIEXPORT void JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_findEPuck
            (JNIEnv *jniEnv, jclass jClass, jobject dataObject, jlong mRgbaAddr)
    {
        if (fieldRect.width > 0)
        {
            Mat &temp = *(Mat *) mRgbaAddr;

            int iHoughCMinDistDivider;
            int iHoughCParam1;
            int iHoughCParam2;
            int iHoughCMinRadius;
            int iHoughCMaxRadius;

            jclass cls = jniEnv->GetObjectClass(dataObject);

            jmethodID methodId;

            methodId = jniEnv->GetMethodID(cls, "getHoughCMinDistDivider", "()I");
            iHoughCMinDistDivider = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHoughCParam1", "()I");
            iHoughCParam1 = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHoughCParam2", "()I");
            iHoughCParam2 = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHoughCMinRadius", "()I");
            iHoughCMinRadius = jniEnv->CallIntMethod(dataObject, methodId);

            methodId = jniEnv->GetMethodID(cls, "getHoughCMaxRadius", "()I");
            iHoughCMaxRadius = jniEnv->CallIntMethod(dataObject, methodId);


            Mat tempCrop = temp(fieldRect);

            Mat mGray;
            struct EPucksData stEPucksData;

            cvtColor(tempCrop, mGray, CV_RGB2GRAY);


            stEPucksData = houghCircleTransform(mGray,
                                                iHoughCMinDistDivider, iHoughCParam1, iHoughCParam2,
                                                iHoughCMinRadius, iHoughCMaxRadius);

            vector<Rect2f> epucksRect;

            for (size_t i = 0; i < stEPucksData.center.size(); i++) {
                if (stEPucksData.radius[i] >= 0) {
                    // circle center
                    circle(tempCrop, stEPucksData.center[i], 3, contourColorRed, -1, 8, 0);
                    // circle outline
                    circle(tempCrop, stEPucksData.center[i], stEPucksData.radius[i],
                           contourColorRed, 2, 8, 0);

                    // Compute the bounding box
                    Rect2f bboxEPuck(stEPucksData.center[i].x - stEPucksData.radius[i],
                                     stEPucksData.center[i].y - stEPucksData.radius[i],
                                     2 * stEPucksData.radius[i], 2 * stEPucksData.radius[i]);

                    epucksRect.push_back(bboxEPuck);
                }
            }

            stMappingObjects.foundEPucks = epucksRect;

            if (stMappingObjects.foundEPucks.size() > 0)
            {
                for (size_t i = 0; i < stMappingObjects.foundEPucks.size(); i++)
                {
                    rectangle(tempCrop, stMappingObjects.foundEPucks[i], contourColorRed, 1, 8);
                }
            } else
            {
                __android_log_print(ANDROID_LOG_DEBUG, TAG, "No E-Puck was found on the field.");
            }
        }
        else
        {
            __android_log_print(ANDROID_LOG_DEBUG, TAG, "Field was not extracted.");
        }
    }


    /*
     * Class:     com_example_gleniosp_OCVCmakeTCCFinal_MainActivity
     * Method:    mapField
     * Signature: (Lcom/example/gleniosp/ocvcmaketccfinal/mapping/OCVConfigData;J)Ljava.lang.object;
     */
    JNIEXPORT jobject JNICALL Java_com_example_gleniosp_ocvcmaketccfinal_MainActivity_mapField
            (JNIEnv *jniEnv, jclass jClass, jobject dataObject, jlong mRgbaAddr)
    {
        if (fieldRect.width > 0)
        {
            Mat &temp = *(Mat *) mRgbaAddr;

            stMappingObjects.stObjectsInCell = objectsInCellEmptyStruct;

            stMappingObjects.stObjectsInCell.gridSize = stMappingObjects.createdGrid.size();

            __android_log_print(ANDROID_LOG_DEBUG, TAG, "Size do Created Grid: %u", stMappingObjects.stObjectsInCell.gridSize);

            Rect Intersection;
            int largestArea = 0;
            int idxLargestArea = 0;

            Mat tempCrop = temp(fieldRect);

            // map Goal
            if (stMappingObjects.foundGoal.width != 0)
            {
                for (size_t j = 0; j < stMappingObjects.createdGrid.size(); j++)
                {
                    Intersection = stMappingObjects.foundGoal & stMappingObjects.createdGrid[j];

                    if (Intersection.area() > 0)
                    {
                        if (Intersection.area() >= largestArea)
                        {
                            largestArea = Intersection.area();
                            idxLargestArea = j;
                        }
                    }
                }

                Intersection = stMappingObjects.foundGoal & stMappingObjects.createdGrid[idxLargestArea];
                rectangle(tempCrop, Intersection, contourColorGreen, 3, 8);

                stMappingObjects.stObjectsInCell.objectName.push_back("goal");
                stMappingObjects.stObjectsInCell.cellPosition.push_back(idxLargestArea);

            }


            // map Obstacles
            for (size_t i = 0; i < stMappingObjects.foundObstacles.size(); i++)
            {

                largestArea = 0;
                idxLargestArea = 0;

                for (size_t j = 0; j < stMappingObjects.createdGrid.size(); j++)
                {
                    Intersection = stMappingObjects.foundObstacles[i] & stMappingObjects.createdGrid[j];

                    if (Intersection.area() > 0)
                    {
                        if (Intersection.area() >= largestArea)
                        {
                            largestArea = Intersection.area();
                            idxLargestArea = j;
                        }
                    }
                }

                Intersection = stMappingObjects.foundObstacles[i] & stMappingObjects.createdGrid[idxLargestArea];
                rectangle(tempCrop, Intersection, contourColorBlue, 3, 8);

                stMappingObjects.stObjectsInCell.objectName.push_back("obstacle");
                stMappingObjects.stObjectsInCell.cellPosition.push_back(idxLargestArea);

            }

            // map E-Puck
            for (size_t i = 0; i < stMappingObjects.foundEPucks.size(); i++)
            {

                largestArea = 0;
                idxLargestArea = 0;

                for (size_t j = 0; j < stMappingObjects.createdGrid.size(); j++)
                {
                    Intersection = stMappingObjects.foundEPucks[i] & stMappingObjects.createdGrid[j];

                    if (Intersection.area() > 0)
                    {
                        if (Intersection.area() >= largestArea)
                        {
                            largestArea = Intersection.area();
                            idxLargestArea = j;
                        }
                    }
                }

                Intersection = stMappingObjects.foundEPucks[i] & stMappingObjects.createdGrid[idxLargestArea];
                rectangle(tempCrop, Intersection, contourColorGreen, 3, 8);

                stMappingObjects.stObjectsInCell.objectName.push_back("epuck");
                stMappingObjects.stObjectsInCell.cellPosition.push_back(idxLargestArea);
            }

            // Create a ArrayList<String> to return to Java

            std::stringstream ss;

            vector<string> mapVector;

            ss << "gridsize" << ":" << stMappingObjects.stObjectsInCell.gridSize << "\n";

            for (size_t i = 0; i < stMappingObjects.stObjectsInCell.cellPosition.size(); i++)
            {
                ss << stMappingObjects.stObjectsInCell.objectName[i] << ":"
                   << stMappingObjects.stObjectsInCell.cellPosition[i] << "\n";
                mapVector.push_back(ss.str());
                ss.str("");
            }

            static jclass java_util_ArrayList;
            static jmethodID java_util_ArrayList_;
            jmethodID java_util_ArrayList_add;

            java_util_ArrayList      = static_cast<jclass>(jniEnv->NewGlobalRef(jniEnv->FindClass("java/util/ArrayList")));
            java_util_ArrayList_     = jniEnv->GetMethodID(java_util_ArrayList, "<init>", "(I)V");
            java_util_ArrayList_add  = jniEnv->GetMethodID(java_util_ArrayList, "add", "(Ljava/lang/Object;)Z");

            jobject result = jniEnv->NewObject(java_util_ArrayList, java_util_ArrayList_, mapVector.size());

            for (size_t i = 0; i < mapVector.size(); i++)
            {
                jstring element = jniEnv->NewStringUTF(mapVector[i].c_str());
                jniEnv->CallBooleanMethod(result, java_util_ArrayList_add, element);
                jniEnv->DeleteLocalRef(element);
            }

            return result;
        }
        else
        {
            __android_log_print(ANDROID_LOG_DEBUG, TAG, "Field was not extracted.");
            return 0;
        }
    }
}


