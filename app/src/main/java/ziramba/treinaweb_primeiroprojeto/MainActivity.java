package ziramba.treinaweb_primeiroprojeto;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Paulo Gama on 11/04/2015.
 */
public class MainActivity extends Activity {
    RelativeLayout relativeLayout;
    View ativo;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        relativeLayout = new RelativeLayout(this);
        Button button = new Button(this);
        button.setId((int)System.currentTimeMillis());
        button.setText("Texto Dinâmico");

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = 120;
        params.topMargin = 50;

        button.setLayoutParams(params);

        ativo = button;

        relativeLayout.addView(button);
        setContentView(relativeLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(getApplicationContext());
                textView.setId((int)System.currentTimeMillis());
                textView.setText("Texto "+System.currentTimeMillis());

                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
                );
                textParams.addRule(RelativeLayout.BELOW,ativo.getId());
                ativo = textView;
                relativeLayout.addView(textView,textParams);
            }
        });
    }

}
