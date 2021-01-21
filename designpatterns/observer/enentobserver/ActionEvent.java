package designdemo.observer.enentobserver;

/**
 * Author : GuDao
 * 2020-10-20
 */

public class ActionEvent {
    /**
     * 时段
     */
    private String timePeriod;


    /**
     * 监听源
     */
    private Object source;

    public ActionEvent(String timePeriod, Object source) {
        this.timePeriod = timePeriod;
        this.source = source;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public Object getSource() {
        return source;
    }
}
