package map;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;

/**
 * Created by Administrator on 2017/4/23.
 */

public class TraceService {

    //开启服务返回成功
    public static int SUCCESS = 0;

    // 轨迹服务ID
    private static long serviceId = 138104;
    // LBSClient客户端
    private static LBSTraceClient mLBSTraceClient = null;
    //mTrace
    private static Trace mTrace = null;
    //标记是否开启轨迹服务
    private static boolean traceFlag = false;

    public TraceService() {}

    public static void start(Context context, String entityName) {
        //实例化轨迹服务客户端
        if (mLBSTraceClient == null) {
            mLBSTraceClient = new LBSTraceClient(context);
        }
        //轨迹服务类型（0 : 不上传位置数据，也不接收报警信息； 1 : 不上传位置数据，但接收报警信息；2 : 上传位置数据，且接收报警信息）
        final int  traceType = 2;
        //实例化轨迹服务
        mTrace = new Trace(context, serviceId, entityName, traceType);

        //实例化开启轨迹服务回调接口
        OnStartTraceListener startTraceListener = new OnStartTraceListener() {
            //开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                if (arg0 == SUCCESS) {
                    traceFlag = true;
                }
            }
            //轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte arg0, String arg1) {
            }
        };
        //开启轨迹服务
        mLBSTraceClient.startTrace(mTrace, startTraceListener);
    }

    public static boolean isStart() {
        return traceFlag;
    }

    public static void stop() {
        OnStopTraceListener stopTraceListener = new OnStopTraceListener(){
            // 轨迹服务停止成功
            @Override
            public void onStopTraceSuccess() {
                traceFlag = false;
            }
            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onStopTraceFailed(int arg0, String arg1) {
            }
        };

        //停止轨迹服务
        mLBSTraceClient.stopTrace(mTrace, stopTraceListener);
        mLBSTraceClient = null;
    }

}
