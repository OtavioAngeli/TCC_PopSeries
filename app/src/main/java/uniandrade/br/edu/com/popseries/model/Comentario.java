package uniandrade.br.edu.com.popseries.model;

import java.util.Map;

/**
 * Created by pnda on 28/04/18
 *
 */

public class Comentario {

    private String serie_id, user_id, user_photo, serie_poster, user_name, serie_name, comentario, user_email, date_comment;
    //private Map<String, String> timestamp;

    public Comentario() {
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

    public void setSerie_poster(String serie_porter) {
        this.serie_poster = serie_porter;
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

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

    /*
    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }
    */
}
