package com.example.iotproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Sound extends AppCompatActivity  {

    LineChart soundChart;
TextView val;
    ArrayList<Entry> valuesTemp = new ArrayList<>();

    long tStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        soundChart =(LineChart)findViewById(R.id.tempChart);
        val=findViewById(R.id.val);
        getData();
        setTitle("Sound");

        tStart = System.currentTimeMillis();
        initLine();

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
                        String url="http://192.168.43.59:3000/getSound";




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



                                            setDataLine(body);
                                            System.out.println(body);
                                            soundChart.invalidate();
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

    public void initLine()
    {
        {   // // Chart Style // //

            // background color
            soundChart.setBackgroundColor(Color.WHITE);

            // disable description text
            soundChart.getDescription().setEnabled(false);

            // enable touch gestures
            soundChart.setTouchEnabled(true);

            // set listeners
           // soundChart.setOnChartValueSelectedListener(this);
            soundChart.setDrawGridBackground(false);

            // create marker to display box when values are selected

            // Set the marker to the chart

            // enable scaling and dragging
            soundChart.setDragEnabled(true);
            soundChart.setScaleEnabled(true);
            //  soundChart.setScaleXEnabled(true);
            // soundChart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            soundChart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = soundChart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = soundChart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            soundChart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(100f);
            yAxis.setAxisMinimum(0);
        }


        // draw points over time
        soundChart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = soundChart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setDataLine(String value) {



        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;

        float val=Float.parseFloat(value);

        valuesTemp.add(new Entry((int)elapsedSeconds, val));

        LineDataSet set1;

        if (soundChart.getData() != null &&
                soundChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) soundChart.getData().getDataSetByIndex(0);
            set1.setValues(valuesTemp);
            set1.notifyDataSetChanged();
            soundChart.getData().notifyDataChanged();
            soundChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(valuesTemp, "Sonore");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.YELLOW);
            set1.setCircleColor(Color.BLUE);


            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return soundChart.getAxisLeft().getAxisMinimum();
                }
            });



            set1.setFillColor(Color.RED);


            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            soundChart.setData(data);
        }
    }
}