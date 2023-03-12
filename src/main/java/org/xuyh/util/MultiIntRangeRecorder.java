/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.util;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Recorder to record ranged regions like 1~3,5,6,8~12
 *
 * @author XuYanhang
 * @since 2023-03-13
 */
public class MultiIntRangeRecorder {
    private final SortedSet<Integer> values;

    /**
     * Create a instance.
     */
    public MultiIntRangeRecorder() {
        values = new TreeSet<>();
    }

    /**
     * Record a value.
     *
     * @param value value to record
     */
    public void record(int value) {
        values.add(value);
    }

    /**
     * Create ranges string.
     *
     * @return ranges string
     */
    public String toRangeString() {
        if (values.isEmpty()) {
            return "";
        }
        ArrayList<String> ranges = new ArrayList<>();
        int lower = 0;
        int upper = 0;
        boolean begin = false;
        for (Integer value : new TreeSet<>(values)) {
            if (!begin) {
                lower = value;
                upper = value;
                begin = true;
                continue;
            }
            if (upper + 1 == value) {
                upper = value;
                continue;
            }
            ranges.add(toRangeString(lower, upper));
            lower = value;
            upper = value;
        }
        ranges.add(toRangeString(lower, upper));
        return String.join(",", ranges);
    }

    /**
     * Create a continuous range string.
     *
     * @param lower the lower value
     * @param upper the upper value
     * @return range string
     */
    private static String toRangeString(int lower, int upper) {
        if (lower == upper) {
            return String.valueOf(lower);
        }
        char split = lower + 1 == upper ? ',' : '~';
        return String.valueOf(lower) + split + upper;
    }
}
