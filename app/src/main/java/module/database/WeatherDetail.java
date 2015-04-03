package module.database;
/**
 * Package: module.database
 * User: niuwei(nniuwei@163.com)
 * Date: 2015-03-29
 * Time: 13:12
 * 当前天气详情
 */
public class WeatherDetail {
    private String city;
    private String date_y;
    private String week;
    private String[] temp;
    private String[] weather;
    private String[] wind;
    private String index_tr;//旅游
    private String index_co;//舒适

    /**
     * 构造函数
     * @param city
     * @param date_y
     * @param week
     * @param temp
     * @param weather
     * @param wind
     * @param index_tr
     * @param index_co
     */
    public WeatherDetail(String city, String date_y, String week, String[] temp, String[] weather, String[] wind, String index_tr, String index_co) {
        this.city = city;
        this.date_y = date_y;
        this.week = week;
        this.temp = temp;
        this.weather = weather;
        this.wind = wind;
        this.index_tr = index_tr;
        this.index_co = index_co;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String[] getTemp() {
        return temp;
    }

    public void setTemp(String[] temp) {
        this.temp = temp;
    }

    public String[] getWeather() {
        return weather;
    }

    public void setWeather(String[] weather) {
        this.weather = weather;
    }

    public String[] getWind() {
        return wind;
    }

    public void setWind(String[] wind) {
        this.wind = wind;
    }

    public String getIndex_tr() {
        return index_tr;
    }

    public void setIndex_tr(String index_tr) {
        this.index_tr = index_tr;
    }

    public String getIndex_co() {
        return index_co;
    }

    public void setIndex_co(String index_co) {
        this.index_co = index_co;
    }
}
