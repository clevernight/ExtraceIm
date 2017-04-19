package extrace.misc.model;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/18.
 */

public class TraceInfo {
    private int UID;
    private Date startTime;
    private Date endTime;
    private String nodeName;

    public TraceInfo(){}

    public TraceInfo(int UID, Date startTime, Date endTime, String nodeName) {
        this.UID = UID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nodeName = nodeName;
    }

    public int getUID() {
        return UID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return "TraceInfo{" +
                "UID=" + UID +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
