import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Log extends Thread {
    private final Date initTime;

    private final InitBuffer initBuffer;
    private final ValidBuffer validBuffer;

    private final Creator[] creators;
    private final Consumer[] consumers;
    private final Reviewer[] reviewers;

    private final Thread[] creatorsThreads;
    private final Thread[] consumersThreads;
    private final Thread[] reviewersThreads;

    public static void clearFile() {
        try {
            PrintWriter pw_log = new PrintWriter(".\\data\\log.txt");
            pw_log.print("");
            pw_log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Log(InitBuffer initBuffer, ValidBuffer validBuffer, Creator[] creators,
               Consumer[] consumers, Reviewer[] reviewers, Thread[] creatorsThreads,
               Thread[] consumersThreads, Thread[] reviewersThreads) {
        this.initBuffer = initBuffer;
        this.validBuffer = validBuffer;
        this.creators = creators;
        this.consumers = consumers;
        this.reviewers = reviewers;
        this.creatorsThreads = creatorsThreads;
        this.consumersThreads = consumersThreads;
        this.reviewersThreads = reviewersThreads;

        initTime = new Date();
    }

    @Override
    public void run() {
        while(true) {
            try {
                writeLog();
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                writeLog();
                break;
            }
        }
    }

    private void writeLog() {
        try {
            PrintWriter pw_log = new PrintWriter(new FileWriter(".\\data\\log.txt", true));

            pw_log.print("*-------------------------------------------------------------------------------*\n");
            pw_log.printf("Execution time: %f [Seg]\n", (float)(new Date().getTime() - initTime.getTime()) / 1000);
            pw_log.printf("InitBuffer size: %d\n", initBuffer.getSize());
            pw_log.printf("ValidBuffer size: %d\n", validBuffer.getSize());
            pw_log.print("*-------------------------------------------------------------------------------*\n\n");

            Creator[] creatorsCopy = creators;
            Reviewer[] reviewersCopy = reviewers;
            Consumer[] consumersCopy = consumers;

            int totalDataLost = 0, totalDataAccepted = 0;
            for (Creator creator: creatorsCopy) {
                totalDataLost += creator.getDataLost();
                totalDataAccepted += creator.getDataAccepted();
            }

            pw_log.println("   Creators Totals:");
            pw_log.printf("       data lost: %d\n", totalDataLost);
            pw_log.printf("       data accepted: %d\n", totalDataAccepted);
            pw_log.printf("       total data produced: %d\n", totalDataAccepted + totalDataLost);
            pw_log.printf("       loss percentage: %f %%\n\n", 100 * (float) totalDataLost / (totalDataAccepted + totalDataLost));

            for (Creator creator: creatorsCopy) {
                pw_log.printf("   %s:\n", creator.getName());
                pw_log.printf("      data lost: %d\n", creator.getDataLost());
                pw_log.printf("      data accepted: %d\n", creator.getDataAccepted());
                pw_log.printf("      total data produced: %d\n", creator.getDataAccepted() + creator.getDataLost());
                pw_log.printf("      loss percentage: %f %%\n", 100 * (float) creator.getDataLost() / (creator.getDataAccepted() + creator.getDataLost()));

                pw_log.printf("      responsibility percentage in lost data: %f %%\n", 100 * (float) creator.getDataLost() / totalDataLost);
                pw_log.printf("      responsibility percentage in accepted data: %f %%\n", 100 * (float) creator.getDataAccepted() / totalDataAccepted);
                pw_log.printf("      responsibility percentage in produced data: %f %%\n\n", 100 * (float) (creator.getDataAccepted() + creator.getDataLost()) / (totalDataAccepted + totalDataLost));
            }

            int totalDataMoved = 0, totalDataRevised = 0;
            for (Reviewer reviewer: reviewersCopy) {
                totalDataMoved += reviewer.getDataMoved();
                totalDataRevised += reviewer.getDataVerified();
            }

            pw_log.println("   Reviewers Totals:");
            pw_log.printf("       data revised: %d\n", totalDataRevised);
            pw_log.printf("       data copied: %d\n\n", totalDataMoved);

            for (Reviewer reviewer: reviewersCopy) {
                pw_log.printf("   %s:\n", reviewer.getName());
                pw_log.printf("      data revised: %d\n", reviewer.getDataVerified());
                pw_log.printf("      data copied: %d\n", reviewer.getDataMoved());

                pw_log.printf("      responsibility percentage in copied data: %f %%\n\n", 100 * (float) reviewer.getDataMoved() / totalDataMoved);
            }

            int totalDataConsumed = 0;
            for (Consumer consumer: consumersCopy) {
                totalDataConsumed += consumer.getDataConsumed();
            }

            pw_log.println("   Consumers Totals:");
            pw_log.printf("       data taken: %d\n\n", totalDataConsumed);

            for (Consumer consumer: consumersCopy) {
                pw_log.printf("   %s:\n", consumer.getName());
                pw_log.printf("      data taken: %d\n", consumer.getDataConsumed());

                pw_log.printf("      responsibility percentage in taken data: %f %%\n\n", 100 * (float) consumer.getDataConsumed() / totalDataConsumed);
            }

            pw_log.println("   Threads State:");

            for (Thread creatorThread: creatorsThreads) {
                pw_log.printf("      %s: %s\n", creatorThread.getName(), creatorThread.getState().name());
            }

            pw_log.println();

            for (Thread consumerThread: consumersThreads) {
                pw_log.printf("      %s: %s\n", consumerThread.getName(), consumerThread.getState().name());
            }

            pw_log.println();

            for (Thread reviewerThread: reviewersThreads) {
                pw_log.printf("      %s: %s\n", reviewerThread.getName(), reviewerThread.getState().name());
            }

            pw_log.println();

            pw_log.print("*-------------------------------------------------------------------------------*\n\n");

            pw_log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
