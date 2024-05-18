package com.example.healthsnsproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    //프래그먼트 객체 생성
    Fragment_main_1 fragment_main_1;
    Fragment_main_2 fragment_main_2;
    Fragment_main_3 fragment_main_3;
    Fragment_main_4 fragment_main_4;



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

        //프래그먼트 할당
        fragment_main_1 = new Fragment_main_1();
        fragment_main_2 = new Fragment_main_2();
        fragment_main_3 = new Fragment_main_3();
        fragment_main_4 = new Fragment_main_4();

        //1번 프래그먼트 컨테이너에 추가
        getSupportFragmentManager().beginTransaction().add(R.id.container_main, fragment_main_1).commit();

        //바텀 내비
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navagation);
        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.tab1) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment_main_1).commit();
                            return true;
                        } else if (itemId == R.id.tab2) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment_main_2).commit();
                            return true;
                        } else if (itemId == R.id.tab3) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment_main_3).commit();
                            return true;
                        } else if (itemId == R.id.tab4) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment_main_4).commit();
                            return true;
                        }
                        return false;
                    }
                }
        );


    }


}