package com.github.johantiden.adventofcode2019;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Seven {


    public static void main(String[] args) {




    }

    static int findBest(int[] program) {
        int max = -1;

        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                for (int c = 0; c < 5; c++) {
                    for (int d = 0; d < 5; d++) {
                        for (int e = 0; e < 5; e++) {
                            ArrayList<Integer> phases = Lists.newArrayList(a, b, c, d, e);
                            if (isValid(phases)) {
                                int output = calculateValueForPhase(program, phases);
                                if (output > max) {
                                    max = output;
                                }
                            }
                        }
                    }
                }
            }
        }

        return max;

    }

    static int findBestPartTwo(int[] program) throws InterruptedException {

        int max = -1;

        for (int a = 5; a < 10; a++) {
            for (int b = 5; b < 10; b++) {
                for (int c = 5; c < 10; c++) {
                    for (int d = 5; d < 10; d++) {
                        for (int e = 5; e < 10; e++) {
                            ArrayList<Integer> phases = Lists.newArrayList(a, b, c, d, e);
                            if (isValid(phases)) {
                                int output = calculateValueForPhaseWithFeedbackLoop(program, phases);
                                if (output > max) {
                                    max = output;
                                }
                            }
                        }
                    }
                }
            }
        }

        return max;

    }

    private static boolean isValid(ArrayList<Integer> phases) {
        Set<Integer> set = new HashSet<>(phases);
        return set.size() == phases.size(); //if there are non-unique elements, the size of the set will be smaller.
    }


    private static int calculateValueForPhase(int[] program, List<Integer> phases) {

        Pipe input = new Pipe();
        Pipe ab = new Pipe();
        Pipe bc = new Pipe();
        Pipe cd = new Pipe();
        Pipe de = new Pipe();

        input.write(phases.get(0));
        ab.write(phases.get(1));
        bc.write(phases.get(2));
        cd.write(phases.get(3));
        de.write(phases.get(4));

        input.write(0);

        AtomicInteger outputValue = new AtomicInteger();
        IntcodeComputer.Output output = outputValue::set;

        IntcodeComputer a = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), input, ab);
        a.run();
        IntcodeComputer b = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), ab, bc);
        b.run();
        IntcodeComputer c = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), bc, cd);
        c.run();
        IntcodeComputer d = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), cd, de);
        d.run();
        IntcodeComputer e = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), de, output);
        e.run();

        return outputValue.get();
    }

    private static int calculateValueForPhaseWithFeedbackLoop(int[] program, List<Integer> phases) throws InterruptedException {

        Pipe ab = new Pipe();
        Pipe bc = new Pipe();
        Pipe cd = new Pipe();
        Pipe de = new Pipe();
        Pipe ea = new Pipe();

        ea.write(phases.get(0));
        ab.write(phases.get(1));
        bc.write(phases.get(2));
        cd.write(phases.get(3));
        de.write(phases.get(4));

        ea.write(0);

        IntcodeComputer a = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), ea, ab);
        IntcodeComputer b = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), ab, bc);
        IntcodeComputer c = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), bc, cd);
        IntcodeComputer d = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), cd, de);
        IntcodeComputer e = new IntcodeComputer(new IntcodeComputer.Memory(program.clone()), de, ea);

        new Thread(d::run).start();
        new Thread(c::run).start();
        new Thread(b::run).start();
        new Thread(a::run).start();
        e.run(); // blocking

        int output = ea.read();
        return output;
    }


    static class Pipe implements IntcodeComputer.Input, IntcodeComputer.Output{

        private final BlockingQueue<Integer> buffer = new LinkedBlockingQueue<>();

        @Override
        public int read() throws InterruptedException {
            return buffer.take();
        }

        @Override
        public void write(int output) {
            buffer.offer(output);
        }
    }
}