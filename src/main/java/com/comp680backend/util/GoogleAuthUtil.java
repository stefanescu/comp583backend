package com.comp680backend.util;

import com.comp680backend.models.User;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Logger;

public class GoogleAuthUtil {
    private static final Logger log = Logger.getLogger(GoogleAuthUtil.class.getName());
    private static final JacksonFactory jacksonFactory = new JacksonFactory();

//    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
//            // Specify the CLIENT_ID of the app that accesses the backend:
//            .setAudience(Collections.singletonList("748827521810-jo9ad9o7brpjmhdb1h7movd6m865c3f5.apps.googleusercontent.com"))
//            // Or, if multiple clients access the backend:
//            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
//            .build();
    private GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(UrlFetchTransport.getDefaultInstance(), jacksonFactory)
            .setAudience(Collections.singletonList("748827521810-jo9ad9o7brpjmhdb1h7movd6m865c3f5.apps.googleusercontent.com"))//CRLConstants.ANDROID_CLIENT_ID_RELEASE, CRLConstants.ANDROID_CLIENT_ID_DEBUG))
            .build();

    public User authenticate(String idTokenString) {

        GoogleIdToken idToken = null;
        System.out.println("verifying");
        try {
            idToken = verifier.verify(idTokenString);
            System.out.println("verified!");

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            System.out.println(email);

            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            User user = new User();
            user.setEmail(email);
            user.setUserName(email);
            user.setId(0);
            return user;

        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
    }
}

//public class GoogleAuthenticator implements Authenticator {
//
//    private static final Logger log = Logger.getLogger(GoogleAuthenticator.class.getName());
//    private static final JacksonFactory jacksonFactory = new JacksonFactory();
//
//    // From: https://developers.google.com/identity/sign-in/android/backend-auth#using-a-google-api-client-library
//    // If you retrieved the token on Android using the Play Services 8.3 API or newer, set
//    // the issuer to "https://accounts.google.com". Otherwise, set the issuer to
//    // "accounts.google.com". If you need to verify tokens from multiple sources, build
//    // a GoogleIdTokenVerifier for each issuer and try them both.
//
//    GoogleIdTokenVerifier verifierForNewAndroidClients = new GoogleIdTokenVerifier.Builder(UrlFetchTransport.getDefaultInstance(), jacksonFactory)
//            .setAudience(Collections.singletonList("748827521810-jo9ad9o7brpjmhdb1h7movd6m865c3f5.apps.googleusercontent.com"))//CRLConstants.ANDROID_CLIENT_ID_RELEASE, CRLConstants.ANDROID_CLIENT_ID_DEBUG))
//            .build();
//
//
//    // Custom Authenticator class for authenticating google accounts
//    @Override
//    public User authenticate(HttpServletRequest request) {
//
//        String token = request.getHeader("google_id_token");
//        if (token != null) {
//
//            GoogleIdToken idToken = null;
//            try {
//                idToken = verifierForNewAndroidClients.verify(token);
//                if(idToken == null) idToken = verifierForOtherClients.verify(token);
//
//                if (idToken != null) {
//
//                    GoogleIdToken.Payload payload = idToken.getPayload();
//
//                    // Get profile information from payload
//                    String userId = payload.getSubject();
//                    String email = payload.getEmail();
//
//                    return new GoogleUser(userId, email);
//
//                } else {
//                    log.warning("Invalid Google ID token.");
//                }
//
//            } catch (GeneralSecurityException e) {
//                log.warning(e.getLocalizedMessage());
//            } catch (IOException e) {
//                log.warning(e.getLocalizedMessage());
//            }
//
//        }