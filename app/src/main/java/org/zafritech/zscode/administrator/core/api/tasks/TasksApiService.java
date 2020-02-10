package org.zafritech.zscode.administrator.core.api.tasks;

import org.zafritech.zscode.administrator.core.api.tasks.models.BasicTask;
import org.zafritech.zscode.administrator.core.api.tasks.models.Category;
import org.zafritech.zscode.administrator.core.api.tasks.models.Note;
import org.zafritech.zscode.administrator.core.api.tasks.models.Schedule;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;
import org.zafritech.zscode.administrator.core.api.tasks.models.TasksRequestDate;
import org.zafritech.zscode.administrator.core.api.tasks.models.TasksRequestRange;

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

public interface TasksApiService {

    // Register new user
    @FormUrlEncoded
    @POST("todos/notes/apikey")
    Single<String> getApiKey(@Field("device_id") String deviceId);

    // Create note
    @GET("todos/tasks/id/{id}")
    Single<Task> getTask(@Path("id") Long taskId);

    // Create note
    @POST("todos/tasks/task/save")
    Single<Task> createTask(@Body BasicTask task);

    // Create note
    @POST("todos/tasks/task/update")
    Single<Task> updateTask(@Body Task task);

    // Delete note
    @DELETE("todos/tasks/task/delete/{id}")
    Completable deleteTask(@Path("id") int taskId);

    // Fetch all categories
    @GET("todos/categories")
    Single<List<Category>> fetchCategories();

    // Fetch scheduled tasks on date
    @POST("todos/tasks/date")
    Single<List<Schedule>> fetchTasksByDate(@Body TasksRequestDate date);

    // Fetch scheduled tasks up to date
    @POST("todos/tasks/date/inclusive")
    Single<List<Schedule>> fetchTasksUpToDate(@Body TasksRequestDate date);

    // Fetch scheduled tasks on date range
    @POST("todos/tasks/range")
    Single<List<Schedule>> fetchTasksByDateRange(@Body TasksRequestRange range);

    // Fetch scheduled tasks on date range
    @POST("todos/tasks/range/inclusive")
    Single<List<Schedule>> fetchAllTasksByDateRange(@Body TasksRequestRange range);

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
