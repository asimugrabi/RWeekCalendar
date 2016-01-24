package com.ramzcalender.sample;

import com.android.datetimepicker.date.DatePickerDialog;
import com.ramzcalender.RWeekCalendar;
import com.ramzcalender.listener.CalenderListener;
import com.ramzcalender.utils.WeekCalendarOptions;

import org.joda.time.LocalDateTime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by rameshvoltella on 11/10/15.
 * Edit: Asi Mugrabi
 */

//## Licence of Date picker using in this sample

/**
 * Copyright 2014 Paul Stöhr
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Sample extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RWeekCalendar rCalendarFragment;
    TextView mDateSelectedTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        mDateSelectedTv = (TextView) findViewById(R.id.txt_date);
        rCalendarFragment = (RWeekCalendar) getSupportFragmentManager().findFragmentByTag(RWeekCalendar.class.getSimpleName());

        if (rCalendarFragment == null) {
            rCalendarFragment = new RWeekCalendar();

            Bundle args = new Bundle();

            /*Should add this attribute if you adding  the ARGUMENT_NOW_BACKGROUND or ARGUMENT_SELECTED_DATE_BACKGROUND Attribute*/
            args.putString(RWeekCalendar.ARGUMENT_PACKAGE_NAME, getApplicationContext().getPackageName());

            /* IMPORTANT: Customization for the calender commenting or un commenting any of the attribute below will reflect change in calendar*/

//---------------------------------------------------------------------------------------------------------------------//

            args.putInt(RWeekCalendar.ARGUMENT_CALENDER_BACKGROUND_COLOR, ContextCompat.getColor(this, R.color.md_deep_purple_500));//set background color to calender

        args.putString(RWeekCalendar.ARGUMENT_SELECTED_DATE_BACKGROUND, "bg_select");//set background to the selected dates - null to disable

//            args.putString(RWeekCalendar.ARGUMENT_SELECTED_DATE_BACKGROUND, null);//set background to the selected dates

//            args.putInt(RWeekCalendar.ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR,ContextCompat.getColor(this, R.color.md_pink_200)); // set text color for the selected date

            args.putInt(RWeekCalendar.ARGUMENT_WEEK_COUNT, 1000);//add N weeks from the current week (53 or 52 week is one year)

//        args.putString(RWeekCalendar.ARGUMENT_NOW_BACKGROUND,"bg_now");//set background to nowView
//
//        args.putInt(RWeekCalendar.ARGUMENT_CURRENT_DATE_TEXT_COLOR, ContextCompat.getColor(this, R.color.md_black_1000));//set color to the currentdate

//        args.putInt(RWeekCalendar.ARGUMENT_PRIMARY_TEXT_COLOR, ContextCompat.getColor(this,R.color.md_white_1000));//Set color to the primary views (Month name and dates)

//        args.putInt(RWeekCalendar.ARGUMENT_DAY_TEXT_SIZE, 18); // set size of primary text views (Month name and dates)

//        args.putInt(RWeekCalendar.ARGUMENT_DAY_TEXT_STYLE, Typeface.BOLD); // set typeface style of primary text views (Month name and dates)

//        args.putInt(RWeekCalendar.ARGUMENT_SECONDARY_TEXT_COLOR, ContextCompat.getColor(this,R.color.md_green_500));//Set color to the secondary views (now view and week names)

//        args.putInt(RWeekCalendar.ARGUMENT_SECONDARY_TEXT_SIZE, 18); // set typeface size of secondary text views (now view and week names)

//        args.putInt(RWeekCalendar.ARGUMENT_SECONDARY_TEXT_STYLE, Typeface.ITALIC); // set typeface style of secondary text views (now view and week names)

            args.putString(RWeekCalendar.ARGUMENT_DAY_HEADER_LENGTH, WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS); // pick between three or one date header letters ex. "Sun" or "S"
            // two options - 1. WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS,  2. WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER

            args.putBoolean(RWeekCalendar.ARGUMENT_DISPLAY_DATE_PICKER, false);

            ArrayList<Calendar> eventDays = new ArrayList<>();
            eventDays.add(Calendar.getInstance());
            eventDays.add(Calendar.getInstance());
            eventDays.add(Calendar.getInstance());
            eventDays.get(1).add(Calendar.DAY_OF_MONTH, 1);
            eventDays.get(2).add(Calendar.WEEK_OF_MONTH, 1);
            args.putSerializable(RWeekCalendar.ARGUMENT_EVENT_DAYS, eventDays); // add days that have events

            args.putString(RWeekCalendar.ARGUMENT_EVENT_COLOR, WeekCalendarOptions.EVENT_COLOR_YELLOW); // color of event dots
            // 5 options - 1. WeekCalendarOptions.EVENT_COLOR_YELLOW, 2. WeekCalendarOptions.EVENT_COLOR_BLUE, 3. WeekCalendarOptions.EVENT_COLOR_GREEN
            // , 4. WeekCalendarOptions.EVENT_COLOR_RED, 5. WeekCalendarOptions.EVENT_COLOR_WHITE

//---------------------------------------------------------------------------------------------------------------------//

            rCalendarFragment.setArguments(args);

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.container, rCalendarFragment, RWeekCalendar.class.getSimpleName());
            t.commit();
        }

        CalenderListener listener = new CalenderListener() {
            @Override
            public void onSelectPicker() {
                //User can use any type of pickers here the below picker is only Just a example
                DatePickerDialog.newInstance(Sample.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }

            @Override
            public void onSelectDate(LocalDateTime mSelectedDate) {
                //callback when a date is selcted
                mDateSelectedTv.setText("" + mSelectedDate.getDayOfMonth() + "-" + mSelectedDate.getMonthOfYear() + "-" + mSelectedDate.getYear());
            }
        };

        //setting the listener
        rCalendarFragment.setCalenderListener(listener);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

        //This is the call back from picker used in the sample you can use custom or any other picker

        //IMPORTANT: get the year,month and date from picker you using and call setDateWeek method
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        rCalendarFragment.setDateWeek(calendar);//Sets the selected date from Picker
    }
}
