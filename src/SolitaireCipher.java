public class SolitaireCipher {

    public Deck key;

    public SolitaireCipher (Deck key) {
        this.key = new Deck(key); // deep copy of the deck
    }

    public int[] getKeystream(int size) {

        // Creating an array keystream with the input size
        int[] keystream = new int[size];

        // Looping size times
        for (int i=0; i < size; i++) {

            // Assigning keystream's ith position its next keystream value
            keystream[i] = key.generateNextKeystreamValue();
        }
        return keystream;
    }

    public String encode(String msg) {

        // Removing non-letter characters and capitalizing all letters from msg
        msg = msg.replaceAll("[^a-zA-Z]", "");
        msg = msg.toUpperCase();

        // Creating variables: an array keystream and a string cipherText will contain the ciphertext
        int[] keystream = this.getKeystream(msg.length());
        String cipherText = "";

        // Looping through each letter of msg
        for (int i=0; i < msg.length(); i++) {

            // Creating a variable letter that will store the letter's number/position
            int letter = msg.charAt(i);

            // Looping the letter's keystream's number of position
            for (int j=0; j < keystream[i]; j++) {

                // If letter is 90, that means it reached the letter Z. letter becomes 64 aka A-1
                if (letter == 90) {
                    letter = 64;
                }
                // Incrementing letter's number/position by 1
                letter++;
            }
            // Adding the new letter into cipherText
            cipherText += (char) (letter);
        }
        return cipherText;
    }

    public String decode(String msg) {

        // Creating variables: an array keystream and a string cipherText will contain the ciphertext
        int[] keystream = this.getKeystream(msg.length());
        String cipherText = "";

        // Looping through each letter of msg
        for (int i=0; i < msg.length(); i++) {

            // Creating a variable letter that will store the letter's number/position
            int letter = msg.charAt(i);

            // Looping the letter's keystream's number of position
            for (int j=0; j < keystream[i]; j++) {

                // If letter is 65, that means it reached the letter A. letter becomes 91 aka Z+1
                if (letter == 65) {
                    letter = 91;
                }

                // Decrementing letter's number/position by 1
                letter--;
            }
            // Adding the new letter into cipherText
            cipherText += (char) (letter);
        }
        return cipherText;
    }
}