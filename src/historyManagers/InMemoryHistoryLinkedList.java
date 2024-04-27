package historyManagers;

import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryLinkedList  {
    private InMemoryHistoryNode lastNode = null;

    private final Map<Integer, InMemoryHistoryNode> indexes = new HashMap<>();

    public int size() { return indexes.size(); }

    public void add (Task task){
        int taskId = task.getTaskId();

        if (indexes.isEmpty()){
            lastNode = new InMemoryHistoryNode(task);
            indexes.put(taskId, lastNode);
            return;
        }

        if (lastNode.getTask().getTaskId() == taskId)
            return;

        if (indexes.containsKey(taskId)) {
            InMemoryHistoryNode node = indexes.get(taskId);

            cutNode(node);

            lastNode.setNextNode(node);
            node.setPrevNode(lastNode);
            lastNode = node;
        }
        else {
            InMemoryHistoryNode node = new InMemoryHistoryNode(task, lastNode, null);

            lastNode.setNextNode(node);

            lastNode = node;

            indexes.put(taskId, lastNode);
        }
    }

    public void remove(int taskId){
        InMemoryHistoryNode node = indexes.remove(taskId);

        if (node == null)
            return;

        cutNode(node);

        if (lastNode.equalTasks(node))
            lastNode = node.getPrevNode();
    }

    public List<Task> getHistory(){
        List<Task> result = new java.util.ArrayList<>(List.of());

        InMemoryHistoryNode node = lastNode;

        while (node != null){
            result.add(node.getTask());
            node = node.getPrevNode();
        }

        return result;
    }

    public List<Task> getHistory(int size){
        List<Task> result = new java.util.ArrayList<>(List.of());

        InMemoryHistoryNode node = lastNode;

        while (node != null && result.size() < size){
            result.add(node.getTask());
            node = node.getPrevNode();
        }

        return result;
    }

    private void cutNode(InMemoryHistoryNode node){
        InMemoryHistoryNode prevNode = node.getPrevNode();
        InMemoryHistoryNode nextNode = node.getNextNode();

        if (prevNode != null)
            prevNode.setNextNode(nextNode);

        if (nextNode != null)
            nextNode.setPrevNode(prevNode);
    }
}
