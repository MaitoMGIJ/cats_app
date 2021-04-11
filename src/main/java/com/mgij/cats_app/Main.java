package com.mgij.cats_app;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int optionMenu = -1;
        String[] buttons = {
                "1. Get Cats",
                "2. Exit"
        };

        do{
            String option = (String) JOptionPane.showInputDialog(
                    null,
                    "Kittens Java",
                    "Main Menu",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    buttons,
                    buttons[0]
                );

            if(option!=null) {
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        optionMenu = i;
                        break;
                    }
                }
            }

            switch (optionMenu){
                case 0:
                    CatsService.getCats();
                    break;
                case 1:
                    optionMenu = -1;
                    break;
                default:
                    break;
            }

        }while(optionMenu!=-1);
    }
}
