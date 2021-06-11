package com.crazy.crazyalarm.closeModeActivity.jigsaw;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazy.crazyalarm.MainActivity;
import com.crazy.crazyalarm.R;
import com.crazy.crazyalarm.clockUtils.AlarmManagerUtil;
import com.crazy.crazyalarm.clockUtils.BasicRingActivity;
import com.crazy.crazyalarm.closeModeActivity.jigsaw.Utils.Utils;
import com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog.SelectImageDialog;
import com.crazy.crazyalarm.closeModeActivity.jigsaw.dialog.SuccessDialog;
import com.crazy.crazyalarm.closeModeActivity.jigsaw.game.PuzzleGame;
import com.crazy.crazyalarm.closeModeActivity.jigsaw.ui.PuzzleLayout;

public class JigsawActivity extends BasicRingActivity implements PuzzleGame.GameStateListener {
    private PuzzleLayout puzzleLayout;
    private PuzzleGame puzzleGame;
    private ImageView srcImg;
//    private Spinner spinner;
    private TextView tvLevel;
    private SelectImageDialog selectImageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        initListener();
        Intent it = new Intent(this, MainActivity.class);
        giveNotice(it.getIntExtra(AlarmManagerUtil.NOTICEFLAG, AlarmManagerUtil.BothSoundAndVibrator));
    }


    private void initView() {
        puzzleLayout = (PuzzleLayout) findViewById(R.id.puzzleLayout);
        puzzleGame = new PuzzleGame(this, puzzleLayout);
        srcImg = (ImageView) findViewById(R.id.ivSrcImg);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvLevel.setText("难度等级：" + puzzleGame.getLevel());
        srcImg.setImageBitmap(Utils.readBitmap(getApplicationContext(), puzzleLayout.getRes(), 4));
    }


    private void initListener() {
        puzzleGame.addGameStateListener(this);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    puzzleGame.changeMode(PuzzleLayout.GAME_MODE_NORMAL);
//                } else {
//                    puzzleGame.changeMode(PuzzleLayout.GAME_MODE_EXCHANGE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        if (selectImageDialog == null) {
            selectImageDialog = new SelectImageDialog();
            selectImageDialog.addItemClickListener(new SelectImageDialog.OnItemClickListener() {
                @Override
                public void itemClick(int postion, int res) {
                    //更新布局
                    puzzleGame.changeImage(res);
                    srcImg.setImageBitmap(Utils.readBitmap(getApplicationContext(), res, 4));
                }
            });
        }

        srcImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageDialog.showDialog(getFragmentManager(), "dialog", 0);
            }
        });
    }

    public void addLevel(View view) {
        puzzleGame.addLevel();
    }

    public void reduceLevel(View view) {
        puzzleGame.reduceLevel();
    }

    public void changeImage(View view) {

    }

    @Override
    public void setLevel(int level) {
        tvLevel.setText("难度等级：" + level);
    }

    @Override
    public void gameSuccess(int level) {
        final SuccessDialog successDialog = new SuccessDialog();
        successDialog.show(getFragmentManager(), "successDialog");
        successDialog.addButtonClickListener(new SuccessDialog.OnButtonClickListener() {
            @Override
            public void nextLevelClick() {
                puzzleGame.addLevel();
                successDialog.dismiss();
            }

            @Override
            public void cancelClick() {
                successDialog.dismiss();
            }
        });
//
//        Intent it = new Intent(this, MainActivity.class);
//        stopNotice(it.getIntExtra(AlarmManagerUtil.NOTICEFLAG, AlarmManagerUtil.BothSoundAndVibrator));

    }
}
