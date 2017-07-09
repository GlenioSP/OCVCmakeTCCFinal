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

public class OCVObstaclesFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View rootView;

    private SeekBar seekBarLowHBlack;
    private TextView tvPosLowHBlack;

    private SeekBar seekBarHighHBlack;
    private TextView tvPosHighHBlack;

    private SeekBar seekBarLowSBlack;
    private TextView tvPosLowSBlack;

    private SeekBar seekBarHighSBlack;
    private TextView tvPosHighSBlack;

    private SeekBar seekBarLowVBlack;
    private TextView tvPosLowVBlack;

    private SeekBar seekBarHighVBlack;
    private TextView tvPosHighVBlack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_obstacles, container, false);

        getWidgetReferences();

        seekBarLowHBlack.setOnSeekBarChangeListener(this);
        seekBarHighHBlack.setOnSeekBarChangeListener(this);
        seekBarLowSBlack.setOnSeekBarChangeListener(this);
        seekBarHighSBlack.setOnSeekBarChangeListener(this);
        seekBarLowVBlack.setOnSeekBarChangeListener(this);
        seekBarHighVBlack.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadSeekBarMax();

        loadSeekBarPos();
    }

    private void getWidgetReferences() {
        seekBarLowHBlack = (SeekBar) rootView.findViewById(R.id.seekBarLowHBlack);
        tvPosLowHBlack = (TextView) rootView.findViewById(R.id.tvPosLowHBlack);

        seekBarHighHBlack = (SeekBar) rootView.findViewById(R.id.seekBarHighHBlack);
        tvPosHighHBlack = (TextView) rootView.findViewById(R.id.tvPosHighHBlack);

        seekBarLowSBlack = (SeekBar) rootView.findViewById(R.id.seekBarLowSBlack);
        tvPosLowSBlack = (TextView) rootView.findViewById(R.id.tvPosLowSBlack);

        seekBarHighSBlack = (SeekBar) rootView.findViewById(R.id.seekBarHighSBlack);
        tvPosHighSBlack = (TextView) rootView.findViewById(R.id.tvPosHighSBlack);

        seekBarLowVBlack = (SeekBar) rootView.findViewById(R.id.seekBarLowVBlack);
        tvPosLowVBlack = (TextView) rootView.findViewById(R.id.tvPosLowVBlack);

        seekBarHighVBlack = (SeekBar) rootView.findViewById(R.id.seekBarHighVBlack);
        tvPosHighVBlack = (TextView) rootView.findViewById(R.id.tvPosHighVBlack);
    }

    private void loadSeekBarMax() {
        seekBarLowHBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarHighHBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorH());
        seekBarLowSBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarHighSBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorS());
        seekBarLowVBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());
        seekBarHighVBlack.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMaxColorV());
    }

    private void loadSeekBarPos() {
        int position = ((MainActivity ) getActivity()).getOcvConfigData().getLowHBlack();
        seekBarLowHBlack.setProgress(position);
        tvPosLowHBlack.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHighHBlack();
        seekBarHighHBlack.setProgress(position);
        tvPosHighHBlack.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowSBlack();
        seekBarLowSBlack.setProgress(position);
        tvPosLowSBlack.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHighSBlack();
        seekBarHighSBlack.setProgress(position);
        tvPosHighSBlack.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getLowVBlack();
        seekBarLowVBlack.setProgress(position);
        tvPosLowVBlack.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getHighVBlack();
        seekBarHighVBlack.setProgress(position);
        tvPosHighVBlack.setText(String.valueOf(position));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int MIN_LIMIT_HUE = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorH();
            int MIN_LIMIT_SATURATION = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorS();
            int MIN_LIMIT_VALUE = ((MainActivity ) getActivity()).getOcvConfigData().getMinColorV();
            switch (seekBar.getId()) {
                case R.id.seekBarLowHBlack:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosLowHBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowHBlack(progress);
                    break;
                case R.id.seekBarHighHBlack:
                    if (progress <= MIN_LIMIT_HUE) {
                        progress = MIN_LIMIT_HUE;
                    }
                    tvPosHighHBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHighHBlack(progress);
                    break;
                case R.id.seekBarLowSBlack:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosLowSBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowSBlack(progress);
                    break;
                case R.id.seekBarHighSBlack:
                    if (progress <= MIN_LIMIT_SATURATION) {
                        progress = MIN_LIMIT_SATURATION;
                    }
                    tvPosHighSBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHighSBlack(progress);
                    break;
                case R.id.seekBarLowVBlack:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosLowVBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setLowVBlack(progress);
                    break;
                case R.id.seekBarHighVBlack:
                    if (progress <= MIN_LIMIT_VALUE) {
                        progress = MIN_LIMIT_VALUE;
                    }
                    tvPosHighVBlack.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setHighVBlack(progress);
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
