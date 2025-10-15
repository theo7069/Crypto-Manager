public class CryptoManager {
    private static final char LOWER_RANGE = ' ';   // ASCII 32
    private static final char UPPER_RANGE = '_';   // ASCII 95
    private static final int RANGE = UPPER_RANGE - LOWER_RANGE + 1; // 64 symbols
    private static final String ALPHABET64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_";

    public static boolean isStringInBounds (String plainText){
        if(plainText == null){
            return false;
        }
        for(int i = 0; i < plainText.length(); i++){
            if(plainText.charAt(i) < LOWER_RANGE || plainText.charAt(i) > UPPER_RANGE){
                return false;
            }
        }
        return true;
    }

    public static String caesarEncryption(String plainText, int key){
        if (!isStringInBounds(plainText))
            return "The selected string is not in bounds, Try again";
        String encrypted_text = "";
        for(int i = 0;i < plainText.length();i++){
            char c = plainText.charAt(i);
            int new_c = (int)c + key;
            while (new_c > 95) {
                new_c = 32 + (new_c - 96);
            }
            char new_char = (char)new_c;
            encrypted_text += new_char;

        }
        return encrypted_text;

    }
    public static String caesarDecryption(String encryptedText, int key){
        if (!isStringInBounds(encryptedText))
            return "The selected string is not in bounds, Try again";
        String plainText = "";
        for(int i = 0;i < encryptedText.length();i++){
            char c = encryptedText.charAt(i);
            int new_c = (int)c - key;
            while (new_c < 32) {
                new_c = 95 + (33 - new_c);
            }
            char new_char = (char)new_c;
            plainText += new_char;

        }
        return plainText;
    }
    public static String vigenereEncryption(String plainText, String key){
        if (!isStringInBounds(plainText))
            return "The selected string is not in bounds, Try again";
        String encryptedText = "";
        int[] nums = new int[key.length()];
        for(int i = 0; i < key.length(); i ++){
            int x = (int)key.charAt(i) - 32;
            nums[i] = x;
        }
        int count = 0;
        for(int i = 0; i < plainText.length();i++){
            int y = (int)plainText.charAt(i);
            y += nums[count];
            while(y > 95){
                y = 32 + (y - 96);
            }
            char new_char = (char)y;
            encryptedText += new_char;
            if(count == key.length() - 1){
                count = 0;
            }
            else{
                count++;
            }

        }
        return encryptedText;
    }
    public static String vigenereDecryption(String encryptedText, String key) {
        if (!isStringInBounds(encryptedText))
            return "The selected string is not in bounds, Try again";

        String plainText = "";
        int[] nums = new int[key.length()];

        // Convert key characters into shifts
        for (int i = 0; i < key.length(); i++) {
            int x = (int) key.charAt(i) - 32;
            nums[i] = x;
        }

        int count = 0;
        for (int i = 0; i < encryptedText.length(); i++) {
            int y = (int) encryptedText.charAt(i);
            y -= nums[count]; // subtract the key shift

            // Wrap below 32
            while (y < 32) {
                y = 95 - (31 - y);
            }

            char new_char = (char) y;
            plainText += new_char;

            // repeat key
            if (count == key.length() - 1) {
                count = 0;
            } else {
                count++;
            }
        }

        return plainText;
    }
    public static String playfairEncryption(String plainText, String key) {
        if (!isStringInBounds(plainText))
            return "The selected string is not in bounds, Try again";

        plainText = plainText.replace("J", "I");
        key = key.replace("J", "I");

        // Step 1: Build key matrix (5x5)
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        StringBuilder matrixBuilder = new StringBuilder();

        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (matrixBuilder.indexOf(String.valueOf(c)) == -1 && alphabet.indexOf(c) != -1)
                matrixBuilder.append(c);
        }
        for (int i = 0; i < alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            if (matrixBuilder.indexOf(String.valueOf(c)) == -1)
                matrixBuilder.append(c);
        }

        char[][] matrix = new char[5][5];
        int idx = 0;
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                matrix[r][c] = matrixBuilder.charAt(idx++);

        // Step 2: Clean and prepare plaintext
        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++) {
            char ch = plainText.charAt(i);
            if (alphabet.indexOf(ch) != -1)
                clean.append(ch);
        }

        StringBuilder pairs = new StringBuilder();
        for (int i = 0; i < clean.length(); i++) {
            char a = clean.charAt(i);
            char b;
            if (i + 1 < clean.length()) b = clean.charAt(i + 1);
            else b = 'X';

            if (a == b) {
                pairs.append(a).append('X');
            } else {
                pairs.append(a).append(b);
                i++;
            }
        }
        if (pairs.length() % 2 != 0) pairs.append('X');

        // Step 3: Encrypt
        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < pairs.length(); i += 2) {
            char a = pairs.charAt(i);
            char b = pairs.charAt(i + 1);

            int rowA = 0, colA = 0, rowB = 0, colB = 0;

            // find positions directly (no helper)
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (matrix[r][c] == a) { rowA = r; colA = c; }
                    if (matrix[r][c] == b) { rowB = r; colB = c; }
                }
            }

            if (rowA == rowB) {
                cipher.append(matrix[rowA][(colA + 1) % 5]);
                cipher.append(matrix[rowB][(colB + 1) % 5]);
            } else if (colA == colB) {
                cipher.append(matrix[(rowA + 1) % 5][colA]);
                cipher.append(matrix[(rowB + 1) % 5][colB]);
            } else {
                cipher.append(matrix[rowA][colB]);
                cipher.append(matrix[rowB][colA]);
            }
        }

        return cipher.toString();
    }


    public static String playfairDecryption(String cipherText, String key) {
        key = key.replace("J", "I");
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

        // Step 1: Build key matrix (same as encryption)
        StringBuilder matrixBuilder = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (matrixBuilder.indexOf(String.valueOf(c)) == -1 && alphabet.indexOf(c) != -1)
                matrixBuilder.append(c);
        }
        for (int i = 0; i < alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            if (matrixBuilder.indexOf(String.valueOf(c)) == -1)
                matrixBuilder.append(c);
        }

        char[][] matrix = new char[5][5];
        int idx = 0;
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                matrix[r][c] = matrixBuilder.charAt(idx++);

        // Step 2: Decrypt
        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i += 2) {
            char a = cipherText.charAt(i);
            char b = cipherText.charAt(i + 1);

            int rowA = 0, colA = 0, rowB = 0, colB = 0;

            // inline search again (no helper)
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (matrix[r][c] == a) { rowA = r; colA = c; }
                    if (matrix[r][c] == b) { rowB = r; colB = c; }
                }
            }

            if (rowA == rowB) {
                plain.append(matrix[rowA][(colA + 4) % 5]);
                plain.append(matrix[rowB][(colB + 4) % 5]);
            } else if (colA == colB) {
                plain.append(matrix[(rowA + 4) % 5][colA]);
                plain.append(matrix[(rowB + 4) % 5][colB]);
            } else {
                plain.append(matrix[rowA][colB]);
                plain.append(matrix[rowB][colA]);
            }
        }

        return plain.toString();
    }



}
