package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 06/05/18.
 *
 */

public class Avaliacao {

    private String serie_id, user_id, serie_poster, serie_name;
    private float avaliacao;

    public Avaliacao() {
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

    public String getSerie_poster() {
        return serie_poster;
    }

    public void setSerie_poster(String serie_poster) {
        this.serie_poster = serie_poster;
    }

    public String getSerie_name() {
        return serie_name;
    }

    public void setSerie_name(String serie_name) {
        this.serie_name = serie_name;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }
}
