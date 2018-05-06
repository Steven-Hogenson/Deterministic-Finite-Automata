import java.io.*;
import java.util.*;

public class DFA {
    /**
     * Various ArrayLists used for storing the information for this assignment.
     */
    static ArrayList<String> inputStrings = new ArrayList<String>();
    static ArrayList<String> states = new ArrayList<String>();
    static ArrayList<String> alphabet = new ArrayList<String>();
    static ArrayList<String> startingState = new ArrayList<String>();
    static ArrayList<String> finalStates = new ArrayList<String>();
    static ArrayList<String> delta = new ArrayList<String>();
    static ArrayList<String> deltaFrom = new ArrayList<String>();
    static ArrayList<String> deltaChar = new ArrayList<String>();
    static ArrayList<String> deltaTo = new ArrayList<String>();
    static Map<String, Map<String, String>> transitions = new TreeMap<String, Map<String, String>>();
    static boolean acceptedNull = false;
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the DFA file location, string file location, and desired output name/location (all one line, separated by spaces):");
        String input = sc.nextLine();
        input = input.trim();
        String inputSplit[] = input.split(" ");
        trimFile(inputSplit[0]);
        File f = new File("DFA_Trim.txt");
        readFile("DFA_Trim.txt");
        f.delete();
        addInputStringsToList(inputSplit[1]);
        addStates();
        addAlphabet();
        addStartingState();
        addFinalStates();
        addDelta();
        for (int i = 0; i < deltaTo.size(); i++) {
            if (!transitions.containsKey(deltaFrom.get(i))) {
                transitions.put(deltaFrom.get(i), new TreeMap<String, String>());
            }
            transitions.get(deltaFrom.get(i)).put(deltaChar.get(i), deltaTo.get(i));
        }
        writeOutput(inputSplit[2]);
    }

    /**
     * Trims the input DFA file to remove all empty lines and excess spaces. The
     * trimmed file is written to a new file named "DFA_Trim.txt". This trimmed
     * file will be read by the readFile method.
     *
     * @param file The string name of the desired file to trim
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void trimFile(String file) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter("DFA_Trim.txt");
        String line;
        while ((line = br.readLine()) != null) {
            line = line.replaceAll(" ", "");
            line = line.trim();
            if (!line.equals("")) {
                fw.write(line, 0, line.length());
            }
        }
        fr.close();
        fw.close();
    }

    /**
     * Reads the trimmed DFA file and splits the string by "=". Each element of
     * the string array is designated to the corresponding ArrayList. The
     * elements in each ArrayLists are refined in the add...() methods below.
     *
     * @param file The string name of the desired file to read
     * @throws IOException
     */
    public static void readFile(String file) throws IOException {
        BufferedReader buff = new BufferedReader(new FileReader(file));
        String readBuff = buff.readLine();
        String[] split = readBuff.split("=");
        while (readBuff != null) {
            states.add(split[2]);
            alphabet.add(split[3]);
            startingState.add(split[4]);
            finalStates.add(split[5]);
            delta.add(split[6]);
            readBuff = buff.readLine();
        }
        buff.close();
    }

    public static void addInputStringsToList(String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line != null) {
            line = line.trim();
            inputStrings.add(line);
            line = br.readLine();
        }
        for (int i = 0; i < inputStrings.size(); i++) {
            if (inputStrings.get(i).equals("")) {
                inputStrings.remove(i);
            }
        }
        br.close();
    }

    /**
     *
     * @param s
     * @throws IOException
     */
    static void writeOutput(String s) throws IOException {
        FileWriter fw = new FileWriter(s);
        for (int i = 0; i < inputStrings.size(); i++) {
            fw.write(test(inputStrings.get(i) + "\r\n"));
            System.out.println(test(inputStrings.get(i)));
        }
        fw.close();
    }

    /**
     * Adds the states present in the text file into the "states" ArrayList.
     * This allows easy access to individual states. Trims the excess text, like
     * {'s and }'s, and splits based on commas.
     */
    static void addStates() {
        String x = states.get(0).substring(0, states.get(0).indexOf("},"));
        x = x.replace("{", "");
        String statesF[] = x.split(",");
        states.clear();
        for (int i = 0; i < statesF.length; i++) {
            states.add(statesF[i]);
        }
    }

    /**
     * Adds the alphabet elements present in the text file into the "alphabet"
     * ArrayList. This allows easy access to individual members of the alphabet.
     * Trims the excess text, like {'s and }'s, and splits based on commas.
     */
    static void addAlphabet() {
        String x = alphabet.get(0).substring(0, alphabet.get(0).indexOf("},"));
        x = x.replace("{", "");
        String alphaF[] = x.split(",");
        alphabet.clear();
        for (int i = 0; i < alphaF.length; i++) {
            alphabet.add(alphaF[i]);
        }
    }

    /**
     * Adds the starting state present in the text file into the "starting
     * state" ArrayList. This allows easy access to the starting state for
     * checking delta. Trims the excess text, like {'s and }'s, and splits based
     * on commas.
     */
    static void addStartingState() {
        String x = startingState.get(0).substring(0, startingState.get(0).indexOf(","));
        startingState.clear();
        startingState.add(x);
    }

    /**
     * Adds the set of final states present in the text file into the "final
     * states" ArrayList. This allows easy access to the final states to see if
     * a string is accepted. Trims the excess text, like {'s and }'s, and splits
     * based on commas.
     */
    static void addFinalStates() {
        String x = finalStates.get(0).substring(0, finalStates.get(0).indexOf("},"));
        x = x.replace("{", "");
        String finalStatesF[] = x.split(",");
        finalStates.clear();
        for (int i = 0; i < finalStatesF.length; i++) {
            finalStates.add(finalStatesF[i]);
        }
    }

    static void addDelta() {
        String x = delta.get(0).substring(0, delta.get(0).indexOf("}"));
        x = x.replace("{", "");
        x = x.replace("(", "");
        x = x.replace(")", "");
        String deltaF[] = x.split(",");
        delta.clear();
        for (int i = 0; i < deltaF.length; i++) {
            delta.add(deltaF[i]);
        }
        for (int i = 0; i < deltaF.length; i += 3) {
            deltaFrom.add(deltaF[i]);
        }
        for (int i = 1; i < deltaF.length; i += 3) {
            deltaChar.add(deltaF[i]);
        }
        for (int i = 2; i < deltaF.length; i += 3) {
            deltaTo.add(deltaF[i]);
        }
    }

    public static boolean match(String s) {
        String state = startingState.get(0);
        for (int i = 0; i < s.length(); i++) {
            String character = s.charAt(i) + "";
            if (!transitions.get(state).containsKey(character)) {
                return false;
            }
            state = transitions.get(state).get(character);
        }
        return finalStates.contains(state);
    }

    private static String test(String input) {
        try {
            if (match(input)) {
                return input + " is accepted.";
            }
        } catch (NullPointerException npe) {
            acceptedNull = true;
        }
        if (acceptedNull) {
            acceptedNull = false;
            return input + " is accepted.";

        } else {
            return input + " is rejected.";
        }
    }
}
