import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
    private final InitBuffer initBuffer;
    private final ValidBuffer validBuffer;
    private final String name;

    private int dataConsumed;

    public Consumer(InitBuffer initBuffer, ValidBuffer validBuffer, String name) {
        this.validBuffer = validBuffer;
        this.initBuffer = initBuffer;
        this.name = name;

        dataConsumed = 0;
    }

    @Override
    public void run() {
        while (validBuffer.getSize() > 0 || validBuffer.isNotReviewCompleted()) {
            try {
                Data value = validBuffer.poll();

                if(value != null) {
                    initBuffer.remove(value);

                    dataConsumed++;
                }

                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDataConsumed() {
        return dataConsumed;
    }

    public String getName() {
        return name;
    }
}
