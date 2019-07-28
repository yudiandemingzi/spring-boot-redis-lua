package com.jincou.redislua.controller;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * @Description: 性能测试接口
 *    主要比较：1、list的 contains   方法
 *             2、map的 containsKey 方法
 *             3、谷歌的布隆过滤器 mightContain 方法
 *
 * @author xub
 * @date 2019/7/26 下午6:34
 */
@Slf4j
@RestController
public class PerformanceController {

    /**
     * 存储2000万条数据
     */
    public static final int SIZE = 5000000;
    /**
     * list集合存储数据
     */
    public static List<String> list = Lists.newArrayListWithCapacity(SIZE);
    /**
     * map集合存储数据
     */
    public static Map<String, Integer> map = Maps.newHashMapWithExpectedSize(SIZE);
    /**
     * guava 布隆过滤器
     */
    BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), SIZE);
    /**
     * 用来校验的集合
     */
    public static List<String> exist = Lists.newArrayList();
    /**
     * 计时工具类
     */
    public static Stopwatch stopwatch = Stopwatch.createUnstarted();

    /**
     * 初始化数据
     */
    @PostConstruct
    public void insertData() {
        for (int i = 0; i < SIZE; i++) {
            String data = UUID.randomUUID().toString();
            data = data.replace("-", "");
            //1、存入list
            list.add(data);
            //2、存入map
           map.put(data, 0);
            //3、存入本地布隆过滤器
            bloomFilter.put(data);
            //校验数据 相当于从这500万条数据,存储5条到这个集合中
            if (i % 1000000 == 0) {
                exist.add(data);
            }
        }
    }

    /**
     * 1、list 查看value是否存在 执行时间
     */
    @RequestMapping("/list")
    public void existsList() {
        //计时开始
        stopwatch.start();
        for (String s : exist) {
            if (list.contains(s)) {
                log.info("list集合存在该数据=============数据{}", s);
            }
        }
        //计时结束
        stopwatch.stop();
        log.info("list集合测试，判断该元素集合中是否存在用时:{}", stopwatch.elapsed(MILLISECONDS));
        stopwatch.reset();
    }

    /**
     * 2、查看map 判断k值是否存在 执行时间
     */
    @RequestMapping("/map")
    public void existsMap() {
        //计时开始
        stopwatch.start();
        for (String s : exist) {
            if (map.containsKey(s)) {
                log.info("map集合存在该数据=============数据{}", s);
            }
        }
        //计时结束
        stopwatch.stop();
        //获取时间差

        log.info("map集合测试，判断该元素集合中是否存在用时:{}", stopwatch.elapsed(MILLISECONDS));
        stopwatch.reset();
    }

    /**
     * 3、查看guava布隆过滤器 判断value值是否存在 执行时间
     */
    @RequestMapping("/bloom")
    public void existsBloom() {
        //计时开始
        stopwatch.start();
        for (String s : exist) {
        if (bloomFilter.mightContain(s)) {
            log.info("guava布隆过滤器存在该数据=============数据{}", s);
        }
        }
        //计时结束
        stopwatch.stop();
        //获取时间差
        log.info("bloom集合测试，判断该元素集合中是否存在用时:{}", stopwatch.elapsed(MILLISECONDS));
        stopwatch.reset();
    }


}
