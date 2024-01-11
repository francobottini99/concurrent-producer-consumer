public class Main {

    private static final int NUMBER_OF_CONSUMERS = 2;
    private static final int NUMBER_OF_CREATORS = 4;
    private static final int NUMBER_OF_REVIEWERS = 2;

    private static final int SIZE_OF_INIT_BUFFER = 100;
    private static final int SIZE_OF_VALID_BUFFER = 100;

    private static final int AMOUNT_OF_DATA_TO_CREATED = 1000;

    private static final int NUMBER_OF_EXECTUTIONS = 1;

    public static void main(String[] args) {
        Log.clearFile();

        for (int j = 0; j < NUMBER_OF_EXECTUTIONS; j++) {
            InitBuffer initBuffer = new InitBuffer(SIZE_OF_INIT_BUFFER, AMOUNT_OF_DATA_TO_CREATED);
            ValidBuffer validBuffer = new ValidBuffer(SIZE_OF_VALID_BUFFER, AMOUNT_OF_DATA_TO_CREATED);

            Creator[] creators = new Creator[NUMBER_OF_CREATORS];
            Consumer[] consumers = new Consumer[NUMBER_OF_CONSUMERS];
            Reviewer[] reviewers = new Reviewer[NUMBER_OF_REVIEWERS];

            Thread[] creatorsThreads = new Thread[NUMBER_OF_CREATORS];
            Thread[] consumersThreads = new Thread[NUMBER_OF_CONSUMERS];
            Thread[] reviewersThreads = new Thread[NUMBER_OF_REVIEWERS];

            Log log = new Log(initBuffer, validBuffer, creators, consumers, reviewers, creatorsThreads, consumersThreads, reviewersThreads);

            for (int i = 0; i < NUMBER_OF_CREATORS; i++) {
                creators[i] = new Creator(initBuffer, "Creator " + i);
                creatorsThreads[i] = new Thread(creators[i]);
                creatorsThreads[i].setName(creators[i].getName() + " (Thread ID: " + creatorsThreads[i].getId() + ")");
            }

            for (int i = 0; i < NUMBER_OF_CONSUMERS; i++) {
                consumers[i] = new Consumer(initBuffer, validBuffer, "Consumer " + i);
                consumersThreads[i] = new Thread(consumers[i]);
                consumersThreads[i].setName(consumers[i].getName() + " (Thread ID: " + consumersThreads[i].getId() + ")");
            }

            for (int i = 0; i < NUMBER_OF_REVIEWERS; i++) {
                reviewers[i] = new Reviewer(initBuffer, validBuffer, NUMBER_OF_REVIEWERS, "Reviewer " + i);
                reviewersThreads[i] = new Thread(reviewers[i]);
                reviewersThreads[i].setName(reviewers[i].getName() + " (Thread ID: " + reviewersThreads[i].getId() + ")");
            }

            for (Thread creatorsThread : creatorsThreads) {
                creatorsThread.start();
            }

            for (Thread reviewersThread : reviewersThreads) {
                reviewersThread.start();
            }

            for (Thread consumersThread : consumersThreads) {
                consumersThread.start();
            }

            log.start();

            try {
                for (Thread creatorsThread : creatorsThreads) {
                    creatorsThread.join();
                }

                for (Thread reviewersThread : reviewersThreads) {
                    reviewersThread.join();
                }

                for (Thread consumersThread : consumersThreads) {
                    consumersThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.interrupt();
        }
    }
}
