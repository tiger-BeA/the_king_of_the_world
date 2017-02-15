package com.example.administrator.noteediter;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import org.xhtmlrenderer.pdf.ITextFontContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class NoteRichEditor extends AppCompatActivity {
    private RichEditor mEditor;
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private String ImageUrl;
    private String ImageAlt = "Empty Image!";
    private String WindowWidth = "100%";
    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    private static final String OUTPUT_PDF = "file:///android_asset/editor.pdf";
    private Bitmap SaveAsBitmap;
//    private TextView mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

//        mPreview = (TextView) findViewById(R.id.preview);
//        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override public void onTextChange(String text) {
//                mPreview.setText(text);
//            }
//        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.WHITE : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                OpenAlbum();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
        /***** 此处设置手写、键盘模式的切换 *****/
        findViewById(R.id.EditorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.RichEditorToolBar).setVisibility(View.VISIBLE);
                // 插入手写toolbar的隐藏
            }
        });
        findViewById(R.id.HandButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.RichEditorToolBar).setVisibility(View.GONE);
                // 插入手写toolbar的出现
            }
        });
        // 显示分享形式
        findViewById(R.id.ShareButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ShareBox).setVisibility(View.VISIBLE);
            }
        });
        // 选择分享形式
        // 图片分享
        findViewById(R.id.ShareAsPic).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                View webView = findViewById(R.id.editor);
                webView.setDrawingCacheEnabled(true);
                SaveAsBitmap = Bitmap.createBitmap(webView.getWidth(),webView.getHeight(),Bitmap.Config.ARGB_8888);
                if (SaveAsBitmap != null) {
                    Canvas canvas = new Canvas(SaveAsBitmap);
                    webView.draw(canvas);
                    new Thread(savePicFileRunnable).start();
                }
            }
        });
        // PDF分享
        findViewById(R.id.ShareAsPDF).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Thread(savePDFFileRunnable).start();
            }
        });

        // 可编辑链接分享
        findViewById(R.id.shareAsEditorLink).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String url = null;
                try {
                    url = new File(SETUP_HTML).toURI().toURL().toString();
                    System.out.println(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void SaveAsPDF() throws IOException, DocumentException {
        String url = new File(SETUP_HTML).toURI().toURL().toString();
        System.out.println(url);

        ITextRenderer renderer = new ITextRenderer();

        // 解决中文乱码
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("././assets/font.Lanting_founder_black", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        renderer.setDocument(url);
        // 解决图片的相对路径问题
        renderer.layout();
        OutputStream os = new FileOutputStream(OUTPUT_PDF);
        renderer.createPDF(os);
        renderer.finishPDF();
        os.close();
    }
    private Runnable savePDFFileRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                SaveAsPDF();
            }catch (IOException ex){
                ex.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    };
    public void SaveAsPic (Bitmap bm) throws IOException {
        File dirFile = new File("file:///android_asset/");
        if (!dirFile.exists()){
            dirFile.mkdir();
        }
        else{
            dirFile.delete();
            dirFile.mkdir();
        }
        File myCaptureFile = new File("file:///android_asset/editor.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        Toast.makeText(NoteRichEditor.this, "图片保存成功！", Toast.LENGTH_SHORT).show();
    }
    private Runnable savePicFileRunnable = new Runnable() {
        @Override
        public void run() {
            try{
                SaveAsPic(SaveAsBitmap);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    };

    private void OpenAlbum(){
        // 打开系统相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        Log.i("--------跳转","开始跳转");
        startActivityForResult(intent,REQUEST_CODE_PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PICK_IMAGE && data != null){
            Uri SelectedImage = data.getData();
            ImageUrl = SelectedImage.toString();
            Log.e("-----图片路径", ImageUrl);
            mEditor.insertImage(ImageUrl,ImageAlt);
        }
    }
}