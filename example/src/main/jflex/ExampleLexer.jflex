// SPDX-License-Identifier: Apache-2.0

package com.example;

%%

%class ExampleLexer
%type Number

Number = [0-9]+

%%

{Number} {
    return Long.valueOf(yytext());
}

[^] {
    throw new java.io.IOException("Invalid character: " + yycharat(0));
}
