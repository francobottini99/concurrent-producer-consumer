public class ValidBuffer extends Buffer {
    private boolean reviewCompleted;

    public ValidBuffer(int maxSize, int targetAmountOfData) {
        super(maxSize, targetAmountOfData);

        reviewCompleted = false;
    }

    public void add(Data value) throws InterruptedException {
        lock.writeLock().lock();

        try {
            while (buffer.size() == maxSize && !reviewCompleted) {
                bufferNotFull.await();
            }

            if(!reviewCompleted) {
                this.buffer.addLast(value);

                amountOfData++;

                if(amountOfData == targetAmountOfData) {
                    reviewCompleted = true;
                }

                System.out.printf("[ValidBuffer (Size: %d)] %s data added <ID: %d - Value: %s>\n", this.buffer.size(), Thread.currentThread().getName(), value.getId(), value.getValue());

                bufferNotEmpty.signalAll();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Data poll() throws InterruptedException {
        lock.writeLock().lock();

        try {
            while (buffer.size() == 0 && !reviewCompleted) {
                bufferNotEmpty.await();
            }

            if (buffer.size() > 0) {
                Data value = this.buffer.poll();

                System.out.printf("[ValidBuffer (Size: %d)] %s data removed <ID: %d - Value: %s>\n", this.buffer.size(), Thread.currentThread().getName(), value.getId(), value.getValue());

                bufferNotFull.signalAll();

                return value;
            } else {
                return null;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isNotReviewCompleted() {
        lock.readLock().lock();

        try {
            return !reviewCompleted;
        } finally {
            lock.readLock().unlock();
        }
    }
}
