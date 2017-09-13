package com.tauruslab.app.popularmovies;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fernando on 13/09/2017.
 */

public class Utility {
    public static Date strToDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        return new Date(format.parse(dateStr).getTime());
    }
}
