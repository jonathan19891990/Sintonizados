package util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by InnovaCaicedo on 6/6/2016.
 */
@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView txtDate;

    public DateDialog(View view)
    {
        txtDate=(TextView)view;
    }

    public Dialog onCreateDialog(Bundle saveInstanceState)
    {
        final Calendar c= Calendar.getInstance();
        int year= c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this, year,month,day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        String date=year+"-"+(month+1)+"-"+day;
        txtDate.setText(date);
    }

}
