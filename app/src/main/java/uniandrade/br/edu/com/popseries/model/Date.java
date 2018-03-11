package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 07/03/18.
 */

public class Date {

    private String data;
    private String year;
    private String month;
    private String day;
    private String convertedDate;


    public Date() {

    }

    public void setData(String data){
        this.data = data;
       setConvertedDate(data);
    }

    public String getYear() {
        return year;
    }

    private void setYear(String year) {
        this.year = year;
    }

    private String getMonth() {
        return month;
    }

    private void setMonth(String month) {
        this.month = month;
    }

    private String getDay() {
        return day;
    }

    private void setDay(String day) {
        this.day = day;
    }

    public String getConvertedDate() {
        return convertedDate;
    }

    private void setConvertedDate(String convertedDate) {
        String[] newDate = convertedDate.split("-");
        setYear(newDate[0]);
        setMonth(newDate[1]);
        setDay(newDate[2]);
        this.convertedDate = getDay()+" / "+getMonth()+" / "+getYear();
    }
}
