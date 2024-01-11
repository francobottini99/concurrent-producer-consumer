import java.util.concurrent.TimeUnit;

public class Creator implements Runnable {
    private int dataAccepted, dataLost;

    private final InitBuffer initBuffer;
    private final String name;

    public Creator(InitBuffer initBuffer, String name) {
        this.initBuffer = initBuffer;
        this.name = name;

        dataAccepted = 0;
        dataLost = 0;
    }

    @Override
    public void run() {
        while (initBuffer.isNotCreationCompleted()) {
            try {
                if (initBuffer.add(new Data())) {
                    dataAccepted++;
                } else {
                    dataLost++;
                }

                TimeUnit.MILLISECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getDataAccepted() {
        return dataAccepted;
    }

    public int getDataLost() {
        return dataLost;
    }
}
