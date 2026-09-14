// SPDX-License-Identifier: Apache-2.0

package com.example;

import java.io.IOException;
import java.io.StringReader;

public final class Example {
    public static void main(String[] args) throws IOException {
        var number = new ExampleLexer(new StringReader(args[0])).yylex();
        System.out.println("Number: " + number);
    }
}
