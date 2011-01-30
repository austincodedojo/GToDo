package my.apps;

import java.io.IOException;

public class NoAccountsFoundException extends IOException {
    public NoAccountsFoundException() {
        super("No Google Accounts Found");
    }
}
