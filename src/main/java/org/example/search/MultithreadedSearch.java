package main.java.org.example.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadedSearch 
{
    public static <T> void countOccurrences(List<T> list, T target, Comparator<T> comparator, int threadCount) {
        if (list == null || list.isEmpty()) {
            System.out.println("Коллекция пуста. Вхождений: 0");
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger totalCount = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();

        int chunkSize = list.size() / threadCount;
        
        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = (i == threadCount - 1) ? list.size() : (i + 1) * chunkSize;
            
            Runnable task = () -> {
                int localCount = 0;
                for (int j = start; j < end; j++) {
                    if (comparator.compare(list.get(j), target) == 0) {
                        localCount++;
                    }
                }
                totalCount.addAndGet(localCount);
                System.out.println(Thread.currentThread().getName() + " обработал индексы " + 
                                 start + "-" + (end-1) + ", найдено: " + localCount);
            };
            
            futures.add(executor.submit(task));
        }

        // Ожидаем завершения всех задач
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        
        System.out.println("\nОбщее количество вхождений элемента: " + totalCount.get());
    }


}
