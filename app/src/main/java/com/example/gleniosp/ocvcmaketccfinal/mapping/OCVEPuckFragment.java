package com.example.gleniosp.ocvcmaketccfinal.mapping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.R;

public class OCVEPuckFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View rootView;

    private SeekBar seekBarHoughCMinDistDivider;
    private TextView tvPosHoughCMinDistDivider;

    private SeekBar seekBarHoughCParam1;
    private TextView tvPosHoughCParam1;

    private SeekBar seekBarHoughCParam2;
    private TextView tvPosHoughCParam2;

    private SeekBar seekBarHoughCMinRadius;
    private TextView tvPosHoughCMinRadius;

    private SeekBar seekBarHoughCMaxRadius;
    private TextView tvPosHoughCMaxRadius;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_epuck, container, false);

        getWidgetReferences();

        seekBarHoughCMinDistDivider.setOnSeekBarChangeListener(this);
        seekBarHoughCParam1.setOnSeekBarChangeListener(this);
        seekBarHoughCParam2.setOnSeekBarChangeListener(this);
        seekBarHoughCMinRadius.setOnSeekBarChangeListener(this);
        seekBarHoughCMaxRadius.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadSeekBarMax();

        loadSeekBarPos();
    }

    private void getWidgetReferences() {
        seekBarHoughCMinDistDivider = (SeekBar) rootView.findViewById(R.id.seekBarHoughCMinDistDivider);
        tvPosHoughCMinDistDivider = (TextView) rootView.findViewById(R.id.tvPosHoughCMinDistDivider);

        seekBarHoughCParam1 = (SeekBar) rootView.findViewById(R.id.seekBarHoughCParam1);
        tvPosHoughCParam1 = (TextView) rootView.findViewById(R.id.tvPosHoughCParam1);

        seekBarHoughCParam2 = (SeekBar) rootView.findViewById(R.id.seekBarHoughCParam2);
        tvPosHoughCParam2 = (TextView) rootView.findViewById(R.id.tvPosHoughCParam2);

        seekBarHoughCMinRadius = (SeekBar) rootView.findViewById(R.id.seekBarHoughCMinRadius);
        tvPosHoughCMinRadius = (TextView) rootView.findViewById(R.id.tvPosHoughCMinRadius);

        seekBarHoughCMaxRadius = (SeekBar) rootView.findViewById(R.id.seekBarHoughCMaxRadius);
        tvPosHoughCMaxRadius = (TextView) rootView.findViewById(R.id.tvPosHoughCMaxRadius);
    }

    private void loadSeekBarMax() {
        seekBarHoughCMinDistDivider.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxHoughCMinDistDivider());
        seekBarHoughCParam1.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxHoughCParam1());
        seekBarHoughCParam2.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxHoughCParam2());
        seekBarHoughCMinRadius.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxHoughCMinRadius());
        seekBarHoughCMaxRadius.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxHoughCMaxRadius());
    }

    private void loadSeekBarPos() {
        int position = ((MainActivity ) getActivity()).getOcvConfigData().getHoughCMinDistDivider();
        seekBarHoughCMinDistDivider.setProgress(position);
        tvPosHoughCMinDistDivider.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHoughCParam1();
        seekBarHoughCParam1.setProgress(position);
        tvPosHoughCParam1.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHoughCParam2();
        seekBarHoughCParam2.setProgress(position);
        tvPosHoughCParam2.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHoughCMinRadius();
        seekBarHoughCMinRadius.setProgress(position);
        tvPosHoughCMinRadius.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHoughCMaxRadius();
        seekBarHoughCMaxRadius.setProgress(position);
        tvPosHoughCMaxRadius.setText(String.valueOf(position));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int MIN_LIMIT_HOUGH_C_MINDD = ((MainActivity ) getActivity()).getOcvConfigData().getMinHoughCMinDistDivider();
            int MIN_LIMIT_HOUGH_C_PARAM1 = ((MainActivity ) getActivity()).getOcvConfigData().getMinHoughCParam1();
            int MIN_LIMIT_HOUGH_C_PARAM2 = ((MainActivity ) getActivity()).getOcvConfigData().getMinHoughCParam2();
            int MIN_LIMIT_HOUGH_C_MINRADIUS = ((MainActivity ) getActivity()).getOcvConfigData().getMinHoughCMinRadius();
            int MIN_LIMIT_HOUGH_C_MAXRADIUS= ((MainActivity ) getActivity()).getOcvConfigData().getMinHoughCMaxRadius();
            switch (seekBar.getId()) {
                case R.id.seekBarHoughCMinDistDivider:
                    if (progress <= MIN_LIMIT_HOUGH_C_MINDD) {
                        progress = MIN_LIMIT_HOUGH_C_MINDD;
                    }
                    tvPosHoughCMinDistDivider.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHoughCMinDistDivider(progress);
                    break;
                case R.id.seekBarHoughCParam1:
                    if (progress <= MIN_LIMIT_HOUGH_C_PARAM1) {
                        progress = MIN_LIMIT_HOUGH_C_PARAM1;
                    }
                    tvPosHoughCParam1.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHoughCParam1(progress);
                    break;
                case R.id.seekBarHoughCParam2:
                    if (progress <= MIN_LIMIT_HOUGH_C_PARAM2) {
                        progress = MIN_LIMIT_HOUGH_C_PARAM2;
                    }
                    tvPosHoughCParam2.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHoughCParam2(progress);
                    break;
                case R.id.seekBarHoughCMinRadius:
                    if (progress <= MIN_LIMIT_HOUGH_C_MINRADIUS) {
                        progress = MIN_LIMIT_HOUGH_C_MINRADIUS;
                    }
                    tvPosHoughCMinRadius.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHoughCMinRadius(progress);
                    break;
                case R.id.seekBarHoughCMaxRadius:
                    if (progress <= MIN_LIMIT_HOUGH_C_MAXRADIUS) {
                        progress = MIN_LIMIT_HOUGH_C_MAXRADIUS;
                    }
                    tvPosHoughCMaxRadius.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHoughCMaxRadius(progress);
                    break;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
