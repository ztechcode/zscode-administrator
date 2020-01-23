package org.zafritech.zscode.administrator.core.api.notes;

import org.zafritech.zscode.administrator.core.api.notes.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotesApiService {

    // Register new user
    @FormUrlEncoded
    @POST("todos/notes/apikey")
    Single<String> getApiKey(@Field("device_id") String deviceId);

    // Fetch all notes
    @GET("todos/notes")
    Single<List<Note>> fetchAllNotes();

    // Create note
    @POST("todos/notes/new")
    Single<Note> createNote(@Body Note note);

    // Update single note
    @PUT("todos/notes/update")
    Completable updateNote(@Body Note note);

    // Delete note
    @DELETE("todos/notes/delete/{id}")
    Completable deleteNote(@Path("id") int noteId);

}
