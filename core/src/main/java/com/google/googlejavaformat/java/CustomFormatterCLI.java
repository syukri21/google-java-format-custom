package com.google.googlejavaformat.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Unified CLI for all custom formatters. Usage: java ... CustomFormatterCLI <style>
 */
public class CustomFormatterCLI {
    public static void main(String[] args) {
        try {
            String source = new BufferedReader(new InputStreamReader(System.in))
                    .lines().collect(Collectors.joining("\n"));

            CustomFormatter formatter = new ProperClassFormatter(2);
            System.out.print(formatter.format(source));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}

