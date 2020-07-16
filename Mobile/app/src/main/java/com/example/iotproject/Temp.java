package com.example.iotproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;


public class Temp extends AppCompatActivity {

    ArrayList<BarEntry> values = new ArrayList<>();

    long tStart;
    BarChart sonChart;
    TextView val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        sonChart =findViewById(R.id.chart2);
        val=findViewById(R.id.valtemp);
        setTitle("Tempurature");
        getData();
        initBar();
        tStart = System.currentTimeMillis();

    }

        public void initBar()
        {
            //sonChart.setOnChartValueSelectedListener(this);

            sonChart.setDrawBarShadow(false);
            sonChart.setDrawValueAboveBar(true);
            //sonChart.setOnChartValueSelectedListener(this);

            sonChart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            sonChart.setMaxVisibleValueCount(80);

            // scaling can now only be done on x- and y-axis separately
            sonChart.setPinchZoom(true);

            sonChart.setDrawGridBackground(false);
            // chart.setDrawYLabels(false);
            sonChart.setDragEnabled(true);
            sonChart.setScaleEnabled(true);





            XAxis xAxis = sonChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);


            YAxis leftAxis = sonChart.getAxisLeft();
            leftAxis.setLabelCount(8, false);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = sonChart.getAxisRight();
            rightAxis.setDrawGridLines(false);
            rightAxis.setLabelCount(8, false);
            rightAxis.setSpaceTop(15f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            Legend l = sonChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);



        }
    public void getData()
    {


        final Handler handler =new Handler();
        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        OkHttpClient client = new OkHttpClient();
                        client.setConnectTimeout(10, TimeUnit.SECONDS);
                        client.setWriteTimeout(10,TimeUnit.SECONDS);
                        client.setReadTimeout(30,TimeUnit.SECONDS);

                        String s;
                        String url="http://192.168.43.59:3000/getTemp";




                        final Request request = new Request.Builder()
                                .url(url)
                                .method("GET", null)
                                .addHeader("Content-Type", "application/json")
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.d("Chart", "Network error");
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(final Response response) throws IOException {
                                final String body = response.body().string();
                                try {
                                    //  JSONObject obj = jRes.getJSONObject(i);

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {



                                                setDataChart(body);
                                            System.out.println(body);
                                                sonChart.invalidate();
                                                val.setText(body);
                                            }

                                    });



                                }
                                catch(Exception ex) {
                                    ex.printStackTrace();
                                }



                            }
                        });

                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);




    }
    private void setDataChart(String value) {

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        float val=Float.parseFloat(value);

        values.add(new BarEntry((int)elapsedSeconds, val));



        BarDataSet set1;

        if (sonChart.getData() != null &&
                sonChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) sonChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            sonChart.getData().notifyDataChanged();
            sonChart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Niveau Tempurature");

            set1.setDrawIcons(false);


            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);


            set1.setBarBorderColor(endColor4);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(0);
            //data.setValueTypeface(tfLight);
            data.setBarWidth(0.9f);

            set1.setColor(endColor4);

            sonChart.setData(data);
        }
    }


    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sonChart.invalidate();


    }




}