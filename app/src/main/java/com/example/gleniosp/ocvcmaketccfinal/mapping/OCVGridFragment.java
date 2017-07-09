package com.example.gleniosp.ocvcmaketccfinal.mapping;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gleniosp.ocvcmaketccfinal.MainActivity;
import com.example.gleniosp.ocvcmaketccfinal.R;

public class OCVGridFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private View rootView;

    private SeekBar seekBarYCells;
    private TextView tvPosYCells;

    private SeekBar seekBarXCells;
    private TextView tvPosXCells;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        getWidgetReferences();

        seekBarYCells.setOnSeekBarChangeListener(this);
        seekBarXCells.setOnSeekBarChangeListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadSeekBarMax();

        loadSeekBarPos();
    }

    private void getWidgetReferences() {
        seekBarYCells = (SeekBar) rootView.findViewById(R.id.seekBarYCells);
        tvPosYCells = (TextView) rootView.findViewById(R.id.tvPosYCells);

        seekBarXCells = (SeekBar) rootView.findViewById(R.id.seekBarXCells);
        tvPosXCells = (TextView) rootView.findViewById(R.id.tvPosXCells);
    }

    private void loadSeekBarMax() {
        seekBarYCells.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMAX_CELL_NUMBER());
        seekBarXCells.setMax(((MainActivity ) getActivity()).getOcvConfigData().getMAX_CELL_NUMBER());
    }

    private void loadSeekBarPos() {
        int position = ((MainActivity ) getActivity()).getOcvConfigData().getYCells();
        seekBarYCells.setProgress(position);
        tvPosYCells.setText(String.valueOf(position));

        position = ((MainActivity ) getActivity()).getOcvConfigData().getXCells();
        seekBarXCells.setProgress(position);
        tvPosXCells.setText(String.valueOf(position));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int MIN_LIMIT = ((MainActivity ) getActivity()).getOcvConfigData().getMIN_CELL_NUMBER();
            if (progress <= MIN_LIMIT) {
                progress = MIN_LIMIT;
            }
            switch (seekBar.getId()) {
                case R.id.seekBarYCells:
                    tvPosYCells.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setYCells(progress);
                    break;
                case R.id.seekBarXCells:
                    tvPosXCells.setText(String.valueOf(progress));
                    ((MainActivity ) getActivity()).getOcvConfigData().setXCells(progress);
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
