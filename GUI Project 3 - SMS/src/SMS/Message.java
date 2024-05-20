package SMS;


import java.util.ArrayList;

public class Message { // obiekt przesyłany

    private int sender;
    private int recipient;
    private String content;

    public Message(int sender, int recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    public int getSender() {
        return sender;
    }

    public int getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString () {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    // ==================================================== PDU ENCODING =============================================================

    public byte[] encodeMessage(Message message) { // koduje do Bajtów, które potem są zamieniane na HexString w VBD

        int sender = message.getSender();
        int recipient = message.getRecipient();
        String content = message.getContent();


        // Obliczanie wszystkich potrzebnych długości:
        int recipientLength = getByteLength(recipient);
        int senderLength = getByteLength(sender);
        int contentLength = content.length() ; // jeden znak HEX to 4 bity, a jeden znak ASCII to 8 bitów

        // tablica, która pomieści całe PDU
        byte[] encodedMessage = new byte[2 + recipientLength + 3 + senderLength + 3 + contentLength];

        // Ustawianie wartości kolejnych bajtów zgodnie z PDU:

        // recipent number length = xx
        encodedMessage[0] = (byte) recipientLength;

        // type of address = 80
        encodedMessage[1] = (byte) 0x80;

        // recipient number
        encodeNumber(encodedMessage, 2, recipient, recipientLength);

        // message waiting = 04
        encodedMessage[2 + recipientLength] = (byte) 0x04;

        // sender number length = xx
        encodedMessage[3 + recipientLength] = (byte) senderLength;

        // type of address = 80
        encodedMessage[4 + recipientLength] = (byte) 0x80;

        // sender number
        encodeNumber(encodedMessage, 5 + recipientLength, sender, senderLength);

        // protocol identifier = 00
        encodedMessage[5 + recipientLength + senderLength] = (byte) 0x00;

        // data coding scheme = 00
        encodedMessage[6 + recipientLength + senderLength] = (byte) 0x80;

        // text length = xx
        encodedMessage[7 + recipientLength + senderLength] = (byte) contentLength;

        // text
        encodeText(encodedMessage, 8 + recipientLength + senderLength, content);

        return encodedMessage;
    }

    public static int getByteLength(int number) {
        int length = 0;
        while (number > 0) {
            length++;
            number >>= 4; // Przesunięcie bitowe w prawo o 4 bity (1 HEX)
        }
        return length;
    }

    public static void encodeNumber(byte[] encodedMessage, int startIndex, int number, int length) {
        for (int i = 0; i < length; i++) {
            int lastDigit = number & 0xF; // pobierz ostatnia cyfre numeru telefonu (ostatnie 4 bity)
            encodedMessage[startIndex + length - 1 - i] = (byte) (0x80 | lastDigit); // najwyższy bit ustawiony na 1, aby oznaczyć, że to cyfra numeru
            number >>= 4; // Przesunięcie bitowe w prawo o 4 bity (odpowiednik podzielenia przez 16), przechodzimy do kolejnej cyfry numeru telefonu
        }
    }

    public static void encodeText(byte[] encodedMessage, int startIndex, String text) {

        int length = text.length();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            String hexByte = String.format("%02X", (int) c); // Konwersja znaku na HEX
            byte value = (byte) Integer.parseInt(hexByte, 16);
            encodedMessage[startIndex + i] = value;
        }
    }




//        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
//        int length = textBytes.length;
//
//        if (length % 2 != 0) {
//            text += "0"; // Dodaj zero na końcu, jeśli ilość znaków jest nieparzysta
//            length++;
//        }
//
//        for (int i = 0; i < length; i++) {
//
//            String hexByte = text.substring(i);
//
//            byte value = (byte) Integer.parseInt(hexByte, 16);
//            encodedMessage[startIndex + i ] = value;
//        }

//        for (int i = 0; i < length; i += 2) {
//
//            String hexByte = text.substring(i, i + 2);
//
//            byte value = (byte) Integer.parseInt(hexByte, 16);
//            encodedMessage[startIndex + i / 2] = value;
//        }







}

