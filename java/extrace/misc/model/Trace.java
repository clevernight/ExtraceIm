package extrace.misc.model;

/**
 * Created by Administrator on 2017/4/18.
 */

public class Trace {
    private int UID;
    private long startTime;
    private long endTime;
    private String nodeName;

    Trace(){}

    public int getUID() {
        return UID;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
