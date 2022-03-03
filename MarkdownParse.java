
// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkdownParse {
    public static Map<String, List<String>> getLinks(File dirOrFile) throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        if(dirOrFile.isDirectory()) {
            for(File f: dirOrFile.listFiles()) {
                result.putAll(getLinks(f));
            }
            return result;
        }
        else {
            Path p = dirOrFile.toPath();
            int lastDot = p.toString().lastIndexOf(".");
            if(lastDot == -1 || !p.toString().substring(lastDot).equals(".md")) {
                return result;
            }
            ArrayList<String> links = getLinks(Files.readString(p));
            result.put(dirOrFile.getPath(), links);
            return result;
        }
    }

    public static ArrayList<String> getLinks(String markdown) {
        ArrayList<String> toReturn = new ArrayList<>();
        // find the next [, then find the ], then find the (, then take up to
        // the next )
        int currentIndex = 0;
        int pastCloseParen = 0;
        while (currentIndex < markdown.length()) {
            int nextOpenBracket = markdown.indexOf("[", currentIndex);
            // System.out.println("Value of current index before loop: " + currentIndex);
            int nextCloseBracket = markdown.indexOf("]", nextOpenBracket);
            int openParen = markdown.indexOf("(", nextCloseBracket);
            int closeParen = markdown.indexOf(")", openParen);
            
            if (pastCloseParen == closeParen || nextOpenBracket == -1 || nextCloseBracket == -1 || openParen == -1
                    || closeParen == -1) {
                break;
            }
            pastCloseParen = closeParen;

            // System.out.println("Index of next open bracket: " + nextOpenBracket);
            // System.out.println("Index of next open bracket - 1: " + (nextOpenBracket -
            // 1));

            if (openParen == nextCloseBracket + 1 && nextCloseBracket != nextOpenBracket + 1) {

                if (!markdown.substring(openParen + 1, closeParen).contains("\n")) {
                    if (nextOpenBracket != 0) {
                        if (!markdown.substring(nextOpenBracket - 1, nextOpenBracket).equals("!")) {
                            toReturn.add(markdown.substring(openParen + 1, closeParen));

                        }
                        currentIndex = closeParen + 1;
                    } else {
                        toReturn.add(markdown.substring(openParen + 1, closeParen));
                        currentIndex = closeParen + 1;
                    }

                } else {
                    // System.out.println(markdown.indexOf("\n", openParen));
                    currentIndex = markdown.indexOf("\n", openParen) - 1;
                    pastCloseParen = 0;
                }
            }
            // System.out.println("Value of current index after loop: " + currentIndex);
        }
        return toReturn;
    }

    public static void main(String[] args) throws IOException {
        // Path fileName = Path.of(args[0]);
        // String contents = Files.readString(fileName);
        // ArrayList<String> links = getLinks(contents);
        // System.out.println(links);
        Path fileName = Path.of(args[0]);
        File dirOrFile = new File(args[0]);
        if (!dirOrFile.isDirectory()) {
            String contents = Files.readString(fileName);
            ArrayList<String> links = getLinks(contents);
            System.out.println(links);
        } else {
            Map<String, List<String>>links = getLinks(dirOrFile);
            System.out.println(links);
        }
    }
}