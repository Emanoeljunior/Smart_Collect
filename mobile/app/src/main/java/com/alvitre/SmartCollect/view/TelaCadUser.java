package com.alvitre.SmartCollect.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.alvitre.SmartCollect.R;
import com.alvitre.SmartCollect.repository.remote.HttpClient;
import com.alvitre.SmartCollect.viewmodel.CadUserViewModel;
import com.alvitre.SmartCollect.repository.model.Usuario;
import com.alvitre.SmartCollect.repository.model.UsuarioDAO;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TelaCadUser extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 4;
    private String photoFileName = null;
    private ImageView ivFoto;
    private File photoFile = null;
    private EditText user,email,password,passwordConf;
    private Button submitCad;
    private CadUserViewModel mCadUserViewModel;
    private com.google.android.material.floatingactionbutton.FloatingActionButton tirafoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        user = (EditText) findViewById(R.id.edUsuarioCad);
        email = (EditText) findViewById(R.id.edEmail);
        password = (EditText) findViewById(R.id.edSenhaCad);
        passwordConf = (EditText) findViewById(R.id.edConfirmaSenha);
        submitCad = (Button) findViewById(R.id.btCadUser);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);

        tirafoto = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.ftTirafoto);
        mCadUserViewModel = new ViewModelProvider(this).get(CadUserViewModel.class);
        if(mCadUserViewModel.getPhoto() != null){
            ivFoto.setImageBitmap(mCadUserViewModel.getPhoto());
        }
        if(mCadUserViewModel.getPhotoFileName() != null){
            this.photoFileName = mCadUserViewModel.getPhotoFileName();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            }

        }

        tirafoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });

        submitCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( user.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty() || passwordConf.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Preencha todos os campos!!",Toast.LENGTH_SHORT).show();
                }else{
                    if(!password.getText().toString().equals(passwordConf.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Senhas não correspondem",Toast.LENGTH_SHORT).show();
                    }else {
                        Usuario usuario = new Usuario();
                        usuario.setUsuario(user.getEditableText().toString());
                        usuario.setEmail(email.getEditableText().toString());
                        usuario.setSenha(password.getEditableText().toString());
                        usuario.setFoto(photoFileName);

                        UsuarioDAO dao = new UsuarioDAO(TelaCadUser.this);
                        dao.inserir(usuario);
                        dao.close();

                        HttpClient client = new HttpClient(TelaCadUser.this, usuario);
                        Log.i("JSON","onClick: " + usuario.toJson().toString());
                        client.postJson(usuario.toJson());

                        finish();

                    }
                }
            }
        });
    }

        protected void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                //Create the File where the photo should go
                try{
                    photoFile = createImageFile();
                }catch (IOException ex){
                    Log.d("FotoError","Erro ao salvar a foto");
                }
                if(photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,"com.alvitre.SmartCollect.fileprovider",photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        private void galleryAddPic(){
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photoFile);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                //galleryAddPic();
                showImage();

            }
        }
        private byte[] bitmap2Byte(Bitmap bitmap){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
            return bos.toByteArray();
        }
        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            this.photoFileName = image.getName();
            return image;
        }
    public void showImage( ){
        int targetW = ivFoto.getWidth();
        int targetH = ivFoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.getAbsolutePath(),bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Toast.makeText(getApplicationContext(),photoFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        Log.d("Foto salvar",photoFile.getAbsolutePath());
        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
        ivFoto.setImageBitmap(bitmap);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mCadUserViewModel.setPhoto(((BitmapDrawable)ivFoto.getDrawable()).getBitmap());
        mCadUserViewModel.setPhotoFileName(this.photoFileName);
    }
}




