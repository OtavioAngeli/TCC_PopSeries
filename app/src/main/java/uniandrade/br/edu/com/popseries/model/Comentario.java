package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 28/04/18
 *
 */

public class Comentario {

    private String serie_id, user_id, user_photo, serie_porter, user_name, serie_name, comentario;

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

    public String getSerie_porter() {
        return serie_porter;
    }

    public void setSerie_porter(String serie_porter) {
        this.serie_porter = serie_porter;
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
}
