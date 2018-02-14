package uniandrade.br.edu.com.popseries.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pnda on 15/12/17.
 *
 */

public interface Service {

    @GET("tv/popular")
    Call<SeriesResults> getPopularSeries(
            @Query("language") String language,
            @Query("api_key") String api_key
    );

    @GET("tv/top_rated")
    Call<SeriesResults> getTopRatedSeries(
            @Query("language") String language,
            @Query("api_key") String api_key
    );

}
