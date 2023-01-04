package tbc.uncagedmist.bhulekhkheshra.Remote;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tbc.uncagedmist.bhulekhkheshra.Model.State;


public interface IMyAPI {

    @GET("getStates.php")
    Observable<List<State>> getStates();


}
