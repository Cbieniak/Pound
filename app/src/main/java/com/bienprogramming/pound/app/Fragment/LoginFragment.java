package com.bienprogramming.pound.app.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bienprogramming.pound.app.Activity.MainActivity;
import com.bienprogramming.pound.app.POJO.User;
import com.bienprogramming.pound.app.R;
import com.facebook.Session;

/**Simple login fragment. Updates depending on if the user is currently logged in or not. Holds facebook on activity results
 *
 */
public class LoginFragment extends Fragment {

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_login, container, false);
        User user = ((MainActivity)getActivity()).getCurrentUser();
        TextView welcomeTextView = (TextView) rootView.findViewById(R.id.welcome);
        welcomeTextView.setText(welcomeTextView.getText().toString() + ((user != null) ? " " + user.getName() : ","));
        return  rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this.getActivity(), requestCode, resultCode, data);
    }

}
