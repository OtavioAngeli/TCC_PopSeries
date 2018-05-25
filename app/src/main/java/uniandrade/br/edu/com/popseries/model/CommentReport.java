package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 24/05/18.
 *
 */

public class CommentReport {
    private String serie_id, user_id, user_photo, serie_poster, user_name, serie_name, comentario, user_email, date_comment;
    private long num_comment_reports;

    public CommentReport() {

    }

    public String getSerie_id() {
        return serie_id;
    }

    public void setSerie_id(String serie_id) {
        this.serie_id = serie_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getSerie_poster() {
        return serie_poster;
    }

    public void setSerie_poster(String serie_poster) {
        this.serie_poster = serie_poster;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSerie_name() {
        return serie_name;
    }

    public void setSerie_name(String serie_name) {
        this.serie_name = serie_name;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

    public long getNum_comment_reports() {
        return num_comment_reports;
    }

    public void setNum_comment_reports(long num_comment_reports) {
        this.num_comment_reports = num_comment_reports;
    }
}
