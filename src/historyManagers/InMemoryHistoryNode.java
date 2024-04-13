package historyManagers;

import tasks.Task;

public class InMemoryHistoryNode {
    private final Task task;
    private InMemoryHistoryNode prevNode = null;
    private InMemoryHistoryNode nextNode = null;

    public InMemoryHistoryNode(Task task, InMemoryHistoryNode prevNode, InMemoryHistoryNode nextNode){
        this.task = task;
        this.prevNode = prevNode;
        this.nextNode = nextNode;
    }

    public InMemoryHistoryNode(Task task){
        this(task, null, null);
    }

    public InMemoryHistoryNode getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(InMemoryHistoryNode prevNode) {
        this.prevNode = prevNode;
    }

    public InMemoryHistoryNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(InMemoryHistoryNode nextNode) {
        this.nextNode = nextNode;
    }

    public Task getTask() {
        return task;
    }

    public boolean equalTasks(InMemoryHistoryNode otherNode){
        if (task == null || otherNode == null || otherNode.task == null)
            return false;

        return task.equals(otherNode.task);
    }
}
