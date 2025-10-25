package com.aitu.mst.util;

/**
 * Утилита для подсчёта операций и измерения времени выполнения.
 */
public class Metrics {
    private long ops = 0;          // количество операций
    private long startNs;          // время старта (наносекунды)
    private double elapsedMs = 0;  // итоговое время (миллисекунды)

    /** Начать измерение времени */
    public void start() {
        startNs = System.nanoTime();
    }

    /** Завершить измерение времени */
    public void stop() {
        elapsedMs = (System.nanoTime() - startNs) / 1_000_000.0;
    }

    /** Увеличить счётчик операций на 1 */
    public void inc() {
        ops++;
    }

    /** Увеличить счётчик операций на n */
    public void add(long n) {
        ops += n;
    }

    /** Получить количество операций */
    public long getOps() {
        return ops;
    }

    /** Получить затраченное время в миллисекундах */
    public double getElapsedMs() {
        return elapsedMs;
    }
}
