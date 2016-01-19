package com.ramzcalender;

import com.ramzcalender.listener.CalenderListener;
import com.ramzcalender.utils.AppController;
import com.ramzcalender.utils.CalUtil;

import org.joda.time.LocalDateTime;
import org.joda.time.Weeks;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Ramesh M Nair
 * Edit: Asi Mugrabi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class RWeekCalendar extends Fragment {
    //Bundle Keys
    public static String ARGUMENT_DATE_SELECTOR_BACKGROUND = "bg:select:date";
    public static String ARGUMENT_CURRENT_DATE_BACKGROUND = "bg:current:bg";
    public static String ARGUMENT_CALENDER_BACKGROUND_COLOR = "bg:cal";
    public static String ARGUMENT_NOW_BACKGROUND = "bg:now";
    public static String ARGUMENT_PRIMARY_TEXT_COLOR = "bg:primary";
    public static String ARGUMENT_SECONDARY_TEXT_COLOR = "bg:secondary";
    public static String ARGUMENT_WEEK_COUNT = "week:count";
    public static String ARGUMENT_DISPLAY_DATE_PICKER = "display:date:picker";

    public static String PACKAGE_NAME = "package";
    public static String POSITION_KEY = "pos";
    public static String PACKAGE_NAME_VALUE = "com.ramzcalender";

    LocalDateTime mStartDate, mSelectedDate;
    boolean mDisplayDatePicker = true;

    TextView mMonthView, mNowView, mSundayTv, mMondayTv, mTuesdayTv, mWednesdayTv, mThursdayTv;
    TextView mFridayTv, mSaturdayTv;
    ViewPager mViewPager;
    LinearLayout mBackground;
    ViewGroup mFrameDatePicker;

    CalenderListener mCalenderListener;

    private static RWeekCalendar sWeekCalendarInstance;
    CalenderAdapter mAdapter;

    //initial values of calender property
    String mSelectorDateIndicatorValue = "bg_red";
    int mCurrentDateIndicatorValue = Color.BLACK;
    int mPrimaryTextColor = Color.WHITE;
    int mWeekCount = 53;//one year

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializing instance
        sWeekCalendarInstance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_calender, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mMonthView = (TextView) view.findViewById(R.id.month_tv);
        mNowView = (TextView) view.findViewById(R.id.now_tv);
        mSundayTv = (TextView) view.findViewById(R.id.week_sunday);
        mMondayTv = (TextView) view.findViewById(R.id.week_monday);
        mTuesdayTv = (TextView) view.findViewById(R.id.week_tuesday);
        mWednesdayTv = (TextView) view.findViewById(R.id.week_wednesday);
        mThursdayTv = (TextView) view.findViewById(R.id.week_thursday);
        mFridayTv = (TextView) view.findViewById(R.id.week_friday);
        mSaturdayTv = (TextView) view.findViewById(R.id.week_saturday);
        mBackground = (LinearLayout) view.findViewById(R.id.background);
        mFrameDatePicker = (ViewGroup) view.findViewById(R.id.frame_date_picker);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNowView.setVisibility(View.GONE);

        handleCustomizationArguments();
        /*Setting Calender Adapter*/
        setupViewPager();

       /*CalUtil is called*/
        CalUtil mCal = new CalUtil();
        mCal.calculate(getActivity());//date calculation called

        mSelectedDate = mCal.getSelectedDate();//sets selected from CalUtil
        mStartDate = mCal.getStartDate();//sets start date from CalUtil

        //Setting the selected date listener
        mCalenderListener.onSelectDate(mStartDate);

        if (mDisplayDatePicker) {
            // Setting the month name
            mMonthView.setText(mSelectedDate.monthOfYear().getAsShortText() + " " + mSelectedDate.year().getAsShortText().toUpperCase());

            /**
             * Change view to  the date of the current week
             */
            mNowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCalenderListener.onSelectDate(mStartDate);
                    mViewPager.setCurrentItem(0);
                }
            });

            /**
             * For quick selection of a date.Any picker or custom date picker can de used
             */
            mMonthView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCalenderListener.onSelectPicker();
                }
            });
        }

    }

    private void setupViewPager() {
        mAdapter = new CalenderAdapter(getActivity().getSupportFragmentManager());
        if (getArguments().containsKey(PACKAGE_NAME)) {
            PACKAGE_NAME_VALUE = getArguments().getString(PACKAGE_NAME);//its for showing the resource value from the parent package
        }
        mViewPager.setAdapter(mAdapter);
        /*Week change Listener*/
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int weekNumber) {
                int addDays = weekNumber * 7;
                mSelectedDate = mStartDate.plusDays(addDays); //add 7 days to the selected date
                if (mDisplayDatePicker) {
                    mMonthView.setText(mSelectedDate.monthOfYear().getAsShortText() + "-" + mSelectedDate.year().getAsShortText().toUpperCase());
                    if (weekNumber == 0) {
                        //the first week comes to view
                        mNowView.setVisibility(View.GONE);
                    } else {
                        //the first week goes from view mNowView set visible for Quick return to first week
                        mNowView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void handleCustomizationArguments() {
        /**
         * Checking for any customization values
         */
        if (getArguments().containsKey(ARGUMENT_CALENDER_BACKGROUND_COLOR)) {
            mBackground.setBackgroundColor(getArguments().getInt(ARGUMENT_CALENDER_BACKGROUND_COLOR));
        }
        if (getArguments().containsKey(ARGUMENT_DATE_SELECTOR_BACKGROUND)) {
            mSelectorDateIndicatorValue = getArguments().getString(ARGUMENT_DATE_SELECTOR_BACKGROUND);
        }
        if (getArguments().containsKey(ARGUMENT_CURRENT_DATE_BACKGROUND)) {
            mCurrentDateIndicatorValue = getArguments().getInt(ARGUMENT_CURRENT_DATE_BACKGROUND);
        }
        if (getArguments().containsKey(ARGUMENT_WEEK_COUNT)) {
            if (getArguments().getInt(ARGUMENT_WEEK_COUNT) > 0)
                mWeekCount = getArguments().getInt(ARGUMENT_WEEK_COUNT);
        }
        if (getArguments().containsKey(ARGUMENT_PRIMARY_TEXT_COLOR)) {
            mMonthView.setTextColor(getArguments().getInt(ARGUMENT_PRIMARY_TEXT_COLOR));
            mPrimaryTextColor = getArguments().getInt(ARGUMENT_PRIMARY_TEXT_COLOR);
        }
        if (getArguments().containsKey(ARGUMENT_SECONDARY_TEXT_COLOR)) {
            mNowView.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mSundayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mMondayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mTuesdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mWednesdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mThursdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mFridayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
            mSaturdayTv.setTextColor(getArguments().getInt(ARGUMENT_SECONDARY_TEXT_COLOR));
        }
        if (getArguments().containsKey(ARGUMENT_DISPLAY_DATE_PICKER)) {
            mDisplayDatePicker = getArguments().getBoolean(ARGUMENT_DISPLAY_DATE_PICKER);
            if (!mDisplayDatePicker) {
                mFrameDatePicker.setVisibility(View.GONE);
            }
        }
        if (getArguments().containsKey(ARGUMENT_NOW_BACKGROUND) && mDisplayDatePicker) {
            Resources resources = getResources();
            mNowView.setBackgroundResource(resources.getIdentifier(
                    getArguments().getString(RWeekCalendar.ARGUMENT_NOW_BACKGROUND)
                    , "drawable"
                    , PACKAGE_NAME_VALUE));
        }
    }

    /**
     * Set set date of the selected week
     */
    public void setDateWeek(Calendar calendar) {
        LocalDateTime ldt = LocalDateTime.fromCalendarFields(calendar);
        AppController.getInstance().setSelected(ldt);
        int nextPage = Weeks.weeksBetween(mStartDate, ldt).getWeeks();
        if (nextPage >= 0 && nextPage < mWeekCount) {
            mViewPager.setCurrentItem(nextPage);
            mCalenderListener.onSelectDate(ldt);
            WeekFragment fragment = (WeekFragment) mViewPager.getAdapter().instantiateItem(mViewPager, nextPage);
            fragment.ChangeSelector(ldt);
        }
    }

    /**
     * Notify the selected date main page
     */
    public void getSelectedDate(LocalDateTime mSelectedDate) {
        mCalenderListener.onSelectDate(mSelectedDate);
    }

    /**
     * Set setCalenderListener when user click on a date
     */
    public void setCalenderListener(CalenderListener calenderListener) {
        this.mCalenderListener = calenderListener;
    }

    /**
     * creating instance of the calender class
     */
    public static synchronized RWeekCalendar getsWeekCalendarInstance() {
        return sWeekCalendarInstance;
    }

    /**
     * Adaptor which shows weeks in the view
     */
    private class CalenderAdapter extends FragmentStatePagerAdapter {
        public CalenderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public WeekFragment getItem(int pos) {
            return WeekFragment.newInstance(pos, mSelectorDateIndicatorValue, mCurrentDateIndicatorValue, mPrimaryTextColor);
        }

        @Override
        public int getCount() {
            return mWeekCount;
        }
    }

/*
    public void SetPrimaryTypeFace(Typeface mFont) {
//        mMonthView.setTypeface(null, Typeface.NORMAL);
    }

    public void SetSecondaryTypeFace(Typeface mFont) {
//        mNowView.setTypeface(mFont);
//        mSundayTv.setTypeface(mFont);
//        mMondayTv.setTypeface(mFont);
//        mTuesdayTv.setTypeface(mFont);
//        mWednesdayTv.setTypeface(mFont);
//        mThursdayTv.setTypeface(mFont);
//        mFridayTv.setTypeface(mFont);
//        mSaturdayTv.setTypeface(mFont);

    }*/
}
