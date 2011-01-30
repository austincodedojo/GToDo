package my.apps;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

public class AuthTokenProvider {
    protected static final String ACCOUNT_TYPE = "com.google";
    protected static final String TOKEN_TYPE = "goanna_mobile";
    private AccountManager accountManager;

    public AuthTokenProvider(Context context) {
        accountManager = AccountManager.get(context);
    }

    public String getToken() throws IOException {
        try {
            Account googleAccount = getGoogleAccount();
            Bundle token = accountManager.getAuthToken(
                googleAccount, TOKEN_TYPE, true, null, null).getResult();
            if(token.containsKey(AccountManager.KEY_INTENT)) {
                Intent userInteraction = (Intent) token.get(AccountManager.KEY_INTENT);
                throw new UserInteractionRequiredException(userInteraction);
            }
            else if(token.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                return token.getString(AccountManager.KEY_AUTHTOKEN);
            }
            throw new IOException("Unable to get authorization token");
        }
        catch(IOException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private Account getGoogleAccount() throws NoAccountsFoundException {
        Account[] googleAccounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if(googleAccounts.length == 0) {
            throw new NoAccountsFoundException();
        }
        return googleAccounts[0];
    }
}
