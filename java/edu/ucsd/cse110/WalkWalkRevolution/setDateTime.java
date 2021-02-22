package edu.ucsd.cse110.WalkWalkRevolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.WalkWalkRevolution.service.FirestoreAdapter;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationService;
import edu.ucsd.cse110.WalkWalkRevolution.service.NotificationServiceFactory;


public class setDateTime extends UpdateActivity {

    static int hour, min;

    TextView txtdate, txttime;
    Button btntimepicker, btndatepicker;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day;
    private String fitnessServiceKey;
    private NotificationService notificationService;

    private String routeName;
    private String routeStartPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_propose);

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);

        btndatepicker = (Button) findViewById(R.id.btndatepicker);
        btntimepicker = (Button) findViewById(R.id.btntimepicker);



        fitnessServiceKey = MockManager.getInstance().getKey();

        NotificationServiceFactory.put(fitnessServiceKey, new NotificationServiceFactory.BluePrint() {
            @Override
            public NotificationService create(UpdateActivity activity) {
                return new FirestoreAdapter(activity);
            }
        });
        notificationService = NotificationServiceFactory.create(fitnessServiceKey, this);
        notificationService.setup();

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                DatePickerDialog dd = new DatePickerDialog(setDateTime.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                                    String dateInString = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                                    Date date = formatter.parse(dateInString);
                                    txtdate.setText(formatter.format(date).toString());
                                } catch (Exception ex) {

                                }
                            }
                        }, year, month, day);
                dd.show();
            }
        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog td = new TimePickerDialog(setDateTime.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");
                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(setDateTime.this)
                );
                td.show();
            }
        });
        Button back_Route_List = (Button) findViewById(R.id.propose_cancel_btn);

        back_Route_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_route();
            }
        });
        Button go_to_invite = (Button) findViewById(R.id.propose_send_btn);

        go_to_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_to_propose();
            }
        });
    }

    public void back_route(){
        Intent intent = new Intent(this,Route_List_page.class);

        startActivity(intent);
    }
    public void go_to_propose(){

        Intent intent = new Intent(this, inviterProposed.class);
        ArrayList<String> time = new ArrayList<>();
        time.add(Integer.toString(year));
        time.add(Integer.toString(month));
        time.add(Integer.toString(day));
        time.add(Integer.toString(hour));
        time.add(Integer.toString(min));
        intent.putExtra("time", time);

        notificationService.createProposedWalk();


        startActivity(intent);
    }
}




