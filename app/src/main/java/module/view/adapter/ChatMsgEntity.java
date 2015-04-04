package module.view.adapter;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-04-04
 * Time: 00:05
 * 人机交互界面的List Entity
 */
public class ChatMsgEntity {
    /* 名字 */
    private String name;
    /* 日期 */
    private String date;
    /* 聊天内容 */
    private String text;
    /* 是否对方发送过来 */
    private boolean isComMsg;

    public ChatMsgEntity() {
    }

    /**
     * 构造函数
     * @param name
     * @param date
     * @param text
     * @param isComMsg
     */
    public ChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMsg = isComMsg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMsg;
    }

    public void setMsgType(boolean isComMsg) {
        this.isComMsg = isComMsg;
    }
}
