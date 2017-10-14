package com.joseaguilar.logingoogle_android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView foto;
    private TextView txtnom, txtemail, txtiden;

    //Paso 1 : Para poder acceder a la informacion del cliente logeado, necesitamos acceder al metodoon ActivityResult del LoginActivity
    // para esto debemos de realizar un LoginSilencioso (SilentLogin)
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foto = (ImageView) findViewById(R.id.ImagenCliente);
        txtnom = (TextView) findViewById(R.id.NombreCliente);
        txtemail = (TextView) findViewById(R.id.CorreoCliente);
        txtiden = (TextView) findViewById(R.id.Identificador);

        //Paso 2: instanciamos el API de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() //con este metodo traera el correo del usuario
                .build();

        //Paso 3: inicializamos el googleApiclient -- el googleApiClient sirve como intermediario entre las APIS y la app
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //este metodo permite gestionar el ciclo de vida del googleApiClient --> en el 2do this te pedira implementar el metodo onConnectionFailed
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso) //con este metodo hacemos el puente entre este proyecto y el API de google
                .build();
    }

    //ESTE METODO SE EJECUTA CUANDO SALE MAL ALGO EN LA APLICACION
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //-------------------------------------------------------------------------------------
    //Paso 4: el metodo mas apropiado para realizar el login silenciono es el metodo " onStart()" -- lo importamos
    @Override
    protected void onStart() {
        super.onStart();
        //Llamamos al metodo para realizar el login silenciono
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        //Creamos condicional para saber si es que ya hemos iniciado sesion antes si -- si no pedira que ingresemos cuenta
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            controlSignInResult(result); // de la misma forma que en la actividad login, se crea el control
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    controlSignInResult(googleSignInResult);
                }
            });
        }

    }

    //Paso 5 : programamos el control para el login silencioso
    //Este es el metodo que realizara ya la accion del login silencioso
    private void controlSignInResult(GoogleSignInResult result) {
        //creamos condicional para ver si todo esta correcto
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount(); //en caso todo este perfecto, con este metodo traeremos los datos del cliente
            txtnom.setText(account.getDisplayName());
            txtemail.setText(account.getEmail());
            txtiden.setText(account.getId());

            //para poder descargar la foto del cliente del gmail, usaremos la libreria glide
            Glide.with(this).load(account.getPhotoUrl()).into(foto);

        } else {
            //En caso no haya sido exitoso el login, devolveremos al cliente a la pestaña de login
            backLogin();
        }
    }

    private void backLogin() {
        Intent x = new Intent(this, LoginActivity.class);
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //se suele agregar flags a los intent para que al momento de pasar de una actividad a otra no se quede colgada con informacion innecesaria
        startActivity(x);
    }

    //-------------------------------------------------------------------------------------
    //ACCIONES DE LOS BOTONES
    public void Logout(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    backLogin();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.Error_Login,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Revocar(View view) {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    backLogin();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error_Revocado,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
