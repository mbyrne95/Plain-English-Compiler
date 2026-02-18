package PlainEnglish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Lexer2Tests {

    @Test
    public void TestNumbersAndStrings() throws Exception {
        var code = "To Run\n"+
                "    Make number named m\n"+
                "    Make number named n\n"+
                "    Make number named ans\n"+
                "    Set m to 9\n"+
                "    Set n to 3\n"+
                "    Math with ans, m, n, \"add\"\n"+
                "    Print with \"9 + 3:\"\n"+
                "    Print with ans\n"+
                "    Print with \"----------\"\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("Run",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(3).Type);
        Assertions.assertEquals(Token.TokenTypes.MAKE, tokens.get(4).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(5).Type);
        Assertions.assertEquals("number",  tokens.get(5).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(6).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(7).Type);
        Assertions.assertEquals("m",  tokens.get(7).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(8).Type);
        Assertions.assertEquals(Token.TokenTypes.MAKE, tokens.get(9).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(10).Type);
        Assertions.assertEquals("number",  tokens.get(10).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(11).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(12).Type);
        Assertions.assertEquals("n",  tokens.get(12).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(13).Type);
        Assertions.assertEquals(Token.TokenTypes.MAKE, tokens.get(14).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(15).Type);
        Assertions.assertEquals("number",  tokens.get(15).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(16).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(17).Type);
        Assertions.assertEquals("ans",  tokens.get(17).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(18).Type);
        Assertions.assertEquals(Token.TokenTypes.SET, tokens.get(19).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(20).Type);
        Assertions.assertEquals("m",  tokens.get(20).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(21).Type);
        Assertions.assertEquals(Token.TokenTypes.NUMBER, tokens.get(22).Type);
        Assertions.assertEquals("9",  tokens.get(22).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(23).Type);
        Assertions.assertEquals(Token.TokenTypes.SET, tokens.get(24).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(25).Type);
        Assertions.assertEquals("n",  tokens.get(25).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(26).Type);
        Assertions.assertEquals(Token.TokenTypes.NUMBER, tokens.get(27).Type);
        Assertions.assertEquals("3",  tokens.get(27).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(28).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(29).Type);
        Assertions.assertEquals("Math",  tokens.get(29).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(30).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(31).Type);
        Assertions.assertEquals("ans",  tokens.get(31).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(32).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(33).Type);
        Assertions.assertEquals("m",  tokens.get(33).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(34).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(35).Type);
        Assertions.assertEquals("n",  tokens.get(35).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(36).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(37).Type);
        Assertions.assertEquals("add",  tokens.get(37).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(38).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(39).Type);
        Assertions.assertEquals("Print",  tokens.get(39).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(40).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(41).Type);
        Assertions.assertEquals("9 + 3:",  tokens.get(41).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(42).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(43).Type);
        Assertions.assertEquals("Print",  tokens.get(43).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(44).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(45).Type);
        Assertions.assertEquals("ans",  tokens.get(45).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(46).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(47).Type);
        Assertions.assertEquals("Print",  tokens.get(47).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(48).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(49).Type);
        Assertions.assertEquals("----------",  tokens.get(49).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(50).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(51).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(52).Type);
    }

    @Test
    public void TestPunctuation() throws Exception {
        var code = "To Math with number named m, number named n, string named op\n"+
                "    If op == \"add\"\n"+
                "        Print with m + n\n"+
                "    Else\n"+
                "        If op == \"subtract\"\n"+
                "        /* We do not want a\n"+
                "            negative answer */\n"+
                "            If m >= n\n"+
                "                Print with m - n\n"+
                "            Else\n"+
                "                Print with n - m\n"+
                "        Else\n"+
                "            If op == \"multiply\"\n"+
                "                Print with m * n\n"+
                "            Else\n"+
                "                /* Prevent division by 0*/\n"+
                "                If op == \"divide\"\n"+
                "                    If n != 0\n"+
                "                        Print with m / n\n"+
                "                Else\n"+
                "                    If (op == \"mod\") or (op == \"remainder\")\n"+
                "                        If n < m\n"+
                "                            Print with m % n\n"+
                "                        Else\n"+
                "                            Print with n % m\n"+
                "                    Else\n"+
                "                        Print with \"INVALID OPERATION\"\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("Math",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(3).Type);
        Assertions.assertEquals("number",  tokens.get(3).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(4).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(5).Type);
        Assertions.assertEquals("m",  tokens.get(5).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(6).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(7).Type);
        Assertions.assertEquals("number",  tokens.get(7).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(8).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(9).Type);
        Assertions.assertEquals("n",  tokens.get(9).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(10).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(11).Type);
        Assertions.assertEquals("string",  tokens.get(11).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(12).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(13).Type);
        Assertions.assertEquals("op",  tokens.get(13).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(14).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(15).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(16).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(17).Type);
        Assertions.assertEquals("op",  tokens.get(17).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(18).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(19).Type);
        Assertions.assertEquals("add",  tokens.get(19).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(20).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(21).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(22).Type);
        Assertions.assertEquals("Print",  tokens.get(22).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(23).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(24).Type);
        Assertions.assertEquals("m",  tokens.get(24).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.PLUS, tokens.get(25).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(26).Type);
        Assertions.assertEquals("n",  tokens.get(26).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(27).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(28).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(29).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(30).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(31).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(32).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(33).Type);
        Assertions.assertEquals("op",  tokens.get(33).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(34).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(35).Type);
        Assertions.assertEquals("subtract",  tokens.get(35).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(36).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(37).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(38).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(39).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(40).Type);
        Assertions.assertEquals("m",  tokens.get(40).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.GREATERTHANEQUAL, tokens.get(41).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(42).Type);
        Assertions.assertEquals("n",  tokens.get(42).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(43).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(44).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(45).Type);
        Assertions.assertEquals("Print",  tokens.get(45).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(46).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(47).Type);
        Assertions.assertEquals("m",  tokens.get(47).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.HYPHEN, tokens.get(48).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(49).Type);
        Assertions.assertEquals("n",  tokens.get(49).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(50).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(51).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(52).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(53).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(54).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(55).Type);
        Assertions.assertEquals("Print",  tokens.get(55).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(56).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(57).Type);
        Assertions.assertEquals("n",  tokens.get(57).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.HYPHEN, tokens.get(58).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(59).Type);
        Assertions.assertEquals("m",  tokens.get(59).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(60).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(61).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(62).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(63).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(64).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(65).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(66).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(67).Type);
        Assertions.assertEquals("op",  tokens.get(67).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(68).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(69).Type);
        Assertions.assertEquals("multiply",  tokens.get(69).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(70).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(71).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(72).Type);
        Assertions.assertEquals("Print",  tokens.get(72).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(73).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(74).Type);
        Assertions.assertEquals("m",  tokens.get(74).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.ASTERISK, tokens.get(75).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(76).Type);
        Assertions.assertEquals("n",  tokens.get(76).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(77).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(78).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(79).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(80).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(81).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(82).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(83).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(84).Type);
        Assertions.assertEquals("op",  tokens.get(84).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(85).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(86).Type);
        Assertions.assertEquals("divide",  tokens.get(86).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(87).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(88).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(89).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(90).Type);
        Assertions.assertEquals("n",  tokens.get(90).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NOTEQUAL, tokens.get(91).Type);
        Assertions.assertEquals(Token.TokenTypes.NUMBER, tokens.get(92).Type);
        Assertions.assertEquals("0",  tokens.get(92).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(93).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(94).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(95).Type);
        Assertions.assertEquals("Print",  tokens.get(95).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(96).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(97).Type);
        Assertions.assertEquals("m",  tokens.get(97).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.SLASH, tokens.get(98).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(99).Type);
        Assertions.assertEquals("n",  tokens.get(99).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(100).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(101).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(102).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(103).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(104).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(105).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(106).Type);
        Assertions.assertEquals(Token.TokenTypes.OPENPAREN, tokens.get(107).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(108).Type);
        Assertions.assertEquals("op",  tokens.get(108).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(109).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(110).Type);
        Assertions.assertEquals("mod",  tokens.get(110).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.CLOSEPAREN, tokens.get(111).Type);
        Assertions.assertEquals(Token.TokenTypes.OR, tokens.get(112).Type);
        Assertions.assertEquals(Token.TokenTypes.OPENPAREN, tokens.get(113).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(114).Type);
        Assertions.assertEquals("op",  tokens.get(114).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.DOUBLEEQUAL, tokens.get(115).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(116).Type);
        Assertions.assertEquals("remainder",  tokens.get(116).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.CLOSEPAREN, tokens.get(117).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(118).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(119).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(120).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(121).Type);
        Assertions.assertEquals("n",  tokens.get(121).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.LESSTHAN, tokens.get(122).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(123).Type);
        Assertions.assertEquals("m",  tokens.get(123).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(124).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(125).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(126).Type);
        Assertions.assertEquals("Print",  tokens.get(126).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(127).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(128).Type);
        Assertions.assertEquals("m",  tokens.get(128).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.PERCENT, tokens.get(129).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(130).Type);
        Assertions.assertEquals("n",  tokens.get(130).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(131).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(132).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(133).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(134).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(135).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(136).Type);
        Assertions.assertEquals("Print",  tokens.get(136).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(137).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(138).Type);
        Assertions.assertEquals("n",  tokens.get(138).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.PERCENT, tokens.get(139).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(140).Type);
        Assertions.assertEquals("m",  tokens.get(140).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(141).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(142).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(143).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(144).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(145).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(146).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(147).Type);
        Assertions.assertEquals("Print",  tokens.get(147).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(148).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(149).Type);
        Assertions.assertEquals("INVALID OPERATION",  tokens.get(149).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(150).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(151).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(152).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(153).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(154).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(155).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(156).Type);
    }

    @Test
    public void TestComparison() throws Exception {
        var code = "To Counter with number named start, number named end\n"+
                "    Loop start <= end\n"+
                "        Set start to start + 1\n"+
                "\n"+
                "    If start > end\n"+
                "        Print with \"Went over\"\n"+
                "    Else\n"+
                "        Print with \"Just right\"\n"+
                "";
        var tokens = new Lexer(code).lex();
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(0).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(1).Type);
        Assertions.assertEquals("Counter",  tokens.get(1).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(2).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(3).Type);
        Assertions.assertEquals("number",  tokens.get(3).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(4).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(5).Type);
        Assertions.assertEquals("start",  tokens.get(5).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.COMMA, tokens.get(6).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(7).Type);
        Assertions.assertEquals("number",  tokens.get(7).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NAMED, tokens.get(8).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(9).Type);
        Assertions.assertEquals("end",  tokens.get(9).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(10).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(11).Type);
        Assertions.assertEquals(Token.TokenTypes.LOOP, tokens.get(12).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(13).Type);
        Assertions.assertEquals("start",  tokens.get(13).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.LESSTHANEQUAL, tokens.get(14).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(15).Type);
        Assertions.assertEquals("end",  tokens.get(15).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(16).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(17).Type);
        Assertions.assertEquals(Token.TokenTypes.SET, tokens.get(18).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(19).Type);
        Assertions.assertEquals("start",  tokens.get(19).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.TO, tokens.get(20).Type);
        Assertions.assertEquals(3, tokens.get(20).LineNumber);
        Assertions.assertEquals(20, tokens.get(20).ColumnNumber);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(21).Type);
        Assertions.assertEquals("start",  tokens.get(21).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.PLUS, tokens.get(22).Type);
        Assertions.assertEquals(Token.TokenTypes.NUMBER, tokens.get(23).Type);
        Assertions.assertEquals("1",  tokens.get(23).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(24).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(25).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(26).Type);
        Assertions.assertEquals(Token.TokenTypes.IF, tokens.get(27).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(28).Type);
        Assertions.assertEquals("start",  tokens.get(28).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.GREATERTHAN, tokens.get(29).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(30).Type);
        Assertions.assertEquals("end",  tokens.get(30).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(31).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(32).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(33).Type);
        Assertions.assertEquals("Print",  tokens.get(33).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(34).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(35).Type);
        Assertions.assertEquals("Went over",  tokens.get(35).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(36).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(37).Type);
        Assertions.assertEquals(Token.TokenTypes.ELSE, tokens.get(38).Type);
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(39).Type);
        Assertions.assertEquals(Token.TokenTypes.INDENT, tokens.get(40).Type);
        Assertions.assertEquals(Token.TokenTypes.IDENTIFIER, tokens.get(41).Type);
        Assertions.assertEquals("Print",  tokens.get(41).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.WITH, tokens.get(42).Type);
        Assertions.assertEquals(Token.TokenTypes.STRINGLITERAL, tokens.get(43).Type);
        Assertions.assertEquals("Just right",  tokens.get(43).Value.orElseThrow());
        Assertions.assertEquals(Token.TokenTypes.NEWLINE, tokens.get(44).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(45).Type);
        Assertions.assertEquals(Token.TokenTypes.DEDENT, tokens.get(46).Type);
    }

    @Test
    public void TestIndentation() throws Exception {
        var code = "To Run\n"+
                "   Print with \"Indentation Error\"\n"+
                "";
        Assertions.assertThrowsExactly(SyntaxErrorException.class, () -> new Lexer(code).lex());
    }

}
