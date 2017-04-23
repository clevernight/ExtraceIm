package map;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import extrace.misc.model.TraceInfo;
import extrace.ui.main.R;

import static android.R.attr.button;

/**
 * Created by Administrator on 2017/4/20.
 */

public class TraceActivity extends AppCompatActivity {

    private long serviceId = 138104;        //服务ID
    private String entityName ;             //实体名
    private int startTime;                 //追踪开始时间
    private int endTime;                   //追踪结束时间
    int simpleReturn = 1;                   // 是否返回精简结果
    int isProcessed = 0;                    // 是否纠偏
    String processOption = null;            // 纠偏选项
    int pageSize = 5000;                    // 分页大小
    int pageIndex ;                      // 分页索引


    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    private LBSTraceClient mLBSTraceClient = null;
    private PolylineOptions polylineOptions = null;  //路线覆盖物
    private List<TraceInfo> traceInfos = null;

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_edit);

        mMapView = (MapView)findViewById(R.id.trace_map);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.clear();

        mLBSTraceClient = new LBSTraceClient(getApplicationContext());
        traceInfos = (ArrayList<TraceInfo>)getIntent().getSerializableExtra("traceInfos");

        getTrace();
    }

    //在OnTrackListener的onQueryHistoryTrackCallback()回调接口中，判断是否已查询完毕。
    OnTrackListener trackListener = new OnTrackListener() {
        public void onQueryHistoryTrackCallback(String message) {
            List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
            try {
                JSONObject dataJson = new JSONObject(message);
                int total = dataJson.getInt("total");
                if (total > (pageSize * pageIndex)) {
                    queryHistoryTrack(++pageIndex);
                }
                // 解析并保存轨迹信息
                JSONArray points = dataJson.getJSONArray("points");
                for (int i = 0; i < points.length(); i++) {
                    JSONArray point = points.getJSONArray(i);
                    LatLng latLng = new LatLng(point.getDouble(1), point.getDouble(0));
                    pointList.add(latLng);
                }
            } catch (JSONException e) {
                Log.i("JSONExcepiton", e.getMessage());
            }
            if (pointList.size() != 0) {
                drawTrace(pointList);
            } else {
                Toast.makeText(getApplicationContext(), "没有记录", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestFailedCallback(String s) {

        }
    };

    //  获得轨迹
    private void getTrace() {

        for (TraceInfo traceInfo : traceInfos) {
            startTime = changeDateToInt(traceInfo.getStartTime());
            endTime = changeDateToInt(traceInfo.getEndTime());
            entityName = String.valueOf(traceInfo.getUID());


            // 查询历史轨迹
            pageIndex = 1;
            queryHistoryTrack(pageIndex);
        }


    }

    private void queryHistoryTrack(int pageIndex) {
        mLBSTraceClient.queryHistoryTrack(serviceId , entityName, simpleReturn, isProcessed,
                processOption, startTime, endTime, pageSize, pageIndex, trackListener);
    }

    //绘制路线
    private void drawTrace(List<LatLng> pointList) {
        Log.i("pointList", pointList.toString());
        MapStatus mMapStatus = new MapStatus.Builder().target(pointList.get(0)).zoom(15).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        polylineOptions = new PolylineOptions().points(pointList).color(0xAA00FF00).width(5);
        //在地图上添加polylineOption，用于显示
        mBaiduMap.addOverlay(polylineOptions);
    }

    //由于date转成时间戳后会将纳秒带出，共13位，因此需要去除后三位
    private int changeDateToInt(Date date) {
        return Integer.parseInt(String.valueOf(date.getTime()).substring(0, 10));
    }
}




/***************************************************************
 *
 *
 *
 *       由于鹰眼v3.0版本鉴权问题无法解决，分2.0(上)和3.0代码(下)
 *
 *
 ********************************************************************/






//package map;
//
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.v7.app.AppCompatActivity;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.PolylineOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.trace.LBSTraceClient;
//import com.baidu.trace.Trace;
//import com.baidu.trace.api.track.HistoryTrackRequest;
//import com.baidu.trace.api.track.HistoryTrackResponse;
//import com.baidu.trace.api.track.OnTrackListener;
//import com.baidu.trace.api.track.TrackPoint;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import extrace.misc.model.TraceInfo;
//import extrace.ui.main.R;
//
//import static android.R.attr.button;
//
///**
// * Created by Administrator on 2017/4/20.
// */
//
//public class TraceActivity extends AppCompatActivity {
//
//    private long serviceId = 138104;        //服务ID
//    private String entityName ;             //实体名
//    private long startTime;                 //追踪开始时间
//    private long endTime;                   //追踪结束时间
//
//
//    private MapView mMapView = null;
//    private BaiduMap mBaiduMap = null;
//
//
//    private LBSTraceClient mLBSTraceClient = null;
//    private List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
//    private PolylineOptions polylineOptions = null;  //路线覆盖物
//    private List<TraceInfo> traceInfos = null;
//    private HistoryTrackRequest historyTrackRequest ;        //追踪请求
//
//    @Override
//    public void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        setContentView(R.layout.activity_trace_edit);
//
//        mMapView = (MapView)findViewById(R.id.trace_map);
//        mBaiduMap = mMapView.getMap();
//
//        mLBSTraceClient = new LBSTraceClient(getApplicationContext());
//        traceInfos = (ArrayList<TraceInfo>)getIntent().getSerializableExtra("traceInfos");
//        pointList = new ArrayList<LatLng>();
//
//        getTrace();
//    }
//
//    //  获得轨迹
//    private void getTrace() {
//
//        // 初始化轨迹监听器
//        OnTrackListener mTrackListener = new OnTrackListener() {
//            // 历史轨迹回调
//            @Override
//            public void onHistoryTrackCallback(HistoryTrackResponse response) {
//                Log.i("response", response.toString());
//                // 解析并保存轨迹信息
//                if (response != null && response.getSize() != 0) {
//                    List<TrackPoint> trackPoints = response.getTrackPoints();
//                    for (TrackPoint trackPoint : trackPoints) {
//                        LatLng latLng = new LatLng(trackPoint.getLocation().getLatitude(), trackPoint.getLocation().getLongitude());
//                        pointList.add(latLng);
//                    }
//                }
//            }
//        };
//
//        // 请求标识
//        int tag = 1;
//        historyTrackRequest = new HistoryTrackRequest(tag, serviceId, "863673033186206");
//        //设置轨迹查询起止时间
//        startTime = 1492560000;
//        endTime = 1492596000;
//        // 设置开始时间
//        historyTrackRequest.setStartTime(startTime);
//        // 设置结束时间
//        historyTrackRequest.setEndTime(endTime);
//        mLBSTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
//
////        pointList.clear();
////        for (TraceInfo traceInfo : traceInfos) {
////            startTime = changeDateToLong(traceInfo.getStartTime());
////            endTime = changeDateToLong(traceInfo.getEndTime());
////            entityName = String.valueOf(traceInfo.getUID());
////
////            historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);
////            // 设置开始时间
////            historyTrackRequest.setStartTime(startTime);
////            // 设置结束时间
////            historyTrackRequest.setEndTime(endTime);
////
////            Log.i("historyTrackRequest", historyTrackRequest.toString());
////            // 查询历史轨迹
////            mLBSTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
////        }
//
//        if (pointList.size() != 0) {
//            drawTrace();
//        } else {
//            Toast.makeText(getApplicationContext(), "没有记录", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    //绘制路线
//    private void drawTrace() {
//        mBaiduMap.clear();
//        MapStatus mMapStatus = new MapStatus.Builder().target(pointList.get(0)).zoom(15).build();
//        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        mBaiduMap.animateMapStatus(mapStatusUpdate);
//        polylineOptions = new PolylineOptions().points(pointList).color(0xAA00FF00).width(5);
//        //在地图上添加polylineOption，用于显示
//        mBaiduMap.addOverlay(polylineOptions);
//    }
//
//    //由于date转成时间戳后会将纳秒带出，共13位，因此需要去除后三位
//    private long changeDateToLong(Date date) {
//        return Long.parseLong(String.valueOf(date.getTime()).substring(0, 10));
//    }
//}
