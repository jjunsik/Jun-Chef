package main.java.util.http.junchef.savesession;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class EncryptedPreferencesManager {
    public static void saveSessionIdInEncryptedSharedPreferences(String sessionId, Context context) {
        try {
            Log.d("JunChef", "EncryptedSharedPreferences 생성 시작, 세션 ID: " + sessionId);
            // MasterKeys.getOrCreate 메서드는 암호화에 사용될 masterKey를 생성하거나 이미 존재하는 키를 가져온다.
            // MasterKeys.AES256_GCM_SPEC는 사용할 키 생성 사양을 지정한다.
            // 이 사양은 AES 256비트 키를 GCM(Block Cipher Mode of Operation)으로 사용한다는 것을 의미한다.
            // 이 키는 암호화와 복호화에 사용된다.
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // EncryptedSharedPreferences.create 메서드는 암호화된 SharedPreferences 인스턴스를 생성한다.
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "SessionId", // SharedPreferences 파일의 이름
                    masterKey, // 위에서 생성한 마스터 키의 별칭
                    context, // 현재 애플리케이션의 컨텍스트
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // 키의 암호화 방식을 지정
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // 값의 암호화 방식을 지정
            );

            SharedPreferences.Editor editor = sharedPreferences.edit(); // 이 객체를 통해 데이터를 저장하거나 수정할 수 있다.

            editor.putString("sessionId", sessionId); // "sessionId"라는 키로 sessionId 값을 저장
            editor.apply(); // apply()는 변경사항을 비동기적으로 적용한다. commit() 대신 apply()를 사용하는 이유는 apply()가 디스크 I/O를 수행하는 동안 UI 스레드를 차단하지 않기 때문이다.

            Log.d("JunChef", "EncryptedSharedPreferences에 세션 ID 저장");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSessionIdFromEncryptedSharedPreferences(Context context) {
        String sessionId = null;
        try {

            Log.d("JunChef", "EncryptedSharedPreferences에 세션 ID 조회 시작");
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "SessionId",
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            sessionId = sharedPreferences.getString("sessionId", null);

            Log.d("JunChef", "EncryptedSharedPreferences에서 세션 ID 조회: " + sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionId;
    }
}
