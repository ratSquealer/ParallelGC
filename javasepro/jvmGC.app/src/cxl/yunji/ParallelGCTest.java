package cxl.yunji;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ParallelGCTest {
    private static final int MAX_POOL_SIZE = 1000; // 最大对象池大小
    private static final Queue<byte[]> OBJECT_POOL = new LinkedList<>(); // 对象池

    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>(); // ArrayList存储数组
        List<byte[]> longerLivedList = new ArrayList<>(); // 存放长时间存活的对象
        int counter = 0; // 初始化计数器
        int maxIterations = 3000; // 设置最大迭代次数，避免无限运行
        int[] martx = new int[]{1, 1, 5, 5, 5, 5, 5, 5, 10, 10, 20, 50, 100};
        int j = 0;
        int longerLivedThreshold = 7; // 定义一个阈值，使部分对象存活更长时间

        while (counter < maxIterations) {
            byte[] array = getObjectFromPool(); // 从对象池获取对象
            if (array == null) {
                array = new byte[martx[j] * 1024 * 1024]; // 如果对象池为空，分配新对象
            }
            counter++;
            list.add(array);
            if (counter % longerLivedThreshold == 0) {
                longerLivedList.add(array);
            }

            j = (j + 1) % martx.length;
            list.subList(0, list.size() / 2).clear();
            System.out.println("Iteration: " + counter + " - Cleared list to trigger GC");

            if (counter % (longerLivedThreshold * 5) == 0) {
                recycleObjects(longerLivedList); // 回收长时间存活的对象到对象池
                System.out.println("Cleared longerLivedList to simulate longer-lived object processing");
            }

            try {
                Thread.sleep(100); // 每次迭代后线程休眠100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Test completed");
    }

    private static byte[] getObjectFromPool() {
        return OBJECT_POOL.poll(); // 从对象池获取对象
    }

    private static void recycleObjects(List<byte[]> objects) {
        if (OBJECT_POOL.size() < MAX_POOL_SIZE) {
            OBJECT_POOL.addAll(objects); // 将对象添加回对象池
        }
        objects.clear(); // 清空列表
    }

}


