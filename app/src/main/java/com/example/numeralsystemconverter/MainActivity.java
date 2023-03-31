package com.example.numeralsystemconverter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText inputNum = findViewById(R.id.input);
        TextView outputNum = findViewById(R.id.output);
        Spinner inputSpinner = findViewById(R.id.inputSpinner);
        Spinner outputSpinner = findViewById(R.id.outputSpinner);
        Button convertButton = findViewById(R.id.convertButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.option));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputSpinner.setAdapter(adapter);
        outputSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(v -> {
            String input = inputNum.getText().toString();
            double inputIndex = inputSpinner.getSelectedItemPosition();
            double outputIndex = outputSpinner.getSelectedItemPosition();
            String result = convert(input, inputIndex, outputIndex);
            outputNum.setText(result);
        });
    }

    public static String convert(String input, double inputIndex, double outputIndex) {
        //this converts from

        String binary = "";

        switch ((int) inputIndex) {
            case 0: // Decimal
                binary = decimalToBinary(input);
                break;
            case 1: // Binary
                binary = input;
                break;
            case 2: // Hexadecimal
                binary = hexToBinary(input);
                break;
            case 3: // Octal
                binary = octalToBinary(input);
                break;
        }

        // converts to
        String output = "";

        switch ((int) outputIndex) {
            case 0: // Decimal
                output = binaryToDecimal(binary);
                break;
            case 1: // Binary
                output = binary;
                break;
            case 2: // Hexadecimal
                output = binaryToHex(binary);
                break;
            case 3: // Octal
                output = binaryToOctal(binary);
                break;
        }

        return output;
    }

    public static String decimalToBinary(String decimalString) {
        double decimal = 0.0;
        try {
            decimal = Double.parseDouble(decimalString);
        } catch (NumberFormatException e) {
            return "μή έγκυρη τιμή";
        }

        String binary = "";
        if (decimal < 0) {
            binary = "-";
            decimal = -decimal;
        }
        int integerPart = (int) decimal;
        double decimalPart = decimal - integerPart;

        // Add condition to check if there is a fractional part before appending the decimal point
        if (decimalPart != 0) {
            binary += Integer.toBinaryString(integerPart) + ".";
        } else {
            binary += Integer.toBinaryString(integerPart);
        }

        int maxDecimalPlaces = 10; // Set a maximum number of decimal places
        while (decimalPart > 0 && maxDecimalPlaces > 0) {
            decimalPart *= 2;
            if (decimalPart >= 1) {
                binary += "1";
                decimalPart -= 1;
            } else {
                binary += "0";
            }
            maxDecimalPlaces--;
        }

        return binary;
    }

    public static String binaryToDecimal(String binaryString) {
        if (binaryString.isEmpty()) {
            return "μή έγκυρη τιμή";
        }

        double decimal = 0.0;
        boolean isNegative = false;
        if (binaryString.startsWith("-")) {
            isNegative = true;
            binaryString = binaryString.substring(1);
        }
        if (binaryString.contains(".")) {
            // Handle decimal binary numbers
            String[] parts = binaryString.split("\\.");
            for (int i = 0; i < parts[0].length(); i++) {
                if (parts[0].charAt(i) == '1') {
                    decimal += Math.pow(2, parts[0].length() - i - 1);
                }
            }
            for (int i = 0; i < parts[1].length(); i++) {
                if (parts[1].charAt(i) == '1') {
                    decimal += Math.pow(2, -i - 1);
                }
            }
        } else {
            decimal = Integer.parseInt(binaryString, 2);
        }
        if (isNegative) {
            decimal = -decimal;
        }
        if (decimal % 1 == 0) {
            return Integer.toString((int) decimal);
        } else {
            return Double.toString(decimal);
        }
    }

    public static String hexToBinary(String hexadecimalString) {
        StringBuilder resultBuilder = new StringBuilder();
        if (hexadecimalString.matches("-?[0-9a-fA-F]+")) {
            if (hexadecimalString.length() > 0) {
                boolean isHexNegative = false;
                if (hexadecimalString.charAt(0) == '-') {
                    isHexNegative = true;
                    hexadecimalString = hexadecimalString.substring(1);
                }
                String[] parts2 = hexadecimalString.split("\\.");
                String integerPart2 = parts2[0];
                String fractionalPart2 = parts2.length > 1 ? parts2[1] : "";
                StringBuilder binaryBuilder2 = new StringBuilder();
                for (int i = 0; i < integerPart2.length(); i++) {
                    char hexDigit = integerPart2.charAt(i);
                    int decimalValue2 = Integer.parseInt(String.valueOf(hexDigit), 16);
                    String fourBits = Integer.toBinaryString(decimalValue2);
                    binaryBuilder2.append("0".repeat(4 - fourBits.length())).append(fourBits);
                }
                while (binaryBuilder2.length() > 1 && binaryBuilder2.charAt(0) == '0') {
                    binaryBuilder2.deleteCharAt(0);
                }
                if (!fractionalPart2.isEmpty()) {
                    binaryBuilder2.append(".");
                    for (int i = 0; i < fractionalPart2.length(); i++) {
                        char hexDigit = fractionalPart2.charAt(i);
                        int decimalValue = Integer.parseInt(String.valueOf(hexDigit), 16);
                        String fourBits = Integer.toBinaryString(decimalValue);
                        binaryBuilder2.append("0".repeat(4 - fourBits.length())).append(fourBits);
                    }
                }
                if (isHexNegative) {
                    binaryBuilder2.insert(0, '-');
                }
                resultBuilder.append(binaryBuilder2);
            }
        } else {
            resultBuilder.append("μή έγκυρη τιμή");
        }
        return resultBuilder.toString();
    }

    public static String binaryToHex(String binaryString) {
        StringBuilder resultBuilder = new StringBuilder();
            if (binaryString.length() > 0) {
                boolean isNegative = false;
                if (binaryString.charAt(0) == '-') {
                    isNegative = true;
                    binaryString = binaryString.substring(1);
                }
                String[] parts = binaryString.split("\\.");
                String integerPart = parts[0];
                String fractionalPart = parts.length > 1 ? parts[1] : "";
                StringBuilder hexBuilder = new StringBuilder();
                int missingZeros = 4 - integerPart.length() % 4;
                if (missingZeros != 4) {
                    integerPart = "0".repeat(missingZeros) + integerPart;
                }
                for (int i = 0; i < integerPart.length(); i += 4) {
                    String fourBits = integerPart.substring(i, i + 4);
                    int decimalValue = Integer.parseInt(fourBits, 2);
                    hexBuilder.append(Integer.toHexString(decimalValue).toUpperCase());
                }
                if (!fractionalPart.isEmpty()) {
                    hexBuilder.append(".");
                    missingZeros = 4 - fractionalPart.length() % 4;
                    if (missingZeros != 4) {
                        fractionalPart += "0".repeat(missingZeros);
                    }
                    for (int i = 0; i < fractionalPart.length(); i += 4) {
                        String fourBits = fractionalPart.substring(i, i + 4);
                        int decimalValue = Integer.parseInt(fourBits, 2);
                        hexBuilder.append(Integer.toHexString(decimalValue).toUpperCase());
                    }
                }
                if (isNegative) {
                    hexBuilder.insert(0, '-');
                }
                resultBuilder.append(hexBuilder);
            }
        return resultBuilder.toString();
    }

    public static String octalToBinary(String octalString) {
        if (octalString.isEmpty()) {
            return "μή έγκυρη τιμή";
        }

        boolean isOctalNegative = false;
        if (octalString.charAt(0) == '-') {
            isOctalNegative = true;
            octalString = octalString.substring(1);
        }
        int pointOctalIndex = octalString.indexOf('.');
        if (pointOctalIndex == -1) {
            pointOctalIndex = octalString.length();
        }
        // Convert the integer part to decimal
        int intOctalPartDecimal = 0;
        for (int i = 0; i < pointOctalIndex; i++) {
            intOctalPartDecimal += Character.getNumericValue(octalString.charAt(i)) * Math.pow(8, pointOctalIndex - i - 1);
        }
        // Convert the fractional part to decimal
        double fracOctalPartDecimal = 0;
        for (int i = pointOctalIndex + 1; i < octalString.length(); i++) {
            fracOctalPartDecimal += Character.getNumericValue(octalString.charAt(i)) * Math.pow(8, pointOctalIndex - i);
        }
        // Convert the integer part to binary
        StringBuilder intPartBinaryBuilder = new StringBuilder();
        while (intOctalPartDecimal > 0) {
            int remainder = intOctalPartDecimal % 2;
            intPartBinaryBuilder.append(remainder);
            intOctalPartDecimal /= 2;
        }
        intPartBinaryBuilder.reverse();
        // Convert the fractional part to binary
        StringBuilder fracPartBinaryBuilder = new StringBuilder(".");
        while (fracOctalPartDecimal > 0) {
            fracOctalPartDecimal *= 2;
            int digit = (int) fracOctalPartDecimal;
            fracPartBinaryBuilder.append(digit);
            fracOctalPartDecimal -= digit;
        }
        // Combine the integer and fractional parts and add negative sign if needed
        StringBuilder resultBinaryBuilder = new StringBuilder();
        if (isOctalNegative) {
            resultBinaryBuilder.append('-');
        }
        resultBinaryBuilder.append(intPartBinaryBuilder);
        if (fracPartBinaryBuilder.length() > 1) {
            resultBinaryBuilder.append(fracPartBinaryBuilder);
        }

        return resultBinaryBuilder.toString();
    }

    public static String binaryToOctal(String binaryString) {
        StringBuilder resultBuilder = new StringBuilder();
        if (binaryString.length() > 0) {
            boolean isNegative = false;
            if (binaryString.charAt(0) == '-') {
                isNegative = true;
                binaryString = binaryString.substring(1);
            }
            int pointIndex = binaryString.indexOf('.');
            if (pointIndex == -1) {
                pointIndex = binaryString.length();
            }
            int intPartDecimal = 0;
            double fracPartDecimal = 0;
            for (int i = 0; i < binaryString.length(); i++) {
                char c = binaryString.charAt(i);
                if (c == '.') {
                    pointIndex = i;
                } else if (c == '-') {
                    isNegative = true;
                } else {
                    int digit = Character.getNumericValue(c);
                    if (i < pointIndex) {
                        intPartDecimal += digit * Math.pow(2, pointIndex - i - 1);
                    } else {
                        fracPartDecimal += digit * Math.pow(2, pointIndex - i);
                    }
                }
            }
            StringBuilder intPartOctalBuilder = new StringBuilder();
            while (intPartDecimal > 0) {
                int remainder = intPartDecimal % 8;
                intPartOctalBuilder.append(remainder);
                intPartDecimal /= 8;
            }
            intPartOctalBuilder.reverse();
            StringBuilder fracPartOctalBuilder = new StringBuilder(".");
            while (fracPartDecimal > 0) {
                fracPartDecimal *= 8;
                int digit = (int) fracPartDecimal;
                fracPartOctalBuilder.append(digit);
                fracPartDecimal -= digit;
            }
            resultBuilder.append(intPartOctalBuilder);
            if (fracPartOctalBuilder.length() > 1) {
                resultBuilder.append(fracPartOctalBuilder);
            }
            if (isNegative) {
                resultBuilder.insert(0, '-');
            }
        }
        return resultBuilder.toString();
    }
}
