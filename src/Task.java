public class Task {

    // ===================================== FIELDS ==========================================
    private String description;
    private boolean completed;

    // ================================== CONSTRUCTORS =======================================
    public Task(String description){
        this.description = description;
        this.completed = false;
    }

    // ==================================== GETTERS ==========================================
    public String getDescription(){ return description; }
    public boolean isCompleted(){ return completed; }

    // ==================================== SETTERS ==========================================
    public void setDescription(String description){ this.description = description; }
    public void setCompleted(boolean completed){ this.completed = completed; }
}
