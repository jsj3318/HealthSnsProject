package com.example.healthsnsproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
        implements Fragment_main_4.OnFragmentInteractionListener {
    private long backPressedTime;


    //프래그먼트 객체 생성
    private Fragment_main_1 fragment_main_1;
    private Fragment_main_2 fragment_main_2;
    private Fragment_main_3 fragment_main_3;
    private Fragment_main_4 fragment_main_4;
    private Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //프래그먼트 생성
        fragment_main_1 = new Fragment_main_1();
        fragment_main_2 = new Fragment_main_2();
        fragment_main_3 = new Fragment_main_3();
        fragment_main_4 = new Fragment_main_4();

        //프래그먼트 네개 모두 컨테이너에 추가하고 숨긴 뒤 1번만 표시
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, fragment_main_1, "1").hide(fragment_main_1)
                    .add(R.id.container_main, fragment_main_2, "2").hide(fragment_main_2)
                    .add(R.id.container_main, fragment_main_3, "3").hide(fragment_main_3)
                    .add(R.id.container_main, fragment_main_4, "4").hide(fragment_main_4)
                    .commit();

            getSupportFragmentManager().beginTransaction().show(fragment_main_1).commit();
            activeFragment = fragment_main_1;
        }

        //바텀 내비게이션
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navagation);
        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //내비게이션 바 선택된 메뉴에 따라 프래그먼트 표시 변경
                        //스위치 문 쓰고 싶은데 id 찾는 데에 오류가 해결이 안돼서 어쩔 수 없이 if-else 씀 ㅠㅠ
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.tab1) {
                            showFragment(fragment_main_1);
                            return true;
                        } else if (itemId == R.id.tab2) {
                            showFragment(fragment_main_2);
                            return true;
                        } else if (itemId == R.id.tab3) {
                            showFragment(fragment_main_3);
                           return true;
                        } else if (itemId == R.id.tab4) {
                            showFragment(fragment_main_4);
                            return true;
                        }
                        return false;
                    }
                }
        );


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            // 2초 이내에 다시 뒤로가기 버튼을 누르면 앱 종료
            super.onBackPressed();
            finish();
        } else {
            // 처음 뒤로가기 버튼을 눌렀을 때 안내 메시지를 표시
            Toast.makeText(this, "한 번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void showFragment(Fragment fragment) {  //전달 받은 프래그먼트를 표시하고 기존 표시되는 프래그먼트 숨김
        if (activeFragment != fragment) {
            getSupportFragmentManager().beginTransaction().hide(activeFragment).show(fragment).commit();
            activeFragment = fragment;
        }
    }

    public void Logout() {  
        //4번째 프래그먼트에 있는 로그아웃 버튼 클릭 시 리스너로 호출되는 로그아웃 함수
        //경고 알림 박스 나오고 로그아웃 할 건지 선택시킨 후 로그인 액티비티로 돌아감
        new AlertDialog.Builder(this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("아니오", null)
                .show();

    }


}