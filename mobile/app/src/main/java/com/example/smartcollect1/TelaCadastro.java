package com.example.smartcollect1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcollect1.Usuario;
import com.example.smartcollect1.UsuarioDAO;

import android.content.Context;
import android.widget.EditText;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.provider.MediaStore.*;




public class TelaCadastro extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseAuth mAuth;

    private ImageView ivFoto;
    private com.google.android.material.floatingactionbutton.FloatingActionButton tirafoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        tirafoto = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.ftTirafoto);

        tirafoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        Button botao = (Button) findViewById(R.id.btCadastrar);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nome = (EditText) findViewById(R.id.edUsuario);
                EditText email = (EditText) findViewById(R.id.edEmail);
                EditText senha = (EditText) findViewById(R.id.edSenha);

                //condição para verificar se usuario ja cadastrado

                UsuarioDAO dao = new UsuarioDAO(TelaCadastro.this);

              /*  if (dao.getUsuario(nome.getEditableText().toString()) == 1){
                    Context contexto = getApplicationContext();
                    String texto = "Usuário já cadastrado!";
                    int duracao = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(contexto, texto,duracao);
                    toast.show();
                }else{
                    if (dao.getUsuario(nome.getEditableText().toString()) == 0){*/
                        Usuario usuario = new Usuario();
                        usuario.setNome(nome.getEditableText().toString());
                        usuario.setEmail(nome.getEditableText().toString());
                        usuario.setSenha(nome.getEditableText().toString());

                        UsuarioDAO dao1 = new UsuarioDAO(TelaCadastro.this);
                        dao1.inserir(usuario);
                        dao1.close();

                        finish();
                    //}
                //}
            }
        });

    }


    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFoto.setImageBitmap(imageBitmap);
        }
    }

//.........................................................................




}
