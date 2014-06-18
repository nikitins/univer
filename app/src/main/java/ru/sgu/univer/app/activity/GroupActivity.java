package ru.sgu.univer.app.activity;

import java.util.Locale;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.sgu.univer.app.fragments.GroupFragment;
import ru.sgu.univer.app.R;
import ru.sgu.univer.app.fragments.RatingFragment;

public class GroupActivity extends ActionBarActivity implements GroupFragment.OnFragmentInteractionListener, RatingFragment.OnFragmentInteractionListener {
    public static final String GROUP_ID_EXTRA = "group_id_extra";
    SectionsPagerAdapter mSectionsPagerAdapter;
    GroupFragment groupFragment;
    RatingFragment ratingFragment;
    ViewPager mViewPager;
    int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        int groupId = getIntent().getIntExtra(GROUP_ID_EXTRA, 0);
        groupFragment = GroupFragment.newInstance(groupId);
        ratingFragment = RatingFragment.newInstance(groupId);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public void onFragmentInteraction(String id) {
    }

    @Override
    public void onTableFragmentInteraction(Uri uri) {
        showMessage(uri.toString());
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            getItem is called to instantiate the fragment for the given page.
//            Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                currentFragment = 0;
                return groupFragment;
            }
            if (position == 1) {
                currentFragment = 1;
                return ratingFragment;
            }
            currentFragment = 2;
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group_list, container, false);
            TextView textView = new TextView(getActivity());
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }

}
