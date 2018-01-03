package newwalletapp.fragmentsmain;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.accounts.newwalletapp.MainActivity;
import com.example.accounts.newwalletapp.R;
import newwalletapp.database.DataPrefrences;

public class FragmentSplash extends Fragment {

    Handler h = new Handler();
    DataPrefrences dataPrefrences;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getResources().getString(R.string.dialog_select_language));
        final RadioGroup rg = new RadioGroup(getActivity());
        rg.setOrientation(RadioGroup.VERTICAL);

        final RadioButton radioButtonLanguageEnglish = new RadioButton(getActivity());
        radioButtonLanguageEnglish.setId(R.id.radio_language_english);
        radioButtonLanguageEnglish.setText(getActivity().getResources().getString(R.string.language_english));
        radioButtonLanguageEnglish.setPadding(10, 5, 20, 5);
        rg.addView(radioButtonLanguageEnglish);
        rg.check(radioButtonLanguageEnglish.getId());
*/
/*
				if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_daily))) {
					rg.check(radioButtonBackupDaily.getId());
					previoslyCheckedId = radioButtonBackupDaily.getId();
				}
*//*


        final RadioButton radioButtonLanguageArabic = new RadioButton(getActivity());
        radioButtonLanguageArabic.setId(R.id.radio_language_arabic);
        radioButtonLanguageArabic.setText(getActivity().getResources().getString(R.string.language_arabic));
        radioButtonLanguageArabic.setPadding(10, 5, 20, 5);
        rg.addView(radioButtonLanguageArabic);
        builder.setView(rg);
        builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
        builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
        final AlertDialog dialog = builder.create();
        dialog.show();
*/
        h.postDelayed(run, 2500);
        dataPrefrences = new DataPrefrences(getActivity());
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (dataPrefrences.GetStoredPrefrence("start").equals("ok")) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                Fragment fragmentDashBoard = new FragmentDashBoard();
                Fragment fragmentGateWay = new FragmentGateWay();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.gateway_content, fragmentGateWay);
                ft.commit();

            }


        }
    };
}
