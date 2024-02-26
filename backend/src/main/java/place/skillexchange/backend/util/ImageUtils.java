package place.skillexchange.backend.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ImageUtils {
    // 이미지 바이너리 데이터의 해시값을 생성하는 메서드
    public static String generateHash(byte[] imageData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(imageData);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

//    // 두 개의 이미지 해시값을 비교하는 메서드
//    public static boolean compareHashes(String hash1, String hash2) {
//        return hash1.equals(hash2);
//    }
}
