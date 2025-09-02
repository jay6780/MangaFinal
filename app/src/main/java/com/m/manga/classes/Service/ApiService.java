package com.m.manga.classes.Service;

import com.m.manga.classes.ApiBean;
import com.m.manga.classes.GenreData;
import com.m.manga.classes.MangaImageBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("manga-list/{page}")
    Observable<ApiBean> getMangaData(@Path("page") int page);

    @GET("search/{search}")
    Observable<ApiBean> getSearch(@Path("search") String keyWord);

    @GET("manga/{manga}")
    Observable<ApiBean> getDetails(@Path("manga") String mangaId);

    @GET("manga/{manga}/{chapter}")
    Observable<MangaImageBean> getChapters(@Path("manga") String mangaId,
                                           @Path("chapter") String chapter);
    @GET("genre")
    Observable<ApiBean> getGenre();

    @GET("genre/{genreName}/{page}")
    Observable<GenreData> getGenreList(@Path("genreName") String genreName,
                                       @Path("page") int page);
}
