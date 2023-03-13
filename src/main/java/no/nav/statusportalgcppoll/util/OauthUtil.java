package no.nav.statusportalgcppoll.util;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OauthUtil {
    // Construct the client credentials grant
    private static String CLIENT_SECRET = System.getenv("AZURE_APP_CLIENT_SECRET");
    private static String CLIENT_ID = System.getenv("AZURE_APP_CLIENT_ID");
    private static String TENANT = System.getenv("TENANT");
    private static String ENV = System.getenv("ENV");
    private static ClientID clientID = new ClientID(CLIENT_ID);
    private static Secret clientSecret = new Secret(CLIENT_SECRET);
    private static ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
    private static Scope scope = new Scope("api://"+ENV+"-gcp.navdig.portalserver/.default");


//    public static String getBearerTokenForPortal() throws URISyntaxException, IOException, ParseException{
//        return "{\"access_token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyJ9.eyJhdWQiOiI1MDE4NTZhNy0yODcyLTQzYjctYTNkOC01ZTk0ODVhZThkZmQiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vNjIzNjY1MzQtMWVjMy00OTYyLTg4NjktOWI1NTM1Mjc5ZDBiL3YyLjAiLCJpYXQiOjE2Nzg2OTc2NjYsIm5iZiI6MTY3ODY5NzY2NiwiZXhwIjoxNjc4NzAxNTY2LCJhaW8iOiJFMlpnWUZoemxJOUI3MlcxMGFUbDdXbVMxOTk5QndBPSIsImF6cCI6Ijg0ZDQzOGNkLWRiODMtNDk1My05MmU3LTJmYzBkNTJjZWY4YiIsImF6cGFjciI6IjEiLCJvaWQiOiI0MzcxZjI4Yi05M2RmLTRhOTktOTU2Ni1iNzI2ZmFlZWU0NjMiLCJyaCI6IjAuQVNBQU5HVTJZc01lWWttSWFadFZOU2VkQzZkV0dGQnlLTGREbzloZWxJV3VqZjBnQUFBLiIsInJvbGVzIjpbImFjY2Vzc19hc19hcHBsaWNhdGlvbiJdLCJzdWIiOiI0MzcxZjI4Yi05M2RmLTRhOTktOTU2Ni1iNzI2ZmFlZWU0NjMiLCJ0aWQiOiI2MjM2NjUzNC0xZWMzLTQ5NjItODg2OS05YjU1MzUyNzlkMGIiLCJ1dGkiOiJrVHk1RUZ3aFIwaUk2ZW5nc0pHVUFBIiwidmVyIjoiMi4wIiwiYXpwX25hbWUiOiJkZXYtZ2NwOm5hdmRpZzpzdGF0dXNwb2xsIn0.kYd0n2JWH_zEMLEWRspsd7wAzi1dK1RHwH7kKRpKpyDm3h0yY3J2ED7xVhIMCMkZ_EMiA0YTb6lz0UXj0qFMPVrVzSs3Z41klrzJGldo3_tl8jsmj12MgkKSJc3ObnrMPeZ2zHZyZSAH0cKCAx260IbHMMgz4MRT86lwTcShPvWE5CRwer5ZUpTAxBBd4AHKe-359GUwOg2mgLwqdqxWZUCkMMNaD0jwuou8isfnMmSvPTVmxFyDaXtKI7N5gGY1BTQUJ7nG_zb77TkfkVxDvaszjAuuQ_7Na3HPfa0DxNqhPefjm17W7bGlWoVrC1tpkgPny0PagMnhTANX1K_Ocg\",\"token_type\":\"Bearer\",\"expires_in\":3599}";
////        return  "Bearer " + getAccessTokenForPortal().toJSONString();
//    }


    public static String getAccessTokenForPortal() throws URISyntaxException, IOException, ParseException {

        AuthorizationGrant clientGrant = new ClientCredentialsGrant();
        // The token endpoint
        URI tokenEndpoint =  new URI("https://login.microsoftonline.com/"+TENANT+"/oauth2/v2.0/token");

        // Make the token request
        TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, clientGrant, scope);

        TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());

        if (!response.indicatesSuccess()) {
            // We got an error response...
            TokenErrorResponse errorResponse = response.toErrorResponse();
        }

        AccessTokenResponse successResponse = response.toSuccessResponse();

        // Get the access token
        AccessToken accessToken = successResponse.getTokens().getAccessToken();

        return accessToken.toJSONString();
    }
}
