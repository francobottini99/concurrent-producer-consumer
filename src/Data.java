import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Data {
    private static int generator = 0;
    private static final Object controlGenerator = new Object();

    private final ReadWriteLock lock;

    private final List<Reviewer> reviews;
    private final int id;
    private final Date value;

    private boolean copiedToValidBuffer;

    private static int newId() {
        synchronized (controlGenerator) {
            return generator++;
        }
    }

    public Data() {
        reviews = new ArrayList<>();
        value = new Date();
        id = newId();

        lock = new ReentrantReadWriteLock(false);

        copiedToValidBuffer = false;
    }

    public void verify(Reviewer reviewer) {
        lock.writeLock().lock();

        try {
            reviews.add(reviewer);
            System.out.printf("[InitBuffer] %s: verified data <ID: %d - Value: %s>\n", Thread.currentThread().getName(), id, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isVerified(Reviewer reviewer) {
        lock.readLock().lock();

        try {
            return reviews.contains(reviewer);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getNumberOfReviews() {
        lock.readLock().lock();

        try {
            return reviews.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isCopiedToValidBuffer() {
        lock.readLock().lock();

        try {
            return copiedToValidBuffer;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean tryCopiedToValidBuffer() {
        lock.writeLock().lock();

        try {
            if(!copiedToValidBuffer) {
                copiedToValidBuffer = true;

                return true;
            } else {
                return false;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Date getValue() {
        return value;
    }

    public int getId() {
        return id;
    }
}
