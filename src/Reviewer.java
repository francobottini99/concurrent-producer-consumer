import java.util.concurrent.TimeUnit;

public class Reviewer implements Runnable {
    private final InitBuffer initBuffer;
    private final ValidBuffer validBuffer;
    private final int totalReviewers;
    private final String name;

    private int dataVerified, dataMoved;

    private Data lastRevisedData;

    public Reviewer(InitBuffer initBuffer, ValidBuffer validBuffer, int totalReviewers, String name) {
        this.initBuffer = initBuffer;
        this.validBuffer = validBuffer;
        this.totalReviewers = totalReviewers;
        this.name = name;

        dataVerified = 0;
        dataMoved = 0;

        lastRevisedData = null;
    }

    @Override
    public void run() {
        while(initBuffer.getSize() > 0 || initBuffer.isNotCreationCompleted()) {
            try {
                lastRevisedData = initBuffer.getItem(lastRevisedData);

                if (lastRevisedData != null) {
                    if (!lastRevisedData.isVerified(this)) {
                        lastRevisedData.verify(this);

                        dataVerified++;

                        if (lastRevisedData.getNumberOfReviews() == totalReviewers) {
                            if(!lastRevisedData.isCopiedToValidBuffer()) {
                                if(lastRevisedData.tryCopiedToValidBuffer()) {
                                    validBuffer.add(lastRevisedData);
                                    dataMoved++;
                                }
                            }
                        }
                    }
                }

                TimeUnit.MILLISECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public int getDataMoved() {
        return dataMoved;
    }

    public int getDataVerified() {
        return dataVerified;
    }

    public String getName() {
        return name;
    }
}
