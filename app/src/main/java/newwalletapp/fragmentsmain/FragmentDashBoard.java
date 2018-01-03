package newwalletapp.fragmentsmain;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.UpdateDashboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashBoard extends android.support.v4.app.Fragment {



    ReadSQLiteData readSQLiteData;
    public AccountFragmentPagerAdapter accountFragmentPagerAdapter;
    public ViewPager viewPager;

    /*    AccountFragmentPagerAdapter accountFragmentPagerAdapter;
        ViewPager viewPager;
        ArrayList<HashMap<String, String>> arrayListAccounts;
    */
    public void updateViewPager()
    {
        viewPager.getAdapter().notifyDataSetChanged();
    }
    public FragmentDashBoard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        log("FragmentDashBoard onCreateView");
        return inflater.inflate(R.layout.fragment_deshboard, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("FragmentDashBoard onActivityCreated");

        readSQLiteData = new ReadSQLiteData(getActivity());
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);

/*
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        arrayListAccounts = readSQLiteData.getAllAccountsData();

        List<android.support.v4.app.Fragment> fragments = getFragments();

        accountFragmentPagerAdapter=new AccountFragmentPagerAdapter(getChildFragmentManager(),getAccountsName(),fragments);
        viewPager.setAdapter(accountFragmentPagerAdapter);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerStrip);
        pagerTabStrip.setDrawFullUnderline(false);
*/


    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.deshboard));
        new SetUpDataTask().execute(null,null,null);
        //accountFragmentPagerAdapter.notifyDataSetChanged();
        //accountFragmentPagerAdapter.updatePage();
    }

    private String[] getAccountsName()
    {

        ArrayList<HashMap<String,String>> map;
        map=readSQLiteData.getAllAccountsData();
        String[] arrayAccountName=new String[map.size()];
        for(int i=0;i<map.size();i++)
        {
            arrayAccountName[i] = map.get(i).get("acc_name");
        }
        return  arrayAccountName;
    }



    private String[] getTotalAmountForAccounts()
    {
        String[] arrayAccountName = getAccountsName();
        String[] total = new String[arrayAccountName.length];
        String currencySymbol = readSQLiteData.getCurrencySymbol();
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i=0;i<arrayAccountName.length;i++)
        {

            total[i]=""+decimalFormat.format(readSQLiteData.getTotalOfAllIncome(arrayAccountName[i]) - readSQLiteData.getTotalOfAllExpense(arrayAccountName[i]))+" "+currencySymbol;
        }
        return total;
    }

    private HashMap<String,ArrayList<HashMap<String,String>>> getArrayMapForTopRecords()
    {
        String[] arrayAccountName = getAccountsName();
        HashMap<String,ArrayList<HashMap<String,String>>> map = new HashMap<String,ArrayList<HashMap<String,String>>>();
        for (int i=0;i<arrayAccountName.length;i++)
        {
            ArrayList<HashMap<String,String>> arrayList = readSQLiteData.getTopTenRecords(arrayAccountName[i]);
            map.put(""+i,arrayList);
        }
        return map;
    }

    private List<android.support.v4.app.Fragment> getFragments(){
        List<android.support.v4.app.Fragment> fList = new ArrayList<android.support.v4.app.Fragment>();

        ArrayList<HashMap<String,String>> map;

        String[] arrayAccountName=getAccountsName();
        for(int i=0;i<arrayAccountName.length;i++)
        {
            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            //fList.add(FragmentDeshPage.newInstance(arrayAccountName[i],""+decimalFormat.format(readSQLiteData.getTotalOfAllIncome(arrayAccountName[i]) - readSQLiteData.getTotalOfAllExpense(arrayAccountName[i]))+" "+readSQLiteData.getCurrencySymbol(),readSQLiteData.getTopTenRecords(arrayAccountName[i])));
            fList.add(FragmentDeshPage.newInstance(arrayAccountName[i]));
        }

        return fList;
    }



