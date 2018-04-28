package uniandrade.br.edu.com.popseries.model;

/**
 * Created by pnda on 02/04/18.
 *
 */

public class Serie {

    private int ID;
    private int serie_ID;
    private String urlCapa;
    private String original_Title;
    private String overview;
    private String apiRate;
    private String dataLancamento;

    /*** CONSTRUCTOR INSTANCE***/
    public Serie() {

    }

    /*** CONSTRUCTOR UPDATE ***/
    public Serie(int ID, int serie_ID, String urlCapa, String original_Title, String overview, String apiRate, String dataLancamento) {
        this.ID = ID;
        this.serie_ID = serie_ID;
        this.urlCapa = urlCapa;
        this.original_Title = original_Title;
        this.overview = overview;
        this.apiRate = apiRate;
        this.dataLancamento = dataLancamento;
    }

    /*** CONSTRUCTOR INSERT ***/
    public Serie(int serie_ID, String urlCapa, String original_Title, String overview, String apiRate, String dataLancamento) {
        this.serie_ID = serie_ID;
        this.urlCapa = urlCapa;
        this.original_Title = original_Title;
        this.overview = overview;
        this.apiRate = apiRate;
        this.dataLancamento = dataLancamento;
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

    public String getUrlCapa() {
        return urlCapa;
    }

    public void setUrlCapa(String urlCapa) {
        this.urlCapa = urlCapa;
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

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
