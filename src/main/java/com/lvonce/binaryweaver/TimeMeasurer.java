package com.lvonce.binaryweaver;

import java.util.Map;
import java.text.Format;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeMeasurer {
    private static final ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(TimeMeasurer.class);
    private static final int printThreshold = 1000;
    private class MethodStatistic {
        private Long invokeCount = 0L;
        private Long averageTimeCost = 0L;
        private Long maximunTimeCost = 0L;
        private String maximumTimeArgs = null;

        private Long beginTime;
        private Object[] invokeArgs;

        public String toString() {
            return String.format("{invokeCount:%s, averageTimeCost:%s ns, maximumTimeCost:%s ns, maximumTimeArgs:%s}",
                    invokeCount, averageTimeCost, maximunTimeCost, maximumTimeArgs);
        }

        public void invokeBegin(Object... args) {
            this.beginTime = System.nanoTime();
            this.invokeArgs = args;
        }

        public void invokeEnd() {
            long timeCost = System.nanoTime() - this.beginTime;
            this.invokeCount++;
            this.averageTimeCost = this.averageTimeCost + (timeCost - this.averageTimeCost) / (this.invokeCount);
            if (timeCost > this.maximunTimeCost) {
                this.maximunTimeCost = timeCost;
                try {
                    this.maximumTimeArgs = mapper.writeValueAsString(this.invokeArgs);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    this.maximumTimeArgs = "-- json format error --";
                }
            }
        }
    }

    private final Class<?> ownerClass;
    private Long classInvokeCount;
    private final Map<String, MethodStatistic> statisticMap;

    public TimeMeasurer(Class<?> clazz) {
        this.ownerClass = clazz;
        this.classInvokeCount = 0L;
        this.statisticMap = new LinkedHashMap<String, MethodStatistic>();
    }

    private MethodStatistic getMethodStatistic(String methodName) {
        if (!this.statisticMap.containsKey(methodName)) {
            this.statisticMap.put(methodName, new MethodStatistic());
        }
        return this.statisticMap.get(methodName);
    }

    public void begin(String methodName, Object... args) {
        this.getMethodStatistic(methodName).invokeBegin(args);
    }

    public void end(String methodName) {
        this.classInvokeCount++;
        this.getMethodStatistic(methodName).invokeEnd();
        if (this.classInvokeCount >= printThreshold) {
            printStatisticInfo();
            this.classInvokeCount = 0L;
        }
    }

    private void printStatisticInfo() {
        logger.info("printStatisticInfo", "---------------------------");
        for (Map.Entry<String, MethodStatistic> entry : this.statisticMap.entrySet()) {
            logger.info("printStatisticInfo", entry.getValue().toString());
        }
        logger.info("printStatisticInfo", "---------------------------");
    }

}