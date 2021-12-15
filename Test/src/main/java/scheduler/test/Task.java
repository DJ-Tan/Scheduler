package scheduler.test;

public class Task {

    private String assignment;

    public Task(String moduleCode, String status, String startDate, String dueDate, String task, String remarks) {
        this.assignment = task;
    }

    public String getTask() {
        return this.assignment;
    }
}