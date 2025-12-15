package com.grabtutor.grabtutor.service.worker;

import com.grabtutor.grabtutor.enums.JobType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkerLauncher implements SmartLifecycle {

    private final JobRunner jobRunner;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executor;

    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) return;

        executor = Executors.newFixedThreadPool(3, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("job-worker-" + t.threadId());
            return t;
        });

        executor.submit(() -> loop(JobType.POST_EXPIRE));
        executor.submit(() -> loop(JobType.CHATROOM_TIMEOUT));
        executor.submit(() -> loop(JobType.CHATROOM_CONFIRMED));

        log.info("Job workers started.");
    }

    private void loop(JobType type) {
        while (running.get()) {
            try {
                int processed = jobRunner.runOnce(type);
                Thread.sleep(processed == 0 ? 500 : 50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Worker loop error type={}", type, e);
                try { Thread.sleep(500); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); break; }
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false) && executor != null) {
            executor.shutdownNow();
        }
        log.info("Job workers stopped.");
    }

    @Override public boolean isRunning() { return running.get(); }
    @Override public int getPhase() { return Integer.MAX_VALUE; }
}
