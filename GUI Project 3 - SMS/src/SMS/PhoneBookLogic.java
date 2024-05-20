package SMS;

import java.util.ArrayList;

public class PhoneBookLogic {
    public static ArrayList<Integer> recipientBook = new ArrayList<Integer>();
    public static ArrayList <Integer> phoneBook = new ArrayList<Integer>(); // Zawiera wszystkie numery telefonów VBD i VRD

    public static int StationsCounter = 1;

    public static int getRandomRecipient () {
        if (recipientBook.size() == 0) {
//            throw new NullRecipentException();
            return 0;
        }
        else {
            return recipientBook.get((int) (Math.random() * recipientBook.size()));

        }
    }

    public static int generateNumber() {
        String number = "";
        for (int i = 0; i < 9; i++) {
            number += (int) (Math.random() * 10);
        }
        if (phoneBook.contains(number)) {
            generateNumber();
        }
        return Integer.parseInt(number);
    }


}
