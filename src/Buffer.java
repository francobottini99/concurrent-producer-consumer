import java.util.LinkedList;
import java.util.concurrent.locks.*;

public abstract class Buffer {
    protected ReadWriteLock lock;
    protected final Condition bufferNotFull;
    protected final Condition bufferNotEmpty;

    protected LinkedList<Data> buffer;

    protected final int maxSize;
    protected final int targetAmountOfData;

    protected int amountOfData;

    public Buffer(int maxSize, int targetAmountOfData) {
        this.maxSize = maxSize;
        this.targetAmountOfData = targetAmountOfData;

        amountOfData = 0;

        buffer = new LinkedList<>();

        lock = new ReentrantReadWriteLock(false);

        bufferNotFull = lock.writeLock().newCondition();
        bufferNotEmpty = lock.writeLock().newCondition();
    }

    public int getSize() {
        lock.readLock().lock();

        try {
            return buffer.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
