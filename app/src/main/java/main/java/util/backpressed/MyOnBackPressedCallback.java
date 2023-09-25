package main.java.util.backpressed;

import static androidx.core.app.ActivityCompat.finishAffinity;
import static main.java.controller.constant.ActivityConstant.BACK_TIME;
import static main.java.controller.constant.ActivityConstant.BACK_TOAST_MESSAGE;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

public class MyOnBackPressedCallback extends OnBackPressedCallback {
    private final Activity activity;
    private boolean doubleBackToExitPressedOnce = false;

    public MyOnBackPressedCallback(boolean enabled, Activity activity) {
        super(enabled);
        this.activity = activity;
    }

    @Override
    public void handleOnBackPressed() {
        // 이전에 뒤로 가기 버튼을 한 번 눌렀었던 경우이다.
        // 즉, doubleBackToExitPressedOnce 값이 이미 true 일 때이다.
        if (doubleBackToExitPressedOnce) {
            // 현재 액티비티를 종료하고, 액티비티 스택에 남아있는 모든 액티비티를 제거.
            // 즉, 앱의 모든 액티비티를 종료하는 역할.
            // 하지만 이 메서드만 호출한다고 해서 앱이 완전히 종료되지는 않는다.
           finishAffinity(activity);

            // 현재 앱의 프로세스가 강제로 종료되므로, 앱의 모든 동작이 즉시 중단됨.
            // 앱을 완전히 종료하고, 모든 자원과 상태를 정리하고 싶을 때 사용될 수 있다.
            // 이는 강제 종료이기 때문에 앱이 예상치 못한 상태에서 종료되는 것이므로 주의해야 함.
//                    System.exit(0);

            // 처음 뒤로 가기 버튼을 누른 경우이다.
            // 즉, 처음 뒤로 가기 버튼을 누른 경우에는 doubleBackToExitPressedOnce 값을 true로 설정하고, 토스트 메시지를 표시한다.
        } else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(activity, BACK_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();

            // 1초 후에 doubleBackToExitPressedOnce 값을 다시 false로 설정
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, BACK_TIME);
        }
    }
}
