package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 02/04/18.
 *
 */

public class Serie {

    private int ID;
    private int serie_ID;
    private String poster;
    private String original_Title;
    private String overview;
    private String apiRate;

    /*** CONSTRUCTOR INSTANCE***/
    public Serie() {

    }

    /*** CONSTRUCTOR UPDATE ***/
    public Serie(int ID, int serie_ID, String poster, String original_Title, String overview, String apiRate) {
        this.ID = ID;
        this.serie_ID = serie_ID;
        this.poster = poster;
        this.original_Title = original_Title;
        this.overview = overview;
        this.apiRate = apiRate;
    }

    /*** CONSTRUCTOR INSERT ***/
    public Serie(int serie_ID, String poster, String original_Title, String overview, String apiRate) {
        this.serie_ID = serie_ID;
        this.poster = poster;
        this.original_Title = original_Title;
        this.overview = overview;
        this.apiRate = apiRate;
    }

    /*** GETTER AND SETTER ***/
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSerie_ID() {
        return serie_ID;
    }

    public void setSerie_ID(int serie_ID) {
        this.serie_ID = serie_ID;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOriginal_Title() {
        return original_Title;
    }

    public void setOriginal_Title(String original_Title) {
        this.original_Title = original_Title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getApiRate() {
        return apiRate;
    }

    public void setApiRate(String apiRate) {
        this.apiRate = apiRate;
    }

}
