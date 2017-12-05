package coms.kxjsj.rotateview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RotateImageView vvv=findViewById(R.id.vvv);
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("这是第"+(i)+"个");
        }
        vvv.setAdapter(new RotateImageView.ViewAdapter<String>(list) {
            @Override
            public View getView(final int i, String o, View conventView, ViewGroup viewGroup) {
                if(conventView==null){
                    conventView=getLayoutInflater().inflate(R.layout.tt,viewGroup,false);
                }
                TextView tv=conventView.findViewById(R.id.tv);
                tv.setText(o);
                tv.setBackgroundColor(i%2==0?0xffff00ff:0xff00ffff);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"点击了"+i,Toast.LENGTH_SHORT).show();
                    }
                });
                return conventView;
            }
        });
    }
}
