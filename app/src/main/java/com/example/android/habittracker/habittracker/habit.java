package com.example.android.habittracker.habittracker;


import android.provider.BaseColumns;

public class habit {
    private habit() {
    }

    public static final class habitEntry implements BaseColumns {

        public final static String TABLE_NAME = "habits";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_HABIT_NAME = "name";

        public final static String COLUMN_HABIT_DATE = "date";

        public final static String COLUMN_HABIT_TIME = "time";

        public final static String COLUMN_HABIT_REPETITION = "repetition";

        public final static String COLUMN_HABIT_REMAINDER = "remainder";

        public static final int REPETITION_DAILY = 0;
        public static final int REPETITION_WEEKLY = 1;
        public static final int REPETITION_MONTHLY = 2;
        public static final int REPETITION_YEARLY = 3;

        public static final int REMAINDER_NO = 0;
        public static final int REMAINDER_5MIN = 1;
        public static final int REMAINDER_10MIN = 2;
        public static final int REMAINDER_30MIN = 3;
        public static final int REMAINDER_1HR = 4;

    }
}
