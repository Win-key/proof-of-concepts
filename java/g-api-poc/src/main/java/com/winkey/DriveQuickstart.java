package com.winkey;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

/* class to demonstarte use of Drive files list API */
public class DriveQuickstart {
    /** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE, DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    private static Credential getCredentialsOfServiceAccount(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        var credsPath = "/service_user_creds.json";
        InputStream in = DriveQuickstart.class.getResourceAsStream(credsPath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credsPath);
        }
        GoogleCredential credential = GoogleCredential.fromStream(in).createScoped(SCOPES);
        // todo: GoogleCredential is deprecated. HttpRequestInitializer is the new way of building service
        // GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
        //        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
        //                credentials);

        //returns an authorized Credential object.
        return credential;
    }

    private static Drive getDrive() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Build a new authorized API client service.
//        Using oAuth 2 flow
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();

        var credsPath = "/service_user_creds.json";
        InputStream in = DriveQuickstart.class.getResourceAsStream(credsPath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credsPath);
        }
        GoogleCredential credential = GoogleCredential.fromStream(in).createScoped(SCOPES);
        // todo: GoogleCredential is deprecated. HttpRequestInitializer is the new way of building service
//         GoogleCredentials credentials = GoogleCredentials.getApplicationDefault().createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
//                HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//                        credentials);

        // using service user flow
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {

        var service = getDrive();
        readFiles(service);

//         Upload a file
        String resultId = uploadBasic(service);
        System.out.println("File id: " + resultId);

    }

    private static void readFiles(Drive service) throws IOException {
        // Print the names and IDs for up to 10 files.
        FileList result = service.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s Id (%s) Parent (%s)\n", file.getName(), file.getId(), file.getParents());
            }
        }
    }

    /**
     *
     * https://developers.google.com/drive/api/guides/manage-uploads
     * Upload new file.
     * @return Inserted file metadata if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
     */
    public static String uploadBasic(final Drive service) throws IOException{
        // Upload file photo.jpg on drive.
        File fileMetadata = new File().setParents(List.of("16_7AD4izA1Q1wthgvjFRQILBKxQUQj_V"));
        fileMetadata.setName("photo"+System.currentTimeMillis()+".png");
        // File's content.
        java.io.File filePath = new java.io.File(DriveQuickstart.class.getResource("/photo.png").getPath());
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent("image/jpeg", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        }catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }
}