package com.example.gooder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private final String TAG = "SettingFragment";

    private FirebaseAuth auth;

    private TextView tvSettingUserName;
    private Switch switchDarkMode;
    private Button btnSettingSignOut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        auth = FirebaseAuth.getInstance();

        tvSettingUserName = view.findViewById(R.id.tv_setting_user_name);
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        btnSettingSignOut = view.findViewById(R.id.btn_setting_sign_out);

        if (auth.getCurrentUser() != null) {
            String name = auth.getCurrentUser().getDisplayName();
            Log.d(TAG, "使用者ID:"+auth.getCurrentUser().getUid());
            if (name != null && !name.isEmpty()) {
                tvSettingUserName.setText("Hello " + name);
            } else {
                tvSettingUserName.setText("Hello " + auth.getCurrentUser().getEmail());
            }
        }

        SharedPreferences prefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(darkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int mode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
            AppCompatDelegate.setDefaultNightMode(mode);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
        });

        btnSettingSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(getContext(), "已登出", Toast.LENGTH_SHORT).show();
                //TODO 跳轉頁面
            }
        });


        return view;
    }
}