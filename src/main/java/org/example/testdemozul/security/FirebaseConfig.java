package org.example.testdemozul.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.InputStream;

public class FirebaseConfig {

    private static GoogleCredentials credentials;

    public static void initFirebase() throws Exception {
        // Đọc file từ classpath
        InputStream serviceAccount = FirebaseConfig.class
                .getClassLoader()
                .getResourceAsStream("firebase/hello-notification-22e1c-firebase-adminsdk-g370w-dae09fe5fc.json");

        if (serviceAccount == null) {
            throw new RuntimeException("Không tìm thấy file Firebase JSON trong resources/firebase/");
        }

        credentials = GoogleCredentials.fromStream(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setStorageBucket("hello-notification-22e1c.appspot.com") // ✅ Không có gs://
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("✅ Firebase initialized successfully!");
        }
    }

    public static GoogleCredentials getCredentials() {
        return credentials;
    }
}
