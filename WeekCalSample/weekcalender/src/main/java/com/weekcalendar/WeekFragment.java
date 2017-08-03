package com.weekcalendar;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weekcalendar.utils.AppController;
import com.weekcalendar.utils.CalUtil;
import com.weekcalendar.utils.WeekCalendarOptions;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Ramesh M Nair
 * Edit: Asi Mugrabi
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE..
 */
public class WeekFragment extends Fragment {
    public static final String POSITION_KEY = "pos";

    private LocalDateTime mSelectedDate, mStartDate, mCurrentDate;
    private LocalDateTime mDirtySelector;

    private TextView mSundayTv, mMondayTv, mTuesdayTv, mWednesdayTv, mThursdayTv, mFridayTv;
    private TextView mSaturdayTv;
    private TextView[] mTextViewArray;
    private ImageView[] mImageViewArray;

    private int mDatePosition = 0, mSelectorDateIndicatorValue = 0, mCurrentDateIndicatorValue = 0;
    private int mCurrentDateIndex = -1;
    private int mPrimaryTextColor, mSelectorHighlightColor = -1;

    private ArrayList<LocalDateTime> mDateInWeekArray = new ArrayList<>();

    /**
     * Set Values including customizable info
     */
    public static WeekFragment newInstance(int position, String selectorDateIndicatorValue
            , int currentDateIndicatorValue, int primaryTextColor, int primaryTextSize
            , int primaryTextStyle, int selectorHighlightColor, long[] eventDays
            , String eventColor) {
        WeekFragment f = new WeekFragment();
        Bundle b = new Bundle();
        b.putInt(POSITION_KEY, position);
        b.putString(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_BACKGROUND, selectorDateIndicatorValue);
        b.putInt(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR, selectorHighlightColor);
        b.putInt(WeekCalendarFragment.ARGUMENT_CURRENT_DATE_TEXT_COLOR, currentDateIndicatorValue);
        b.putInt(WeekCalendarFragment.ARGUMENT_PRIMARY_TEXT_COLOR, primaryTextColor);
        b.putInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_SIZE, primaryTextSize);
        b.putInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_STYLE, primaryTextStyle);
        b.putSerializable(WeekCalendarFragment.ARGUMENT_EVENT_DAYS, eventDays);
        b.putString(WeekCalendarFragment.ARGUMENT_EVENT_COLOR, eventColor);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekcell, container, false);

        mSundayTv = (TextView) view.findViewById(R.id.sun_txt);
        mMondayTv = (TextView) view.findViewById(R.id.mon_txt);
        mTuesdayTv = (TextView) view.findViewById(R.id.tue_txt);
        mWednesdayTv = (TextView) view.findViewById(R.id.wen_txt);
        mThursdayTv = (TextView) view.findViewById(R.id.thu_txt);
        mFridayTv = (TextView) view.findViewById(R.id.fri_txt);
        mSaturdayTv = (TextView) view.findViewById(R.id.sat_txt);

        ImageView sundayEvent = (ImageView) view.findViewById(R.id.img_sun_txt);
        ImageView mondayEvent = (ImageView) view.findViewById(R.id.img_mon_txt);
        ImageView tuesdayEvent = (ImageView) view.findViewById(R.id.img_tue_txt);
        ImageView wednesdayEvent = (ImageView) view.findViewById(R.id.img_wen_txt);
        ImageView thursdayEvent = (ImageView) view.findViewById(R.id.img_thu_txt);
        ImageView fridayEvent = (ImageView) view.findViewById(R.id.img_fri_txt);
        ImageView saturdayEvent = (ImageView) view.findViewById(R.id.img_sat_txt);

        /*Adding WeekViews to array for background changing purpose*/
        mTextViewArray = new TextView[]{mSundayTv, mMondayTv, mTuesdayTv, mWednesdayTv
                , mThursdayTv, mFridayTv, mSaturdayTv};
        mImageViewArray = new ImageView[]{sundayEvent, mondayEvent, tuesdayEvent, wednesdayEvent
                , thursdayEvent, fridayEvent, saturdayEvent};

        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*Setting the date info in the Application class*/
        mStartDate = AppController.getInstance().getDate();
        mCurrentDate = AppController.getInstance().getDate();
        /*Setting the Resources values and Customization values to the views*/
        String identifierName = getArguments()
                .getString(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_BACKGROUND);
        if (identifierName != null) {
            Resources resources = getActivity().getResources();
            mSelectorDateIndicatorValue = resources.getIdentifier(identifierName, "drawable",
                    WeekCalendarFragment.PACKAGE_NAME_VALUE);
        }

        mCurrentDateIndicatorValue = getArguments()
                .getInt(WeekCalendarFragment.ARGUMENT_CURRENT_DATE_TEXT_COLOR);
        mSelectorHighlightColor = getArguments()
                .getInt(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR);

        mDatePosition = getArguments().getInt(POSITION_KEY);
        int addDays = mDatePosition * 7;

        mStartDate = mStartDate.plusDays(addDays);//Adding the 7days to the previous week

        mSelectedDate = AppController.getInstance().getSelected();

         /*Fetching the data's for the week to display*/
        for (int i = 0; i < 7; i++) {
            if (mSelectedDate != null) {
                if (mSelectedDate.getDayOfMonth() == mStartDate.getDayOfMonth()) {
                   /*Indicate  if the day is selected*/
                    setSelectedDateBackground(mTextViewArray[i]);
                    AppController.getInstance().setSelected(null);//null the selected date
                }
            }
            mDateInWeekArray.add(mStartDate);//Adding the days in the selected week to list
            mStartDate = mStartDate.plusDays(1); //Next day
        }

        int primaryTextStyle = getArguments().getInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_STYLE, -1);
        int primaryTextSize = getArguments().getInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_SIZE, 0);
        if (primaryTextSize > 0 || primaryTextStyle > -1) {
            for (TextView tv : mTextViewArray) {
                if (primaryTextSize > 0) {
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, primaryTextSize);
                }
                if (primaryTextStyle > -1) {
                    tv.setTypeface(tv.getTypeface(), primaryTextStyle);
                }
            }
        }

        /*Setting color in the week views*/
        mPrimaryTextColor = getArguments().getInt(WeekCalendarFragment.ARGUMENT_PRIMARY_TEXT_COLOR);
        for (TextView tv : mTextViewArray) {
            tv.setTextColor(mPrimaryTextColor);
        }

        long[] eventDaysPrim  = (long[]) getArguments()
                .getSerializable(WeekCalendarFragment.ARGUMENT_EVENT_DAYS);
        ArrayList<LocalDateTime> eventDays  = new ArrayList<>();
        if (eventDaysPrim != null) {
            for (long eventDay : eventDaysPrim) {
                eventDays.add(LocalDateTime.fromDateFields(new Date(eventDay)));
            }
        }
        int eventColorDrawable = getEventColorDrawable(getArguments()
                .getString(WeekCalendarFragment.ARGUMENT_EVENT_COLOR));

        /*Displaying the days in the week views*/
        int dayOfWeek = 0;
        for (TextView dayTv : mTextViewArray) {
            dayTv.setText(Integer.toString(mDateInWeekArray.get(dayOfWeek).getDayOfMonth()));
            if (!eventDays.isEmpty()) {
                if (CalUtil.isDayInList(mDateInWeekArray.get(dayOfWeek), eventDays)) {
                    mImageViewArray[dayOfWeek].setImageResource(eventColorDrawable);
                }
            }
            dayOfWeek++;
        }

        /*if the selected week is the current week indicates the current day*/
        if (mDatePosition == 0) {
            for (int i = 0; i < 7; i++) {
                if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        == mDateInWeekArray.get(i).getDayOfMonth()) {
                    mCurrentDateIndex = i;
                    mTextViewArray[i].setTextColor(mCurrentDateIndicatorValue);
                }
            }
        }

        setSelectedDateBackground(mTextViewArray[0]); //Setting the first days of the week as selected

        /**
         * Click listener of all week days with the indicator change and passing listener info.
         */
        mSundayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(0);
                setSelectedDateBackground((TextView) view);
            }
        });
        mMondayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(1);
                setSelectedDateBackground((TextView) view);
            }
        });
        mTuesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(2);
                setSelectedDateBackground((TextView) view);
            }
        });
        mWednesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(3);
                setSelectedDateBackground((TextView) view);
            }
        });
        mThursdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(4);
                setSelectedDateBackground((TextView) view);
            }
        });
        mFridayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(5);
                setSelectedDateBackground((TextView) view);
            }
        });
        mSaturdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedDateInfo(6);
                setSelectedDateBackground((TextView) view);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /**
         * Reset date to first day of week when week goes from the view
         */

        if (isVisibleToUser) {
            if (mDateInWeekArray.size() > 0) {
                // display first day of week if there is no selected date
                if (AppController.getInstance().getSelected() == null) {
                    WeekCalendarFragment.getsWeekCalendarInstance()
                            .getSelectedDate(mDateInWeekArray.get(0));
                }
            }
        }
        if (mSelectedDate != null) {
            setSelectedDateBackground(mTextViewArray[0]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDirtySelector != null) {
            ChangeSelector(mDirtySelector);
            mDirtySelector = null;
        }
    }

    /**
     * Passing the selected date info
     */
    public void mSelectedDateInfo(int position) {
        WeekCalendarFragment.getsWeekCalendarInstance().getSelectedDate(mDateInWeekArray.get(position));
        mSelectedDate = mDateInWeekArray.get(position);
        AppController.getInstance().setSelected(mSelectedDate);
    }

    /**
     * Setting date when selected from picker
     */
    public void ChangeSelector(LocalDateTime mSelectedDate) {
        if (mTextViewArray == null) {
            mDirtySelector = mSelectedDate;
            return;
        }
        LocalDateTime startDate = AppController.getInstance().getDate();
        int addDays = mDatePosition * 7;
        startDate = startDate.plusDays(addDays);
        for (int i = 0; i < 7; i++) {
            if (mSelectedDate.getDayOfMonth() == startDate.getDayOfMonth()) {
                setSelectedDateBackground(mTextViewArray[i]);
            }
            startDate = startDate.plusDays(1);
        }
    }

    private void setSelectedDateBackground(TextView selectedDateTv) {
        if (mSelectorDateIndicatorValue != 0) {
            for (TextView tv : mTextViewArray) {
                tv.setBackgroundColor(Color.TRANSPARENT);
            }
            selectedDateTv.setBackgroundResource(mSelectorDateIndicatorValue);
        }

        if (mSelectorHighlightColor != -1) {
            for (TextView tv : mTextViewArray) {
                tv.setTextColor(mPrimaryTextColor);
            }
            if (mCurrentDateIndex > -1) {
                mTextViewArray[mCurrentDateIndex].setTextColor(mCurrentDateIndicatorValue);
            }
            selectedDateTv.setTextColor(mSelectorHighlightColor);
        }
    }

    private int getEventColorDrawable(String eventColor) {
        if (eventColor.equals(WeekCalendarOptions.EVENT_COLOR_BLUE)) {
            return R.drawable.blue_circle;
        }
        if (eventColor.equals(WeekCalendarOptions.EVENT_COLOR_GREEN)) {
            return R.drawable.green_circle;
        }
        if (eventColor.equals(WeekCalendarOptions.EVENT_COLOR_RED)) {
            return R.drawable.red_circle;
        }
        if (eventColor.equals(WeekCalendarOptions.EVENT_COLOR_YELLOW)) {
            return R.drawable.yellow_circle;
        } else {
            return R.drawable.white_circle;
        }
    }
}
