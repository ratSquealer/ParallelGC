package cxl.yunji;

import java.util.ArrayList;
import java.util.List;

public class ParallelGCTest {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();  // ArrayList存储数组
        int counter = 0;                        // 初始化计数器
        int maxIterations = 1500;               // 设置最大迭代次数，避免无限运行
        while (counter < maxIterations) {
            // 每次迭代时向list添加1MB
            list.add(new byte[1024 * 1024]);
            if (++counter % 100 == 0) {
                list.clear();
                System.out.println("Iteration: " + counter + " - Cleared list to trigger GC");
            }
            try {
                // 每次迭代后线程休眠10ms
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // 处理中断异常，并恢复线程的中断状态
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Test completed");
    }
}
