package com.joulin.lab3.monitoringBeans;

public interface CounterMXBean {
    int addHit(boolean hitResult);

    int addCorrectHit();

    int addMissedHit();

    void clearMissedStreak();

    int getHitsCount();

    int getMissedHitsCount();

    int getMissedHitsStreakCount();
}
