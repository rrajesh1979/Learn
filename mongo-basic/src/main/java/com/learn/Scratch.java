package com.learn;

import java.util.ArrayList;
import java.util.Arrays;

public class Scratch {
    public static void main(String[] args) {
        learningArrays();
    }

    public static void learningArrays() {
        ArrayList<String> cast = new ArrayList<>();
        cast.add("Elon Musk");
        cast.add("Robert Redford");
        cast.add("Julia Roberts");

        System.out.println(Arrays.toString(cast.toArray(new String[0])));
        System.out.println(cast.toArray(new String[0]));
        System.out.println(cast.toArray());
        System.out.println(cast);
    }
}
