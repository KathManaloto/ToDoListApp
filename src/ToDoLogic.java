import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class ToDoLogic {

    // ===================================== FIELDS =======================================
    private final List<Task> tasks = new ArrayList<>();
    private final File storageFile;
    private final Gson gson = new Gson();

    // ================================== CONSTRUCTOR =====================================
    public ToDoLogic(){
        // Create "data" folder if it doesn't exist
        File dataFolder = new File("data");
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                System.err.println("⚠ Could not create data folder: " + dataFolder.getAbsolutePath());
            }
        }

        // Place the tasks.json file inside that folder
        storageFile = new File(dataFolder, "tasks.json");

        // Load existing tasks when app starts
        loadTasks();
    }

    // =================================== ADD TASK =======================================
    public void addTask(String description){
        if(description == null || description.trim().isEmpty()){
            return;
        }
        tasks.add(new Task(description));
        saveTasks();
    }

    // ================================== EDIT TASK =======================================
    public void editTask(Task task, String newDescription) {
        if (task != null && newDescription != null && !newDescription.trim().isEmpty()) {
            task.setDescription(newDescription.trim());
            saveTasks();
        }
    }

    // ================================== DELETE TASK =======================================
    public void deleteTask(Task task) {
        if (task != null) {
            tasks.remove(task);
            saveTasks();
        }
    }

    // ============================== TOGGLE TASK STATUS =======================================
    public void toggleTaskStatus(Task task){
        if (task != null) {
            task.setCompleted(!task.isCompleted());
            saveTasks();
        }
    }

    // ================================ FILTER TASKS =======================================

    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks);
    }

    public List<Task> getCompletedTasks(){
        List<Task> result = new ArrayList<>();
        for(Task task : tasks){
            if(task.isCompleted()){
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> getPendingTasks(){
        List<Task> result = new ArrayList<>();
        for(Task task : tasks){
            if(!task.isCompleted()){
                result.add(task);
            }
        }
        return result;
    }

    // ================================ SAVE & LOAD =======================================
    private void saveTasks(){
        try(Writer writer = new FileWriter(storageFile)){
            gson.toJson(tasks,writer);

        }catch (IOException e){
            e.getLocalizedMessage();
        }
    }

    private void loadTasks(){
        if(!storageFile.exists()) return;
        try(Reader reader = new FileReader(storageFile)){
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> loaded = gson.fromJson(reader,listType);

            if(loaded != null){
                tasks.clear();
                tasks.addAll(loaded);
            }
        }

        catch (IOException e){
            e.getLocalizedMessage();
        }
    }

}
