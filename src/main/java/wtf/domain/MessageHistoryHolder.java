package wtf.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageHistoryHolder {

    private Map<Room, Queue<String>> historyByRoom = new ConcurrentHashMap<>();
    private int size;

    public MessageHistoryHolder(int size) {
        this.size = size;
    }

    public void storeMessageFor(Room room, String message) {
        Queue<String> queue = getMessageQueueFor(room);
        if (queue.size() >= size) {
            queue.poll();
        }
        queue.add(message);
    }

    public Iterable<String> getLastMessagesFor(Room room) {
        return Collections.unmodifiableCollection(getMessageQueueFor(room));
    }

    private Queue<String> getMessageQueueFor(Room room) {
        return historyByRoom.computeIfAbsent(room, r -> new ConcurrentLinkedQueue<>());
    }
}
