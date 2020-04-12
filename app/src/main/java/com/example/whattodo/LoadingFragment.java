package com.example.whattodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoadingFragment extends Fragment {

    private Handler mHandler = new Handler();
    private static int TIME_OUT = 3500;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_loading, container, false);

        mHandler.postDelayed(new Runanble() {
            public void run() {

                getFragmentManager().beginTransaction().replace(R.id.fragment_main, new LogInFragment()).commit();
            }
        }, TIME_OUT); // 3,5 seconds

        return mView;
    }

    private class Runanble implements Runnable {
        @Override
        public void run() {

        }
    }
}
