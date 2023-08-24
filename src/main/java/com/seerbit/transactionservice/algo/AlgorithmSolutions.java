package com.seerbit.transactionservice.algo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 11:23 PM
 */
public class AlgorithmSolutions {
    // The main function that takes a set of intervals, merges
    // overlapping intervals and prints the result
    public static Integer mergeIntervals(Interval[] arr) {
        int result = 0;
        // Check if the given set has at least one interval
        if (arr.length <= 0) {
            return 0;
        }

        // Create an empty stack of intervals
        Stack<Interval> stack = new Stack<>();

        // sort the intervals in increasing order of start time
        Arrays.sort(arr, Comparator.comparingInt(i -> i.start));

        // push the first interval to stack
        stack.push(arr[0]);

        // Start from the next interval and merge if necessary
        for (int i = 1; i < arr.length; i++) {
            // get interval from stack top
            Interval top = stack.peek();

            // if current interval is not overlapping with stack top, push it to the stack
            if (top.end < arr[i].start) {
                stack.push(arr[i]);
            } else if (top.end < arr[i].end) {
                // Otherwise update the ending time of top if ending of current interval is more
                top.end = arr[i].end;
                stack.pop();
                stack.push(top);
            }
        }

        // Print contents of stack
        System.out.print("The Merged Intervals are: ");
        while (!stack.isEmpty()) {
            Interval interval = stack.pop();
            System.out.print("[" + interval.start + "," + interval.end + "] ");
            result = interval.start + interval.end;
        }
        return result;

    }

    public static void main(String[] args) {
        Interval[] intervals = new Interval[4];
        intervals[0] = new Interval(6, 8);
        intervals[1] = new Interval(1, 9);
        intervals[2] = new Interval(2, 4);
        intervals[3] = new Interval(4, 7);
        Integer result = mergeIntervals(intervals);
        System.out.println("result: " + result);
    }
}

