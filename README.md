Android Week Calendar With Events
=================
A fast richly customizable week calendar fragment library for Android</br>
Forked from [rameshvoltella/RWeekCalendar](https://github.com/rameshvoltella/RWeekCalendar)

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s3.png" width="400">

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s4.png" width="400">

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s5.png" width="400">

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s6.png" width="400">

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s1.png" width="400">

<img src="https://github.com/asimugrabi/Week-Calendar-Fragment/blob/master/Screens/s2.png" width="400">

Download
-------
In your module

    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        compile 'com.github.asimugrabi:Week-Calendar-Fragment:2.0.2'
    }


Usage:
------------------------

1. Add WeekCalendarFragment as any fragment using
  
   ```xml
       <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:id="@+id/container"/>
```

2. Customize using the following arguments
  
    ```java
           Bundle args = new Bundle();

            /* Must add this attribute if using ARGUMENT_NOW_BACKGROUND or ARGUMENT_SELECTED_DATE_BACKGROUND*/
            args.putString(WeekCalendarFragment.ARGUMENT_PACKAGE_NAME
                    , getApplicationContext().getPackageName());

            //sets background drawable to the selected dates - null to disable
            args.putString(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_BACKGROUND, "bg_select");
            //  args.putString(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_BACKGROUND, null); // disable

            //Sets background color to calender
            args.putInt(WeekCalendarFragment.ARGUMENT_CALENDER_BACKGROUND_COLOR
                    , ContextCompat.getColor(this, R.color.md_deep_purple_500));

            // Sets text color for the selected date
            args.putInt(WeekCalendarFragment.ARGUMENT_SELECTED_DATE_HIGHLIGHT_COLOR 
            			, ContextCompat.getColor(this, R.color.md_pink_200));

            // Adds N weeks from the current week (53 or 52 week is one year)
            args.putInt(WeekCalendarFragment.ARGUMENT_WEEK_COUNT, 1000);

            // Cancels date picker
            args.putBoolean(WeekCalendarFragment.ARGUMENT_DISPLAY_DATE_PICKER, false);

            // Sets background resource drawable to nowView
            args.putString(WeekCalendarFragment.ARGUMENT_NOW_BACKGROUND,"bg_now");

            // Sets text color to the current date
            args.putInt(WeekCalendarFragment.ARGUMENT_CURRENT_DATE_TEXT_COLOR
            			, ContextCompat.getColor(this, R.color.md_green_500));

            // Sets color to the primary views (Month name and dates)
            args.putInt(WeekCalendarFragment.ARGUMENT_PRIMARY_TEXT_COLOR
            			, ContextCompat.getColor(this,R.color.md_yellow_500));

            // Sets text size of dates
            args.putInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_SIZE, 18);

            // Sets typeface style of date texts
            args.putInt(WeekCalendarFragment.ARGUMENT_DAY_TEXT_STYLE, Typeface.BOLD_ITALIC);

            // Sets color to the secondary views (now view and week names)
            args.putInt(WeekCalendarFragment.ARGUMENT_SECONDARY_TEXT_COLOR
                  		, ContextCompat.getColor(this,R.color.md_green_500));

            // Sets typeface size of secondary text views (now view and week names)
            args.putInt(WeekCalendarFragment.ARGUMENT_SECONDARY_TEXT_SIZE, 18);

            // Sets typeface style of secondary text views (now view and week names)
            args.putInt(WeekCalendarFragment.ARGUMENT_SECONDARY_TEXT_STYLE, Typeface.ITALIC);

            // Picks between three or one date header letters ex. "Sun" or "S"
            // two options:
            // 1. WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS
            // 2. WeekCalendarOptions.DAY_HEADER_LENGTH_ONE_LETTER
            args.putString(WeekCalendarFragment.ARGUMENT_DAY_HEADER_LENGTH
                    		, WeekCalendarOptions.DAY_HEADER_LENGTH_THREE_LETTERS);

            // Days that have events
            ArrayList<Calendar> eventDays = new ArrayList<>();
            eventDays.add(Calendar.getInstance());
            eventDays.add(Calendar.getInstance());
            eventDays.add(Calendar.getInstance());
            eventDays.get(1).add(Calendar.DAY_OF_MONTH, 1);
            eventDays.get(2).add(Calendar.WEEK_OF_MONTH, 1);
            args.putSerializable(WeekCalendarFragment.ARGUMENT_EVENT_DAYS, eventDays);

            // Sets the color of event dots
            // 5 options:
            // 1. WeekCalendarOptions.EVENT_COLOR_YELLOW, 2. WeekCalendarOptions.EVENT_COLOR_BLUE
            // 3. WeekCalendarOptions.EVENT_COLOR_GREEN, 4. WeekCalendarOptions.EVENT_COLOR_RED
            // 5. WeekCalendarOptions.EVENT_COLOR_WHITE
            args.putString(WeekCalendarFragment.ARGUMENT_EVENT_COLOR
                    , WeekCalendarOptions.EVENT_COLOR_YELLOW);

            mWeekCalendarFragment.setArguments(args);
```

3. Week Calendar Listener
  
    ```java

      CalendarListener listener=new CalendarListener() {
            @Override
           public void onSelectPicker() {
				// Show picker. You can use custom picker or any other picker library
            }

            @Override
            public void onSelectDate(LocalDateTime mSelectedDate) {
                // callback when a date is selected
            }
        };

        //setting the listener
        mWeekCalendarFragment.setCalendarListener(listener);
```

4. Setting a date to calendar
  
    ```java
		mWeekCalendarFragment.setDateWeek(Calendar calendar);
```

Example
-------

[Sample Usage](WeekCalSample/app/src/main/java/com/weekcalendar/sample/Sample.java)

## License

    The MIT License (MIT)

    Copyright (c) 2015 Ramesh M Nair, Asi Mugrabi
 
     Permission is hereby granted, free of charge, to any person obtaining a copy
     of this software and associated documentation files (the "Software"), to deal
     in the Software without restriction, including without limitation the rights
     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     copies of the Software, and to permit persons to whom the Software is
     furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
