package com.lvonce.binaryweaver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeMeasurerFactory {
    //private static final Map<Class<?>, TimeMeasurer> measurerMap = new ConcurrentHashMap<>();
    private static final ThreadLocal<LinkedHashMap<Class<?>, TimeMeasurer>> localMeasurerMap = new ThreadLocal<LinkedHashMap<Class<?>, TimeMeasurer>>() {
        @Override
        protected LinkedHashMap<Class<?>, TimeMeasurer> initialValue() {
            return new LinkedHashMap<Class<?>, TimeMeasurer>();
        };
    };

    public static TimeMeasurer getTimeMeasurer(Class<?> clazz) {
        LinkedHashMap<Class<?>, TimeMeasurer> measurerMap = localMeasurerMap.get();
        if (!measurerMap.containsKey(clazz)) {
            measurerMap.put(clazz, new TimeMeasurer(clazz));
        }
        return measurerMap.get(clazz);
    }
}