/*
    private class AccountFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<android.support.v4.app.Fragment> fragments;
        String[] accounts;
        FragmentManager fragmentManager;
        public AccountFragmentPagerAdapter(FragmentManager fm,String[] acc_name, List<android.support.v4.app.Fragment> fragments) {
            super(fm);
            this.accounts=acc_name;
            this.fragmentManager=fm;
            this.fragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
*/
/*
            FragmentDeshPage fragmentDeshPage= (FragmentDeshPage) object;
            log("getItemPosition");
            if (fragmentDeshPage!=null)
            {
                log("fragmentDeshPage!=null");

                fragmentDeshPage.update();
            }
            return super.getItemPosition(object);
*//*

            return POSITION_NONE;
        }



        @Override
        public android.support.v4.app.Fragment getItem(int position) {



            return this.fragments.get(position);
        }



        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return accounts[position];
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer!=null)
            {
                super.unregisterDataSetObserver(observer);
            }

        }
    }
*/



    private class AccountFragmentPagerAdapter extends FragmentPagerAdapter{

        String[] accounts,total;
        HashMap<String,ArrayList<HashMap<String,String>>> arrayMap;
        FragmentManager fragmentManager;
/*
        public AccountFragmentPagerAdapter(FragmentManager fm,String[] acc_name,String[] totalAmount,HashMap<String,ArrayList<HashMap<String,String>>> mapArrayMap) {
            super(fm);
            this.accounts=acc_name;
            this.fragmentManager=fm;
            this.arrayMap=mapArrayMap;
            this.total=totalAmount;

        }
*/

        public AccountFragmentPagerAdapter(FragmentManager fm,String[] acc_name) {
            super(fm);
            this.accounts=acc_name;
            this.fragmentManager=fm;
/*
    this.arrayMap=mapArrayMap;
    this.total=totalAmount;
*/

        }

        @Override
        public int getItemPosition(Object object) {
           log("getItemPosition");

           return POSITION_NONE;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            //android.support.v4.app.Fragment fragment= new FragmentDeshPage().newInstance(accounts[position],total[position],arrayMap.get(""+position));
            android.support.v4.app.Fragment fragment = new FragmentDeshPage().newInstance(accounts[position]);

            return fragment;
        }

        public void updateData()
        {
            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return this.accounts.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return accounts[position];
        }


        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            if (observer!=null)
            {
                super.unregisterDataSetObserver(observer);
            }

        }
    }




    private class SetUpDataTask extends AsyncTask<Void,Void,Void>{

        ReadSQLiteData readSQLiteData;
        //AccountFragmentPagerAdapter accountFragmentPagerAdapter;

        String[] totalAmount;
        HashMap<String,ArrayList<HashMap<String,String>>> mapArray;
        ProgressDialog progressDialog;
        List<android.support.v4.app.Fragment> fragments;
        ArrayList<HashMap<String, String>> arrayListAccounts;
        String[] accountNames;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            readSQLiteData = new ReadSQLiteData(getActivity());
            progressDialog = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.please_wait),getActivity().getResources().getString(R.string.loading_data));
        }

        @Override
        protected Void doInBackground(Void... params) {

            arrayListAccounts = readSQLiteData.getAllAccountsData();
            //fragments = getFragments();
            accountNames = getAccountsName();
            //mapArray=getArrayMapForTopRecords();
            //totalAmount=getTotalAmountForAccounts();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();


            PagerTabStrip pagerTabStrip = (PagerTabStrip) getActivity().findViewById(R.id.pagerStrip);
            pagerTabStrip.setDrawFullUnderline(false);
            //accountFragmentPagerAdapter=new AccountFragmentPagerAdapter(getChildFragmentManager(),getAccountsName(),fragments);
            //accountFragmentPagerAdapter=new AccountFragmentPagerAdapter(getChildFragmentManager(),getAccountsName(),totalAmount,mapArray);
            accountFragmentPagerAdapter = new AccountFragmentPagerAdapter(getChildFragmentManager(),getAccountsName());
            accountFragmentPagerAdapter.updateData();
            viewPager.setAdapter(accountFragmentPagerAdapter);




        }
    }


    void log(String msg)
    {
        Log.d("FragmentDeshBoard",msg);
    }


}
