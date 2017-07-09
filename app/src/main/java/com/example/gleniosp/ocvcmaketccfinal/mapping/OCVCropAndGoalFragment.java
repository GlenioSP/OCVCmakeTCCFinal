package com.example.gleniosp.ocvcmaketccfinal.mapping;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.R;

public class OCVCropAndGoalFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View rootView;

    private SeekBar seekBarLowerLowHRed;
    private TextView tvPosLowerLowHRed;

    private SeekBar seekBarLowerHighHRed;
    private TextView tvPosLowerHighHRed;

    private SeekBar seekBarLowerLowSRed;
    private TextView tvPosLowerLowSRed;

    private SeekBar seekBarLowerHighSRed;
    private TextView tvPosLowerHighSRed;

    private SeekBar seekBarLowerLowVRed;
    private TextView tvPosLowerLowVRed;

    private SeekBar seekBarLowerHighVRed;
    private TextView tvPosLowerHighVRed;

    private SeekBar seekBarUpperLowHRed;
    private TextView tvPosUpperLowHRed;

    private SeekBar seekBarUpperHighHRed;
    private TextView tvPosUpperHighHRed;

    private SeekBar seekBarUpperLowSRed;
    private TextView tvPosUpperLowSRed;

    private SeekBar seekBarUpperHighSRed;
    private TextView tvPosUpperHighSRed;

    private SeekBar seekBarUpperLowVRed;
    private TextView tvPosUpperLowVRed;

    private SeekBar seekBarUpperHighVRed;
    private TextView tvPosUpperHighVRed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        rootView = inflater.inflate(R.layout.fragment_crop_and_goal, container, false);

        getWidgetReferences();

        seekBarLowerLowHRed.setOnSeekBarChangeListener(this);
        seekBarLowerHighHRed.setOnSeekBarChangeListener(this);
        seekBarLowerLowSRed.setOnSeekBarChangeListener(this);
        seekBarLowerHighSRed.setOnSeekBarChangeListener(this);
        seekBarLowerLowVRed.setOnSeekBarChangeListener(this);
        seekBarLowerHighVRed.setOnSeekBarChangeListener(this);

        seekBarUpperLowHRed.setOnSeekBarChangeListener(this);
        seekBarUpperHighHRed.setOnSeekBarChangeListener(this);
        seekBarUpperLowSRed.setOnSeekBarChangeListener(this);
        seekBarUpperHighSRed.setOnSeekBarChangeListener(this);
        seekBarUpperLowVRed.setOnSeekBarChangeListener(this);
        seekBarUpperHighVRed.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadSeekBarMax();

        loadSeekBarPos();
    }

    private void getWidgetReferences() {
        seekBarLowerLowHRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerLowHRed);
        tvPosLowerLowHRed = (TextView) rootView.findViewById(R.id.tvPosLowerLowHRed);

        seekBarLowerHighHRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerHighHRed);
        tvPosLowerHighHRed = (TextView) rootView.findViewById(R.id.tvPosLowerHighHRed);

        seekBarLowerLowSRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerLowSRed);
        tvPosLowerLowSRed = (TextView) rootView.findViewById(R.id.tvPosLowerLowSRed);

        seekBarLowerHighSRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerHighSRed);
        tvPosLowerHighSRed = (TextView) rootView.findViewById(R.id.tvPosLowerHighSRed);

        seekBarLowerLowVRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerLowVRed);
        tvPosLowerLowVRed = (TextView) rootView.findViewById(R.id.tvPosLowerLowVRed);

        seekBarLowerHighVRed = (SeekBar) rootView.findViewById(R.id.seekBarLowerHighVRed);
        tvPosLowerHighVRed = (TextView) rootView.findViewById(R.id.tvPosLowerHighVRed);

        seekBarUpperLowHRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperLowHRed);
        tvPosUpperLowHRed = (TextView) rootView.findViewById(R.id.tvPosUpperLowHRed);

        seekBarUpperHighHRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperHighHRed);
        tvPosUpperHighHRed = (TextView) rootView.findViewById(R.id.tvPosUpperHighHRed);

        seekBarUpperLowSRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperLowSRed);
        tvPosUpperLowSRed = (TextView) rootView.findViewById(R.id.tvPosUpperLowSRed);

        seekBarUpperHighSRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperHighSRed);
        tvPosUpperHighSRed = (TextView) rootView.findViewById(R.id.tvPosUpperHighSRed);

        seekBarUpperLowVRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperLowVRed);
        tvPosUpperLowVRed = (TextView) rootView.findViewById(R.id.tvPosUpperLowVRed);

        seekBarUpperHighVRed = (SeekBar) rootView.findViewById(R.id.seekBarUpperHighVRed);
        tvPosUpperHighVRed = (TextView) rootView.findViewById(R.id.tvPosUpperHighVRed);
    }

    private void loadSeekBarMax() {
        seekBarLowerLowHRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarLowerHighHRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarLowerLowSRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarLowerHighSRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarLowerLowVRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());
        seekBarLowerHighVRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());

        seekBarUpperLowHRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarUpperHighHRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarUpperLowSRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarUpperHighSRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarUpperLowVRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());
        seekBarUpperHighVRed.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());
    }

    private void loadSeekBarPos() {
        int position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerLowHRed();
        seekBarLowerLowHRed.setProgress(position);
        tvPosLowerLowHRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerHighHRed();
        seekBarLowerHighHRed.setProgress(position);
        tvPosLowerHighHRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerLowSRed();
        seekBarLowerLowSRed.setProgress(position);
        tvPosLowerLowSRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerHighSRed();
        seekBarLowerHighSRed.setProgress(position);
        tvPosLowerHighSRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerLowVRed();
        seekBarLowerLowVRed.setProgress(position);
        tvPosLowerLowVRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowerHighVRed();
        seekBarLowerHighVRed.setProgress(position);
        tvPosLowerHighVRed.setText(String.valueOf(position));


        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperLowHRed();
        seekBarUpperLowHRed.setProgress(position);
        tvPosUpperLowHRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperHighHRed();
        seekBarUpperHighHRed.setProgress(position);
        tvPosUpperHighHRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperLowSRed();
        seekBarUpperLowSRed.setProgress(position);
        tvPosUpperLowSRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperHighSRed();
        seekBarUpperHighSRed.setProgress(position);
        tvPosUpperHighSRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperLowVRed();
        seekBarUpperLowVRed.setProgress(position);
        tvPosUpperLowVRed.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getUpperHighVRed();
        seekBarUpperHighVRed.setProgress(position);
        tvPosUpperHighVRed.setText(String.valueOf(position));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int MIN_LIMIT_HUE = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorH();
            int MIN_LIMIT_SATURATION = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorS();
            int MIN_LIMIT_VALUE = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorV();
            switch (seekBar.getId()) {
                case R.id.seekBarLowerLowHRed:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosLowerLowHRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerLowHRed(progress);
                    break;
                case R.id.seekBarLowerHighHRed:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosLowerHighHRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerHighHRed(progress);
                    break;
                case R.id.seekBarLowerLowSRed:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosLowerLowSRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerLowSRed(progress);
                    break;
                case R.id.seekBarLowerHighSRed:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosLowerHighSRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerHighSRed(progress);
                    break;
                case R.id.seekBarLowerLowVRed:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosLowerLowVRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerLowVRed(progress);
                    break;
                case R.id.seekBarLowerHighVRed:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosLowerHighVRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowerHighVRed(progress);
                    break;

                case R.id.seekBarUpperLowHRed:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosUpperLowHRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperLowHRed(progress);
                    break;
                case R.id.seekBarUpperHighHRed:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosUpperHighHRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperHighHRed(progress);
                    break;
                case R.id.seekBarUpperLowSRed:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosUpperLowSRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperLowSRed(progress);
                    break;
                case R.id.seekBarUpperHighSRed:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosUpperHighSRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperHighSRed(progress);
                    break;
                case R.id.seekBarUpperLowVRed:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosUpperLowVRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperLowVRed(progress);
                    break;
                case R.id.seekBarUpperHighVRed:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosUpperHighVRed.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setUpperHighVRed(progress);
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
