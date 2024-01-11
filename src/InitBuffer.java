import java.util.Random;

public class InitBuffer extends Buffer {
    private boolean creationCompleted;

    public InitBuffer(int maxSize, int targetAmountOfData) {
        super(maxSize, targetAmountOfData);

        creationCompleted = false;
    }

    public boolean add(Data value) throws InterruptedException {
        lock.writeLock().lock();

        try {
            if(!creationCompleted) {
                if (buffer.size() < maxSize) {
                    buffer.addLast(value);

                    amountOfData++;

                    if (amountOfData == targetAmountOfData) {
                        creationCompleted = true;
                    }

                    System.out.printf("[InitBuffer (Size: %d)] %s data added <ID: %d - Value: %s>\n", this.buffer.size(), Thread.currentThread().getName(), value.getId(), value.getValue());

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Data getItem(Data last) throws InterruptedException {
        lock.readLock().lock();

        try {
            if (buffer.size() > 0) {
                try {
                    return buffer.get(buffer.indexOf(last) + 1);
                } catch (IndexOutOfBoundsException e) {
                    return buffer.get(new Random().nextInt(buffer.size()));
                }
            } else {
                return null;
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void remove(Data value) {
        lock.writeLock().lock();

        try {
            if(this.buffer.remove(value)) {
                System.out.printf("[InitBuffer (Size: %d)] %s data removed <ID: %d - Value: %s>\n", this.buffer.size() , Thread.currentThread().getName(), value.getId(), value.getValue());
                bufferNotFull.signalAll();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isNotCreationCompleted() {
        lock.readLock().lock();

        try {
            return !creationCompleted;
        } finally {
            lock.readLock().unlock();
        }
    }
}
