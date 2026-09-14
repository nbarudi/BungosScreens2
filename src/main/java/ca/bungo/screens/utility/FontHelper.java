package ca.bungo.screens.utility;

import java.util.Map;

public class FontHelper {

    private static final Map<Character, Integer> CHAR_WIDTH = Map.<Character, Integer>ofEntries(
            Map.entry(' ', 4),Map.entry('!', 2),Map.entry('"', 4),Map.entry('#', 6),
            Map.entry('$', 6),Map.entry('%', 6),Map.entry('&', 6),Map.entry('\'', 2),
            Map.entry('(', 4),Map.entry(')', 4),Map.entry('*', 4),Map.entry('+', 6),
            Map.entry('-', 6),Map.entry(',', 2),Map.entry('.', 2),Map.entry('/', 6),
            Map.entry('0', 6),Map.entry('1', 6),Map.entry('2', 6),Map.entry('3', 6),
            Map.entry('4', 6),Map.entry('5', 6),Map.entry('6', 6),Map.entry('7', 6),
            Map.entry('8', 6),Map.entry('9', 6),Map.entry(':', 2),Map.entry(';', 2),
            Map.entry('<', 5),Map.entry('=', 6),Map.entry('>', 5),Map.entry('?', 6),
            Map.entry('@', 7),Map.entry('A', 6),Map.entry('B', 6),Map.entry('C', 6),
            Map.entry('D', 6),Map.entry('E', 6),Map.entry('F', 6),Map.entry('G', 6),
            Map.entry('H', 6),Map.entry('I', 4),Map.entry('J', 6),Map.entry('K', 6),
            Map.entry('L', 6),Map.entry('M', 6),Map.entry('N', 6),Map.entry('O', 6),
            Map.entry('P', 6),Map.entry('Q', 6),Map.entry('R', 6),Map.entry('S', 6),
            Map.entry('T', 6),Map.entry('U', 6),Map.entry('V', 6),Map.entry('W', 6),
            Map.entry('X', 6),Map.entry('Y', 6),Map.entry('Z', 6),Map.entry('[', 4),
            Map.entry('\\', 6),Map.entry(']', 4),Map.entry('^', 6),Map.entry('_', 6),
            Map.entry('`', 3),Map.entry('a', 6),Map.entry('b', 6),Map.entry('c', 6),
            Map.entry('d', 6),Map.entry('e', 6),Map.entry('f', 5),Map.entry('g', 6),
            Map.entry('h', 6),Map.entry('i', 2),Map.entry('j', 6),Map.entry('k', 5),
            Map.entry('l', 3),Map.entry('m', 6),Map.entry('n', 6),Map.entry('o', 6),
            Map.entry('p', 6),Map.entry('q', 6),Map.entry('r', 6),Map.entry('s', 6),
            Map.entry('t', 4),Map.entry('u', 6),Map.entry('v', 6),Map.entry('w', 6),
            Map.entry('x', 6),Map.entry('y', 6),Map.entry('z', 6),Map.entry('{', 4),
            Map.entry('|', 2),Map.entry('}', 4),Map.entry('~', 7),Map.entry('£', 6),
            Map.entry('ª', 5),Map.entry('«', 7),Map.entry('¬', 6),Map.entry('°', 5),
            Map.entry('±', 6),Map.entry('²', 5),Map.entry('º', 5),Map.entry('»', 7),
            Map.entry('÷', 6),Map.entry('ƒ', 6),Map.entry('ⁿ', 5),Map.entry('∅', 8),
            Map.entry('∈', 6),Map.entry('∙', 3),Map.entry('√', 7),Map.entry('≈', 7),
            Map.entry('≡', 7),Map.entry('≤', 6),Map.entry('≥', 6),Map.entry('⌠', 5),
            Map.entry('⌡', 5),Map.entry('─', 9),Map.entry('│', 3),Map.entry('┌', 6),
            Map.entry('┐', 6),Map.entry('└', 6),Map.entry('┘', 6),Map.entry('├', 6),
            Map.entry('┤', 6),Map.entry('┬', 9),Map.entry('┴', 9),Map.entry('┼', 9),
            Map.entry('═', 9),Map.entry('║', 6),Map.entry('╒', 6),Map.entry('╓', 7),
            Map.entry('╔', 7),Map.entry('╕', 6),Map.entry('╖', 8),Map.entry('╗', 8),
            Map.entry('╘', 6),Map.entry('╙', 7),Map.entry('╚', 7),Map.entry('╛', 6),
            Map.entry('╜', 8),Map.entry('╝', 8),Map.entry('╞', 6),Map.entry('╟', 7),
            Map.entry('╠', 7),Map.entry('╡', 6),Map.entry('╢', 8),Map.entry('╣', 8),
            Map.entry('╤', 9),Map.entry('╥', 9),Map.entry('╦', 9),Map.entry('╧', 9),
            Map.entry('╨', 9),Map.entry('╩', 9),Map.entry('╪', 9),Map.entry('╫', 9),
            Map.entry('╬', 9),Map.entry('▀', 9),Map.entry('▄', 9),Map.entry('█', 9),
            Map.entry('▌', 5),Map.entry('▐', 5),Map.entry('░', 8),Map.entry('▒', 9),
            Map.entry('▓', 9),Map.entry('■', 6)
    );
    private static final int DEFAULT_WIDTH = 6; // fallback for anything not in the table

    public static float measureWidth(String text) {
        float total = 0;
        for (char c : text.toCharArray()) {
            total += CHAR_WIDTH.getOrDefault(c, DEFAULT_WIDTH);
        }
        return total;// * (float) TextDisplayMetrics.PIXELS_TO_WORLD; // if you need world units, not raw pixels
    }

}
