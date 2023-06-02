package com.joulin.lab3.monitoringBeans;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;


public class Counter extends NotificationBroadcasterSupport implements CounterMXBean, Serializable {
    private final int NUM_MISSES_TO_STREAK = 4;
    private int hitsCount = 0;
    private int missedHitsCount = 0;
    private int missedHitsStreakCount = 0;
    private int sequenceNumber = 1;

    @Override
    public int addHit(boolean hitResult) {
        return hitResult ? addCorrectHit() : addMissedHit();
    }

    @Override
    public int addCorrectHit() {
        clearMissedStreak();
        return ++hitsCount;
    }

    @Override
    public int addMissedHit() {
        hitsCount++;
        missedHitsCount++;
        if (++missedHitsStreakCount % NUM_MISSES_TO_STREAK == 0) {
            String message = NUM_MISSES_TO_STREAK + " misses streak";
            sendNotification(
                new Notification(message, this, sequenceNumber++, message)
            );
        }
        return missedHitsStreakCount;
    }

    @Override
    public void clearMissedStreak() {
        missedHitsStreakCount = 0;
    }

    @Override
    public int getHitsCount() {
        return hitsCount;
    }

    @Override
    public int getMissedHitsCount() {
        return missedHitsCount;
    }

    @Override
    public int getMissedHitsStreakCount() {
        return missedHitsStreakCount;
    }
}
